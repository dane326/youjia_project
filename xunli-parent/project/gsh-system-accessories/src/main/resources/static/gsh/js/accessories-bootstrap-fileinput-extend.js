/**
 * 通用文件上传js方法封装处理
 * Copyright (c) 2019 yigehui
 */
(function ($) {
    $.extend({
        localFileInputs:[],
        // 文件上传封装处理
        fileInputExtend: {
            // 初始化文件上传参数
            init: function (fileTypeInputElement, filesHiddenValueElement, options) {
                var defaults = {
                    language: 'zh',
                    maxFileSize: 10000,
                    readonly:false,
                    showPreview: false,
                    showUpload: false,
                    showRemove: false,
                    uploadAsync: true,
                    //elErrorContainer: '#kartik-file-errors',
                    allowedFileExtensions: ["jpg", "png", "gif"],
                    uploadUrl: ctx + 'system/accessories/upload'
                };
                var options = $.extend(defaults, options);
                var _fileTypeInputElement = $(fileTypeInputElement);
                var _filesHiddenValueElement = $(filesHiddenValueElement);

                //初始化页面附件
                if($.common.isNotEmpty(_filesHiddenValueElement.val())){
                    $.post(ctx+"system/accessories/listByIds", {ids:_filesHiddenValueElement.val()}, function(result) {
                        $.each(result.rows,function (index,value) {
                            //添加附件到当前控件上方
                            addAccessoryFileElement(_fileTypeInputElement,filesHiddenValueElement,value,options.readonly);
                        })
                    });
                }
                //初始化 fileinput
                $.localFileInputs.push(_fileTypeInputElement.fileinput(options).on('fileuploaded', function (event, data, previewId, index) {
                        $.modal.closeLoading();
                        if(data.response.code == 0){
                            var accessory = data.response.data;
                            var ev = _filesHiddenValueElement.val();
                            ev == ''?_filesHiddenValueElement.val(accessory.id):_filesHiddenValueElement.val(ev + ',' + accessory.id);
                            //添加附件到当前控件上方
                            addAccessoryFileElement(_fileTypeInputElement,filesHiddenValueElement,accessory);
                        }else{
                            $.modal.alertError(data.response.msg);
                        }
                        _fileTypeInputElement.fileinput('enable');
                        _fileTypeInputElement.fileinput('refresh');
                    }).on('fileselect', function (event, numFiles, label) {
                        if(options.allowedUploadMaxFileSize && _filesHiddenValueElement.val().split(",").length+1>options.allowedUploadMaxFileSize){
                            $.modal.alertError("文件最多上传"+options.allowedUploadMaxFileSize +"个");
                            _fileTypeInputElement.fileinput('clear');
                            return false;
                        }
                        $.modal.loading("正在上传，请稍后...");
                        _fileTypeInputElement.fileinput('upload');
                        _fileTypeInputElement.fileinput('refresh');
                    }).on('fileuploaderror', function (event, data, msg) {
                        // 进行自定义验证并返回如下所示的错误
                        $.modal.closeLoading();
                        if (data != null) {
                            $.modal.alertError(msg);
                        }
                        _fileTypeInputElement.fileinput('clear');
                    })
                );
                //回显代码,隐藏选择文件的按钮
                if(options.readonly){
                    _fileTypeInputElement.parents(".file-input").hide();
                }

            },
            destory:function(){
                $.localFileInputs = [];
            }
            ,removeUploadedFile:function(id,fileName,filesHiddenValueElement) {
                $.modal.confirm("确定删除\"" + fileName + "\"吗？", function() {
                    //ajax删除附件
                    $.modal.loading("正在处理，请稍后...");
                    $.post(ctx+"system/accessories/remove", {ids:id}, function(result) {
                        if(result.code ==0){
                            var ev = $(filesHiddenValueElement).val().split(",");
                            arrayRemoveItem(ev,id);
                            $(filesHiddenValueElement).val(ev.join(","));
                            $("#"+id).remove();
                        }
                        $.modal.closeLoading();
                    });
                });
            }
        }

    });
})(jQuery);



function arrayRemoveItem(arr, delVal) {
    if (arr instanceof Array) {
        var index = arr.indexOf(delVal);
        if (index > -1) {
            arr.splice(index, 1);
        }
    }
}


function addAccessoryFileElement(_fileTypeInputElement,filesHiddenValueElement,value,readonly){
    var deleteButton = "<i class='fa fa-times' style='cursor: pointer;' onclick='$.fileInputExtend.removeUploadedFile(\"" + value.id + "\",\"" + value.fileName + "\",\""+filesHiddenValueElement+"\")'></i>";
    if(readonly){
        deleteButton = "";
    }
    _fileTypeInputElement.parents(".file-input").before("<div id='" + value.id + "'><a href='"+ctx+"system/accessories/download/" + value.id + "' target='_blank'><label style='cursor: pointer;'>" + value.fileName + "</label></a>&nbsp;&nbsp;"+deleteButton+"<div>");

}