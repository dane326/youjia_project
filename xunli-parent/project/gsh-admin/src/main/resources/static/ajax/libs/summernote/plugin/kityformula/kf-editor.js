(function(factory) {
    /* global define */
    if (typeof define === 'function' && define.amd) {
        // AMD. Register as an anonymous module.
        define([ 'jquery' ], factory);
    } else if (typeof module === 'object' && module.exports) {
        // Node/CommonJS
        module.exports = factory(require('jquery'));
    } else {
        // Browser globals
        factory(window.jQuery);
    }
}(function($) {
    // Extends plugins for adding hello.
    // - plugin is external module for customizing.
    $.extend($.summernote.plugins, {
        /**
         * @param {Object}
         *            context - context object has status of editor.
         */
        'kfeditor' : function(context) {
            var self = this;

            // ui has renders to build ui elements.
            // - you can create a button with `ui.button`
            var ui = $.summernote.ui;
            var $editor = context.layoutInfo.editor;
            var $options = context.options;

            // add hello button
            context.memo('button.kfeditor', function() {
                // create button
                var button = ui.button({
                    contents : '<span style="font: icon; font-weight: bold; line-height: 15px;">&Sigma;</span>',
                    tooltip : '数学公式',
                    click : function() {
                        self.show();
                    },
                });
                button.markup = '<button type="button" style="padding: 8px 10px 2px 10px" class="note-btn btn btn-default btn-sm" role="button" tabindex="-1">';
                // create jQuery object from button instance.
                var $kfeditor = button.render();
                return $kfeditor;
            });

            // This events will be attached when editor is initialized.
            this.events = {
                'summernote.mousedown' : function(we, e) {
                    var target = e.target;
                    if (context.isDisabled()) {
                        return false;
                    }

                    var isImage = target && (target.nodeName.toUpperCase() === "SVG");
                    if (isImage) {
                        var $selection = context.modules.handle.$handle.find('.note-control-selection');
                        
                        var pos = $.summernote.dom.posFromPlaceholder(target);
                        var posEditor = $.summernote.dom.posFromPlaceholder(context.layoutInfo.editable[0]);
                        context.modules.imagePopover.$popover.css({
                            display: 'block',
                            left: $options.popatmouse ? event.pageX - 20 : pos.left,
                            top: $options.popatmouse ? event.pageY : Math.min(pos.top, posEditor.top)
                        });
                        
                        var $image = $(target).closest("span.latex");
                        
                        var position = $image.position();
                        var pos = {
                            left : position.left + parseInt($image.css('marginLeft'), 10),
                            top : position.top + parseInt($image.css('marginTop'), 10),
                        };

                        // exclude margin
                        var imageSize = {
                            w : $image.outerWidth(false),
                            h : $image.outerHeight(false),
                        };

                        $selection.css({
                            display : 'block',
                            left : pos.left,
                            top : pos.top,
                            width : imageSize.w,
                            height : imageSize.h,
                        }).data('target', $image); // save current image element.

                        const sizingText = imageSize.w + 'x' + imageSize.h;
                        $selection.find('.note-control-selection-info').text(sizingText);
                        
                        context.invoke('editor.saveTarget', $image[0]);
                        
                        e.preventDefault();
                    }
                }
            };

            this.show = function() {
                parent.window.summernote_modal = window.frameElement.id;
                window.summernote_context = "";

                context.invoke('imagePopover.update');
                var target = context.invoke('editor.restoreTarget');
                if (target) {
                    var $target = $(target);
                    if($target.hasClass("latex")){
                        window.summernote_context = target.childNodes[2].innerHTML;
                    } else if($target.hasClass("kfformula")){
                        window.summernote_context = $target.data("latex");
                    }                    
                }                

                parent.window.$.modal.open('插入公式', ctx + 'ajax/libs/summernote/plugin/kityformula/kityEditor.html', 800, 479, function(index, layero) {
                    var iframeWin = layero.find('iframe')[0];
                    iframeWin.contentWindow.submitHandler(function(node) {
                        if (node) {
                            var target = context.invoke('editor.restoreTarget');
                            if (target) {
                                var $target = $(target);
                                if($target.hasClass("latex") || $target.hasClass("kfformula")){
                                    $(target).remove();
                                }                    
                            }
                            
                            // insert node
                            context.invoke('editor.insertNode', node);
                        }
                        
                        parent.window.layer.close(index);
                        window.summernote_context = null;
                        
                        context.invoke('editor.clearTarget');
                    });
                });
            };
        },
    });
}));
