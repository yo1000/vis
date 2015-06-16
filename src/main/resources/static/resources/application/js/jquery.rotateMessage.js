/**
 * jQuery.rotateMessage 1.0.1
 * requires jQuery 1.*
 *
 * @author Yoichi KIKUCHI
 * @param args = {
 *   msgs:array          Screen messages.
 *   dispTime:number     Message display time. (in milliseconds)
 *   switchTime:number   Message switching time (milliseconds, or predefined constants: 'slow','normal','fast')
 *   cssProps:object     Style to add to the message element (Sets the hash key and value)
 *   msgHandler:function Sets the handler for a message. (Get the message argument)
 * }
 */
$.fn.extend({
    rotateMessage: function(args) {
        var $this = $(this);
        $this.contents().remove();
        $this.append('<p class="rotate-message-contents"></p>');
        var dispIndex = 0;

        var switchMessage = function() {
            $('.rotate-message-contents').animate({
                    height: 'hide',
                    top : '+=1em'
                },
                args.switchTime, 'swing', function() {
                    $this.contents().remove();
                    var $msg = $('<p class="rotate-message-contents" style="position:absolute; display:none;"></p>');

                    $msg.append(args.msgHandler != null
                        ? args.msgHandler(args.msgs[dispIndex++ % args.msgs.length])
                        : args.msgs[dispIndex++ % args.msgs.length]);
                    $this.append($msg);
                    if (args.cssProps != null) {
                        $msg.css(args.cssProps);
                    }

                    $('.rotate-message-contents').animate({
                        height: 'show',
                        top: '-=1em'
                    }, {
                        duration: args.switchTime,
                        complete: function() {
                            if (args.dispTime > 0) {
                                setTimeout(function(){
                                    switchMessage();
                                }, args.dispTime);
                            }
                        }
                    });
                }
            );
        };
        switchMessage();
    }
});
