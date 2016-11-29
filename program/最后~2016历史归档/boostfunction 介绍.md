#  [boost::function
介绍](http://www.cnblogs.com/sld666666/archive/2010/12/16/1907591.html)

本片文章主要介绍boost::function的用法。 boost::function 就是一个函数的包装器(function
wrapper)，用来定义函数对象。

## 1\. 介绍

Boost.Function 库包含了一个类族的函数对象的包装。它的概念很像广义上的回调函数。其有着和函数指针相同的特性但是又包含了一个调用的接口。一个函数
指针能够在能以地方被调用或者作为一个回调函数。boost.function能够代替函数指针并提供更大的灵活性。

## 2\. 使用

Boost.Function 有两种形式：首选形式和便携式形式， 其语法如下：

首选形式

便携式形式

boost::function<float(int x, int y)>f

boost::function2<float, int, int>f

但是便携式形式不是所有的编译器都支持的， 所以这里我只介绍首选形式。

### 2.1 普通函数

我们可以看下如下的例子：

     1 void do_sum(int *values, int n)   
     2 {   
     3     int sum(0);  
     4     for (int i = 0; i < n; ++i)   
     5     {   
     6         sum += values[i];   
     7     }   
     8     cout << sum << endl;   
     9 };  
    10 int _tmain(int argc, _TCHAR* argv[])   
    11 {   
    12     boost::function<void(int *values, int n)> sum;   
    13     sum = &do_sum;  
    14     int a[] = {1,2,3,4,5};   
    15     sum(a, 5);  
    16     return 0;   
    17 }

sum 可以理解为一个广义的函数对象了，其只用就是保存函数do_sum， 然后再调用之。

### 2.2 成员函数

在很多系统中， 对于类的成员函数的回调需要做特殊处理的。这个特殊的处理就是“参数绑定”。当然这个超出了我们讨论的范围了。
boost::function对于成员函数的使用可以看下如下代码：

     1 class X{   
     2 public:   
     3     int foo(int a)   
     4     {   
     5         cout << a <<endl;   
     6         return a;   
     7     }   
     8 };  
     9 int _tmain(int argc, _TCHAR* argv[])   
    10 {   
    11     boost::function<int(X*, int)>f;  
    12     f = &X::foo;  
    13     X x;   
    14     f(&x, 5);  
    15     return 0;   
    16 }

我们发现， 对类的成员函数的对象化从语法是没有多大的区别。

## 3\. 一个典型的例子

上面的几个例子没有体现出boost::function的作用来， 这里在写一个例子。比如当程序执行到某一处的时候想绑定某一个函数， 但是不想立即执行，
我们就可以声明一个函数对象，给此对象绑定相应的函数， 做一些其他事情，然后再来执行绑定的函数， 代码如下：

     1 void print(int a)   
     2 {   
     3     cout << a << endl;   
     4 }  
     5 typedef boost::function<void (int)> SuccessPrint;  
     6 int _tmain(int argc, _TCHAR* argv[])   
     7 {   
     8     vector<SuccessPrint> printList;  
     9     SuccessPrint printOne = boost::bind(print, _1);   
    10      printList.push_back(printOne);   
    11     SuccessPrint printTwo = boost::bind(print, _1);   
    12     printList.push_back(printTwo);   
    13     SuccessPrint printThree = boost::bind(print, _1);   
    14     printList.push_back(printTwo);   
    15     // do something else  
    16     for (int i = 0; i < printList.size(); ++i)   
    17         printList.at(i)(i);  
    18     return 0;   
    19 }

上述代码中首先把声明一个函数对象 typedef boost::function<void (int)> SuccessPrint，
然后把print绑定到斥对象中， 放入vector中， 到最后才来执行这print()函数。

posted @ 2010-12-16 09:59 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1907591) 收藏

##备注 
 @post in:2010-12-16 09:59