#  [jQuery----事件](http://www.cnblogs.com/sld666666/p/4126151.html)

一个典型的格式如下：

    $(document).ready(function() {
        $('thingToTouch').event(function() {
            $('thingToAffect').effect();
        });
    });

如：

    $(document).ready(function() {
        $('button').click(function() {
            var toAdd = $("input[name=message]").val();
            $('#messages').append("<p>"+toAdd+"</p>");
        });
    });

几个典型的事件：

  1. click
  2. dblclick
  3. hover
  4. focus

当天我们也可以添加多个function

    $('div').hover(
        function(){
          $(this).addClass('highlight');
        },
        function(){
          $(this).removeClass('highlight');
        }
    );

如焦点进入边框变红：

    $(document).ready(function() {
        $('input').focus(function() {
            $(this).css('outline-color','#FF0000');
        });
    });

posted @ 2014-11-27 12:54 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4126151) 收藏

##备注 
 @post in:2014-11-27 12:54