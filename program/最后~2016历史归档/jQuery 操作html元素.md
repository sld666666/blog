#  [jQuery 操作html元素](http://www.cnblogs.com/sld666666/p/4125983.html)

### 1\. 添加元素

动态添加html 元素是一个非常重要的功能。  
jQuery可以非常方便的做这样的事情：

    $h1 = $('<h1>Hello</h1>') 
    $(".info").append("<p>Stuff!</p>");
    $(".info").prepend("<p>Stuff!</p>");
    $('<p>Stuff!</p>').appendTo('.info');

用append，prepend，appendTo可以再元素间添加元素  
利用before, after可以再元素内添加元素

### 2\. 移动元素

    var $paragraph = $("p"); // existing element
    $("div").after($paragraph); // Move it!
    // Same as:
    $("div").after($("p"));

用 $("p") 选择元素，然后把它移到div里面

### 3\. 移除元素

.empty()和.remove()方法实现了清空和删除功能

    $("div").remove();

### 4\. 添加和移除 class

jQuery 利用.addClass()和.removeClass()添加和删除元素上的class属性

    $('selector').addClass('className');
    $('selector').removeClass('className');

.toggleClass()如果有则移除， 没有则添加

### 5\. 更改元素的属性

.height()和.width() 可以更改元素的宽和高。

    $("div").height("100px");
    $("div").width("50px");

.css（） 可以添加css， 如：  
$('div').css("border-radius", "10");

### 6\. 改动元素的内容

可以使用.html(), .val（） 更改html元素的内容。

    `<form name="checkListForm">
            <input type="text" name="checkListItem"/>
    </form>`
    var to add = $('input[name=checkListItem]').val();

### 7\. 事件

    $(document).on('click', '.item', function(){
    $(this).remove();
    });

posted @ 2014-11-27 11:28 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4125983) 收藏

##备注 
 @post in:2014-11-27 11:28