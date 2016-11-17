#  [IDA
使用小结](http://www.cnblogs.com/sld666666/archive/2011/04/08/2009964.html)

这边文章是对于IDA Pro的使用小结。想要学习IDA使用的请看[这里](http://www.pediy.com/practise/IDA.htm)。
我这里只是小小的个人的总结。不是作为教程来写的。

### 1\. Begin

这里有这样一段代码：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223922
2926.png)

我们用IDA来进行反汇编的练习。

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223922
7943.png)

发现IDA的反汇编还是比较强大的而且结构也比较清晰。

Var_C 表明声明了一块内存地址。

argc, argv, envp 是对函数参数的赋值。

    fldz 清除状态寄存器
    push 61h  第三个参数
    sub esp8, fstp [esp+0ch+var_c], 第二个参数； 
    调用 printf 函数
    堆栈平衡。
        我们可以按![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/201104082239225152.png) 改变不同类型的视图。也可以在这里打开几个相同的view
    ![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/201104082239227137.png) 

### 2\. 基本类型的识别

        我们按下Ctrl+S 可以转到不同的段中。
    ![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/201104082239236613.png) 

这里选择数据段。

只要按"D"我们就可以任意转换这些不确定的类型.可以变成byte,word,dword(db,dw,dd)。

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223923
5218.png)

选择“_Options_”菜单的“_Setup data types_”命令就可以设置了

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223923
234.png)

撤销所有的转换按”U”键。

可以按下这里来选择显示数据的进制格式：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223923
583.png)

### 3\. 字符串的操作

字符串是一个比较重要的内容， 所以这里单独列出来。

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223923
8108.png)

这样就能显示字符串。

不同的编程语言值不同的格式，我们在这里选择我们的字符串格式。

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223924
3124.png)

### 4\. 数组

用：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223924
7825.png)

来 make array.

然后： ![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/2011040822
39245350.png)

##### 5\. 结构体

选择创建一个结构体：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223924
4827.png)

按下D键增加一个成员变量， N 重命名，这样我们就建立完成了一个结构体：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223924
1795.png)

定义了自己的结构体之后可以这么使用：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201104/20110408223925
3780.png)

posted @ 2011-04-08 22:39 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2009964) 收藏

##备注 
 @post in:2011-04-08 22:39