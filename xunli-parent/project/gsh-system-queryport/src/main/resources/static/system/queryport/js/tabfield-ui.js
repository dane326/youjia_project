/**
 * @param tabfield
 */
(function ($) {
    $.extend({
        tabfield : {
                // 初始化列表 查询配置
            initListTable: function (tableOptions, options) {
                var defaultTableOptions = {
                    url: tableOptions.prefix + "/list",
                    createUrl: tableOptions.prefix + "/add",
                    updateUrl: tableOptions.prefix + "/edit/{id}",
                    removeUrl: tableOptions.prefix + "/remove",
                    exportUrl: tableOptions.prefix + "/export",
                    modalName: options.formName,
                    columns: [{
                        checkbox: true
                    },{
                        field: '_id',
                        title: '主键',
                        visible: false
                    }],
                    operateColumn:{
                        title: '操作',
                        align: 'center',
                        formatter: function(value, row, index) {
                            var actions = [];
                            actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" onclick="$.operate.edit(\'' + row._id + '\')"><i class="fa fa-edit"></i>编辑</a> ');
                            actions.push('<a class="btn btn-danger btn-xs " href="javascript:void(0)" onclick="$.operate.remove(\'' + row._id + '\')"><i class="fa fa-remove"></i>删除</a>');
                            return actions.join('');
                        }
                    }
                    }
                tableOptions = $.extend(true, defaultTableOptions, tableOptions);   //深层合并对象
                $.tabfield.displayTableFieldLayout(tableOptions, options);
            },
            // 初始化 增加配置
            initAddForm: function (options) {
                $.tabfield.displayFormFieldLayout(options,"add");
            },
            // 初始化 编辑配置
            initEditForm: function (options,data) {
                $.tabfield.displayFormFieldLayout(options,"edit");
            },
            // 初始化 详情配置
            initDetailForm: function (options,data) {
                $.tabfield.displayFormFieldLayout(options,"detail");
            },
            // 初始化 查询配置
            initSearchForm: function (options) {
                $.tabfield.displayFormFieldLayout(options,"search");
            },
             /**
              * 显示新增和修改界面信息
              */
             displayFormFieldLayout:function(options,configType){
                 if (options.fieldConfData) {
                     var data = options.data;
                     //初始化form验证
                     if("detail"!=configType){
                         $(options.form).validate({
                             focusCleanup: true
                         });
                     }
                     //初始化页面
                     $(options.element).html("");
                     $.each(options.fieldConfData,function (index,column) {
                         if($.common.isEmpty(column.htmlName)){
                             column.htmlName = column.htmlId;
                         }
                         var modifyType=null;
                         if("add"==configType){
                             modifyType = column.isInsert;
                         }else if("edit"==configType){
                             modifyType = column.isEdit;
                         }else if("detail"==configType){
                             modifyType = column.isDetail;
                         }else if("search"==configType){
                             modifyType = column.isSearch;
                         }
                         //类型不存在/不是主键返回,
                         if ($.common.isEmpty(column.isPk) && $.common.isEmpty(modifyType) ) {
                             return ;
                         }
                         var value = data ? $.common.isEmpty(data[column.htmlId])? $.common.nullToString(column.defaultValue):data[column.htmlId] : $.common.nullToString(column.defaultValue);
                         //编辑页面需要加上隐藏id
                         if(column.isPk){
                             if("edit"==configType){
                                 $(options.element).append("<input id=\"" + column.htmlId + "\" name=\"" + column.htmlName + "\" value=\"" + value + "\" type=\"hidden\">");
                             }
                         }else{
                             if(modifyType){
                                 if(configType == "search"){
                                     var liClass  = column.queryType == "BETWEEN" &&  configType == "search" ? "select-time":"";
                                     $(options.element).append("<li class='"+liClass+"'>" + $.tabfield.tabSplitHtml(column,value,configType) + "</li>");
                                 }else{
                                     $(options.element).append("<div class='col-sm-6'>" + $.tabfield.tabSplitHtml(column,value,configType) + "</div>");
                                 }
                             }
                             if ('date' == column.htmlType && "detail"!=configType) {
                                 //这里使用laydate
                                 if(column.queryType == "BETWEEN" &&  configType == "search" ){
                                     $.tabfield.loadLaydateTime($("input[name='params[begin" + column.htmlName + "]']")[0],"date");
                                     $.tabfield.loadLaydateTime($("input[name='params[end" + column.htmlName + "]']")[0],"date");
                                 }else{
                                     $.tabfield.loadLaydateTime($("input[name='" + column.htmlName + "']")[0],"date");
                                 }
                             }
                             if ('time' == column.htmlType && "detail"!=configType) {
                                 //这里使用laydate
                                 if(column.queryType == "BETWEEN" &&  configType == "search" ){
                                     $.tabfield.loadLaydateTime($("input[name='params[begin" + column.htmlName + "]']")[0],"datetime");
                                     $.tabfield.loadLaydateTime($("input[name='params[end" + column.htmlName + "]']")[0],"datetime");
                                 }else {
                                     $.tabfield.loadLaydateTime($("input[name='" + column.htmlName + "']")[0], "datetime");
                                 }
                             }
                             //简单规则校验
                             if("detail"!=configType && column.ruleCheck){
                                 $.tabfield.tabFieldRulesCheck(options.form,column.ruleCheck,column.htmlName);
                             }
                         }
                         //额外方法配置
                         /*var extRuleCheck = column.extRuleCheck;
                         tabExtFieldRulesCheck(extRuleCheck,htmlId);*/
                     });
                 }else{
                     console.log("无法获取字段配置信息");
                 }
             },
                //拼接新增编辑html
                tabSplitHtml:function(column,value,configType){
                var elementDiv = "";
                var element = "";
                //详情页面不显示必填
                var className=$.common.isEmpty(column.isRequired) || "detail" == configType || "search" == configType ?"" : "is-required";
                //详情不验证
                var require = $.common.isEmpty(column.isRequired) || "detail" == configType || "search" == configType ?"" : "required";
                //详细显示也默认为只读
                var readonly = $.common.isEmpty(column.isReadOnly) ?"":"readonly";
                if( "detail" == configType){
                    readonly = "disabled";
                }
                if ('date' == column.htmlType || 'time' == column.htmlType) {
                    if(column.queryType == "BETWEEN" &&  configType == "search" ){
                        element =
                            "<input name='params[begin" + column.htmlName + "]' style='display: inline-block;' class='form-control' placeholder=\"开始时间\" type='text' "+require+" "+readonly+"  value='"+value+"'/>"
                            +"<span>-</span>"
                            +"<input name='params[end" + column.htmlName + "]' style='display: inline-block;' class='form-control' placeholder=\"结束时间\" type='text' "+require+" "+readonly+"  value='"+value+"'/>"
                    }else{
                        element =
                            "<input name='" + column.htmlName + "' class='form-control' placeholder='yyyy-MM-dd' type='text' "+require+" "+readonly+"  value='"+value+"'/>";
                    }
                }else if('dictSelect' == column.htmlType){
                    element = "<select id='" + column.htmlId + "' name='" + column.htmlName + "' class='form-control custom-from-exdselect' "+require+" "+readonly+" data-exdselect-value='"+value+"' data-exdselect-options='{\"parentDict\" :\""+column.dictType+"\"}'>" +
                        "</select>";

                }else if('dictSelectMulti' == column.htmlType){
                    element = "<select id='" + column.htmlId + "' name='" + column.htmlName + "' class='form-control custom-from-exdselect' "+require+" "+readonly+" multiple data-exdselect-value='"+value+"' data-exdselect-options='{\"parentDict\" :\""+column.dictType+"\"}'>" +
                        "</select>";

                }else if('dictRadio' == column.htmlType){
                    element = "<div class=\"custom-from-exdselect\" id=\"" + column.htmlId + "\" "+require+" "+readonly+" data-exdselect-value='"+value+"' data-exdselect-options='{\n" +
                        "\"parentDict\" :\"" + column.dictType + "\",\n" +
                        "\"selectType\":\"radio\",\n" +
                        "\"checkName\" : \"" + column.htmlName + "\"\n" +
                        "}'>\n" +
                        "</div>\n";

                }else if('dictCheckBox' == column.htmlType){
                    element ="<div class=\"custom-from-exdselect\" id=\"" + column.htmlId + "\" "+require+" "+readonly+" data-exdselect-value='"+value+"' data-exdselect-options='{\n" +
                        "\"parentDict\" :\"" + column.dictType + "\",\n" +
                        "\"selectType\":\"checkBox\",\n" +
                        "\"checkName\" : \"" + column.htmlName + "\"\n" +
                        "}'>\n" +
                        "</div>\n";

                }else if('codeDictSelect' == column.htmlType){
                    element = "<select id='" + column.htmlId + "' name='" + column.htmlName + "' class='form-control custom-from-exdselect' "+require+" "+readonly+" data-exdselect-value='"+value+"' data-exdselect-options='{\"parentCode\" :\""+column.dictType+"\"}'>" +
                        "</select>";

                }else if('codeDictSelectMulti' == column.htmlType){
                    element = "<select id='" + column.htmlId + "' name='" + column.htmlName + "' class='form-control custom-from-exdselect' "+require+" "+readonly+" multiple data-exdselect-value='"+value+"' data-exdselect-options='{\"parentCode\" :\""+column.dictType+"\"}'>" +
                        "</select>";

                }else if('codeDictRadio' == column.htmlType){
                    element = "<div class=\"custom-from-exdselect\" id=\"" + column.htmlName + "\" "+require+" "+readonly+" data-exdselect-value='"+value+"' data-exdselect-options='{\n" +
                        "\"parentCode\" :\"" + column.dictType + "\",\n" +
                        "\"selectType\":\"radio\",\n" +
                        "\"checkName\" : \"" + column.htmlId + "\"\n" +
                        "}'>\n" +
                        "</div>\n";

                }else if('codeDictCheckBox' == column.htmlType){
                    element ="<div class=\"custom-from-exdselect\" id=\"" + column.htmlId + "\" "+require+" "+readonly+" data-exdselect-value='"+value+"' data-exdselect-options='{\n" +
                        "\"parentCode\" :\"" + column.dictType + "\",\n" +
                        "\"selectType\":\"checkBox\",\n" +
                        "\"checkName\" : \"" + column.htmlName + "\"\n" +
                        "}'>\n" +
                        "</div>\n";

                }else if('userTree' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/user/treeData\"}' placeholder=\"请输入用户名称\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('userTreeMulti' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/user/treeData\",\"checkBox\":true}' placeholder=\"请输入用户名称\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('deptTree' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/dept/treeData\",\"expandLevel\":2}' placeholder=\"请输入部门\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('deptTreeMulti' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/dept/treeData\",\"expandLevel\":2,\"checkBox\":true}' placeholder=\"请输入部门\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('roleTree' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/role/treeData\",\"enableAsync\":false}' placeholder=\"请输入角色\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('roleTreeMulti' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/role/treeData\",\"enableAsync\":false,\"checkBox\":true}' placeholder=\"请输入角色\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('factoryUserTree' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/factory/user/treeData\"}' placeholder=\"请输入用户名称\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('factoryUserTreeMulti' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/factory/user/treeData\",\"checkBox\":true}' placeholder=\"请输入用户名称\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('factoryDeptTree' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/factory/dept/treeData\",\"expandLevel\":2}' placeholder=\"请输入部门\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('factoryDeptTreeMulti' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/factory/dept/treeData\",\"expandLevel\":2,\"checkBox\":true}' placeholder=\"请输入部门\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('factoryRoleTree' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/factory/role/treeData\",\"enableAsync\":false}' placeholder=\"请输入角色\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('factoryRoleTreeMulti' == column.htmlType){
                    element =
                        "<input id=\"" + column.htmlId + "NameId\" name=\"" + column.htmlName + "\" type=\"hidden\" value=\""+value+"\"/>\n" +
                        "<input id=\"" + column.htmlId + "Name\"  data-exdtree-options='{\"url\":\"system/factory/role/treeData\",\"enableAsync\":false,\"checkBox\":true}' placeholder=\"请输入角色\" class=\"form-control\" value=\""+$.queryportui.id2Name("user",value)+"\" type=\"text\" "+require+" "+readonly+">\n";
                }else if('hidden' == column.htmlType){
                    element = "<input id=\"" + column.htmlId + "\" name='" + column.htmlName + "' type='hidden' class='form-control' "+require+" "+readonly+" value='"+value+"'></input>";

                }else {
                    element = "<input id=\"" + column.htmlId + "\" name='" + column.htmlName + "' type='text' class='form-control' "+require+" "+readonly+" value='"+value+"'></input>";

                }
                if(configType=="search"){
                    elementDiv = "";
                    if('hidden' != column.htmlType){
                        elementDiv += "<p>"+column.columnComment+"：</p>";
                    }
                    elementDiv += element;
                }else{
                    elementDiv = "<div class='form-group'>" +
                        "<label class='col-sm-3 control-label "+className+"'>" + column.columnComment + "：</label>" +
                        " <div class='col-sm-8'>" + element
                    "</div>" +
                    "</div>";
                }

                return elementDiv;
            },
             //简单规则校验
             tabFieldRulesCheck:function(form,ruleCheck,htmlName){
                if("number"==ruleCheck){
                    $(form+" [name="+htmlName+"]").rules("add",{isNum:true});
                }
                if("phone"==ruleCheck){
                    $(form+" [name="+htmlName+"]").rules("add",{isPhone:true});
                }
                if("email"==ruleCheck){
                    $(form+" [name="+htmlName+"]").rules("add",{email:true});
                }
                if("name"==ruleCheck){
                    $(form+" [name="+htmlName+"]").rules("add",{isName:true});
                }
                if("tel"==ruleCheck){
                    $(form+" [name="+htmlName+"]").rules("add",{isTel:true});
                }
            },
             //加载日期
             loadLaydateTime:function(element,type){
                layui.use('laydate', function () {
                    var com = layui.laydate;
                    newBtnArr = ['clear', 'now', 'confirm'];
                    com.render({
                        elem: element,
                        theme: 'molv',
                        trigger: 'click',
                        type: type,
                        newBtnArr:newBtnArr,
                    });
                });

            },
            displayTableFieldLayout:function(tableOptions,searchOptions) {

                var fieldConfData = searchOptions.fieldConfData;
                //需要修改的数据
                if (!fieldConfData || fieldConfData.length == 0) {
                    $.table.init(tableOptions);
                    console.log("no conf:" + searchOptions.formName);
                } else {
                    $.tabfield.showListData(fieldConfData,tableOptions);
                    // 初始化 查询配置
                    $.tabfield.displayFormFieldLayout(searchOptions,"search");
                    if(tableOptions.operateColumn){
                        tableOptions.columns.push(tableOptions.operateColumn);
                    }
                    $.table.init(tableOptions);
                }
            },
            //查询界面展示数据
            showListData:function(fieldConfData,options){
                fieldConfData.forEach(function (column, index) {
                    if (column.isList && (!column.isPk)) {
                        //字典类型
                        if (!column.dictType) {
                            options.columns.push({field: column.htmlId, title: column.columnComment});
                        } else {
                            options.columns.push({
                                field: column.htmlId, title: column.columnComment,
                                formatter: function (value, row, index) {
                                    return $.exdSelect.selectDictLabel(column.dictType,value);
                                }
                            });
                        }

                    }
                });
            }
        }
});

})(jQuery);

/*//额外方法配置
function tabExtFieldRulesCheck(extRuleCheck,htmlId){
    if(extRuleCheck){
        $("[name="+htmlId+"]").click(function(){
            eval(extRuleCheck+'()');
        })
    };
}


function dataFormat(date){
    var date =new Date(date);
    var curr_date = date.getDate();
    var curr_month = date.getMonth() + 1;
    var curr_year = date.getFullYear();
    String(curr_month).length < 2 ? (curr_month = "0" + curr_month): curr_month;
    String(curr_date).length < 2 ? (curr_date = "0" + curr_date): curr_date;
    var yyyyMMdd = curr_year + "-" + curr_month +"-"+ curr_date;
    return yyyyMMdd;
}*/
