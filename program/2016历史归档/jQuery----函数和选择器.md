#  [jQuery----函数和选择器](http://www.cnblogs.com/sld666666/p/4125976.html)

### 1\. $(document).ready

几乎所有的jQuery代码都是以如下方式开始的

    $(document).ready(function() {
        Do something
    });

  1. $(document) 是一个jQuery对象。$()其实是一个函数的装饰，它返回一个jQuery对象。
  2. .read()是一个函数。当html document准备完毕以后，这个函数被调用
  3. 在function(){} 是当read()函数被执行时候要做的实际操作。function(){}返回的是一个对象。

### 2\. 带参数的函数

    function(input1, input2, etc) {
        Do a thing
        Do another thing
        Do yet another thing!
    }

jQuery一个非常好的东西就死我们可以给函数的参数定义为任何内容（可以为一个函数）。这就是为什么在.ready()函数中， 可以带一个函数的参数。

### 3\. 变量

    var lucky = 7;
    var name = "Codecademy";
    var $p = $('p');

javascript是一个弱类型的语言，所以我们可以给var 付给任意的东西。  
$('p') 是jQuery的选择器， $p只是一个变量名。加上$只是约定。

### 4\. 更有弹性的选择

所有能够不css选择的元素，都能不jQuery选择并改变。我可以这样：

    $('p').fadeTo('slow', 0);

也可以这样：  
$('li').fadeTo('slow', 0);  
或者这样：  
$('p, li').fadeTo('slow', 0);

### 5\. 'this'非常重要

    $(document).ready(function() {
        $('div').mouseenter(function() {
            $(this).hide();
        });
    });

this 关键字表示正在操作的当前元素

posted @ 2014-11-27 11:27 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4125976) 收藏

##备注 
 @post in:2014-11-27 11:27