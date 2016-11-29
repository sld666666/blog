#  [Boost
中的智能指针](http://www.cnblogs.com/sld666666/archive/2010/12/16/1908265.html)

这篇文章主要介绍 boost中的智能指针的使用。

内存管理是一个比较繁琐的问题，C++中有两个实现方案： 垃圾回收机制和智能指针。垃圾回收机制因为性能等原因不被C++的大佬们推崇，
而智能指针被认为是解决C++内存问题的最优方案。

## 1\. 定义

一个智能指针就是一个C++的对象， 这对象的行为像一个指针，但是它却可以在其不需要的时候自动删除。注意这个“其不需要的时候”，
这可不是一个精确的定义。这个不需要的时候可以指好多方面：局部变量退出函数作用域、类的对象被析构……。所以boost定义了多个不同的智能指针来管理不同的场景。

shared_ptr<T>

内部维护一个引用计数器来判断此指针是不是需要被释放。是boost中最常用的智能指针了。

scoped_ptr<t>

当这个指针的作用域消失之后自动释放

intrusive_ptr<T>

也维护一个引用计数器，比shared_ptr有更好的性能。但是要求T自己提供这个计数器。

weak_ptr<T>

弱指针，要和shared_ptr 结合使用

shared_array<T>

和shared_ptr相似，但是访问的是数组

scoped_array<T>

和scoped_ptr相似，但是访问的是数组

## 2\. Boost::scoped_ptr<T>

scoped_ptr 是boost中最简单的智能指针。scoped_ptr的目的也是很简单，
当一个指针离开其作用域时候，释放相关资源。特别注意的一定就是scoped_ptr
不能共享指针的所有权也不能转移所有权。也就是说这个内存地址就只能给的声明的变量用，不能给其他使用。

下面是scoped_ptr的几个特点：

  1. scoped_ptr的效率和空间的消耗内置的指针差不多。
  2. scoped_ptr不能用在标准库的容器上。(用shared_ptr代替)
  3. scoped_ptr 不能指向一块能够动态增长的内存区域(用scoped_array代替)
     1 class test   
     2 {   
     3 public:   
     4     void print()   
     5     {   
     6         cout << "test print now" <<endl;   
     7     }   
     8 };  
     9 int _tmain(int argc, _TCHAR* argv[])   
    10 {   
    11     boost::scoped_ptr<test> x(new test);   
    12     x->print();   
    13     return 0;   
    14 }

## 3.Boost::shared_ptr<T>

shared_ptr 具有如下几个特点：

  1. 在内部维护一个引用计数器， 当有一个指针指向这块内存区域是引用计数+1， 反之-1， 如果没有任何指针指向这块区域， 引用计数器为0，释放内存区域。
  2. 可以共享和转移所有权。
  3. 可以被标准库的容器所使用
  4. 不能指向一块动态增长的内存(用share_array代替)

我们可以看下如下例子：

    1 int _tmain(int argc, _TCHAR* argv[])   
    2 {   
    3     boost::shared_ptr<test> ptr_1(new test);   
    4     ptr_1->print();//引用计数为1  
    5     boost::shared_ptr<test> ptr_2 = ptr_1;   
    6     ptr_2->print();//引用计数为2  
    7     ptr_1->print();// 引用计数还是为2  
    8     return 0;   
    9 }

## 4\. Boost::intrusive_ptr<T>

intrusive_ptr 的主要和share_ptr一样， 对比share_ptr,其效率更高，但是需要自己维护一个引用计数器， 这里不做详细介绍。

## 5\. Boost::weak_ptr<T>

weak_ptr 就是一个弱指针。weak_ptr 被shared_ptr控制，
它可以通过share_ptr的构造函数或者lock成员函数转化为share_ptr。

weak_ptr的一个最大特点就是它共享一个share_ptr的内存，但是无论是构造还是析构一个weak_ptr 都不会影响引用计数器。

     1 int _tmain(int argc, _TCHAR* argv[])   
     2 {   
     3     boost::shared_ptr<test> sharePtr(new test);;  
     4     boost::weak_ptr<test> weakPtr(sharePtr);   
     5     //weakPtr 就是用來保存指向這塊內存區域的指針的   
     6     //干了一大堆其他事情  
     7     boost::shared_ptr<test> sharePtr_2 = weakPtr.lock();   
     8     if (sharePtr_2)   
     9         sharePtr_2->print();  
    10     return 0;   
    11 }

## 6\. Boost::shared_array<T> 和Boost::scoped_array<T>

前面提到过shared_ptr和scoped_ptr不能用于数组的内存（new
[]），所以shared_array和scoped_array就是他们的代替品。我们可以看下shared_array的用法

    1 int _tmain(int argc, _TCHAR* argv[])   
    2 {   
    3     const int size = 10;   
    4     boost::shared_array<test> a(new test[]);  
    5     for (int i = 0; i < size; ++i)   
    6         a[i].print();  
    7     return 0;   
    8 }

## 7\. 使用智能指针的几个注意点

下面是几个使用智能指针需要注意的地方：

  1. 声明一个智能指针的时候要立即给它实例化， 而且一定不能手动释放它。
  2. …_ptr<T> 不是T* 类型。所以：

a: 声明的时候要…_ptr<T> 而不是….._ptr<T*>

b：不能把T* 型的指针赋值给它

c: 不能写ptr=NULl, 而用ptr.reset()代替。

  1. 不能循环引用。
  2. 不要声明临时的share_ptr， 然后把这个指针传递给一个函数

## 8\. 总结

智能指针使用上还是比较简单的， 而且能比较有效得解决C++内存泄露的问题，各位使用C++的童鞋赶快用起来吧。

posted @ 2010-12-16 15:19 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1908265) 收藏

##备注 
 @post in:2010-12-16 15:19