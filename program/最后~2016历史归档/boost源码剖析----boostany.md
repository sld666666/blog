#  [boost源码剖析----boost::any](http://www.cnblogs.com/sld666666/p/3965147.html)

# boost源码剖析----boost::any

有的时候我们需要有一个万能类型来进行一些操作，这时候boost::any就派上用场了。

    boost::Any testInt(10);
    int val = static_cast<int>(testInt);

用法比较简单，我们来研究下boost::any是如何实现的。

## 原理

c++是一个强类型的语言，要实现一个万能类型可以考虑用void*来保存数据，然后用类型转换进行操作，如：

    class MyAny{
        MyAny(void* input):content_(input){
        }
        template<typename T>
        T any_cast(){return *static_cast<T*>(content_)}
        private:
        void* content_;
    }

但是这样的写法有一个明显的缺点就是类型不安全。

显然我们可以用template来改进我们的程序：

    template<typename T>
    class MyAny{
        MyAny(T input):content_(input){
        }
        T any_cast(){return content_;}
        private:
        T content_;
    }

但是这样我们好像就没有解决问题：vector> 好吧，这里就写不下去了。

为了能写下如下的代码：

    vector<MyAny> items;
    items.push_bacck(1);
    items.push_bacck(2.0);

我们需要我们的万能类型有如下的行为：

  1. 对外是一个一般的类，使用者压入参数的时候不应该关心类型
  2. 它只是一个中间层，具体保存数据的应该是一个模板类(Holder)
  3. 必须要能有方法支持任意类型的输入和输出为任意类型

## 实现

我们可以通过添加一个中间层来解决任何问题。  
在boost::any中， 通过两个中间层来达成我们上面的目标， 类any作为对外的接口层，承担以模板作为参数并提供必要的对外方法。  
类placeholder作为接口类，让any使用。而holder是一个模板类作为类placeholder的实现者，
这样就避免的any对泛型参数的要求（能自动推到导出来）。  
我这里模仿了相关的实现，其代码结构应该是这样的：

    class  Any
    {
        public:
            Any() :holder_(nullptr){}
            template<typename ValueType>
            Any(const ValueType& val)
                : holder_(new Holder<ValueType>(val)){
            }
        private:
            IHolder* holder_;
        };
        mb_interface IHolder{
    }
    template<typename ValueType>
    class Holder : public IHolder{
        public:
            Holder(const ValueType& val) : value_(val){
            }
        }
        public:
            ValueType value_;
    }

其中Holder提供了具体的数据存储服务，而 Any提供了对外的接口能力。  
  
其中Holder必须提供两个方法：

        mb_interface IHolder{
            virtual ~IHolder(){}
            virtual const std::type_info& type() const = 0;
            virtual IHolder* clone() const = 0;
        };

  1. type()提供了查询类型的能力
  2. clone()提供了产生数据的能力

在 Any中， 提供了以下几个个接口：

        bool    empty(){
            return !holder_;
        }
        const std::type_info& type() const {
            return holder_ ? holder_->type() : typeid(void);
        }
        Any& operator=(Any rhs){
            return swap(rhs);
        }
        template<typename ValueType>
        Any& operator=(const ValueType& val){
            return Any(val).swap(*this);
        }

判断是否为空，查询类型操作，赋值操作

当然必须还有最重要的any_cast操作，我们看其实现：

    template<typename ValueType>
    ValueType* anyCast(Any* val){
        return (val && val->type() == typeid(ValueType))
            ? &static_cast<Holder<ValueType>*>(val->getHolder())->value_ : 0;
    }
    template<typename ValueType>
    ValueType anyCast(Any& val){
        ValueType* rtn = anyCast<ValueType>(&val);
        if (!rtn)boost::throw_exception(badAnyCast());
        return *rtn;
    }

果然好简单。呵呵~~~

最后添上单元测试，整个代码就完善了：

    mb::Any testInt(10);
    mb::Any testDouble(10.0);
    mb::Any testInt2(testInt);
    EXPECT_EQ(testInt.empty(), false);
    EXPECT_EQ(std::string(testDouble.type().name()), "double");
    EXPECT_EQ(std::string(testInt2.type().name()), "int");
    int val = mb::anyCast<int>(testInt);
    EXPECT_EQ(val, 10);

## 总结

  1. 代码和boost::any中有一些出入，但是我们的目的是为了研究其实现，就忽略了某些细节
  2. 模板技巧： 模板类原来还可以这么用---声明非模板接口，并用模板类实现， 这样在使用这个接口的时候就能避免宿主类显示声明参数类型
  3. boost::any是整个boost库中最简单的类之一，但是某些代码细节还是非常值得学习和借鉴的。
  4. typeid和type_info 感觉有点像c++中的鸡肋，但是某些时候还是有用的
  5. 相关的代码上传到github上，有需要的同学可以看下[any.h](https://github.com/sld666666/myboost/blob/master/myboost/any/any.h),  
[holder.h](https://github.com/sld666666/myboost/blob/master/myboost/any/holder
.h)

posted @ 2014-09-10 21:01 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=3965147) 收藏

##备注 
 @post in:2014-09-10 21:01