#  [cpp异常详解](http://www.cnblogs.com/sld666666/p/4090418.html)

### 1\. 异常介绍

在函数在执行过程中如果碰到对错误的处理可以有两种方式， 1. 返回错误，2. 使用异常。 如果作为函数的调用者想要知道具体的错误信息，
就需要维护一套错误列表， 或者用string类型的返回。显然这两种使用起来都不那么方便。

C++中处理异常的过程是这样的：在执行程序发生异常，可以不在本函数中处理，而是抛出一个错误信息，把它传递给上一级的函数来解决，上一级解决不了，再传给其上一级
，由其上一级处理。如此逐级上传，直到最高一级还无法处理的话，运行系统会自动调用系统函数terminate，由它调用abort终止程序。这样的异常处理方法使得
异常引发和处理机制分离，而不在同一个函数中处理。这使得底层函数只需要解决实际的任务，而不必过多考虑对异常的处理，而把异常处理的任务交给上一层函数去处理。

### 2\. 使用介绍

c++的异常处理机制由三部分组成：try(检查)， throw（抛出），catch(捕获)。最典型的用法如下：

* * *
    void doThrow(){
        throw std::string("throw everything");
    }
    void testException(){
        try
        {
            doThrow();
        }
        catch (std::string e){
            std::cout << e << std::endl;
        }
        catch (...)
        {
            std::cout << "exception" << std::endl;
        }
    }

* * *

throw 可以任何东西，catch也可以捕捉然后exception.

### 3\. 进一步用法

当然编程中有一个非常重要的约定就是“约束”。我们要对能抛出的异常能进行约束， 所以标准库提供了一个类std::exception
来给我们继承使用。exception 中一个最重要的函数是：

* * *
    class exception{
    virtual const char * __CLR_OR_THIS_CALL what() const _THROW0()
        {   // return pointer to message string
        return (_Ptr != 0 ? _Ptr : "unknown exception");
        }
    }

* * *

我们可以重载它，然后抛出自己的错误信息。举个例子：

* * *
    class ParserException : public std::exception
    {
    public:
        ParserException(const char *what, void *where)
            : m_what(what)
            , m_where(where)
        {
        }
        virtual const char *what() const throw()
        {
            return m_what;
        }
        template<class Ch>
        Ch *where() const
        {
            return reinterpret_cast<Ch *>(m_where);
        }
    private:
        const char *m_what;
        void *m_where;
    };
    #define PARSE_ERROR(what, where) throw ParserException(what, where)
    void doThrow(){
        PARSE_ERROR("throw everything", nullptr);
    }
    void testException(){
        try
        {
            doThrow();
        }
        catch (ParserException e){
            std::cout << e.what() << std::endl;
        }
    }

* * *

好吧， 其实换汤不换药。

### 4\. 总结

本文主要介绍了c++异常的典型使用方法。  
异常时一种常见的错误处理方法，它可以把错误从程序的逻辑处理中剥离出来。当然显然，异常在c++中就是一个能引起口水的问题，
用还是不用？好吧我的意见就是：想用就用。

posted @ 2014-11-11 20:20 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4090418) 收藏

##备注 
 @post in:2014-11-11 20:20