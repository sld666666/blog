#  [设计模式----
观察者模式（C++）](http://www.cnblogs.com/sld666666/archive/2011/05/25/2057307.html)

假设一个数据有三个部分显示数据的， 分别是 max, min,average,我们可以写下这个一个类：

     1 class Datashow   
     2 {   
     3 public:   
     4     void measurmentChanged   
     5     {   
     6         float temp = getTemplate();   
     7         float humidity = getHumdity();   
     8         float pressure = getPressure();  
     9   
    10         average.updata(temp, humidity, pressure);   
    11         max.updata(temp, humidity, pressure);   
    12         min.updata(tmep, humidity, pressure);   
    13     }   
    14 };

这样有什么需要改进的吗？

  * updata 看起来像一个统一的接口
  * 针对具体实现编程了，这样导致了以后增加或删除显示的时候必须修改 dataShow 类

### 1\. 观察者模式

如果使用观察者模式我们可以这样来理解：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110525211620
4716.png)

如图：我们添加观察者4或者删除观察者3 就比较容易了。

观察者模式定义了对象之间一对多的依赖，这样依赖，当一个对象改变状态时，所有的依赖者都会接收到通知并更新。

观察者模式提供了一种对象设计，让主题和观察者之间松耦合：

  * 运行时我们用新的观察者取代现有的观察者，主题不会受到影响。
  * 有新类型的观察者出现时，主题代码不需要更改。
  * 可以轻易地独立使用或复用主题或者观察者。
  * 改变主题或者观察者的任何一方都不会影响另一方。

### 2\. uml图

下面可以看下观察者模式的uml图：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201105/20110525211620
223.png)

### 3\. 代码实现

下面是一份C++代码实现

首先是观察者， 这里实现了三个观察者， average, min, max

     1 class Observer   
     2 {   
     3 public:   
     4     virtual void updata(float temperature, float humidity, float pressure) = 0;   
     5 };  
     6   
     7 class Average : public Observer   
     8 {   
     9 public:   
    10     void updata(float temperature, float humidity, float pressure)   
    11     {   
    12         cout << (temperature+humidity+pressure)/3 <<endl;   
    13     }   
    14 };  
    15   
    16 class Min : public Observer   
    17 {   
    18 public:   
    19     void updata(float temperature, float humidity, float pressure)   
    20     {   
    21         float tmp = min(temperature,humidity);   
    22         tmp = min(tmp, pressure);  
    23   
    24         cout << tmp <<endl;   
    25     }   
    26 };  
    27   
    28 class Max : public Observer   
    29 {   
    30     void updata(float temperature, float humidity, float pressure)   
    31     {   
    32         float tmp = max(temperature,humidity);   
    33         tmp = max(tmp, pressure);  
    34   
    35         cout << tmp <<endl;   
    36     }   
    37 };

然后是主题：

     1 class SubjectBase   
     2 {   
     3 public:   
     4     virtual void registerObject(shared_ptr<Observer> observer) = 0;   
     5     virtual void removeObject(shared_ptr<Observer> observer) = 0;   
     6     virtual void notifyObject() = 0;   
     7 };  
     8   
     9 class WeatherData : public SubjectBase   
    10 {   
    11 public:   
    12 explicit WeatherData(float temperature, float humidity, float pressure)   
    13     {   
    14         temperature_    = temperature;   
    15         humidity_        = humidity;   
    16         pressure_        = pressure;   
    17     }  
    18   
    19     void registerObject(shared_ptr<Observer> observer)   
    20     {   
    21         observersList_.push_back(observer);   
    22         //observer->updata(temperature_, humidity_, pressure_);   
    23     }   
    24     void removeObject(shared_ptr<Observer> observer)   
    25     {   
    26         observersList_.remove(observer);   
    27     }  
    28   
    29     void notifyObject()   
    30     {   
    31         for (list<shared_ptr<Observer>>::iterator pos = observersList_.begin();   
    32             pos != observersList_.end(); ++pos)   
    33         {   
    34             (*pos)->updata(temperature_, humidity_, pressure_);   
    35         }   
    36     }  
    37   
    38 private:   
    39     list<shared_ptr<Observer>>    observersList_;   
    40     float                        temperature_;   
    41     float                        humidity_;   
    42     float                        pressure_;   
    43 };

最后看下如何使用的：

     1 int _tmain(int argc, _TCHAR* argv[])   
     2 {   
     3     shared_ptr<Observer> observer_1(new Average());   
     4      shared_ptr<Observer> observer_2(new Min());   
     5      shared_ptr<Observer> observer_3(new Max());   
     6     shared_ptr<SubjectBase> subject(new WeatherData(100, 110, 120));   
     7     subject->registerObject(observer_1);   
     8     subject->registerObject(observer_2);   
     9     subject->registerObject(observer_3);   
    10     subject->notifyObject();  
    11   
    12     return 0;   
    13 }

posted @ 2011-05-25 21:16 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2057307) 收藏

##备注 
 @post in:2011-05-25 21:16