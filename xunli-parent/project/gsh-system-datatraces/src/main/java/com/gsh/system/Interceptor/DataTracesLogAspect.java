package com.gsh.system.Interceptor;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gsh.common.annotation.Excel;
import com.gsh.common.core.domain.BaseEntity;
import com.gsh.common.json.JSON;
import com.gsh.common.utils.StringUtils;
import com.gsh.system.annotation.DataTraces;
import com.gsh.system.domain.SysDataTracesDetail;
import com.gsh.system.domain.SysDataTracesLog;
import com.gsh.system.parser.ContentParser;
import com.gsh.system.service.ISysDataTracesLogService;
import com.gsh.system.util.OpMethodName;
import com.gsh.system.util.ReflectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 拦截@dataTraces注解的方法
 * 拦截service中只有一个参数且返回值为int的增删改方法
 * 将具体修改存储到日志表和日志详情表中
 * Created by ygh on 2019/11/20.
 */
@Aspect
@Component
public class DataTracesLogAspect {

    private final static Logger logger = LoggerFactory.getLogger(DataTracesLogAspect.class);

    private SysDataTracesLog sysDataTracesLog;

    /**
     * 数据对象
     * 主要是参数上的数据
     */
    private Object objectData;

    private Object oldObject;

    private Object newObject;

    /**
     * 当前类主键对应的值
     */
    private  Object objectIdValue;

    private Map<String, Object> oldObjectMap;

    /**
     * 注解类
     */
    private DataTraces dataTraces;

    @Autowired
    private ISysDataTracesLogService sysDataTracesLogService;

    private void initDataTracesLogAspect(){
        sysDataTracesLog=new SysDataTracesLog();
        objectData = null;
        oldObject = null;
        newObject = null;
        objectIdValue = null;
        oldObjectMap = null;
        dataTraces = null;
    }

    //获取返回值为int的方法并且类上有dataTraces注解的方法
    @Before("@target(com.gsh.system.annotation.DataTraces) && execution(public int com.gsh.*.service.impl..*.*(..))")
    public void doBefore(JoinPoint joinPoint){
        initDataTracesLogAspect();
        // 设置操作方法和参数值
        setOperateMethodAndArgs(joinPoint);
        //获取目标类上的注解对象
        dataTraces = joinPoint.getTarget().getClass().getAnnotation(DataTraces.class);
        if(OpMethodName.UPDATE.equals(sysDataTracesLog.getOpMethod())){
            //修改方法初始化日志对象需要的值
            objectIdValue= ReflectionUtils.getFieldValue(objectData, dataTraces.objectPkName());
            oldObject =  selectDataTracesObjectData(objectIdValue,dataTraces);
            //oldMap只有修改用
            oldObjectMap= (Map<String, Object>) objectToMap(oldObject);
        }

    }

