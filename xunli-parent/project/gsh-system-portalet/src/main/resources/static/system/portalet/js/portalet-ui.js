/**
 * portaletui
 */

var portaletUI = {
	initJqueryExtend: function(){
		$.extend({
	        portaletui: this
	    });
	},
    /**
     * 生成guid
     */
    getUUid: function() {
        const s = [];
        const hexDigits = '0123456789abcdef';
        for (let i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = '4';
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);
        s[8] = s[13] = s[18] = s[23] = ''; 
        return s.join('');
    },
    /**
     * 初始化card的collapse的单击事件
     */
    initCardCollapseLink: function(){
        $(".collapse-link").click(function() {
            var $this = $(this);
            var i = $this.find("i");
            var li = $this.closest("li");
            var div_ibox = $this.closest("div.ibox");
            div_ibox.find("div.ibox-content").slideToggle(200);            
            div_ibox.toggleClass("").toggleClass("border-bottom");
            
            i.toggleClass("fa-chevron-up").toggleClass("fa-chevron-down");
            
            if(i.hasClass("fa-chevron-up")){
                li.height(li.attr("data-height"));
            } else {
                li.height("auto");
            }
            
            setTimeout(function() {
                div_ibox.resize();
            }, 50);
        });
    },
    /**
     * 初始化card的close的单击事件
     */
    initCardCloseLink: function(title, url){
        $(".ui-state-default .ibox-title .close-link").click(function () {
            var $this = $(this).closest(".ui-state-default");
            $.modal.confirm(title, function() {
                var id = $this.attr("id").replace("mine_id_","");
                $.operate.onlyGet(url.replace('{id}', id), function(result){
                    $this.remove();
                    $.modal.msgSuccess(result.msg);
                });
            });
        })
    },
    /**
     * 初始化card的wrench的单击事件
     */
    initCardWrenchLink: function(title, url){
        $(".ui-state-default .ibox-title .wrench-link").click(function () {
            var resId = $(this).closest(".ui-state-default").attr("data-resId");
            $.modal.openTab("修改可配置资源", "system/portalet/res/edit/" + resId + "?showButtonFlag=true");
        })
    },
    /**
     * 初始化sortable和resizable
     */
    initSortableResizable: function(isSortable, isResizable){
        //排序
        if(isSortable){
            $( "#sortable" ).sortable();
            $( "#sortable" ).disableSelection();
            /* $( "#sortable" ).on( "sortstop", function( event, ui ) {
                log.info("调整前位置：" + JSON.stringify(ui.originalPosition));
                log.info("调整后位置：" + JSON.stringify(ui.position));
            } ); */
        }
        
        //大小
        if(isResizable){
            $( ".ui-state-default" ).resizable({ grid: [10, 20] });
            $( ".ui-state-default" ).on( "resizestop", function( event, ui ) {
                /* log.info("调整前大小：" + JSON.stringify(ui.originalSize));
                log.info("调整后大小：" + JSON.stringify(ui.size)); */
                ui.element.attr("data-width", ui.element.width());
                ui.element.attr("data-height", ui.element.height());
                var ele = ui.element.find('.ibox-content[data-queryport-id]');
                if(ele.length > 0){
                    $.queryportui.reDrawCustomQueryportChart(ele.attr('id'));
                }            
            } );
        }
        
        $(".ui-state-default .ibox-content[data-portalet-uri]").each(function(index, element){
            $(element).load(ctx + $(element).attr('data-portalet-uri'));
        });
        $.queryportui.initCustomQueryportAllChart('body');
    },
    /**
     * 保存视图
     */
    saveAndReloadParentTab: function(title, url, parentTabId){
    	var $classThis = this;
        $.modal.confirm(title, function() {
            var data = [];
            $("#sortable li.ui-state-default").each(function(index, element){
                var $this = $(element);
                if($this.attr("id")){
                    var ele = {};
                    ele.id = $this.attr("id").replace("mine_id_","");
                    ele.width = $this.width();
                    ele.height = $this.height();
                    ele.sortNo = index + 1;
                    
                    data.push(ele);
                }
            });
            $.modal.loading("正在处理中，请稍后...");
            $.operate.onlyPost(url, data, null, function(){
            	$.modal.closeLoading();
            	$classThis.reloadParentTab(parentTabId);
                $.modal.closeTab();
            });
        });
    },
    reloadParentTab: function(currentId){
        var topWindow = $(window.parent.document);
        var target = $('.gsh_iframe[data-id="' + currentId + '"]', topWindow);
        target.attr('src', target.attr('src')).ready();
    },
    
    selectResSubmitHandler: function(url, pathId){
        var rows = [];
        $.each($("#bootstrap-table").bootstrapTable('getSelections'),function(index,value){
            rows.push(value.id);
        });
        
        if(rows.length == 0){
            $.modal.msgWarning("请选择资源");
            return;
        }
        
        $.operate.save(url, { "ids": rows.join() });
    }
};

/**
 * 有可能会受其他界面的影响，导致jquery被初始化
 */
/*(function ($) {
    $.extend({
        portaletui: portaletUI
    });
})(jQuery);*/










