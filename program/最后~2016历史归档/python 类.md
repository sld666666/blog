#  [python
类](http://www.cnblogs.com/sld666666/archive/2011/05/25/2056694.html)

     1 class Person:   
     2     population = 0   
     3     def __init__(self, name):   
     4         self.name = name   
     5         Person.population += 1   
     6     def __del__(self):   
     7         print 'now,%d destory'%Person.population   
     8         print "\n"   
     9     def sayHi(self):   
    10         print 'Hello, how are you%d'%Person.population   
    11         print "\n"   
    12 p = Person("John")   
    13 p.sayHi()  
    14   
    15 kalam = Person("kalam")   
    16 kalam.sayHi()  
    17   
    18 p.sayHi()

1\. python的成员函数和普通函数的唯一区别是附加了一个额外的参数(self)。this

2\. __init__方法在对象建立时候被创建。构造函数

3\. 类的变量有这个类的每个对象拥有，没有对象拷贝一份这个变量。

4\. __del__ 就是析构函数

5\. self.name = name 表明这个变量是属于声明的对象的

6\. population = 0 变量是公有的。比： static

继承

class ChinaPersion(Person):

posted @ 2011-05-25 15:20 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2056694) 收藏

##备注 
 @post in:2011-05-25 15:20