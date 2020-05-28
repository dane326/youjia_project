/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

// 当前table相关信息
var table = {
    config: {},
    // 当前实例配置
    options: {},
    // 设置实例配置
    set: function(id) {
        if($.common.getLength(table.config) > 1) {
            var tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find(".table").attr("id") : id;
            if ($.common.isNotEmpty(tableId)) {
                table.options = table.get(tableId);
            }
        }
    },
    // 获取实例配置
    get: function(id) {
        return table.config[id];
    },
    // 记住选择实例组
    rememberSelecteds: {},
    // 记住选择ID组
    rememberSelectedIds: {}
};

(function ($) {
    $.extend({
        _tree: {},
        _exdTree:{},
        bttTable: {},
        // 表格封装处理
        table: {
            // 初始化表格参数
            init: function(options) {
                var defaults = {
                    id: "bootstrap-table",
                    type: 0, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: undefined,
                    sidePagination: "server",
                    sortName: "",
                    sortOrder: "asc",
                    pagination: true,
                    pageSize: 10,
                    pageList: [10, 25, 50],
                    toolbar: "toolbar",
                    striped: false,
                    escape: false,
                    firstLoad: true,
                    showFooter: false,
                    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: true,
                    showColumns: true,
                    showToggle: true,
                    showExport: false,
                    clickToSelect: false,
                    singleSelect: false,
                    mobileResponsive: true,
                    rememberSelected: false,
                    fixedColumns: false,
                    fixedNumber: 0,
                    rightFixedColumns: false,
                    rightFixedNumber: 0,
                    queryParams: $.table.queryParams,
                    rowStyle: {},
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                $('#' + options.id).bootstrapTable({
                    id: options.id,
                    url: options.url,                                   // 请求后台的URL（*）
                    contentType: "application/x-www-form-urlencoded",   // 编码类型
                    method: 'post',                                     // 请求方式（*）
                    cache: false,                                       // 是否使用缓存
                    height: options.height,                             // 表格的高度
                    striped: options.striped,                           // 是否显示行间隔色
                    sortable: true,                                     // 是否启用排序
                    sortStable: true,                                   // 设置为 true 将获得稳定的排序
                    sortName: options.sortName,                         // 排序列名称
                    sortOrder: options.sortOrder,                       // 排序方式  asc 或者 desc
                    pagination: options.pagination,                     // 是否显示分页（*）
                    pageNumber: 1,                                      // 初始化加载第一页，默认第一页
                    pageSize: options.pageSize,                         // 每页的记录行数（*）
                    pageList: options.pageList,                         // 可供选择的每页的行数（*）
                    firstLoad: options.firstLoad,                       // 是否首次请求加载数据，对于数据较大可以配置false
                    escape: options.escape,                             // 转义HTML字符串
                    showFooter: options.showFooter,                     // 是否显示表尾
                    iconSize: 'outline',                                // 图标大小：undefined默认的按钮尺寸 xs超小按钮sm小按钮lg大按钮
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    sidePagination: options.sidePagination,             // server启用服务端分页client客户端分页
                    search: options.search,                             // 是否显示搜索框功能
                    searchText: options.searchText,                     // 搜索框初始显示的内容，默认为空
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showPageGo: options.showPageGo,               		// 是否显示跳转页
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    showToggle: options.showToggle,                     // 是否显示详细视图和列表视图的切换按钮
                    showExport: options.showExport,                     // 是否支持导出文件
                    uniqueId: options.uniqueId,                         // 唯 一的标识符
                    clickToSelect: options.clickToSelect,				// 是否启用点击选中行
                    singleSelect: options.singleSelect,                 // 是否单选checkbox
                    mobileResponsive: options.mobileResponsive,         // 是否支持移动端适配
                    detailView: options.detailView,                     // 是否启用显示细节视图
                    onClickRow: options.onClickRow,                     // 点击某行触发的事件
                    onDblClickRow: options.onDblClickRow,               // 双击某行触发的事件
                    onClickCell: options.onClickCell,                   // 单击某格触发的事件
                    onDblClickCell: options.onDblClickCell,             // 双击某格触发的事件
                    onEditableSave: options.onEditableSave,             // 行内编辑保存的事件
                    onExpandRow: options.onExpandRow,                   // 点击详细视图的事件
                    rememberSelected: options.rememberSelected,         // 启用翻页记住前面的选择
                    fixedColumns: options.fixedColumns,                 // 是否启用冻结列（左侧）
                    fixedNumber: options.fixedNumber,                   // 列冻结的个数（左侧）
                    rightFixedColumns: options.rightFixedColumns,       // 是否启用冻结列（右侧）
                    rightFixedNumber: options.rightFixedNumber,         // 列冻结的个数（右侧）
                    onReorderRow: options.onReorderRow,                 // 当拖拽结束后处理函数
                    queryParams: options.queryParams,                   // 传递参数（*）
                    rowStyle: options.rowStyle,                         // 通过自定义函数设置行样式
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.table.responseHandler,           // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.table.onLoadSuccess,               // 当所有数据被加载时触发处理函数
                    exportOptions: options.exportOptions,               // 前端导出忽略列索引
                    detailFormatter: options.detailFormatter,           // 在行下面展示其他数据列表
                });
            },
            // 获取实例ID，如存在多个返回#id1,#id2 delimeter分隔符
            getOptionsIds: function(separator) {
                var _separator = $.common.isEmpty(separator) ? "," : separator;
                var optionsIds = "";
                $.each(table.config, function(key, value){
                    optionsIds += "#" + key + _separator;
                });
                return optionsIds.substring(0, optionsIds.length - 1);
            },
            // 查询条件
            queryParams: function(params) {
                var curParams = {
                    // 传递参数查询参数
                    pageSize:       params.limit,
                    pageNum:        params.offset / params.limit + 1,
                    searchValue:    params.search,
                    orderByColumn:  params.sort,
                    isAsc:          params.order
                };
                var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
                return $.extend(curParams, $.common.formToJSON(currentId));
            },
            // 请求获取数据后处理回调函数
            responseHandler: function(res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res);
                }
                if (res.code == 0) {
                    if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination == 'client') {
                        return res.rows;
                    } else {
                        if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                            var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                            $.each(res.rows, function(i, row) {
                                row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
                            })
                        }
                        return { rows: res.rows, total: res.total };
                    }
                } else {
                    $.modal.alertWarning(res.msg);
                    return { rows: [], total: 0 };
                }
            },
            // 初始化事件
            initEvent: function() {
                // 实例ID信息
                var optionsIds = $.table.getOptionsIds();
                // 监听事件处理
                // 选中、取消、全部选中、全部取消（事件）
                $(optionsIds).on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function (e, rows) {
                    table.set($(this).attr("id"));
                    // 复选框分页保留保存选中数组
                    var rowIds = $.table.affectedRowIds(rows);
                    if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                        func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
                        var selectedIds = table.rememberSelectedIds[table.options.id];
                        if($.common.isNotEmpty(selectedIds)) {
                            table.rememberSelectedIds[table.options.id] = _[func](selectedIds, rowIds);
                        } else {
                            table.rememberSelectedIds[table.options.id] = _[func]([], rowIds);
                        }
                        var selectedRows = table.rememberSelecteds[table.options.id];
                        if($.common.isNotEmpty(selectedRows)) {
                            table.rememberSelecteds[table.options.id] = _[func](selectedRows, rows);
                        } else {
                            table.rememberSelecteds[table.options.id] = _[func]([], rows);
                        }
                    }
                });
                // 加载成功、选中、取消、全部选中、全部取消（事件）
                $(optionsIds).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function () {
                    var toolbar = table.options.toolbar;
                    var uniqueId = table.options.uniqueId;
                    // 工具栏按钮控制
                    var rows = $.common.isEmpty(uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(uniqueId);
                    // 非多个禁用
                    $('#' + toolbar + ' .multiple').toggleClass('disabled', !rows.length);
                    // 非单个禁用
                    $('#' + toolbar + ' .single').toggleClass('disabled', rows.length!=1);
                });
                // 图片预览事件
                $(optionsIds).off("click").on("click", '.img-circle', function() {
                    var src = $(this).attr('src');
                    var target = $(this).data('target');
                    var height = $(this).data('height');
                    var width = $(this).data('width');
                    if($.common.equals("self", target)) {
                        layer.open({
                            title: false,
                            type: 1,
                            closeBtn: true,
                            shadeClose: true,
                            area: ['auto', 'auto'],
                            content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
                        });
                    } else if ($.common.equals("blank", target)) {
                        window.open(src);
                    }
                });
                // 单击tooltip事件
                $(optionsIds).on("click", '.tooltip-show', function() {
                    var target = $(this).data('target');
                    var input = $(this).prev();
                    if ($.common.equals("copy", target)) {
                        input.select();
                        document.execCommand("copy");
                    } else if ($.common.equals("open", target)) {
                        parent.layer.alert(input.val(), {
                            title: "信息内容",
                            shadeClose: true,
                            btn: ['确认'],
                            btnclass: ['btn btn-primary'],
                        });
                    }
                });
            },
            // 当所有数据被加载时触发
            onLoadSuccess: function(data) {
                if (typeof table.options.onLoadSuccess == "function") {
                    table.options.onLoadSuccess(data);
                }
                // 浮动提示框特效
                $(".table [data-toggle='tooltip']").tooltip();
                // 气泡弹出框特效
                $('.table [data-toggle="popover"]').each(function() {
                    $(this).popover({ trigger: "manual", html: true, animation: false, container: "body", placement: "left"
                    }).on("mouseenter",
                        function() {
                            var _this = this;
                            $(this).popover("show");
                            $(".popover").on("mouseleave", function() {
                                $(_this).popover('hide');
                            });
                        }).on("mouseleave",
                        function() {
                            var _this = this;
                            setTimeout(function() {
                                if (!$(".popover:hover").length)
                                    $(_this).popover("hide");
                            }, 100);
                        });
                });
            },
            // 表格销毁
            destroy: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('destroy');
            },
            // 序列号生成
            serialNumber: function (index, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                var tableParams = $("#" + currentId).bootstrapTable('getOptions');
                var pageSize = tableParams.pageSize;
                var pageNumber = tableParams.pageNumber;
                return pageSize * (pageNumber - 1) + index + 1;
            },
            // 列超出指定长度浮动提示 target（copy单击复制文本 open弹窗打开文本）
            tooltip: function (value, length, target) {
                var _length = $.common.isEmpty(length) ? 20 : length;
                var _text = "";
                var _value = $.common.nullToStr(value);
                var _target = $.common.isEmpty(target) ? 'copy' : target;
                if (_value.length > _length) {
                    _text = _value.substr(0, _length) + "...";
                    _value = _value.replace(/\'/g,"&apos;");
                    _value = _value.replace(/\"/g,"&quot;");
                    var actions = [];
                    actions.push($.common.sprintf('<input id="tooltip-show" style="opacity: 0;position: absolute;z-index:-1" type="text" value="%s"/>', _value));
                    actions.push($.common.sprintf('<a href="###" class="tooltip-show" data-toggle="tooltip" data-target="%s" title="%s">%s</a>', _target, _value, _text));
                    return actions.join('');
                } else {
                    _text = _value;
                    return _text;
                }
            },
            // 下拉按钮切换
            dropdownToggle: function (value) {
                var actions = [];
                actions.push('<div class="btn-group">');
                actions.push('<button type="button" class="btn btn-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false">');
                actions.push('<i class="fa fa-cog"></i>&nbsp;<span class="fa fa-chevron-down"></span></button>');
                actions.push('<ul class="dropdown-menu">');
                actions.push(value.replace(/<a/g,"<li><a").replace(/<\/a>/g,"</a></li>"));
                actions.push('</ul>');
                actions.push('</div>');
                return actions.join('');
            },
            // 图片预览
            imageView: function (value, height, width, target) {
                if ($.common.isEmpty(width)) {
                    width = 'auto';
                }
                if ($.common.isEmpty(height)) {
                    height = 'auto';
                }
                // blank or self
                var _target = $.common.isEmpty(target) ? 'self' : target;
                if ($.common.isNotEmpty(value)) {
                    return $.common.sprintf("<img class='img-circle img-xs' data-height='%s' data-width='%s' data-target='%s' src='%s'/>", height, width, _target, value);
                } else {
                    return $.common.nullToStr(value);
                }
            },
            // 搜索-默认第一个form
            search: function(formId, tableId, data) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
                params.queryParams = function(params) {
                    var search = $.common.formToJSON(currentId);
                    if($.common.isNotEmpty(data)){
                        $.each(data, function(key) {
                            search[key] = data[key];
                        });
                    }
                    search.pageSize = params.limit;
                    search.pageNum = params.offset / params.limit + 1;
                    search.searchValue = params.search;
                    search.orderByColumn = params.sort;
                    search.isAsc = params.order;
                    return search;
                }
                if($.common.isNotEmpty(tableId)){
                    $("#" + tableId).bootstrapTable('refresh', params);
                } else{
                    $("#" + table.options.id).bootstrapTable('refresh', params);
                }
            },
            // 导出数据
            exportExcel: function(formId) {
                table.set();
                $.modal.confirm("确定导出所有" + table.options.modalName + "吗？", function() {
                    var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                    var params = $("#" + table.options.id).bootstrapTable('getOptions');
                    var dataParam = $("#" + currentId).serializeArray();
                    dataParam.push({ "name": "orderByColumn", "value": params.sortName });
                    dataParam.push({ "name": "isAsc", "value": params.sortOrder });
                    $.modal.loading("正在导出数据，请稍后...");
                    $.post(table.options.exportUrl, dataParam, function(result) {
                        if (result.code == web_status.SUCCESS) {
                            window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg);
                        }
                        $.modal.closeLoading();
                    });
                });
            },
            // 下载模板
            importTemplate: function() {
                table.set();
                $.get(table.options.importTemplateUrl, function(result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                    } else if (result.code == web_status.WARNING) {
                        $.modal.alertWarning(result.msg)
                    } else {
                        $.modal.alertError(result.msg);
                    }
                });
            },
            // 导入数据
            importExcel: function(formId) {
                table.set();
                var currentId = $.common.isEmpty(formId) ? 'importTpl' : formId;
                layer.open({
                    type: 1,
                    area: ['400px', '230px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: '导入' + table.options.modalName + '数据',
                    content: $('#' + currentId).html(),
                    btn: ['<i class="fa fa-check"></i> 导入', '<i class="fa fa-remove"></i> 取消'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    btn1: function(index, layero){
                        var file = layero.find('#file').val();
                        if (file == '' || (!$.common.endWith(file, '.xls') && !$.common.endWith(file, '.xlsx'))){
                            $.modal.msgWarning("请选择后缀为 “xls”或“xlsx”的文件。");
                            return false;
                        }
                        var index = layer.load(2, {shade: false});
                        $.modal.disable();
                        var formData = new FormData(layero.find('form')[0]);
                        $.ajax({
                            url: table.options.importUrl,
                            data: formData,
                            cache: false,
                            contentType: false,
                            processData: false,
                            type: 'POST',
                            success: function (result) {
                                if (result.code == web_status.SUCCESS) {
                                    $.modal.closeAll();
                                    $.modal.alertSuccess(result.msg);
                                    $.table.refresh();
                                } else if (result.code == web_status.WARNING) {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertWarning(result.msg)
                                } else {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertError(result.msg);
                                }
                            }
                        });
                    }
                });
            },
            // 刷新表格
            refresh: function(tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('refresh', {
                    silent: true
                });
            },
            // 查询表格指定列值
            selectColumns: function(column) {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return row[column];
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(table.rememberSelecteds[table.options.id], function (row) {
                            return row[column];
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            // 获取当前页选中或者取消的行ID
            affectedRowIds: function(rows) {
                var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                var rowIds;
                if ($.isArray(rows)) {
                    rowIds = $.map(rows, function(row) {
                        return row[column];
                    });
                } else {
                    rowIds = [rows[column]];
                }
                return rowIds;
            },
            // 查询表格首列值
            selectFirstColumns: function() {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return row[table.options.columns[1].field];
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(selectedRows, function (row) {
                            return row[table.options.columns[1].field];
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            // 回显数据字典
            selectDictLabel: function(datas, value) {
                var actions = [];
                $.each(datas, function(index, dict) {
                    if (dict.dictValue == ('' + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false;
                    }
                });
                return actions.join('');
            },
            // 显示表格指定列
            showColumn: function(column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('showColumn', column);
            },
            // 隐藏表格指定列
            hideColumn: function(column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('hideColumn', column);
            }
        },
        // 表格树封装处理
        treeTable: {
            // 初始化表格
            init: function(options) {
                var defaults = {
                    id: "bootstrap-tree-table",
                    type: 1, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: 0,
                    rootIdValue: null,
                    ajaxParams: {},
                    toolbar: "toolbar",
                    striped: false,
                    expandColumn: 1,
                    showSearch: true,
                    showRefresh: true,
                    showColumns: true,
                    expandAll: true,
                    expandFirst: true
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                    code: options.code,                                 // 用于设置父子关系
                    parentCode: options.parentCode,                     // 用于设置父子关系
                    type: 'post',                                       // 请求方式（*）
                    url: options.url,                                   // 请求后台的URL（*）
                    data: options.data,                                 // 无url时用于渲染的数据
                    ajaxParams: options.ajaxParams,                     // 请求数据的ajax的data属性
                    rootIdValue: options.rootIdValue,                   // 设置指定根节点id值
                    height: options.height,                             // 表格树的高度
                    expandColumn: options.expandColumn,                 // 在哪一列上面显示展开按钮
                    striped: options.striped,                           // 是否显示行间隔色
                    bordered: false,                                     // 是否显示边框
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    expandAll: options.expandAll,                       // 是否全部展开
                    expandFirst: options.expandFirst,                   // 是否默认第一级展开--expandAll为false时生效
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.treeTable.responseHandler        // 当所有数据被加载时触发处理函数
                });
            },
            // 条件查询
            search: function(formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable('refresh', params);
            },
            // 刷新
            refresh: function() {
                $.bttTable.bootstrapTreeTable('refresh');
            },
            // 查询表格树指定列值
            selectColumns: function(column) {
                var rows = $.map($.bttTable.bootstrapTreeTable('getSelections'), function (row) {
                    return row[column];
                });
                return $.common.uniqueFn(rows);
            },
            // 请求获取数据后处理回调函数，校验异常状态提醒
            responseHandler: function(res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res);
                }
                if (res.code != undefined && res.code != 0) {
                    $.modal.alertWarning(res.msg);
                    return [];
                } else {
                    return res;
                }
            },
        },
        // 表单封装处理
        form: {
            // 表单重置
            reset: function(formId, tableId) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId)[0].reset();
                if (table.options.type == table_type.bootstrapTable) {
                    if($.common.isEmpty(tableId)){
                        $("#" + table.options.id).bootstrapTable('refresh');
                    } else{
                        $("#" + tableId).bootstrapTable('refresh');
                    }
                } else if (table.options.type == table_type.bootstrapTreeTable) {
                    if($.common.isEmpty(tableId)){
                        $("#" + table.options.id).bootstrapTreeTable('refresh', []);
                    } else{
                        $("#" + tableId).bootstrapTreeTable('refresh', []);
                    }
                }
            },
            // 获取选中复选框项
            selectCheckeds: function(name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function(i) {
                    if (0 == i) {
                        checkeds = $(this).val();
                    } else {
                        checkeds += ("," + $(this).val());
                    }
                });
                return checkeds;
            },
            // 获取选中下拉框项
            selectSelects: function(name) {
                var selects = "";
                $('#' + name + ' option:selected').each(function (i) {
                    if (0 == i) {
                        selects = $(this).val();
                    } else {
                        selects += ("," + $(this).val());
                    }
                });
                return selects;
            }
        },
        // 弹出层封装处理
        modal: {
            // 显示图标
            icon: function(type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 0;
                } else if (type == modal_status.SUCCESS) {
                    icon = 1;
                } else if (type == modal_status.FAIL) {
                    icon = 2;
                } else {
                    icon = 3;
                }
                return icon;
            },
            // 消息提示
            msg: function(content, type) {
                if (type != undefined) {
                    layer.msg(content, { icon: $.modal.icon(type), time: 1000, shift: 5 });
                } else {
                    layer.msg(content);
                }
            },
            // 消息提示
            msg: function(content, type, time) {
                if (type != undefined) {
                    layer.msg(content, { icon: $.modal.icon(type), time: time, shift: 5 });
                } else {
                    layer.msg(content);
                }
            },
            // 错误消息
            msgError: function(content) {
                $.modal.msg(content, modal_status.FAIL);
            },
            // 成功消息
            msgSuccess: function(content) {
                $.modal.msg(content, modal_status.SUCCESS);
            },
            // 警告消息
            msgWarning: function(content) {
                $.modal.msg(content, modal_status.WARNING);
            },
            // 拒绝消息
            msgDenied: function(content) {
                $.modal.msg(content, modal_status.WARNING,3000);
            },
            // 弹出提示
            alert: function(content, type) {
                layer.alert(content, {
                    icon: $.modal.icon(type),
                    title: "系统提示",
                    btn: ['确认'],
                    btnclass: ['btn btn-primary'],
                });
            },
            // 消息提示并刷新父窗体
            msgReload: function(msg, type) {
                layer.msg(msg, {
                        icon: $.modal.icon(type),
                        time: 500,
                        shade: [0.1, '#8F8F8F']
                    },
                    function() {
                        $.modal.reload();
                    });
            },
            // 错误提示
            alertError: function(content) {
                $.modal.alert(content, modal_status.FAIL);
            },
            // 成功提示
            alertSuccess: function(content) {
                $.modal.alert(content, modal_status.SUCCESS);
            },
            // 警告提示
            alertWarning: function(content) {
                $.modal.alert(content, modal_status.WARNING);
            },
            // 关闭窗体
            close: function () {
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            },
            // 关闭全部窗体
            closeAll: function () {
                layer.closeAll();
            },
            // 确认窗体
            confirm: function (content, callBack) {
                layer.confirm(content, {
                    icon: 3,
                    title: "系统提示",
                    btn: ['确认', '取消']
                }, function (index) {
                    layer.close(index);
                    callBack(true);
                });
            },
            // 弹出层指定宽度
            open: function (title, url, width, height, callback) {
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(callback)) {
                    callback = function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    }
                }
                layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    yes: callback,
                    cancel: function(index) {
                        return true;
                    }
                });
            },
            // 弹出层指定参数选项
            openOptions: function (options) {
                var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
                var _title = $.common.isEmpty(options.title) ? "系统窗口" : options.title;
                var _width = $.common.isEmpty(options.width) ? "800" : options.width;
                var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) : options.height;
                var _btn = ['<i class="fa fa-check"></i> 确认', '<i class="fa fa-close"></i> 关闭'];
                if ($.common.isEmpty(options.yes)) {
                    options.yes = function(index, layero) {
                        options.callBack(index, layero);
                    }
                }
                layer.open({
                    type: 2,
                    maxmin: true,
                    shade: 0.3,
                    title: _title,
                    fix: false,
                    area: [_width + 'px', _height + 'px'],
                    content: _url,
                    shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                    skin: options.skin,
                    btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
                    yes: options.yes,
                    cancel: function () {
                        return true;
                    }
                });
            },
            // 弹出层全屏
            openFull: function (title, url, width, height) {
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                var index = layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['确定', '关闭'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    yes: function(index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    },
                    cancel: function(index) {
                        return true;
                    }
                });
                layer.full(index);
            },
            // 选卡页方式打开
            openTab: function (title, url) {
                createMenuItem(url, title);
            },
            // 选卡页同一页签打开
            parentTab: function (title, url) {
                var dataId = window.frameElement.getAttribute('data-id');
                createMenuItem(url, title);
                closeItem(dataId);
            },
            // 关闭选项卡
            closeTab: function (dataId) {
                closeItem(dataId);
            },
            // 禁用按钮
            disable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
            },
            // 启用按钮
            enable: function() {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            // 打开遮罩层
            loading: function (message) {
                $.blockUI({ message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>' });
            },
            // 关闭遮罩层
            closeLoading: function () {
                setTimeout(function(){
                    $.unblockUI();
                }, 50);
            },
            // 重新加载
            reload: function () {
                parent.location.reload();
            }
        },
        // 操作封装处理
        operate: {
            // 提交数据
            submit: function(url, type, dataType, data, callback) {
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.ajaxSuccess(result);
                    }
                };
                $.ajax(config)
            },
         // 提交数据
        	submitV2: function(url, type, dataType, data, callback) {
            	var config = {
        	        url: url,
        	        type: type,
        	        dataType: dataType,
        	        data: data,
        	        beforeSend: function () {
        	        	$.modal.loading("处理中，请稍后...");
        	        },
        	        success: function(result) {
        	        	if (typeof callback == "function") {
        	        	    callback(result);
        	        	}
        	        	$.modal.closeLoading();
        	        }
        	    };
        	    $.ajax(config)
            },
            onlySubmit: function(url, type, dataType, data,needBefore, callback) {
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                    }
                };
                var config1 = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    contentType: "application/json",
                    data: JSON.stringify(data),
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                    }
                };
                //不为空需锁屏
                if(needBefore != '' && needBefore != null){
                    $.ajax(config1)
                }else{
                    $.ajax(config)
                }
            },
            // post请求传输
            post: function(url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback);
            },
            onlyPost: function(url, data,needBefore,callback) {
                $.operate.onlySubmit(url, "post", "json", data,needBefore, callback);
            },
            // get请求传输
            get: function(url, callback) {
                $.operate.submit(url, "get", "json", "", callback);
            },
            // get请求传输
            onlyGet: function(url, callback) {
                $.operate.onlySubmit(url, "get", "json", "", null, callback);
            },
         // get请求传输
            ajaxGet: function(url, callback) {
            	$.operate.submitV2(url, "get", "text", "", callback);
            },
            // 详细信息
            detail: function(id, width, height) {
                table.set();
                var _url = $.operate.detailUrl(id);
                var _width = $.common.isEmpty(width) ? "800" : width;
                var _height = $.common.isEmpty(height) ? ($(window).height() - 50) : height;
                //如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    _width = 'auto';
                    _height = 'auto';
                }
                var options = {
                    title: table.options.modalName + "详细",
                    width: _width,
                    height: _height,
                    url:  _url,
                    skin: 'layui-layer-gray',
                    btn: ['关闭'],
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                };
                $.modal.openOptions(options);
            },
            // 详细访问地址
            detailUrl: function(id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.detailUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = table.options.detailUrl.replace("{id}", id);
                }
                return url;
            },
            // 删除信息
            remove: function(id) {
                table.set();
                $.modal.confirm("确定删除该条" + table.options.modalName + "信息吗？", function() {
                    var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
                    if(table.options.type == table_type.bootstrapTreeTable) {
                        $.operate.get(url);
                    } else {
                        var data = { "ids": id };
                        $.operate.submit(url, "post", "json", data);
                    }
                });

            },
            // 批量删除信息
            removeAll: function() {
                table.set();
                var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("请至少选择一条记录");
                    return;
                }
                $.modal.confirm("确认要删除选中的" + rows.length + "条数据吗?", function() {
                    var url = table.options.removeUrl;
                    var data = { "ids": rows.join() };
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // 清空信息
            clean: function() {
                table.set();
                $.modal.confirm("确定清空所有" + table.options.modalName + "吗？", function() {
                    var url = table.options.cleanUrl;
                    $.operate.submit(url, "post", "json", "");
                });
            },
            // 添加信息
            add: function(id) {
                table.set();
                $.modal.open("添加" + table.options.modalName, $.operate.addUrl(id));
            },
            // 扩充添加信息
            addOther: function(id) {
                $.modal.open("添加" + table.options.oTherModalName, $.operate.oTherAddUrl(id));
            },
            // 添加信息，以tab页展现
            addTab: function (id) {
                table.set();
                $.modal.openTab("添加" + table.options.modalName, $.operate.addUrl(id));
            },
            // 扩充添加信息
            addTabOther: function(id) {
                $.modal.openTab("添加" + table.options.oTherModalName, $.operate.oTherAddUrl(id));
            },
            // 添加信息 全屏
            addFull: function(id) {
                table.set();
                var url = $.common.isEmpty(id) ? table.options.createUrl : table.options.createUrl.replace("{id}", id);
                $.modal.openFull("添加" + table.options.modalName, url);
            },
            // 添加访问地址
            addUrl: function(id) {
                var url = $.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
                return url;
            },
            // 扩展添加访问地址
            oTherAddUrl: function(id) {
                var url = $.common.isEmpty(id) ? table.options.createOtherUrl.replace("{id}", "") : table.options.createOtherUrl.replace("{id}", id);
                return url;
            },
            // 修改信息
            edit: function(id) {
                table.set();
                if($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
                    var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                    if ($.common.isEmpty(row)) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("修改" + table.options.modalName, url);
                } else {
                    $.modal.open("修改" + table.options.modalName, $.operate.editUrl(id));
                }
            },
            // 修改信息，以tab页展现
            editTab: function(id) {
                table.set();
                $.modal.openTab("修改" + table.options.modalName, $.operate.editUrl(id));
            },
            // 修改信息 全屏
            editFull: function(id) {
                table.set();
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id);
                } else {
                    var row = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    url = table.options.updateUrl.replace("{id}", row);
                }
                $.modal.openFull("修改" + table.options.modalName, url);
            },
            // 修改访问地址
            editUrl: function(id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("请至少选择一条记录");
                        return;
                    }
                    url = table.options.updateUrl.replace("{id}", id);
                }
                return url;
            },
            // 保存信息 刷新表格
            save: function(url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                        $.modal.disable();
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successCallback(result);
                    }
                };
                $.ajax(config)
            },
            // 保存信息带文件 刷新表格
            saveWithFile: function(url, formId, callback) {
            	var form = new FormData(document.getElementById(formId));
            	var config = {
        	        url: url,
        	        type: "post",
        	        data: form,
        	        cache: false,
        	        processData: false,
        	        contentType: false,
        	        beforeSend: function () {
        	        	$.modal.loading("正在处理中，请稍后...");
        	        	$.modal.disable();
        	        },
        	        success: function(result) {
        	        	if (typeof callback == "function") {
        	        	    callback(result);
        	        	}
        	        	//$.modal.closeLoading();
        	        }
        	    };
        	    $.ajax(config)
            },
            // 保存信息 弹出提示框
            saveModal: function(url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        if (result.code == web_status.SUCCESS) {
                            $.modal.alertSuccess(result.msg)
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg);
                        }
                        $.modal.closeLoading();
                    }
                };
                $.ajax(config)
            },
            // 保存选项卡信息
            saveTab: function(url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("正在处理中，请稍后...");
                    },
                    success: function(result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successTabCallback(result);
                    }
                };
                $.ajax(config)
            },
            // 保存结果弹出msg刷新table表格
            ajaxSuccess: function (result) {
                if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTable) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                } else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
                    $.modal.msgSuccess(result.msg);
                    $.treeTable.refresh();
                } else if (result.code == web_status.SUCCESS && table.option.type == undefined) {
                    $.modal.msgSuccess(result.msg)
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else if (result.code == web_status.DENIED) {
                    $.modal.msgDenied(result.msg)
                }  else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功结果提示msg（父窗体全局更新）
            saveSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else if (result.code == web_status.DENIED) {
                    $.modal.msgDenied(result.msg)
                }  else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功回调执行事件（父窗体静默更新）
            successCallback: function(result) {
                if (result.code == web_status.SUCCESS) {
                    var parent = window.parent;
                    if (parent.table.options.type == table_type.bootstrapTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.table.refresh();
                    } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.treeTable.refresh();
                    } else {
                        $.modal.msgReload("保存成功,正在刷新数据请稍后……", modal_status.SUCCESS);
                    }
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else if (result.code == web_status.DENIED) {
                    $.modal.msgDenied(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
                $.modal.enable();
            },
            // 选项卡成功回调执行事件（父窗体静默更新）
            successTabCallback: function(result) {
                if (result.code == web_status.SUCCESS) {
                    var topWindow = $(window.parent.document);
                    var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-panel');
                    var $contentWindow = $('.gsh_iframe[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
                    $.modal.close();
                    $contentWindow.$.modal.msgSuccess(result.msg);
                    $contentWindow.$(".layui-layer-padding").removeAttr("style");
                    if ($contentWindow.table.options.type == table_type.bootstrapTable) {
                        $contentWindow.$.table.refresh();
                    } else if ($contentWindow.table.options.type == table_type.bootstrapTreeTable) {
                        $contentWindow.$.treeTable.refresh();
                    }
                    $.modal.closeTab();
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else if (result.code == web_status.DENIED) {
                    $.modal.msgDenied(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            postCurrent:function(url,params){
                var form = $("<form method='post'></form>");
                var input;
                form.attr({"action":url});
                $.each(params,function (key,value) {
                    input = $("<input type='hidden'>");
                    input.attr({"name":key});
                    input.val(value);
                    form.append(input);
                });
                $(document.body).append(form);
                form.submit();
            }
        },
        // 校验封装处理
        validate: {
            // 判断返回标识是否唯一 false 不存在 true 存在
            unique: function (value) {
                if (value == "0") {
                    return true;
                }
                return false;
            },
            // 表单验证
            form: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().form();
            },
            // 重置表单验证（清除提示信息）
            reset: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().resetForm();
            }
        },
        // 树插件封装处理
        tree: {
            _option: {},
            _lastValue: {},
            // 初始化树结构
            init: function(options) {
                var defaults = {
                    id: "tree",                    // 属性ID
                    expandLevel: 0,                // 展开等级节点
                    view: {
                        selectedMulti: false,      // 设置是否允许同时选中多个节点
                        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                    },
                    check: {
                        enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
                        nocheckInherit: true,      // 设置子节点是否自动继承
                    },
                    data: {
                        key: {
                            title: "title"         // 节点数据保存节点提示信息的属性名称
                        },
                        simpleData: {
                            enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
                        }
                    },
                };
                var options = $.extend(defaults, options);
                $.tree._option = options;
                // 树结构初始化加载
                var setting = {
                    callback: {
                        onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
                        onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                        onDblClick: options.onDblClick,                 // 用于捕获鼠标双击之后的事件回调函数
                        beforeClick:options.beforeClick					//用于捕获点击之前做判断
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data
                };
                $.get(options.url, function(data) {
                    var treeId = $("#treeId").val();
                    tree = $.fn.zTree.init($("#" + options.id), setting, data);
                    $._tree = tree;
                    var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
                    for (var i = 0; i < nodes.length; i++) {
                        tree.expandNode(nodes[i], true, false, false);
                    }
                    var node = tree.getNodesByParam("id", treeId, null)[0];
                    $.tree.selectByIdName(treeId, node);
                });
            },
            // 搜索节点
            searchNode: function() {
                // 取得输入的关键字的值
                var value = $.common.trim($("#keyword").val());
                if ($.tree._lastValue == value) {
                    return;
                }
                // 保存最后一次搜索名称
                $.tree._lastValue = value;
                var nodes = $._tree.getNodes();
                // 如果要查空字串，就退出不查了。
                if (value == "") {
                    $.tree.showAllNode(nodes);
                    return;
                }
                $.tree.hideAllNode(nodes);
                // 根据搜索值模糊匹配
                $.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value));
            },
            // 根据Id和Name选中指定节点
            selectByIdName: function(treeId, node) {
                if ($.common.isNotEmpty(treeId) && treeId == node.id) {
                    $._tree.selectNode(node, true);
                }
            },
            // 显示所有节点
            showAllNode: function(nodes) {
                nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    if (nodes[i].getParentNode() != null) {
                        $._tree.expandNode(nodes[i], true, false, false, false);
                    } else {
                        $._tree.expandNode(nodes[i], true, true, false, false);
                    }
                    $._tree.showNode(nodes[i]);
                    $.tree.showAllNode(nodes[i].children);
                }
            },
            // 隐藏所有节点
            hideAllNode: function(nodes) {
                var tree = $.fn.zTree.getZTreeObj("tree");
                var nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    $._tree.hideNode(nodes[i]);
                }
            },
            // 显示所有父节点
            showParent: function(treeNode) {
                var parentNode;
                while ((parentNode = treeNode.getParentNode()) != null) {
                    $._tree.showNode(parentNode);
                    $._tree.expandNode(parentNode, true, false, false);
                    treeNode = parentNode;
                }
            },
            // 显示所有孩子节点
            showChildren: function(treeNode) {
                if (treeNode.isParent) {
                    for (var idx in treeNode.children) {
                        var node = treeNode.children[idx];
                        $._tree.showNode(node);
                        $.tree.showChildren(node);
                    }
                }
            },
            // 更新节点状态
            updateNodes: function(nodeList) {
                $._tree.showNodes(nodeList);
                for (var i = 0, l = nodeList.length; i < l; i++) {
                    var treeNode = nodeList[i];
                    $.tree.showChildren(treeNode);
                    $.tree.showParent(treeNode)
                }
            },
            // 获取当前被勾选集合
            getCheckedNodes: function(column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._tree.getCheckedNodes(true);
                return $.map(nodes, function (row) {
                    return row[_column];
                }).join();
            },
            // 不允许根父节点选择
            notAllowParents: function(_tree) {
                var nodes = _tree.getSelectedNodes();
                if(nodes.length == 0){
                    $.modal.msgError("请选择节点后提交");
                    return false;
                }
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].level == 0) {
                        $.modal.msgError("不能选择根节点（" + nodes[i].name + "）");
                        return false;
                    }
                    if (nodes[i].isParent) {
                        $.modal.msgError("不能选择父节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 不允许最后层级节点选择
            notAllowLastLevel: function(_tree) {
                var nodes = _tree.getSelectedNodes();
                for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.msgError("不能选择最后层级节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 隐藏/显示搜索栏
            toggleSearch: function() {
                $('#search').slideToggle(200);
                $('#btnShow').toggle();
                $('#btnHide').toggle();
                $('#keyword').focus();
            },
            // 折叠
            collapse: function() {
                $._tree.expandAll(false);
            },
            // 展开
            expand: function() {
                $._tree.expandAll(true);
            }
        },
        // 树插件封装处理
        exdTree: {
            _option: {},
            _lastValue: {},
            // 初始化树结构
            init: function(options) {
                if(!options.id || !options.url){
                    console.log("绑定exdtree的dom的id和url必须指定");
                    return false;
                }
                var exdTreeId= options.id+"MyTree";
                var exdTreeDiv= options.id+"Div";
                //默认配置
                var defaults = {
                    id: "",                    // 属性ID
                    title: "选择",              // 标题title
                    valueId:"",                // 隐藏值ID
                    valueTypeId:"",            // 返回类型值，可空
                    rootData:{},                 //用于过滤root节点
                    revertKey:"key",           // 返回值的树节点key
                    revertValue:"value",       // 返回值显示的树节点key
                    expandLevel: 0,                // 展开等级节点 ,异步加载这里会失效
                    isPclick:false,                //父节点可选
                    enableAsync:true,              //默认为异步
                    view: {
                        selectedMulti: false,      // 设置是否允许同时选中多个节点
                        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                    },
                    checkBox:false,                //初始化多选 ,默认关闭
                    enableMulti:false,             //单选的多层模式
                    multiSeparator: "/",           //单选的多层模式的分隔符
                    check: {
                        enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
                        chkStyle: "checkbox",
                        chkboxType: { "Y": "", "N": ""}, //设置是否影响"p" 表示操作会影响父级节点；"s" 表示操作会影响子级节点。
                        radioType: "all"            //radio分组，可选level、all
                    },
                    data: {
                        key: {
                            title: "title"         // 节点数据保存节点提示信息的属性名称
                        },
                        simpleData:{
                            idKey:'id',
                            pIdKey:'pId',
                            //rootPId: 100,
                            enable: true
                        }
                    },
                    async: {
                        enable: true,             //异步加载
                        url: options.url,
                        autoParam:["id"],
                    },
                    showType:"select" , // 可选择：modal 模态框、select 下拉

                };
                var options = $.extend(true,defaults, options);   //深层合并对象
                if(options.checkBox){ // 设置多选
                    options.check.enable = true;
                }
                if(options.expandLevel > 0){ // 设置同步
                    options.enableAsync = false;
                }
                if(!options.enableAsync){ // 设置同步
                    options.async.enable = false;
                }
                if(options.multiSeparator != "/"){ //设置单选多层
                    options.enableMulti = true;
                    options.check.enable = false;
                }
                if(!$("#"+options.id).length>0){
                    $.modal.alertError(options.id+"初始化树图失败");
                    return;
                }
                //判断是选择还是点击初始化事件
                if(options.check.enable){
                    options.onClick = function(e, treeId, treeNode) {
                        $._exdTree[treeId].checkNode(treeNode, !treeNode.checked, true, true);
                    }
                    options.onCheck = function(e, treeId, treeNode) {
                        var nodes = $._exdTree[exdTreeId].getCheckedNodes(true);
                        var nValues="",vValues = "",tValues = "";
                        for (var i=0, l=nodes.length; i<l; i++) {
                            nValues += nodes[i][options.revertValue] + ",";
                            vValues += nodes[i][options.revertKey] + ",";
                            tValues += nodes[i]["type"] + ",";
                        }
                        if (nValues.length > 0 ) nValues = nValues.substring(0, nValues.length-1);
                        if (vValues.length > 0 ) vValues = vValues.substring(0, vValues.length-1);
                        if (tValues.length > 0 ) tValues = tValues.substring(0, tValues.length-1);
                        $("#"+options.id).attr("value", nValues);
                        $("#"+options.valueId).attr("value", vValues);
                        $("#"+options.valueTypeId).attr("value", tValues);
                        log.info("value:" + vValues);
                        log.info("name:" + nValues);
                        log.info("type:" + tValues);
                    }
                    if(!options.isPclick && options.beforeCheck == null){
                        options.beforeCheck= function (treeId, treeNode) {
                            return !treeNode.isParent;//当是父节点 返回false 不让选取
                        };
                    }
                }else{
                    options.onClick = function(e, treeId, treeNode) {
                        var nValues="",vValues = "",tValues = "";
                        nValues = treeNode[options.revertValue];
                        vValues = treeNode[options.revertKey];
                        tValues = treeNode["type"];
                        if($.exdTree._option[treeId].enableMulti){
                            var multiSeparator = $.exdTree._option[treeId].multiSeparator;
                            var node = treeNode.getParentNode();
                            while(node){
                                nValues = node[options.revertValue] + multiSeparator + nValues;
                                vValues = node[options.revertKey] + multiSeparator + vValues;
                                tValues = node["type"] + multiSeparator + tValues;
                                node = node.getParentNode();
                            }
                        }
                        $("#"+options.id).attr("value", nValues);
                        $("#"+options.valueId).attr("value", vValues);
                        $("#"+options.valueTypeId).attr("value", tValues);
                        log.info("value:" + vValues);
                        log.info("name:" + nValues);
                        log.info("type:" + tValues);
                    }
                    if(!options.isPclick && options.beforeClick == null) {
                        options.beforeClick = function (treeId, treeNode, clickFlag) {
                            return !treeNode.isParent;//当是父节点 返回false 不让选取
                        };
                    }
                }
                //默认异步选中
                options.onAsyncSuccess =  function (event, treeId, treeNode, msg) {
                    $.exdTree.selectByIdName(treeId);
                };
                $.exdTree._option[exdTreeId] = options;
                // 树结构初始化加载
                var setting = {
                    callback: {
                        onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
                        onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                        onDblClick: options.onDblClick,                 // 用于捕获鼠标双击之后的事件回调函数
                        beforeClick:options.beforeClick,				//用于捕获点击之前做判断
                        beforeCheck:options.beforeCheck,				//用于捕获选择之前做判断
                        onAsyncSuccess:options.onAsyncSuccess			//用于捕获选择之前做判断
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data,
                    async: options.async
                };

                //拼接页面
                if(!$("#"+exdTreeDiv).length>0){
                    $("body").append($.exdTree.initTreeHtml(options));
                }

                $.modal.loading("正在加载，请稍后...");
                //是否异步加载
                var tree;
                if(options.async.enable){
                    $.post(options.url, options.rootData, function(data) {
                        tree = $.fn.zTree.init($("#" + exdTreeId), setting, data);
                        if(tree.getNodes().length > 0) { //让第一个父节点展开
                            tree.expandNode(tree.getNodes()[0], true, false, true);
                        }
                        $.exdTree.initMyTree(options,tree);
                        $.modal.closeLoading();
                    });
                }else{
                    $.get(options.url, options.rootData, function(data) {
                        tree = $.fn.zTree.init($("#" + exdTreeId), setting, data);
                        $.exdTree.initMyTree(options,tree);
                        $.modal.closeLoading();
                    });
                }
            },
            reInit: function(exdTreeId, options){
                $._exdTree[exdTreeId].destroy();
                $.exdTree.init(options);
            },
            // 搜索节点
            searchNode: function(exdTreeId) {
                // 取得输入的关键字的值
                var value = $.common.trim($("#"+exdTreeId+"keyword").val());
                if ($.exdTree._lastValue[exdTreeId] == value) {
                    return;
                }
                // 保存最后一次搜索名称
                $.exdTree._lastValue[exdTreeId] = value;
                var nodes = $._exdTree[exdTreeId].getNodes();
                //异步加载去后台模糊查询
                // 根据搜索值模糊匹配
                if($.exdTree._option[exdTreeId].async.enable){
                    $.post($.exdTree._option[exdTreeId].url,{"searchKey":value}, function(data) {
                        var setting = $._exdTree[exdTreeId].setting;
                        $._exdTree[exdTreeId].destroy();
                        tree = $.fn.zTree.init($("#" + exdTreeId), setting,data);
                        $._exdTree[exdTreeId] = tree;
                        $.exdTree.initMyTree( $.exdTree._option[exdTreeId],tree);
                    });
                }else{
                    // 如果要查空字串，就退出不查了。
                    if (value == "") {
                        $.exdTree.showAllNode(exdTreeId,nodes);
                        return;
                    }
                    $.exdTree.hideAllNode(exdTreeId,nodes);
                    $.exdTree.updateNodes(exdTreeId,$._exdTree[exdTreeId].getNodesByParamFuzzy("name", value));
                }
            },
            // 根据Id和Name选中指定节点
            selectByIdName: function(tree) {
                //默认选中
                var treeIdValue = $("#"+ $.exdTree._option[tree].valueId).val();
                //这里有的地方不存在valueId
                if($.common.isNotEmpty(treeIdValue)){
                    treeIdValue.split(",").map(function(value,index,array){
                        var node =  $._exdTree[tree].getNodesByParam($.exdTree._option[tree].revertKey, value, null)[0];
                        if (node && $.common.isNotEmpty(tree)) {
                            if($.exdTree._option[tree].checkBox){
                                $._exdTree[tree].checkNode(node, true, true);
                            }else{
                                $._exdTree[tree].selectNode(node, true);
                            }
                        }
                    });
                }
            },
            // 显示所有节点
            showAllNode: function(exdTreeId,nodes) {
                nodes = $._exdTree[exdTreeId].transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    if (nodes[i].getParentNode() != null) {
                        $._exdTree[exdTreeId].expandNode(nodes[i], true, false, false, false);
                    } else {
                        $._exdTree[exdTreeId].expandNode(nodes[i], true, true, false, false);
                    }
                    $._exdTree[exdTreeId].showNode(nodes[i]);
                    $.exdTree.showAllNode(exdTreeId,nodes[i].children);
                }
            },
            // 隐藏所有节点
            hideAllNode: function(exdTreeId,nodes) {
                var nodes = $._exdTree[exdTreeId].transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    $._exdTree[exdTreeId].hideNode(nodes[i]);
                }
            },
            // 显示所有父节点
            showParent: function(exdTreeId,treeNode) {
                var parentNode;
                while ((parentNode = treeNode.getParentNode()) != null) {
                    $._exdTree[exdTreeId].showNode(parentNode);
                    $._exdTree[exdTreeId].expandNode(parentNode, true, false, false);
                    treeNode = parentNode;
                }
            },
            // 显示所有孩子节点
            showChildren: function(exdTreeId,treeNode) {
                if (treeNode.isParent) {
                    for (var idx in treeNode.children) {
                        var node = treeNode.children[idx];
                        $._exdTree[exdTreeId].showNode(node);
                        $.exdTree.showChildren(exdTreeId,node);
                    }
                }
            },
            // 更新节点状态
            updateNodes: function(exdTreeId,nodeList) {
                $._exdTree[exdTreeId].showNodes(nodeList);
                for (var i = 0, l = nodeList.length; i < l; i++) {
                    var treeNode = nodeList[i];
                    $.exdTree.showChildren(exdTreeId,treeNode);
                    $.exdTree.showParent(exdTreeId,treeNode)
                }
            },
            // 获取当前选中集合
            getSelectedNodes: function(exdTreeId,column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes =  $._exdTree[exdTreeId].getSelectedNodes();
                return $.map(nodes, function (row) {
                    return row[_column];
                }).join();
            },
            // 获取当前被勾选集合
            getCheckedNodes: function(exdTreeId,column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._exdTree[exdTreeId].getCheckedNodes(true);
                return $.map(nodes, function (row) {
                    return row[_column];
                }).join();
            },
            // 不允许根父节点选择
            notAllowParents: function(exdTreeId) {
                var nodes = $._exdTree[exdTreeId].getSelectedNodes();
                if(nodes.length == 0){
                    $.modal.msgError("请选择节点后提交");
                    return false;
                }
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].level == 0) {
                        $.modal.msgError("不能选择根节点（" + nodes[i].name + "）");
                        return false;
                    }
                    if (nodes[i].isParent) {
                        $.modal.msgError("不能选择父节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 不允许最后层级节点选择
            notAllowLastLevel: function(exdTreeId) {
                var nodes = $._exdTree[exdTreeId].getSelectedNodes();
                for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.msgError("不能选择最后层级节点（" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 隐藏/显示搜索栏
            toggleSearch: function(exdTreeId) {
                $('#'+exdTreeId+'search').slideToggle(200);
                $('#'+exdTreeId+'btnShow').toggle();
                $('#'+exdTreeId+'btnHide').toggle();
                $('#'+exdTreeId+'keyword').focus();
            },
            // 隐藏/显示搜索栏
            confirmModal: function(exdTreeId) {
                var options = $.exdTree._option[exdTreeId];
                if(!$('#'+options.id).val()){
                    $.modal.msgWarning("请选择内容");
                    return false;
                }else{
                    $('#' + options.id + 'myModal').modal('hide');
                }
            },
            //通过参数初始化exdTree,必须放到load监听方法里
            initialize:function () {
                $.modal.loading("正在加载，请稍后...");
                $.each($("input"),function (k,v) {
                    if($(this).data("exdtree-options")){
                        var options = $(this).data("exdtree-options");
                        options.url = ctx + options.url;
                        options.id = $(this).attr("id");
                        var exdTreeId = options.id+"MyTree";
                        if($.common.isNotEmpty($.exdTree._option[exdTreeId])){
                            return;
                        }
                        if(!options.valueId){
                            options.valueId = options.id + "Id";
                        }
                        if(!options.valueTypeId){
                            options.valueTypeId = options.id + "TypeId";
                        }
                        $.exdTree.init(options);
                    }
                });
                $.modal.closeLoading();
            },
            initMyTree :function (options,tree) {
                var exdTreeId= options.id+"MyTree";
                var exdTreeDiv= options.id+"Div";
                var exdTreeCnt= options.id+"Cnt";
                var treeDivHeight = 280;
                if(!options.enableAsync){
                    var treeNodesSize = tree.transformToArray(tree.getNodes()).length; //获取树所有节点
                    treeDivHeight = (48 + 20) + treeNodesSize * 22;
                    treeDivHeight = treeDivHeight > 280 ? 280 : treeDivHeight;
                }
                $._exdTree[exdTreeId] = tree;
                //展开级别
                var nodes = tree.getNodesByParam("level", options.expandLevel - 1);
                for (var i = 0; i < nodes.length; i++) {
                    tree.expandNode(nodes[i], true, false, false);
                }
                //默认选中
                $.exdTree.selectByIdName(exdTreeId);
                //为当前input绑定事件
                $("#"+options.id).attr("readonly","readonly");
                $("#"+options.id).click(function () {
                    console.log($("#"+exdTreeCnt).outerHeight(true));
                    if(options.showType=="select"){
                        var treeDivWidth = $(this)[0].offsetWidth;
                        treeDivWidth = treeDivWidth < 300 ? 300 : treeDivWidth;
                        $("#"+exdTreeDiv).css({
                            left: $(this).offset().left + "px",
                            top: $(this).offset().top + $(this).outerHeight() + "px",
                            width: treeDivWidth + "px",
                            height: treeDivHeight + 'px',
                            overflow: 'auto',
                        }).slideDown("fast");
                        //body绑定事件
                        $("body").bind("mousedown", function (event) {
                            if (!(event.target.id == options.id || event.target.id == exdTreeDiv || $(event.target).parents("#"+exdTreeDiv).length>0)) {
                                $("#"+exdTreeDiv).fadeOut("fast");
                            }
                        });
                    }else if(options.showType=="modal"){
                        //显示模态框
                        $("#"+options.id+"myModal").modal();
                    }else{
                        $.modal.alertError("tree初始化显示类型错误！");
                    }

                })
            },
            initTreeHtml: function (options) {
                var exdTreeId= options.id+"MyTree";
                var exdTreeDiv= options.id+"Div";
                var exdTreeCnt= options.id+"Cnt";
                //想页面添加tree的html
                var htmlClass = options.showType=="modal"?"":"exdTree";
                var exdTreeHtml = "<div class='"+htmlClass+"' id=\""+exdTreeDiv+"\">";
                exdTreeHtml += "<div class=\"\" id=\"" + exdTreeCnt + "\">";
                // 同步 和 异步的非多层模式显示 显示 搜索
                if(!options.enableAsync || (options.enableAsync && !options.enableMulti)){
                    exdTreeHtml += "" +
                        "   <div class=\"treeShowHideButton\" onclick=\"$.exdTree.toggleSearch('"+exdTreeId+"');\">\n" +
                        "		<label id=\""+exdTreeId+"btnShow\" title=\"显示搜索\" style=\"display:none;\">︾</label>\n" +
                        "		<label id=\""+exdTreeId+"btnHide\" title=\"隐藏搜索\">︽</label>\n" +
                        "	</div>\n" +
                        //"   <button type=\"button\" class=\"btn btn-box-tool\" id=\"btnRefresh\" title=\"刷新\"><i class=\"fa fa-refresh\"></i></button>"+
                        "	<div class=\"treeSearchInput\" id=\""+exdTreeId+"search\">\n" +
                        "		<label for=\"keyword\">关键字：</label><input type=\"text\" class=\"empty\" id=\""+exdTreeId+"keyword\" maxlength=\"50\">\n" +
                        "		<button class=\"btn\" id=\"btn\" onclick=\"$.exdTree.searchNode('"+exdTreeId+"')\"> 搜索 </button>\n" +
                        "	</div>\n";
                }
                exdTreeHtml += "	<div class=\"treeExpandCollapse\">\n";
                // 异步 不支持 全部展开
                if(!options.enableAsync){
                    exdTreeHtml += "		<a href=\"javascript:;\" onclick=\"$._exdTree['"+exdTreeId+"'].expandAll(true)\">展开</a> /\n";
                }
                exdTreeHtml += "		<a href=\"javascript:;\" onclick=\"$._exdTree['"+exdTreeId+"'].expandAll(false)\">折叠</a>\n";
                exdTreeHtml += "	</div>\n"+
                    "<div id=\""+exdTreeId+"\" class=\"ztree treeselect\"></div>\n" +
                    "</div>\n"+
                    "</div>";
                if(options.showType == "modal"){
                    exdTreeHtml = "<!-- 模态框（Modal） -->\n" +
                        "<div class=\"modal fade\" id=\""+options.id+"myModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n" +
                        "    <div class=\"modal-dialog\" style='width: 360px;border-radius: 2px 2px 0 0;'>\n" +
                        "        <div class=\"modal-content\">\n" +
                        "            <div class=\"modal-header\" style='height: 42px;line-height: 42px;background:#F8F8F8;border-radius: 2px 2px 0 0;padding:12px;'>\n" +
                        "                <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">×</button>\n" +
                        "                <h4 class=\"modal-title\" id=\"myModalLabel\">" + options.title + "</h4>\n" +
                        "            </div>\n" +
                        "            <div class=\"modal-body\" style='height: 360px;overflow: auto;padding: 0px;'>"+exdTreeHtml+"</div>\n" +
                        "            <div class=\"modal-footer\" style='height: 50px;background: #f0f4f7;border-radius: 0px 0px 2px 2px; padding: 9px 18px;'>\n" +
                        "                <button type=\"button\" class=\"btn btn-sm btn-success\" onclick=\"$.exdTree.confirmModal('"+exdTreeId+"');\"><i class=\"fa fa-check\"></i>确认</button>\n" +
                        "                <button type=\"button\" class=\"btn btn-sm btn-white\" data-dismiss=\"modal\"><i class=\"fa fa-close\"></i>关闭</button>\n" +
                        "            </div>\n" +
                        "        </div><!-- /.modal-content -->\n" +
                        "    </div><!-- /.modal-dialog -->\n" +
                        "</div>\n" +
                        "<!-- /.modal -->"
                }
                return exdTreeHtml;
            }

        },
        // 下拉列表封装处理
        exdSelect: {
            _option: {},
            // 初始化树结构
            init: function (options) {
                //默认配置
                var defaults = {
                    id: "",                      // 属性ID
                    dictMode : "type",           // 模式 type/code
                    parentDict : "",             // 字典父节点值
                    parentCode : "",         // code模式字典父节点值
                    valueField: "dictValue", // 字典取值字段：type模式取值 dictValue，code模式取值 dictCode
                    relateId : "",               // 关联ID
                    selectType : "select",       // 显示类型 select/radio/checkbox
                    relateSuffix : "",           // 关联后缀
                    // url : ctx + "/system/dict/data/get/",
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
                if($.common.isNotEmpty(options.parentCode)){
                    options.dictMode = "code";
                    options.parentDict = options.parentCode;
                    options.valueField = "dictCode";
                    options.relateSuffix = ""; //mode模式不支持跨级
                }
                $.exdSelect._option[options.id] = options;
                //开始初始化html
                if(options.selectType == "select"){
                    if($.common.isEmpty(options.parentDict) || options.parentDict == "-7"){
                        $.exdSelect.initSelect(_this,options);
                    }else{
                        $.exdSelect.initSelect(_this,options,$.exdSelect.getDictData(options.parentDict, options.dictMode));
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
                    _this.html($.exdSelect.initRadioHtml($.exdSelect.getDictData(options.parentDict, options.dictMode),options));
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
                    _this.html($.exdSelect.initCheckBoxHtml($.exdSelect.getDictData(options.parentDict, options.dictMode),options));
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
                    _this.append($.exdSelect.initSelectHtml(data, options.defaultValue, options.valueField, multiple));
                }else{
                    _this.append($.exdSelect.initSelectHtml([], options.defaultValue, options.valueField, multiple));
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
                            var parentDict = $(this).data("parentDict");
                            parentDict = options.dictMode==="type" ? parentDict +"_"+ $(this).val() + options.relateSuffix : $(this).val();
                            //console.log("下级字典节点：" + parentDict);
                            var data = $.exdSelect.getDictData(parentDict, options.dictMode);
                            if(data.length > 0) {
                                $("#" + options.relateId).data("parentDict", parentDict);
                                $("#" + options.relateId).empty();
                                $("#" + options.relateId).append($.exdSelect.initSelectHtml(data, "", options.valueField));
                                $("#" + options.relateId).trigger("change");
                            }else{
                                console.log("未获取到字典数据：" + parentDict);
                            }
                        }else{
                            $("#" + options.relateId).empty();
                            $("#" + options.relateId).append($.exdSelect.initSelectHtml([]));
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
                $.each($(".custom-from-exdselect"),function (k,v) {
                    //data方法区分大小写，但是html不区分大小写，注意！！
                    if($(this).data("exdselect-options")){
                        var options = $(this).data("exdselect-options");
                        options.defaultValue=$(this).data("exdselect-value");
                        options.id = $(this).attr("id");
                        if($.common.isNotEmpty($.exdSelect._option[options.id])){
                            return ;
                        }
                        $.exdSelect.init(options);
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
            getDictData:function (parentDict, dictMode) {
                var mode = dictMode ? dictMode : "type";
                var data = [];
                if($.common.isEmpty(parentDict)){
                    return data;
                }
                //先查询缓存
                var jsonData = sessionStorage.getItem("dict-" + parentDict);
                if($.common.isEmpty(jsonData)){
                    $.exdSelect.get(ctx + "/system/dict/data/"+mode+"/"+parentDict, function(result) {
                        if(result.code == '0') {
                            data = result.data;
                            //放入缓存
                            if(data.length > 0){
                                sessionStorage.setItem("dict-" + parentDict, JSON.stringify(data));
                            }
                        }else{
                            //$.modal.msgError(result.msg);
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
                var datas = $.exdSelect.getDictData(parentDict,dictMode);
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
                var datas = $.exdSelect.getDictData(dictType);
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

        // 下拉列表封装处理
        exdFactoryDictSelect: {
            _option: {},
            // 初始化树结构
            init: function (options) {
                //默认配置
                var defaults = {
                    id: "",                      // 属性ID
                    parentCode : "",         // code模式字典父节点值
                    valueField: "dictCode", // 字典取值字段：type模式取值 dictValue，code模式取值 dictCode
                    relateId : "",               // 关联ID
                    selectType : "select",       // 显示类型 select/radio/checkbox
                    relateSuffix : "",           // 关联后缀
                    // url : ctx + "/system/dict/data/get/",
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

                $.exdFactoryDictSelect._option[options.id] = options;
                //开始初始化html
                if(options.selectType == "select"){
                    if($.common.isEmpty(options.parentCode) || options.parentCode == "-7"){
                        $.exdFactoryDictSelect.initSelect(_this,options);
                    }else{
                        $.exdFactoryDictSelect.initSelect(_this,options,$.exdFactoryDictSelect.getDictData(options.parentCode));
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
                    _this.html($.exdFactoryDictSelect.initRadioHtml($.exdFactoryDictSelect.getDictData(options.parentCode),options));
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
                    _this.html($.exdFactoryDictSelect.initCheckBoxHtml($.exdFactoryDictSelect.getDictData(options.parentCode),options));
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
                if(!multiple){
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
                    _this.data("parentCode",options.parentCode);
                    _this.append($.exdFactoryDictSelect.initSelectHtml(data, options.defaultValue, options.valueField, multiple));
                }else{
                    _this.append($.exdFactoryDictSelect.initSelectHtml([], options.defaultValue, options.valueField, multiple));
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
                            var parentCode= $(this).data("parentCode");
                            parentCode = options.dictMode==="type" ? parentCode +"_"+ $(this).val() + options.relateSuffix : $(this).val();
                            //console.log("下级字典节点：" + parentCode);
                            var data = $.exdFactoryDictSelect.getDictData(parentCode);
                            if(data.length > 0) {
                                $("#" + options.relateId).data("parentCode", parentCode);
                                $("#" + options.relateId).empty();
                                $("#" + options.relateId).append($.exdFactoryDictSelect.initSelectHtml(data, "", options.valueField));
                                $("#" + options.relateId).trigger("change");
                            }else{
                                console.log("未获取到字典数据：" + parentCode);
                            }
                        }else{
                            $("#" + options.relateId).empty();
                            $("#" + options.relateId).append($.exdFactoryDictSelect.initSelectHtml([]));
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
                $.each($(".custom-from-exdfactorydictselect"),function (k,v) {
                    //data方法区分大小写，但是html不区分大小写，注意！！
                    if($(this).data("exdfactorydictselect-options")){
                        var options = $(this).data("exdfactorydictselect-options");
                        options.defaultValue=$(this).data("exdfactorydictselect-value");
                        options.id = $(this).attr("id");
                        if($.common.isNotEmpty($.exdFactoryDictSelect._option[options.id])){
                            return ;
                        }
                        $.exdFactoryDictSelect.init(options);
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
            getDictData:function (parentCode) {
                //var mode = dictMode ? dictMode : "type";
                var data = [];
                if($.common.isEmpty(parentCode)){
                    return data;
                }
                //先查询缓存
                 var jsonData = sessionStorage.getItem("factiorydict-" + parentCode);
                if($.common.isEmpty(jsonData)){
                $.exdFactoryDictSelect.get(ctx + "/system/factory/factorydict/code/"+parentCode, function(result) {
                    if(result.code == '0') {
                        data = result.data;
                        //放入缓存
                        if(data.length > 0){
                            sessionStorage.setItem("factiorydict-" + parentCode, JSON.stringify(data));
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
            selectDictLabel: function(parentCode, value, dictMode) {
                var dictMode = dictMode ? dictMode : "type";
                var valueField = dictMode=="type" ? "dictValue" : "dictCode";
                var datas = $.exdFactoryDictSelect.getDictData(parentCode,dictMode);
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
                var datas = $.exdFactoryDictSelect.getDictData(dictType);
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
        // 通用方法封装处理
        common: {
            // 判断字符串是否为空
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "") {
                    return true;
                }
                return false;
            },
            // 判断一个字符串是否为非空串
            isNotEmpty: function (value) {
                return !$.common.isEmpty(value);
            },
            // 空对象转字符串
            nullToStr: function(value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            // 空对象转字符串
            nullToString: function(value) {
                if ($.common.isEmpty(value)) {
                    return "";
                }
                return value;
            },
            // 是否显示数据 为空默认为显示
            visible: function (value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            // 空格截取
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            // 比较两个字符串（大小写敏感）
            equals: function (str, that) {
                return str == that;
            },
            // 比较两个字符串（大小写不敏感）
            equalsIgnoreCase: function (str, that) {
                return String(str).toUpperCase() === String(that).toUpperCase();
            },
            // 将字符串按指定字符分割
            split: function (str, sep, maxLen) {
                if ($.common.isEmpty(str)) {
                    return null;
                }
                var value = String(str).split(sep);
                return maxLen ? value.slice(0, maxLen - 1) : value;
            },
            // 字符串格式化(%s )
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg;
                });
                return flag ? str : '';
            },
            // 指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            // 判断字符串是否是以start开头
            startWith: function(value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            // 判断字符串是否是以end结尾
            endWith: function(value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            // 数组去重
            uniqueFn: function(array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            // 数组中的所有元素放入一个字符串
            join: function(array, separator) {
                if ($.common.isEmpty(array)) {
                    return null;
                }
                return array.join(separator);
            },
            // 获取form下所有的字段并转换为json对象
            formToJSON: function(formId) {
                var json = {};
                $.each($("#" + formId).serializeArray(), function(i, field) {
                    if(json[field.name]) {
                        json[field.name] += ("," + field.value);
                    } else {
                        json[field.name] = field.value;
                    }
                });
                return json;
            },
            // 获取obj对象长度
            getLength: function(obj) {
                var count = 0;
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        count++;
                    }
                }
                return count;
            },
            // 判断移动端
            isMobile: function () {
                return navigator.userAgent.match(/(Android|iPhone|SymbianOS|Windows Phone|iPad|iPod)/i);
            },
            // 下划线转换驼峰
            toHump: function(name) {
                return name.replace(/\_(\w)/g, function(all, letter){
                    return letter.toUpperCase();
                });
            },
            // 驼峰转换下划线
            toLine: function(name) {
                return name.replace(/([A-Z])/g,"_$1").toLowerCase();
            },
            // 判断移动端
            isMobile: function () {
                return navigator.userAgent.match(/(Android|iPhone|SymbianOS|Windows Phone|iPad|iPod)/i);
            }
        }
    });
})(jQuery);

/** 表格类型 */
table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};

/** 消息状态码 */
web_status = {
    SUCCESS: 0,
    FAIL: 500,
    WARNING: 301,
    DENIED: 403
};

/** 弹窗状态码 */
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};