    /**
     * 通过反射根据主键id去查询对象
     * @param value
     * @param dataTraces
     * @return
     */
    private Object selectDataTracesObjectData(Object value ,DataTraces dataTraces) {
        ContentParser contentParser= null;
        try {
            contentParser = (ContentParser) dataTraces.parseclass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return contentParser.getResult(value,dataTraces.objectClass().getSimpleName());
    }

    //@target 匹配类上是否包含类  execution(public(可以不写默认) 返回值 方法名 参数 ))
    @AfterReturning(pointcut = "@target(com.gsh.system.annotation.DataTraces) && execution(public int com.gsh.*.service.impl..*.*(..))",returning = "object")
    public void doAfterReturing(Object object){
        //处理修改、新增、删除
        List<SysDataTracesDetail> sysDataTracesDetails = new ArrayList<>();
        if(OpMethodName.UPDATE.equals(sysDataTracesLog.getOpMethod())){
            //设置外键属性
            setDataTraceForeginObjec(oldObject,dataTraces);
            //重新根据id查询新对象
            newObject = selectDataTracesObjectData(objectIdValue,dataTraces);
            //组装日志的content字段
            handleUpdateOperate(dataTraces, sysDataTracesDetails);
            //保存
            saveDataTracesLogAndDetails(dataTraces, sysDataTracesDetails);
        } else if(OpMethodName.INSERT.equals(sysDataTracesLog.getOpMethod())){
            //需要重新获得id
            objectIdValue= ReflectionUtils.getFieldValue(objectData,dataTraces.objectPkName());
            newObject = objectData;
            setDataTraceForeginObjec(objectData,dataTraces);
            handleInsertOperate(sysDataTracesDetails);
            saveDataTracesLogAndDetails(dataTraces, sysDataTracesDetails);
        } else if(OpMethodName.DELETE.equals(sysDataTracesLog.getOpMethod())){
            //删除的时候会传递Long id / String ids(逗号分隔)
            if(objectData instanceof Long){
                //这里是把单个删除交给批量删除来处理
                objectData = String.valueOf(objectData);
            }
            if (objectData instanceof String){
                //批量删除
                for(String temp : ((String) objectData).split(",")){
                    objectIdValue = Long.parseLong(temp);
                    oldObject = selectDataTracesObjectData(objectIdValue,dataTraces);
                    //oldMap只有修改用
                    oldObjectMap= (Map<String, Object>) objectToMap(oldObject);
                    setDataTraceForeginObjec(oldObject,dataTraces);
                    sysDataTracesDetails = handleDeleteOperate();
                    saveDataTracesLogAndDetails(dataTraces, sysDataTracesDetails);
                }
            }
        }
    }

    /**
     * 设置关联对象和关联对象的ID
     * @param objectData
     * @param dataTraces
     */
    private void setDataTraceForeginObjec(Object objectData, DataTraces dataTraces){
        //设置外键参数
       if(!dataTraces.foreignObjectClass().getName().equals(DataTraces.class.getName())) {
            sysDataTracesLog.setOpForeginName(dataTraces.foreignObjectClass().getSimpleName());
            if(StringUtils.isNotBlank(dataTraces.foreignObjectKey())) {
                sysDataTracesLog.setOpForeginId((Long)ReflectionUtils.getFieldValue(objectData,dataTraces.foreignObjectKey()));
            }
        }
    }

    /**
     * 保存日志表及日志详情表
     * @param dataTraces
     * @param sysDataTracesDetails
     */
    private void saveDataTracesLogAndDetails(DataTraces dataTraces, List<SysDataTracesDetail> sysDataTracesDetails) {
        //保存对象
        try {
            //再次获得对象id
            //新增需要从新获得objectId需要重新设置newobject
            sysDataTracesLog.setOpObjectName(dataTraces.objectClass().getSimpleName());
            sysDataTracesLog.setOpObjectId((Long)objectIdValue);
            sysDataTracesLog.setOldObject(JSON.marshal(oldObject));
            sysDataTracesLog.setNewObject(JSON.marshal(newObject));
            if(StringUtils.isBlank(sysDataTracesLog.getOpContent())){
                sysDataTracesLog.setOpContent( sysDataTracesLog.getOpMethod());
            }
        } catch (Exception e) {
            logger.error("service加载失败:",e);
        }
        sysDataTracesLogService.insertSysDataTracesLog(sysDataTracesLog,sysDataTracesDetails);
    }

    /**
     * 处理删除操作
     */
    private List<SysDataTracesDetail> handleDeleteOperate() {
        List<SysDataTracesDetail>  sysDataTracesDetails = new ArrayList<SysDataTracesDetail>();
        oldObjectMap.forEach((key,value)->{
            if(value!=null){
                String fieldCnName = getFiledCnName(key, oldObject);
                if(fieldCnName!=null){
                    sysDataTracesDetails.add(getSysDataTracesDetail(key, fieldCnName, value, ""));
                }
            }
        });
        return sysDataTracesDetails;
    }

    /**
     * 处理新增操作
     * @param sysDataTracesDetails
     */
    private void handleInsertOperate( List<SysDataTracesDetail> sysDataTracesDetails) {
        try {
            Map<String, Object> newMap= (Map<String, Object>) objectToMap(newObject);
            newMap.forEach((key,value)->{
                if(value!=null){
                    String fieldCnName = getFiledCnName(key, newObject);
                    if(fieldCnName!=null){
                        sysDataTracesDetails.add(getSysDataTracesDetail(key, fieldCnName, "", value));
                    }
                }

            });
        } catch (Exception e) {
            logger.error("比较异常",e);
        }
    }

    /**
     * 处理修改操作
     * @param dataTraces
     * @param sysDataTracesDetails
     */
    private void handleUpdateOperate(DataTraces dataTraces, List<SysDataTracesDetail> sysDataTracesDetails) {
        try {
            Map<String, Object> newMap= (Map<String, Object>) objectToMap(newObject);
            StringBuilder opContent=new StringBuilder();
            oldObjectMap.forEach((key,value)->{
                Object newValue=newMap.get(key);
                if(value!=null && (!value.equals(newValue) || "all".equals(dataTraces.updateOpDetailLevel()))){
                    String fieldCnName = getFiledCnName(key, newObject);
                    if(fieldCnName!=null){
                        appendDataTracesOpContent(dataTraces, opContent, value, newValue, fieldCnName);
                        sysDataTracesDetails.add(getSysDataTracesDetail(key, fieldCnName, value, newValue));
                    }
                }

            });
            sysDataTracesLog.setOpContent(opContent.toString());
        } catch (Exception e) {
            logger.error("比较异常",e);
        }
    }

    /**
     * 根据注解的level追加日志的操作信息
     * @param dataTraces
     * @param opContent
     * @param oldValue
     * @param newValue
     * @param fieldCnName
     */
    private void appendDataTracesOpContent(DataTraces dataTraces, StringBuilder opContent, Object oldValue, Object newValue, String fieldCnName) {
        if(!oldValue.equals(newValue)) {
            if ("details".equals(dataTraces.updateOpContentLevel())) {
                opContent.append("【").append(fieldCnName).append("】")
                        .append("从【").append(oldValue).append("】")
                        .append("改为了【").append(newValue).append("】；\n");
            } else {
                if (opContent.length() > 3) {
                    opContent.append("【").append(fieldCnName).append("】 ");
                } else {
                    opContent.append("修改了【").append(fieldCnName).append("】 ");
                }
            }
        }
    }

    /**
     * 日志详细信息赋值
     * @param fieldName
     * @param fieldCnName
     * @param oldValue
     * @param newValue
     * @return
     */
    private SysDataTracesDetail getSysDataTracesDetail(String fieldName, String fieldCnName, Object oldValue, Object newValue) {
        SysDataTracesDetail sysDataTracesDetail = new SysDataTracesDetail();
        sysDataTracesDetail.setFieldName(fieldName);
        sysDataTracesDetail.setFieldCnName(fieldCnName);
        sysDataTracesDetail.setOldValue(StringUtils.nvl(oldValue,"").toString());
        sysDataTracesDetail.setNewValue(StringUtils.nvl(newValue,"").toString());
        return sysDataTracesDetail;
    }

    /**
     * 获取对象字段的中文注解名称，这里使用了Excel的注解名称
     * @param filed
     * @param object
     * @return
     */
    private String getFiledCnName(String filed, Object object) {
        Field field = ReflectionUtils.getAccessibleField(object, filed);
        if(field!=null){
            Excel excel = field.getAnnotation(Excel.class);
            if(excel!=null){
                return excel.name();
            }
        }
        return null;
    }

    /**
     * 根据方法名称，获取操作方法和方法参数
     * @param joinPoint
     * @return
     */
    private void setOperateMethodAndArgs(JoinPoint joinPoint) {
        String operateMethod = "";
        if(joinPoint.getArgs()!=null && joinPoint.getArgs().length == 1) {
            String opMethodName = joinPoint.getSignature().getName().toLowerCase();
            if (opMethodName.contains("insert")) {
                operateMethod = OpMethodName.INSERT;
            } else if (opMethodName.contains("update")) {
                operateMethod = OpMethodName.UPDATE;
            } else if (opMethodName.contains("delete")) {
                operateMethod = OpMethodName.DELETE;
            }
            //为新增对象/修改对象/删除对象赋值
            objectData = joinPoint.getArgs()[0];
            sysDataTracesLog.setOpMethod(operateMethod);
        }
    }


    /**
     * objectToMap
     * @param obj
     * @return
     */
    private Map<?, ?> objectToMap (Object obj) {
        if (obj == null) {
            return null;
        }
        ObjectMapper mapper=new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //如果使用JPA请自己打开这条配置
        //mapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
        Map<?, ?> mappedObject = mapper.convertValue(obj, Map.class);

        return mappedObject;
    }

}
