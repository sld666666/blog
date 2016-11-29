#  [boost::bind
介绍](http://www.cnblogs.com/sld666666/archive/2010/12/14/1905980.html)

这篇文章介绍boost::bind()的用法， 文章的主要内容是参考boost的文档。

## 1\. 目的

boost::bind 是std::bindlist 和
std::bind2nd的结合体。它提供一个任意的函数对象(仿函数)、函数、函数指针、成员函数指针。 它可以绑定任意的参数。bind
没有对函数对象有任何的要求。

## 2\. 把bind()用在函数和函数指针上

有如下代码：

    1 void f(int a, int b)   
    2 {   
    3     cout << a + b << endl;   
    4 }  
    5  void g(int a, int b, int c)   
    6 {   
    7     cout << a + b + c << endl;;   
    8 }

当调用boost::bind(f, 1, 2);的时候， 它会产生一个空的函数对象，这个对象没有参数， 返回 f(1,2).当然我们也可以给它加个参数：

    1 int a = 10;   
    2 boost::bind(f, _1, 5)(a);  
    3 int x(10),y(20),z(30);   
    4 boost::bind(g,_1,_2,_3)(x, y, z);

结果：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201012/20101214193034
497.png)

作为和std::bindlst的对比我们可以看下如下的代码：

    1 std::bind1st(std::ptr_fun(f), 5)(x);   // f(5, x)  
    2 boost::bind(f, 5, _1)(x);              // f(5, x)

是不是boost::bind()简单多了。

## 3\. 把bind()用在函数对象(仿函数)上

bind()不仅能够用在函数上，而且可以接受任意的函数对象(仿函数)。如：

     1 class F   
     2 {   
     3 public:   
     4     int operator()(int a, int b)   
     5     {   
     6         cout << a+b <<endl;   
     7         return a+b;   
     8     }  
     9     double operator()(double a, double b)   
    10     {   
    11         cout << a+b<< endl;   
    12         return a +b;   
    13     }   
    14 };  
    15 int _tmain(int argc, _TCHAR* argv[])   
    16 {  
    17     F f;   
    18     int a[] = {1, 2, 3, 4, 5, 6,7};   
    19     double aDouble[] = {1.1, 2.2, 3.3, 4.4,5.5,6.6,7.7};  
    20     for_each(a, a+7, boost::bind<int>(f, _1, _1));  
    21     for_each(aDouble, aDouble+7, boost::bind<double>(f, _1, _1));  
    22     return 0;   
    23 }

## 4\. 把bind()用在成员变量和成员函数上

指向成员变量的指针和指向成员函数的指针和仿函数不一样， 因为他们没有提供operater()。boost用它的第一个参数接受类成员的指针，这样就像用boos
t::mem_fn()把类成员的指针转化为仿函数一样。如：

    bind(&X::f, _args_)

就等于

    bind<R>(mem_fn(&X::f), _args_)//R 是x::f的返回值。
    列如：
     1 struct X   
     2 {   
     3     bool f(int a)   
     4     {   
     5         cout << a <<endl;  
     6         return static_cast<bool>(a);   
     7     }  
     8 };  
     9 int _tmain(int argc, _TCHAR* argv[])   
    10 {  
    11     X x;   
    12     boost::shared_ptr<X> p(new X);  
    13     int i = 5;   
    14     boost::bind(&X::f, &x, _1)(i);    // (&x)->f(i);   
    15     boost::bind(&X::f, x, _1)(i);   //(copy x).f(i);   
    16     boost::bind(&X::f, p, _1)(i);   //(copy p)->f(i);   
    17     return 0;   
    18 }

boost::bind()的基本用法就这些， 在使用的过程中发现确实比较爽， 但是这不知道这是不是常常被人批判的语法糖。

posted @ 2010-12-14 19:31 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1905980) 收藏

##备注 
 @post in:2010-12-14 19:31