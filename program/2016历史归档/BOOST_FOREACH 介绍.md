#  [BOOST_FOREACH
介绍](http://www.cnblogs.com/sld666666/archive/2011/05/30/2063140.html)

“Make simple things easy.”  
**_\-- Larry Wall_**

c++中，写一个循环去迭代一个序列是很单调的。

    1 string hello("hello, boost!");  
    2   
    3  for (int i = 0; i != hello.size(); ++i)   
    4 {   
    5     cout<<hello.at(i);   
    6 }

我们可以用std:for_each,但是这样并没有减少代码了，而且让很多功能分离太远

     1 void print (char ch)   
     2 {   
     3     cout << ch;   
     4 }   
     5 int _tmain(int argc, _TCHAR* argv[])   
     6 {   
     7     string hello("hello, boost!");   
     8     for_each(hello.begin(), hello.end(),print);  
     9   
    10      cout<<endl;  
    11   
    12     return 0;   
    13 }

`BOOST_FOREACH` 是为了易用性和高效性而设计的。它不进行动态的内存分配，没有虚拟函数调用或通过函数指针的调用。这样可以生成近似于最优化的代码。

    1 string hello("hello, boost!");   
    2     BOOST_FOREACH(char ch, hello)   
    3     {   
    4         cout<< ch;   
    5     }  
    6   
    7      cout<<endl;

`BOOST_FOREACH` 支持所有序列式容器。

当然，为了更漂亮我们可以这样改造

    1 #define foreach BOOST_FOREACH  
    2 #define reverse_foreach BOOST_REVERSE_FOREACH

  

posted @ 2011-05-30 11:42 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2063140) 收藏

##备注 
 @post in:2011-05-30 11:42