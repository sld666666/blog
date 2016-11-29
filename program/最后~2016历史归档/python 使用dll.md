#  [python
使用dll](http://www.cnblogs.com/sld666666/archive/2011/05/24/2055621.html)

1\. python 用关键字 `ctypes` 来导入dll。

有三种导入dll的方式：

  * cdll()： dll函数以c方式导出 
  * windll(): dll 函数以标准方式导出 
  * oledll(): com 方式导出 

如：

    1 from ctypes import *  
    2   
    3 msvcrt = cdll.msvcrt   
    4 message_string = "hello world\n"   
    5 msvcrt.printf("testing: %s", message_string)

调用c运行库的printf 函数。

2\. c&c++数据类型和python数据类型的映射关系

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110524161334
341.png)

3\. 引用传值

byref()返回一个变量的指针，所以在调用的时候只需： function(byref(param));

4\. struct 和unins

c中

     1 struct beef_recipe   
     2 {   
     3     int amt_barley;   
     4     int amt_water;   
     5 }  
     6   
     7   
     8   
     9 unin{   
    10      long barley_long;   
    11      int barley_int;   
    12      char barley_char[8];   
    13 } barley_amount;

python 中:

     1 class beer_recipe(Structure):   
     2     _fields_ = [   
     3     ("amt_barley", c_int),   
     4     ("amt_water", c_int)   
     5     ]  
     6   
     7  class barley_amount(Union):   
     8     _fields = [   
     9     ("barley_long", c_long),   
    10     ("barley_int", c_int),   
    11     ("barley_char", c_char*8),   
    12     ]

posted @ 2011-05-24 15:42 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2055621) 收藏

##备注 
 @post in:2011-05-24 15:42