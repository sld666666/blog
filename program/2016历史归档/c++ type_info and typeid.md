#  [c++ type_info and typeid](http://www.cnblogs.com/sld666666/p/3963182.html)

## c++ type_info and typeid

### typeid

关键字typeid提供了对一个对象查询类型的功能。 该关键字和dynami_cast一起提供了c++的RTTI(rumtime type
identification)支持.

* * *
    struct MyStruct
    {
        int i;
    };
    int _tmain(int argc, _TCHAR* argv[])
    {
        int i(0);
        double j(1.0);
        MyStruct myObj;
        cout << typeid(int).name() << endl;
        cout << typeid(i).name() << endl;
        cout << typeid(&i).name() << endl;
        cout << typeid(MyStruct).name() << endl;
        cout << typeid(myObj).name() << endl;
        cout << typeid(&myObj).name() << endl;
        return 0;
    }

* * *

结果为：  
![typeid_show](http://pic2.qnpic.com/doimg/sld666666/43e14610/)

但是等等， typeid不是运行时确定的吗， 为什么cout << typeid(int).name() << endl;这样的代码是可以的。 那是因为：

* * *

When applied to an expression of polymorphic type, evaluation of a typeid
expression may involve runtime overhead (a virtual table lookup), otherwise
typeid expression is resolved at compile time.

* * *

好吧， c++编译器果然据说是这个世界上最难写的编译器了。

#### std::type_info

typeid
返回的是类type_info的对象，它由标准库提供，在"typeinfo"中实现。但是我们不能直接使用std::type_info,它提供了以下几个方法：

  * operator==
  * operator!=
  * before
  * name
  * raw_name
  * hash_code

* * *

std::type_info info = typeid(int);

type_[info::type_info(const](info::type_info\(const) type_info &)”: 尝试引用已删除的函数

* * *

那是因为type_info的构造函数和operator=函数都被删除的

* * *
    __CLR_OR_THIS_CALL type_info(const type_info&) = delete;
    type_info& __CLR_OR_THIS_CALL operator=(const type_info&) = delete;

* * *

但是我们可以这么使用：

* * *
    const std::type_info& info = typeid(int);
    bool same = (info == typeid(int));
    cout << same << endl;

* * *

### 总结

理解了 type_info & typeid的用法以及细节

posted @ 2014-09-09 18:55 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=3963182) 收藏

##备注 
 @post in:2014-09-09 18:55