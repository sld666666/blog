#  [设计模式----
工厂模式(c++)](http://www.cnblogs.com/sld666666/archive/2011/05/27/2060234.html)

### 1\. 场景

假设有如下的场景：

卖pizza的， 可以有很多种pizza(CheesePizza， GreekPizza， PepperoniPizza)，我们该如何处理它呢，当然我们可以
声明一个类，类中创建每一个种类的pizza。但是这样有什么问题吗？我们没有把需要改变的部分剥离出来。软件设计的一个原则就是“是易于扩充而不是修改”，另一个是
“对接口编程而不是对实现编程”。

这里就轮到工厂模式出手了。 工厂模式有三种：

  * 简单工厂
  * 抽象工厂
  * 工厂方法

### 2\. 简单工厂模式

####  1.1 Uml

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110527201130
1858.png)

####  1.2 源代码

     1 class Pizza   
     2 {   
     3  public:   
     4      Pizza(){};   
     5     virtual ~Pizza(){};  
     6   
     7     virtual void MakePizza() = 0;   
     8 };  
     9   
    10  class CheesePizza : public Pizza   
    11 {   
    12  public:   
    13     CheesePizza(){};   
    14     ~CheesePizza(){};  
    15   
    16  void MakePizza()   
    17 {   
    18     cout << "make cheese pizza" <<endl;   
    19 }   
    20 };  
    21   
    22 class GreekPizza : public Pizza   
    23 {   
    24 public:   
    25     GreekPizza(){};   
    26     ~GreekPizza(){};  
    27   
    28     void MakePizza()   
    29     {   
    30         cout << "make greek pizza" <<endl;   
    31     }  
    32   
    33 };  
    34   
    35 class PepperoniPizza : public Pizza   
    36 {   
    37 public:   
    38     PepperoniPizza(){};   
    39     ~PepperoniPizza(){};  
    40   
    41     void MakePizza()   
    42     {   
    43         cout << "make peperoni pizza" <<endl;   
    44     }  
    45   
    46 };  
    47   
    48 enum PIZZA_TYPE   
    49 {   
    50     PIZZA_TYPE_CHEESE = 0,   
    51     PIZZA_TYPE_GREEK,   
    52     PIZZA_TYPE_PEPPERONI   
    53 };  
    54   
    55 class PizzaFactory   
    56 {   
    57 public:   
    58     PizzaFactory(){};   
    59     ~PizzaFactory(){};  
    60   
    61     static shared_ptr<Pizza> createPizza(PIZZA_TYPE pizzaType)   
    62     {   
    63         switch (pizzaType)   
    64         {   
    65         case PIZZA_TYPE_CHEESE:   
    66             return shared_ptr<Pizza>(new CheesePizza());   
    67         case PIZZA_TYPE_GREEK:   
    68             return shared_ptr<Pizza>(new GreekPizza());   
    69         case PIZZA_TYPE_PEPPERONI:   
    70             return shared_ptr<Pizza>(new PepperoniPizza());   
    71         default:   
    72             return shared_ptr<Pizza>(new PepperoniPizza());   
    73         }   
    74     }   
    75 };

测试代码

     1 int _tmain(int argc, _TCHAR* argv[])   
     2 {  
     3   
     4     shared_ptr<Pizza> cheesePizza = PizzaFactory::createPizza(PIZZA_TYPE_CHEESE);   
     5     shared_ptr<Pizza> greekPizza = PizzaFactory::createPizza(PIZZA_TYPE_GREEK);   
     6     shared_ptr<Pizza> pepperoniPizza = PizzaFactory::createPizza(PIZZA_TYPE_PEPPERONI);  
     7   
     8     cheesePizza->MakePizza();   
     9     greekPizza->MakePizza();   
    10     pepperoniPizza->MakePizza();   
    11     return 0;   
    12 }

这样写好什么好处呢？

  1. .静态工厂方法统一管理对象的创建
  2. 静态工厂方法推迟了产品的实例化。

### 2\. 工厂方法模式

定义： 通过让子类决定该创建的对象是什么，来达到将对象创建的过程封装的目的。

当然从代码的角度就是其实就是把 PizzaFactory 抽象出来，用子类来实现。

## 3\. 抽象工厂

抽象工厂和工厂方法的组要区别是工厂方法使用继承来创建对象，而抽象工厂是使用组合。

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110527201130
5970.png)

代码实现也就是把如上的PizzaFactory类抽象出来。

posted @ 2011-05-27 20:12 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2060234) 收藏

##备注 
 @post in:2011-05-27 20:12