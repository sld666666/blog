#  [设计模式----
装饰者模式(c++)](http://www.cnblogs.com/sld666666/archive/2011/05/26/2059179.html)

想象一下有如下的场景：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110526204436
1732.png)

有两种主咖啡， 可以搭配任意的饮料，我们的目标是允许类容易扩展，在不修改现有代码的情况下，可以搭配新的行为。这里可以用装饰者模式来实现。

### 1\. uml 图

装饰者模式动态地将责任附加到对象上，若要扩展功能，装饰者提供了比继承更有弹性的替代方案。

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110526204436
5287.png)

这里吹一下装饰者模式的好处：

  * 每个组件都可以单独使用，或者被装饰者包装起来使用
  * 装饰者可以扩展组件的状态
  * 装饰可以加上新的方法
  * 可以为组件添加新的方法而用修改装饰者，反之亦可

### 2\. 代码示例

首先构造基类

    1 class Beverage   
    2 {   
    3  public:   
    4         virtual int cost() = 0;   
    5 };

然后 定义组件， 这里定义了两个 Blend 和 DarkRoast

    1 class Blend : public Beverage   
    2 {   
    3  public:   
    4     int cost()   
    5     {   
    6         return 1;   
    7     }   
    8 };
    1 class DarkRoast : public Beverage   
    2 {   
    3  public:   
    4     int cost()   
    5     {   
    6         return 2;   
    7     }   
    8 };

下一步就定义 装饰者， 这里定义了3个， 所有的装饰者都是从Decorator 类继承的

     1 class Decorator : public Beverage   
     2 {   
     3  public:   
     4     int cost()   
     5     {   
     6         return observed_->cost() + 0;   
     7     }   
     8     void setObserver(shared_ptr<Beverage> observed)   
     9     {   
    10         observed_ = observed;   
    11     }  
    12   
    13  protected:   
    14     shared_ptr<Beverage> getObserver()   
    15     {   
    16         return observed_;   
    17     }  
    18   
    19  private:   
    20     shared_ptr<Beverage> observed_;   
    21 };  
    22   
    23  class Milk : public Decorator   
    24 {   
    25  public:   
    26     int cost()   
    27     {   
    28         return getObserver()->cost() + 1;   
    29     }   
    30 };  
    31   
    32  class Mocha : public Decorator   
    33 {   
    34  public:   
    35     int cost()   
    36     {   
    37         return getObserver()->cost() + 2;   
    38     }   
    39 };   
    40 class Soy : public Decorator   
    41 {   
    42 public:   
    43     int cost()   
    44     {   
    45         return getObserver()->cost() + 3;   
    46     }   
    47 };

最后看下测试代码

     1 int _tmain(int argc, _TCHAR* argv[])   
     2 {  
     3   
     4     shared_ptr<Beverage> blendObserved(new Blend()) ;   
     5     shared_ptr<Beverage> darkObserved(new DarkRoast());  
     6   
     7     shared_ptr<Decorator> blendWithMilk(new Milk());   
     8     blendWithMilk->setObserver(blendObserved);  
     9   
    10     shared_ptr<Decorator> blendWithMocha(new Mocha());   
    11     blendWithMocha->setObserver(blendObserved);  
    12   
    13     shared_ptr<Decorator> blendWithSoy(new Soy());   
    14     blendWithSoy->setObserver(blendObserved);  
    15   
    16     cout<<blendWithMilk->cost()<<endl;   
    17     cout<<blendWithMocha->cost()<<endl;   
    18     cout<<blendWithSoy->cost()<<endl;   
    19     return 0;   
    20 }

### 3\. 总结

开放关闭原则： 对扩展开放，对修改关闭。

  * 继承属于扩展形式之一，但不见得是达到弹性设计的最佳方式
  * 在我们的设计中，应该允许行为可以被扩展，而无需修改实现
  * 除了继承，装饰者模式也可以让我们扩展行为，而装饰者模式意味着一群装饰者类来包装具体的组件。
  * 装饰者会导致设计中出现许多小对象，如果过度使用会是程序变得复杂。

posted @ 2011-05-26 20:45 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2059179) 收藏

##备注 
 @post in:2011-05-26 20:45