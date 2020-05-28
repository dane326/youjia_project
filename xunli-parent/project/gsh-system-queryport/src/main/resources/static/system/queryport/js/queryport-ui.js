/**
 * queryportui
 */

(function ($) {
    $.extend({
        queryportui:{
            prefix: ctx + "system/queryport",
            drawDatas: {},
            chartDatas: {},
            sysDictData: {},
            queryportMainData: {},
            init:function(queryportMain,queryportPanel){
                var formId ="formId"+queryportMain.id;
                $.queryportui.queryportMainData[queryportMain.id] = queryportMain;
                $.queryportui.initQueryportPanel(formId,queryportMain,queryportPanel);
                $.queryportui.initQueryportForm(formId,queryportMain,queryportPanel);
                //加载时间待时分秒
                $.queryportui.initQueryportDateOptions()
                //加载字典值
                $.exdSelect.initialize();
                //加载树图
                $.exdTree.initialize();
                if(queryportMain.showType.indexOf("list") > -1){
                    $.queryportui.initCustomQueryportBootstrapTable(queryportMain);
                }
                if(queryportMain.showType.indexOf("shape") > -1){
                    if("Y" == queryportMain.ifFirstLoad){
                        $.queryportui.initCustomQueryportChart($("#"+formId).serialize(), 'echartPicture'+queryportMain.id,queryportMain)
                    }
                }
            },
            initQueryportPanel:function(formId,queryportMain,queryportPanel){
                var panel = "" ;
                if(queryportMain.showType.indexOf("shape")>-1){
                    panel += "<div class=\"col-sm-12 ibox select-table\">\n" +
                        "         <div class=\"ibox-title\">\n" +
                        "              <h5>" + queryportMain.name + "</h5>\n" +
                        "         </div>" +
                        "         <div class=\"ibox-content echarts\" id=\"echartPicture"+queryportMain.id+"\">\n" +
                        "     </div><hr>";
                }
                if(queryportMain.showType.indexOf("list")>-1){
                    panel +="<div class=\"col-sm-12 select-table table-striped\">\n" +
                        "                <div class=\"btn-group-sm\" id=\"toolbar"+queryportMain.id+"\" role=\"group\">\n" +
                        "                    <a class=\"btn btn-info\" target=\"_blank\" onclick=\"$.queryportui.displayExport('"+formId+"','"+queryportMain.id+"')\" >\n" +
                        "                        <i class=\"fa fa-download\"></i> 导出\n" +
                        "                    </a>\n" +
                        "                    <a class=\"btn btn-warning\" target=\"_blank\" onclick=\"$.modal.closeTab()\" >\n" +
                        "                        <i class=\"fa fa-reply\"></i> 返回\n" +
                        "                    </a>\n" +
                        "                 </div>\n" +
                        "                <table id=\"bootstrap-queryport-table"+queryportMain.id+"\" ></table>\n" +
                        "            </div>\n" +
                        "\n";
                }
                $(queryportPanel).append(panel);

            },
            displayExport:function(formId,mainId){
                var params = $.common.formToJSON(formId);
                params.id = mainId;
                $.operate.postCurrent(table.config['bootstrap-queryport-table'+mainId].exportUrl,params);
            },
            initQueryportForm:function(formId,queryportMain,queryportPanel) {
                var display = queryportMain.type=='board'? "none":"inline-block";
                var form = " <div style=\"display:"+ display + "\" class=\"col-sm-12 search-collapse\">\n" +
                    "                <form id=\""+formId+"\">\n" +
                    "                    <div class=\"select-list\">\n" +
                    "                        <ul id=\"queryCondition"+queryportMain.id+"\">\n" +
                    "                        </ul>\n" +
                    "                    </div>\n" +
                    "                </form>\n" +
                    "            </div>"

                $(queryportPanel).before(form);
                var searchOptions = {
                    form:"#"+formId,
                    element:"#queryCondition"+queryportMain.id,
                    fieldConfData:[]
                }
                $.each(queryportMain.queryportParams,function (k,v) {
                    var fieldConf = {};
                    fieldConf.columnComment = v.cname;
                    fieldConf.htmlId = v.ename;
                    fieldConf.htmlName = "params["+v.ename+"]";
                    fieldConf.htmlType = v.inputMode;
                    fieldConf.isSearch = "1";
                    fieldConf.dictType = v.typeParam1;
                    fieldConf.defaultValue = v.defaultValue;
                    if(v.viewFlag != "Y"){
                        fieldConf.htmlType = "hidden" ;
                    }
                    searchOptions.fieldConfData.push(fieldConf);
                })
                $.tabfield.initSearchForm(searchOptions);
                var search = "";
                if(queryportMain.showType == 'shape'){
                    search = "<a  class=\"btn btn-primary btn-rounded btn-sm\" onclick=\"$.queryportui.drawChart('"+queryportMain.id + "')\"><i class=\"fa fa-search\"></i>&nbsp;搜索</a>\n";
                }else{
                    search =  "<a class=\"btn btn-primary btn-rounded btn-sm\" onclick=\"$.table.search('','bootstrap-queryport-table"+queryportMain.id+"')\"><i class=\"fa fa-search\"></i>&nbsp;搜索</a>\n";
                }
                var searchButton = "<li>\n" + search +
                    "<a class=\"btn btn-warning btn-rounded btn-sm\" onclick=\"$.form.reset('formId')\"><i class=\"fa fa-refresh\"></i>&nbsp;重置</a>\n" +
                    "</li>"
                $("#"+formId+" #queryCondition"+queryportMain.id).append(searchButton);
            },
            getPostData: function (url, params, callback) {
                var data = null;
                $.ajax({
                    type: 'post',
                    dataType: 'json',
                    data: params,
                    url: url,
                    cache: false,
                    success: function (result) {
                        data = result;
                        if (typeof callback == "function") {
                            callback(result);
                        }
                    }
                });
                return data;
            },
            getDisplayData: function (mainId, params, callback) {
                $.queryportui.getPostData($.queryportui.prefix + "/display/list?id="+mainId, params, callback);

            },
            getUrlParam:function(name) {
                var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
                var r = window.location.search.substr(1).match(reg);
                if (r != null) {
                    return unescape(r[2]);
                }
                return null;
            },
            drawChart: function(mainId){
                var formId ="formId"+mainId;
                var chartElement = 'echartPicture'+mainId;
                $.queryportui.initCustomQueryportChart($("#"+formId).serialize(), chartElement, $.queryportui.queryportMainData[mainId]);
            },
            initCustomQueryportChart: function (params, chartElement,queryportMain) {
                var mainId = queryportMain.id;
                $.queryportui.getDisplayData(mainId,params,function (displayData) {
                    //var myChart = echarts.init(chartElement);
                    //myChart.dispose();
                    if(displayData.rows.length>0){
                        var drawData={};
                        var chartResults = queryportMain.queryportCharts;
                        var columnResult = queryportMain.queryportColumns;
                        //多个配置绘制多个图
                        $.each(chartResults,function(k,chartResult){
                            if(k>0){
                                var chartStyleHeight = "style=\"height: " + chartResult.height + ";\"";
                                $("#"+chartElement).after("<div class=\"echarts\"" + chartStyleHeight + "id=\""+chartElement+k+"\"></div>")
                                chartElement = chartElement+k;
                            }
                            //获取y轴id,y轴可有多条线
                            var yaxiss=chartResult.yaxis.split(",");
                            //获取x轴id
                            var xaxis=chartResult.xaxis;
                            //x轴数据为单条
                            var xdata = $.queryportui.getQueryportChartXData(columnResult, xaxis);
                            //y轴数据可为多条
                            var ydata = $.queryportui.getQueryportChartYdata(yaxiss,columnResult);
                            var echartConfig = $.queryportui.getQueryportChartConfig(chartResult);
                            var echartData = $.queryportui.getQueryportChartData(displayData,xdata,ydata);
                            drawData['echart_data'] = echartData;
                            drawData['echart_config'] =echartConfig;
                            var myChart = new xchart({
                                divId : chartElement,
                                drawData: drawData
                            });
                            myChart.on('click', function(params) {
                                console.log(params);
                                if(params.data.xLink){
                                    $.modal.openTab(params.name + "" + params.seriesName + "详情",params.data.xLink);
                                }
                            });
                            $.queryportui.drawDatas[chartElement] = drawData;
                            $.queryportui.chartDatas[chartElement] = myChart;
                        })
                    }else{
                        $.modal.msgError("未获取到视图数据");
                    }
                });
            },
            reDrawCustomQueryportChart: function(chartElement){
                $("#"+chartElement).html("");
                $("#"+chartElement).removeAttr("_echarts_instance_");
                var drawData = $.queryportui.drawDatas[chartElement];
                new xchart({
                    divId : chartElement,
                    drawData: drawData
                });
            },
            initCustomQueryportAllChart: function (parentElement) {
                $.modal.loading("正在处理中，请稍后...");
                $("body").find(".custom-private-queryport").each(function (n, element) {
                    //使用js加载
                    $.get(ctx+"system/queryport/conf/"+$(element).attr("data-queryport-id"),function (result) {
                        if(result.code == '0') {
                            data = result.data;
                            if(data.showType == 'shape'){
                                $.queryportui.initCustomQueryportChart(null, $(element).attr("id"),data);
                            }else{
                                $.queryportui.init(data,"#queryport-panel"+data.id);
                            }
                        }else{
                            //$.modal.msgError(result.msg);
                        }

                    });
                    //这里可以使用iframe
                    //$(element).load(ctx+"/system/queryport/display/"+$(element).attr("data-queryport-id"));
                });
                $.modal.closeLoading();
            },
            initQueryportDateOptions: function() {
                if ($(".time-date").length > 0) {
                    layui.use('laydate', function () {
                        var com = layui.laydate;
                        $(".time-date").each(function (index, item) {
                            var time = $(item);
                            // 控制控件外观
                            var type = time.attr("data-type") || 'datetime';
                            // 控制回显格式
                            var format = time.attr("data-format") || 'yyyy-MM-dd HH:mm:ss';
                            // 控制日期控件按钮
                            var buttons = time.attr("data-btn") || 'clear|now|confirm', newBtnArr = [];
                            // 日期控件选择完成后回调处理
                            var callback = time.attr("data-callback") || {};
                            if (buttons) {
                                if (buttons.indexOf("|") > 0) {
                                    var btnArr = buttons.split("|"), btnLen = btnArr.length;
                                    for (var j = 0; j < btnLen; j++) {
                                        if ("clear" === btnArr[j] || "now" === btnArr[j] || "confirm" === btnArr[j]) {
                                            newBtnArr.push(btnArr[j]);
                                        }
                                    }
                                } else {
                                    if ("clear" === buttons || "now" === buttons || "confirm" === buttons) {
                                        newBtnArr.push(buttons);
                                    }
                                }
                            } else {
                                newBtnArr = ['clear', 'now', 'confirm'];
                            }
                            com.render({
                                elem: item,
                                theme: 'molv',
                                trigger: 'click',
                                type: type,
                                format: format,
                                btns: newBtnArr,
                                done: function (value, data) {
                                    if (typeof window[callback] != 'undefined'
                                        && window[callback] instanceof Function) {
                                        window[callback](value, data);
                                    }
                                }
                            });
                        });
                    });
                }
            },
            //获取字典值
            id2Name:function (type, value) {
                var className = {
                    "user":"com.gsh.system.domain.SysUser",
                    "role":"com.gsh.system.domain.SysRole",
                    "dept":"com.gsh.system.domain.SysDept",
                    "dictCode":"com.gsh.system.domain.SysDictData"
                };
                var data = "";
                var typeName = className[type];
                if($.common.isEmpty(typeName) || $.common.isEmpty(value)){
                    return data;
                }
                //先查询缓存
                var jsonData = sessionStorage.getItem("id2name-" +type+"-"+ value);
                if($.common.isEmpty(jsonData)){
                    //同步请求防止同时请求多次
                    $.exdSelect.get(ctx + "/system/id2name/getNameByIds?cls="+typeName+"&ids="+value, function(result) {
                        if(result.code == '0') {
                            //放入缓存
                            if(!$.common.isEmpty(result.msg)){
                                data = result.msg;
                                sessionStorage.setItem("id2name-" +type+"-"+ value, result.msg);
                            }
                        }else{
                            //$.modal.msgError(result.msg);
                        }
                    });
                }else{
                    data = jsonData;
                }
                return data;
            },
            initCustomQueryportBootstrapTable: function(queryportMain) {
                //加载列表
                var options = {
                    id: "bootstrap-queryport-table"+queryportMain.id,
                    url: $.queryportui.prefix + "/display/list?id="+queryportMain.id,
                    exportUrl: $.queryportui.prefix + "/display/export?mainId="+queryportMain.id,
                    modalName: "查询视图配置",
                    firstLoad: false,
                    toolbar:"toolbar"+queryportMain.id,
                    columns: []
                };
                //看板类型隐藏工具栏
                if(queryportMain.type == "board"){
                    options.showRefresh = false;
                    options.showColumns = false;
                    options.showToggle = false;
                }

                queryportMain.queryportColumns.forEach(function(item, i){
                    if("Y"==item.viewFlag){
                        var column = {};
                        column.title = item.cname;
                        column.field = item.ename;
                        if("dict" === item.convertType){
                            column.fieldDictType = item.typeParam1;
                            column.formatter = function(value, row, index) {
                                return $.exdSelect.selectDictLabel(this.fieldDictType, value);
                            };
                        }else if("user" === item.convertType|| "dept" === item.convertType || "role" === item.convertType || "dictCode" === item.convertType){
                            column.formatter = function(value, row, index) {
                                return $.queryportui.id2Name(item.convertType,value);
                            };
                        }
                        else{
                            column.formatter = function(value, row, index) {
                                if(item.columnLink){
                                    return "<a href='javascript:;' onclick='$.modal.openTab(\""+item.cname+"详情\",\""+row[item.columnLink]+"\")'>"+value+"</a>";
                                }else{
                                    return value;
                                }
                            };
                        }
                        options.columns.push(column);
                    }
                });
                if(queryportMain.mergecols){
                    options.onLoadSuccess= function (data) {
                        $('#bootstrap-queryport-table'+queryportMain.id).tablesMergeCell({
                            cols:queryportMain.mergecols.split(",")
                        });
                    }
                }
                if("N"==queryportMain.pagination){
                    options.pagination=false;
                }
                $.table.init(options);

                var urlValueLoad;
                //加载url上的参数
                $.each(queryportMain.queryportParams,function (k,v) {
                    var urlValue = $.queryportui.getUrlParam(v.ename);
                    if(urlValue){
                        urlValueLoad = true;
                        $("#"+v.ename).val(urlValue);
                        //$.table.search();
                    }
                })
                if("board"==queryportMain.type){
                    $('button[name="showSearch"]').remove();
                }
                if("Y" == queryportMain.ifFirstLoad || urlValueLoad){
                    $.table.search();
                }
            },
            /**
             * initQueryportSelectOptions
             * 加载时间
             */
            initCustomPrivateDateOptions:function() {
                $("body").find(".time-input").each(function (n, element) {
                    var id=element.id;
                    var li=$("#"+id).parent();
                    li.addClass("select-time");
                    console.log(li.html());
                    //element.parent().className="select-time";
                    var dateEname = $(element).attr("date-ename");
                    console.log(dateEname);
                    var dateCname = $(element).attr("date-cname");
                    console.log(dateCname);
                    li.html("<p>"+dateCname+"：</p> " +
                        "<input type=\"text\" class=\"time-input\" id=\"begin"+dateEname+"\" placeholder=\"开始时间\" name=\"params[begin"+dateEname+"]\"/> " +
                        "<span>-</span> " +
                        "<input type=\"text\" class=\"time-input\" id=\"end"+dateEname+"\" placeholder=\"结束时间\" name=\"params[end"+dateEname+"]\"/>");
                });
            },
            /**
             * 合并单元格
             * @param data  原始数据（在服务端完成排序）
             * @param fieldName 合并属性名称
             * @param colspan   合并列
             * @param target    目标表格对象
             */
            mergeCells:function(data, exhibitionName, fieldName, colspan, target){
                //声明一个map计算相同属性值在data对象出现的次数和
                var sortMap = {};
                for(var i = 0 ; i < data.length ; i++){
                    for(var prop in data[i]){
                        if(prop == exhibitionName){
                            var key = data[i][prop]
                            if(sortMap.hasOwnProperty(key)){
                                sortMap[key] = sortMap[key] * 1 + 1;
                            } else {
                                sortMap[key] = 1;
                            }
                            break;
                        }
                    }
                }
                for(var prop in sortMap){
                    console.log(prop,sortMap[prop])
                }
                var index = 0;
                for(var prop in sortMap){
                    var count = sortMap[prop] * 1;
                    $(target).bootstrapTable('mergeCells',{index:index, field:fieldName, colspan: colspan, rowspan: count});
                    index += count;
                }
            },

            /**
             * 获取视图得x/y轴数据
             * @param columnResult
             * @param xaxis
             */
            getQueryportChartXData:function(columnResult, xaxis) {
                var xdata = {};
                columnResult.forEach(function (item, x) {
                    if (item.id == xaxis) {
                        xdata = item;
                    }
                });
                return xdata;
            },
            /**
             * 获取echartdata
             * @param callback
             */
            getQueryportChartData:function(result, xdata, ydatas) {
                var echart_data=[];
                result.rows.forEach(function (item, i) {
                    ydatas.forEach(function (ydata, i) {
                        if("dict"==xdata.convertType||"dept"==xdata.convertType||"role"==xdata.convertType){
                            echart_data.push({"title":$.exdSelect.selectDictLabelOnly(xdata.typeParam1,item[xdata.ename]), "get_score":item[ydata.ename], "score_link":item[ydata.columnLink], "project_name":ydata.cname});
                        }else{
                            echart_data.push({"title":item[xdata.ename], "get_score":item[ydata.ename], "score_link":item[ydata.columnLink], "project_name":ydata.cname});
                        }

                    });

                });
                return echart_data;
            },
            /**
             * 获取echartconfig
             * @param callback
             */
            getQueryportChartConfig:function(chartResult) {
                var echart_config={};
                echart_config['title']=chartResult.title;
                echart_config['type']=chartResult.shapeType;
                echart_config['name_key']="title";
                echart_config['value_key']="get_score";
                echart_config['data_key']="project_name";
                return echart_config;
            },

            /**
             * 获取视图得y轴数据
             * @param yaxiss
             * @param columnResult
             * @returns {[]}
             */
            getQueryportChartYdata:function(yaxiss, columnResult) {
                var ydata = [];
                yaxiss.forEach(function (yaxis, y) {
                    columnResult.forEach(function (item, a) {
                        if (item.id == yaxis) {
                            ydata.push(item);
                        }
                    });
                });
                return ydata;
            }
        }
    });

})(jQuery);










