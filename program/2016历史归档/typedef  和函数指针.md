#  [typedef
和函数指针](http://www.cnblogs.com/sld666666/archive/2010/06/20/1761457.html)

# 1.函数指针的一般原理

一个函数只能做两件事情：被调用和返回其地址。

函数指针就是利用了返回其地址的特性。

一个典型的用法如下。

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) void error(string str)
    {
    	cout <<str<<endl;
    }
    
    void (*efct)(string);
    
    int _tmain(int argc, _TCHAR* argv[])
    {
     	efct = error;
    	efct("erro");
    }

恩，不过如此。

# 2\. 个typedef 结合使用

**typedef 返回类型（*新类型）(参数)**
    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)

# 3.nginx内存池中的ngx_pool_cleanup_t

nginx中定义了这样一个清理函数

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) typedef void (*ngx_pool_cleanup_pt)(void *data);
    struct ngx_pool_cleanup_s {
        ngx_pool_cleanup_pt   handler;
        void                 *data;
        ngx_pool_cleanup_t   *next;
    };

看一下它是怎么清理的：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) ngx_pool_cleanup_t*    c;
    
    for (c = pool->cleanup; c; c = c->next) {
            if (c->handler) {
                c->handler(c->data);
            }
        }

哇,太经典了，一个结构体中不仅保存着数据，而且保存了这些数据的清理函数，而这些清理函数是

作为结构体一部分的，当需要摧毁整个内存池的时候，直接调用这个结构体保存的清理函数，简直

用得太巧妙了。

posted @ 2010-06-20 17:48 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1761457) 收藏

##备注 
 @post in:2010-06-20 17:48