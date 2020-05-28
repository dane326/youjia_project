(function ($) {
    $.extend({
        factoryDict: {
            _option: {},
            // 初始化树结构
            init: function (options) {
                //默认配置
                var defaults = {
                    id: "",                      // 属性ID
                    dictMode : "type",           // 模式 type/code
                    parentDict : "",             // 字典父节点值
                    valueField: "dictValue", // 字典取值字段：type模式取值 dictValue，code模式取值 dictCode
                    relateId : "",               // 关联ID
                    selectType : "select",       // 显示类型 select/radio/checkbox
                    relateSuffix : "",           // 关联后缀
                    defaultValue : "",
                    checkName : "",
                    onExdChange : null,    //select的扩展change事件
                    onExdChecked : null    //radio和checkbox的扩展checked事件
                };
                var options = $.extend(true, defaults, options);   //深层合并对象
                var _this = $("#"+options.id);
                //验证代码
                if($.common.isEmpty(options.id)){
                    $.modal.msgError("id不能为空");
                    return;
                }
                $.factoryDict._option[options.id] = options;
                //开始初始化html
                if(options.selectType == "select"){
                    if($.common.isEmpty(options.parentDict) || options.parentDict == "-7"){
                        $.factoryDict.initSelect(_this,options);
                    }else{
                        $.factoryDict.initSelect(_this,options,$.factoryDict.getDictData(options.parentDict));
                    }
                    // select2复选框事件绑定
                    if ($.fn.select2 !== undefined) {
                        $.fn.select2.defaults.set( "theme", "bootstrap" );
                        $("select.form-control:not(.noselect2)").each(function () {
                            $(this).select2().on("change", function () {
                                $(this).valid();
                            })
                        })
                    }
                }else if(options.selectType == "radio"){
                    _this.html($.factoryDict.initRadioHtml($.factoryDict.getDictData(options.parentDict),options));
                    //radio的扩展change事件
                    if ($.fn.iCheck !== undefined) {
                        $("#"+options.id).find("input[type=radio]").each(function() {
                            $(this).iCheck({
                                radioClass: 'iradio-blue',
                            });
                            // iCheck单选框及复选框事件绑定
                            if (typeof options.onExdChecked == "function") {
                                $(this).on("ifChecked", function () {
                                    options.onExdChecked(this);
                                })
                            }
                        })
                    }
                }else if(options.selectType == "checkBox"){
                    _this.html($.factoryDict.initCheckBoxHtml($.factoryDict.getDictData(options.parentDict),options));
                    // iCheck单选框及复选框事件绑定
                    if ($.fn.iCheck !== undefined) {
                        $("#"+options.id).find("input[type=checkbox]").each(function() {
                            $(this).iCheck({
                                checkboxClass: 'icheckbox-blue',
                            });
                            // iCheck单选框及复选框事件绑定
                            if (typeof options.onExdChecked == "function") {
                                $(this).on("ifChecked", function () {
                                    options.onExdChecked(this);
                                })
                            }
                        })
                    }
                }
            },
            initSelectHtml:function (data, defaultValue, valueField, multiple) {
                var optionHtml = "";
                if(!multiple&&data.length>0){
                    optionHtml += "<option value=''>请选择</option>";
                }
                $.each(data,function(k,dict){
                    if(dict[valueField] == defaultValue){
                        optionHtml += "<option value='"+dict[valueField]+"' selected>"+dict.dictLabel+"</option>\n";
                    }else{
                        optionHtml += "<option value='"+dict[valueField]+"'>"+dict.dictLabel+"</option>\n";
                    }
                });
                return optionHtml
            },
            initRadioHtml:function(data,options){
                var radioHtml = "";
                var name = $.common.isEmpty(options.checkName) ?options.id:options.checkName;
                $.each(data,function(k,dict){
                    var defaultValue = options.defaultValue ? options.defaultValue : data[0][options.valueField];
                    var checked = dict[options.valueField] == defaultValue ? "checked":"";
                    radioHtml += "<div class=\"radio-box\">\n" +
                        "<input type=\"radio\" id=\""+ name + dict.dictCode + "\" name=\"" + name + "\" value=\""+dict[options.valueField]+"\" "+checked+">\n" +
                        "<label for=\""+ name + dict.dictCode+"\" class='no-select-list'>"+dict.dictLabel+"</label>\n" +
                        "</div>";
                });
                return radioHtml
            },
            initCheckBoxHtml:function(data,options){
                var checkBoxHtml = "";
                var name = $.common.isEmpty(options.checkName) ?options.id:options.checkName;
                $.each(data,function(k,dict){
                    var checked = "";
                    if(options.defaultValue.split(",").indexOf(dict[options.valueField]+"")>-1){
                        checked = "checked";
                    }
                    checkBoxHtml += "<label class=\"check-box no-select-list\">\n" +
                        "<input id=\""+ name + dict.dictCode + "\" name=\""+name+"\" type=\"checkbox\" value=\""+dict[options.valueField]+"\" "+checked+">" + dict.dictLabel +
                        "</label>"
                });
                return checkBoxHtml
            },
            initSelect:function(_this,options,data){
                //_this.empty();
                var multiple = $("#"+options.id).attr("multiple");
                if(data){
                    _this.data("parentDict",options.parentDict);
                    _this.append($.factoryDict.initSelectHtml(data, options.defaultValue, options.valueField, multiple));
                }else{
                    _this.append($.factoryDict.initSelectHtml([], options.defaultValue, options.valueField, multiple));
                    _this.val(options.defaultValue);
                }
                // 多选赋值
                if(multiple){
                    options.relateId = ""; //多选不支持级联
                    if(options.defaultValue){
                        $("#"+options.id).val(options.defaultValue.split(",")).trigger("change");
                    }
                }
                //绑定关联事件
                if(options.relateId){
                    _this.change(function () {
                        if($(this).val()){
                            $("#"+this.id + "Name").val($(this).find("option:selected").text());
                            var parentDict= $(this).data("parentDict");
                            parentDict = options.dictMode==="type" ? parentDict +"_"+ $(this).val() + options.relateSuffix : $(this).val();
                            //console.log("下级字典节点：" + parentDict);
                            var data = $.factoryDict.getDictData(parentDict);
                            if(data.length > 0) {
                                $("#" + options.relateId).data("parentDict", parentDict);
                                $("#" + options.relateId).empty();
                                $("#" + options.relateId).append($.factoryDict.initSelectHtml(data, "", options.valueField));
                                $("#" + options.relateId).trigger("change");
                            }else{
                                console.log("未获取到字典数据：" + parentDict);
                            }
                        }else{
                            $("#" + options.relateId).empty();
                            $("#" + options.relateId).append($.factoryDict.initSelectHtml([]));
                            $("#" + options.relateId).trigger("change");
                            return;
                        }
                        //select的扩展change事件
                        if (typeof options.onExdChange == "function") {
                            options.onExdChange(this);
                        }
                    });
                    if(options.defaultValue && options.relateId){
                        _this.trigger("change");
                    }
                }else{
                    _this.change(function () {
                        if ($(this).val()) {
                            var selectedName = "";
                            $(this).find("option:selected").each(function (i,element) {
                                selectedName += $(element).text() + ",";
                            })
                            selectedName = selectedName ? selectedName.substring(0, selectedName.length-1) : "";
                            $("#" + this.id + "Name").val(selectedName);
                            console.log(this.id + ":" + $(this).val());
                            console.log(this.id + "Name:" + selectedName);
                        }
                        //select的扩展change事件
                        if (typeof options.onExdChange == "function") {
                            options.onExdChange(this);
                        }
                    });
                }
            },
            initialize:function(){
                $.modal.loading("正在加载，请稍后...");
                $.each($(".custom-from-factorydict"),function (k,v) {
                    //data方法区分大小写，但是html不区分大小写，注意！！
                    if($(this).data("factorydict-options")){
                        var options = $(this).data("factorydict-options");
                        options.defaultValue=$(this).data("factorydict-value");
                        options.id = $(this).attr("id");
                        if($.common.isNotEmpty($.factoryDict._option[options.id])){
                            return ;
                        }
                        $.factoryDict.init(options);
                    }
                });
                $.modal.closeLoading();
            },
            // 提交数据
            get: function(url, callback) {
                var config = {
                    url: url,
                    type: "get",
                    dataType: "json",
                    data: "",
                    async:false,
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                    }
                };
                $.ajax(config)
            },
            //获取字典值
            getDictData:function (parentDict) {
                //var mode = dictMode ? dictMode : "type";
                var data = [];
                if($.common.isEmpty(parentDict)){
                    return data;
                }
                //先查询缓存
                 var jsonData = sessionStorage.getItem("factiorydict-" + parentDict);
                if($.common.isEmpty(jsonData)){
                $.factoryDict.get(ctx + "/system/factory/dict/type/"+parentDict, function(result) {
                    if(result.code == '0') {
                        data = result.data;
                        //放入缓存
                        if(data.length > 0){
                            sessionStorage.setItem("factiorydict-" + parentDict, JSON.stringify(data));
                        }
                    }else{
                        $.modal.msgError(result.msg);
                    }
                });
                 }else{
                     data = JSON.parse(jsonData);
                 }
                return data;
            },
            // 回显数据字典带样式
            selectDictLabel: function(parentDict, value, dictMode) {
                var dictMode = dictMode ? dictMode : "type";
                var valueField = dictMode=="type" ? "dictValue" : "dictCode";
                var datas = $.factoryDict.getDictData(parentDict,dictMode);
                var actions = [];
                $.each(datas, function(index, dict) {
                    if (dict[valueField] == ('' + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false;
                    }
                });
                return actions.join('');
            },
            // 回显数据字典
            selectDictLabelOnly: function(dictType, value) {
                var datas = $.factoryDict.getDictData(dictType);
                var actions = [];
                $.each(datas, function(index, dict) {
                    if (dict.dictValue == ('' + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("%s", dict.dictLabel));
                        return false;
                    }
                });
                return actions.join('');
            },
        },
    });
})(jQuery);







