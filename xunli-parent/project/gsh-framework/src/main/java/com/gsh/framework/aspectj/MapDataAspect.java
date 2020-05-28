package com.gsh.framework.aspectj;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gsh.common.annotation.MapData;
import com.gsh.common.annotation.MapDataField;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.utils.StringUtils;
import com.gsh.framework.util.LogUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据过滤处理
 *
 * @author gsh-xrl
 */
@Aspect
@Component
@Configuration
public class MapDataAspect
{
    @Value("${spring.data.solr.host}")
    private String url;

    // 配置织入点
    @Pointcut("@annotation(com.gsh.common.annotation.MapData)")
    public void mapDataPointCut()
    {
    }

    @Before("mapDataPointCut()")
    public void doBefore(JoinPoint point) throws Throwable
    {

    }
    @After("mapDataPointCut()")
    public void doAfter(JoinPoint point) throws Throwable
    {
        handleDataScope(point);
    }

    protected void handleDataScope(final JoinPoint joinPoint)
    {
        // 获得注解
        MapData mapDataAnnotation = getAnnotationLog(joinPoint);
        if (mapDataAnnotation == null)
        {
            return;
        }
        mapDataFilter(joinPoint, mapDataAnnotation);
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint 切点
     * @param mapDataAnnotation 切入点 方法注解
     */
    public  void mapDataFilter(JoinPoint joinPoint, MapData mapDataAnnotation)
    {
        JSONArray ja=new JSONArray();
        Object obj=joinPoint.getArgs()[0];
        if(obj!=null){
            if(obj instanceof  java.util.List){//处理对象集合
                List list=(List)obj;
                if(!list.isEmpty()){//集合不能为空
                    for(Object argobj:list){
                        if(argobj instanceof BaseEntity){
                            JSONObject jo=getMapData(argobj);
                            ja.add(jo);
                        }
                    }
                }

            }else if(obj instanceof BaseEntity[]){//处理继承于BaseEntity对象 数组
                BaseEntity[]  BaseEntities=( BaseEntity[])obj;
                if(BaseEntities.length>0){//集合不能为空
                    for(Object argobj:BaseEntities){
                        if(argobj instanceof BaseEntity){
                            JSONObject jo=getMapData(argobj);
                            ja.add(jo);
                        }
                    }
                }
            }else if(obj instanceof BaseEntity){//处理继承于BaseEntity对象
                JSONObject jo=getMapData(obj);
                ja.add(jo);
            }
        }
        //TODO 根据注解判断 当前放法的操作类型 ja构造不同的solr 所需对象
        String operateType=mapDataAnnotation.operateType();
        if(MapData.TYPE_ADD.equals(operateType)||MapData.TYPE_UPDATE.equals(operateType)){//新增或更新
            List<SolrInputDocument> doclist = new ArrayList<>();
            for(int i=0;i<ja.size();i++){
                SolrInputDocument doc = new SolrInputDocument();
                JSONObject jo=ja.getJSONObject(i);
                doc.addField("id", jo.getString("id"));
                doc.addField("parent_id", jo.getString("parentId"));
                doc.addField("info", jo.getString("info"));
                doc.addField("dept_id", jo.getString("deptId"));
                doc.addField("user_id", jo.getString("userId"));
                doc.addField("detail_url", jo.getString("detailUrl"));
                doc.addField("open_type", jo.getString("openType"));
                doc.addField("sort", jo.getString("sort"));
                doc.addField("store_lat_lon", jo.getString("latitude")+","+jo.getString("longitude"));
                doclist.add(doc);
            }
            if(!doclist.isEmpty()){
                try {
                    addSolrDocuments(doclist);
                    LogUtils.getAccessLog().info("solr插入/更新公共地图点数据索引成功！");
                } catch (SolrServerException e) {
                    LogUtils.getErrorLog().info("solr插入/更新公共地图点数据失败！"+e.getMessage());
                } catch (IOException e) {
                    LogUtils.getErrorLog().info("solr插入/更新公共地图点数据失败！"+e.getMessage());
                }
            }
        }else if(MapData.TYPE_DELETE.equals(operateType)){//删除，删除只能根据id来所以如果实体类中没有注解id 那么删除将不会进行
            try {
                if(obj instanceof  java.util.List||obj instanceof BaseEntity){
                    List<String> ids=new ArrayList<String>();
                    for(int i=0;i<ja.size();i++){
                        ids.add(ja.getJSONObject(i).getString("id"));
                    }
                    if(ids.size()>0){
                        deleteSolrDocumentsByQuery("id:("+ StringUtils.join(ids," ") +")");
                    }
                }else if(obj instanceof String&&StringUtils.isNotBlank(obj+"")){
                    String objStr=(obj+"").replace(","," ");
                    deleteSolrDocumentsByQuery("id:("+ objStr +")");
                }else if(obj instanceof Long ||obj instanceof Integer){
                    deleteSolrDocumentsByQuery("id:("+ obj +")");
                }
            } catch (SolrServerException e) {
                LogUtils.getErrorLog().info("solr删除公共地图点数据索引失败！"+e.getMessage());
                throw new RuntimeException("solr删除公共地图点数据索引失败！");
            } catch (IOException e) {
                LogUtils.getErrorLog().info("solr删除公共地图点数据索引失败！"+e.getMessage());
                throw new RuntimeException("solr删除公共地图点数据索引失败！");
            }
        }
    }

    /**
     * 从方法传入的实例类中或许注解配置的对应属性 构造成json 用于后续转化为solr document 或者 solr查询语句
     * @param obj
     * @return
     */
    private  JSONObject getMapData(Object obj) {
        JSONObject jsonObject=new JSONObject();
        Field[] fields=obj.getClass().getDeclaredFields();
        StringBuilder info=new StringBuilder();
        for(Field field:fields){
            MapDataField mapDataField=field.getAnnotation(MapDataField.class);
            if (mapDataField!=null) {
                try {
                    switch (mapDataField.type()){
                        case MapDataField.TYPE_ID:
                            String id=this.getValue(obj,field)+"";
                            if(StringUtils.isBlank(id)){
                                id= StringUtils.uuid();//属性中没有配置id时生成一个uuid
                            }
                            info.append("ID："+id+"|");
                            jsonObject.put("id",id);
                            jsonObject.put("detailUrl",mapDataField.detailUrl());
                            jsonObject.put("openType",mapDataField.openType());
                            break;
                        case MapDataField.TYPE_LATITUDE:
                            jsonObject.put("latitude",this.getValue(obj,field));
                            break;
                        case MapDataField.TYPE_LONGITUDE:
                            jsonObject.put("longitude",this.getValue(obj,field));
                            break;
                        case MapDataField.TYPE_PARENTID:
                            jsonObject.put("parentId",this.getValue(obj,field));
                            break;
                        case MapDataField.TYPE_DEPTID:
                            jsonObject.put("deptId",this.getValue(obj,field));
                            break;
                        case MapDataField.TYPE_USERID:
                            jsonObject.put("userId",this.getValue(obj,field));
                            break;
                        case MapDataField.TYPE_INFO:
                            info.append(mapDataField.name()+"："+this.getValue(obj,field)+"|");
                            break;
                        case MapDataField.TYPE_SORT:
                            jsonObject.put("sort",this.getValue(obj,field));
                            break;
                        default:
                            break;
                    }
                } catch (NoSuchMethodException e) {
                    LogUtils.getErrorLog().error("配置的地图信息属性缺少get方法");
                    e.printStackTrace();
                } catch (Exception e){
                    LogUtils.getErrorLog().error("获取配置的地图信息属性值失败");
                    e.printStackTrace();
                }

            }
        }
        jsonObject.put("info",info.toString());
        return jsonObject;
    }

    /**
     * 是否存在注解MapData，如果存在就获取
     */
    private MapData getAnnotationLog(JoinPoint joinPoint)
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(MapData.class);
        }
        return null;
    }

    private Object getValue(Object obj,Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String fieldName=field.getName();
        String getMethodName="get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1);
        Method method=obj.getClass().getDeclaredMethod(getMethodName);
        Object returnObj=method.invoke(obj);
        return returnObj==null?"":returnObj+"";
    }

    /**
     * 新增索引数据
     * @param doclist
     * @throws SolrServerException
     * @throws IOException
     */
    private void addSolrDocuments(List<SolrInputDocument> doclist) throws SolrServerException, IOException {
        HttpSolrClient service = getSolrService();
        service.add(doclist);
        service.commit();
    }
    private HttpSolrClient getSolrService() {
        return  new HttpSolrClient.Builder(url+"mapData")
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }
    private void deleteSolrDocumentsByQuery(String query) throws SolrServerException, IOException {
        HttpSolrClient service = getSolrService();
        service.deleteByQuery(query);
        service.commit();
    }
}
