#  [C++
智能指针的一篇调研](http://www.cnblogs.com/sld666666/archive/2010/11/09/1872896.html)

很久之前写的一篇关于C++智能指针的调研报告，这里贴出来。

Auto_ptr本来就是给局部变量用的，目的很单一。主要就是为了解决异常一旦出现函数立即退出，这样的问题。

auto_ptr不会降低程序的效率，理论上，合理使用容器加智能指针，C++可以完全避免内存泄露，效率只有微不足道的下降。

最主要的特点：拥有权的转移。

A:一个对象决不能被两个其指针“拥有”

    1 Eg:  
    2  //int*ptr1 = new int;  
    3  std::auto_ptr<int>ptr1(newint);  
    4 std::auto_ptr<int>ptr2(newint);  
    5 ptr2 =ptr1;//ptr1 = null;

用着方便，和一般指针没多少两样，只要注意拥有权这个概念即可。

  1. auto_ptr不适用于数组，auto_ptr根本不可以大规模使用
  2. 作为函数参数传递的时候有危险性。
    1 Eg: std::auto_ptr<int>ptr(new int)  
    2         F1(ptr);  
    3  //now ptr = null,next will down  
    4 F2(ptr);

这样情况下建议用const auto_ptr<int>& ?,此时意味着拥有权不能转移。

  1. 拥有权的转移。
  2. 如果不注意很容易被无用。

总结：

auto_ptr用法比较简单，但是有几个使用的误区。

效率上的损失，有的说不足1%，但是我没验证过，不敢确定.

用还是不用？aut_ptr能很好的解决其设计目的上的问题。但是我想如果不是新开发的项目，会造成某些接口调用不兼容的问题。

posted @ 2010-11-09 17:37 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1872896) 收藏

##备注 
 @post in:2010-11-09 17:37