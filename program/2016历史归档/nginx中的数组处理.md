#  [nginx中的数组处理](http://www.cnblogs.com/sld666666/archive/2010/07/04/1770938.h
tml)

nginx是用c开发的，在C中缺少像C++标准库之类的对数据结构进行包装的东西，

所以一切都得自己来。

nginx中自己定义了数组，在ngx_array.h和ngx_array.c文件中。这个数组是

可以动态扩展的，俗称“动态数组”（vector抗议了： 你抢了我的名字）。

自己重新定义数组而不用原生数组有如下几个好处：

1\. 方便管理。

2\. 安全，防止数组越界。

3.容量可扩展，效率高（只要不改变整个数组的总大小，对比vector中的capacity）。

看了相关代码，发现nginx中的动态数组对STL中vector有很重的模仿痕迹，当然两者

不是一个重量级的东西了。

## 1\. ngx_array_t代码分析

### 1.1 ngx_array_t的定义

首先看一下ngx_array_t结构体的定义：

    typedef	 unsigned __int64 ngx_uint_t;
    typedef struct ngx_array_s	ngx_array_t;
    
    
    struct ngx_array_s 
    {
    	void*		elts;//数据块
    	ngx_uint_t	nelts;//已经使用的个数
    	size_t		size;//每个数据的大小
    	ngx_uint_t	nalloc;//已经分配的区域
    	ngx_pool_t*	pool; //内存池
    
    };

其中

elts表示这个数组的数据块,

nelts便是已经使用的个数,

size 表示每个数据的大小(int， double)，

nalloc表示分配内存的大小

关于nelts和nalloc，可以对比一下STL中的size和capacity，两者是完全一样的概念。

ngx_pool_t就便是内存池了，elts的内存是从内存池中提取出来的， 在这里为了

给出源代码方便， 我屏蔽掉内存池，让其用malloc代替，所以就有了如下代码：

    struct ngx_pool_t
    {
    	void*	Nothing;
    };

nginx提供了如下5个函数来控制、使用ngx_array_st：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 //未创建 ngx_array_t结构体时，创建数组  
     2  ngx_array_t *ngx_array_create(ngx_pool_t *p, ngx_uint_t n, size_t，size);  
     3   
     4   
     5  //已创建ngx_array_t结构体时，创建数组  
     6    
     7  static inline int ngx_array_init(ngx_array_t *array, ngx_pool_t *pool, ngx_uint_t n, size_t size);  
     8   
     9   
    10  //delete 数组  
    11    
    12  void ngx_array_destroy(ngx_array_t *a);  
    13   
    14   
    15  //增加一个数组元素  
    16    
    17  void *ngx_array_push(ngx_array_t *a);  
    18   
    19   
    20  //增加N个数组元素  
    21    
    22  void *ngx_array_push_n(ngx_array_t *a, ngx_uint_t n);

下面逐一解释个个函数。

### 1.2 ngx_array_create

要使用一个动态数组，前提是创建它。函数ngx_array_create 表示

未创建 ngx_array_t结构体时，创建数组。它的实现非常简单，

首先创建一块内存区域，然后标志已经使用的和未尝使用的。

代码如下所示：

**

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    ngx_array_t * ngx_array_create(ngx_pool_t *p, ngx_uint_t n, size_t size)  
    {  
        ngx_array_t* a;  
        //a = ngx_palloc(p, sizeof(ngx_array_t));  
        a = (ngx_array_t*)malloc(sizeof(ngx_array_t));  
        if (a == NULL)  
            return NULL;  
        a->elts = (void*)malloc(n*size);  
        if (a->elts == NULL)  
            return NULL;  
        a->nelts = 0;  
        a->size = size;  
        a->nalloc = n;  
        a->pool = p;  
        return a;  
    }

当我们使用的时候，直接

**

ngx_array_t* parray = ngx_array_create(NULL, 10, sizeof(int));

这里用NULL是因为我屏蔽掉了内存池。

### 1.3 ngx_array_push

创建了数组的有效地址之后，我们就要使用它了，有两函数：

ngx_array_push 和 ngx_array_push_n，因为这两个函数

非常相似， 所以这里只提取其中一个ngx_array_push来分析。

ngx_array_push的代码非常简单：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    void *ngx_array_push(ngx_array_t *a)  
    {  
        void        *elt, *pNew;//这个原来是new，因为我用.cpp所以改成这个  
        size_t    size;  
        ngx_pool_t *p;  
        if (a->nelts == a->nalloc)  
        {  
            //当分配的个数等与已经用掉的个数  
            //即这个array 满了  
            size = a->size * a->nalloc;  
            pNew = (void* )malloc(2*size);  
            if (pNew == NULL)  
                return NULL;  
            memcpy(pNew, a->elts, size);  
            a->elts = pNew;  
            a->nalloc *= 2;  
            //整一个array扩大2陪，vector也是这样的  
            //然后用原来的数据初始化新申请的区域  
        }  
        elt = (u_char* ) a->elts + a->size * a->nelts;  
        a->nelts++;  
        return elt;//返回的是当前的新增加的数据  
    }

这个函数干了一件事：得到数组中下一个未使用的地址，如果这个数组满了，

  

就扩大一倍。

所以上层的调用代码可以为这样：

int* pTest = (int*)ngx_array_push(parray);

*(pTest )= 10;

int* pTest = (int*)ngx_array_push(parray);

*(pTest )= 10;

### 1.4 ngx_array_destroy

看完了创建，使用以后，在来看怎么删除它：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    void ngx_array_destroy(ngx_array_t *a)  
    {  
        if (a == NULL)  
            return ;  
        free (a);  
    //如果用内存池的话就稍微复杂了点，需要把不用的内存还给内存池。  
    //     ngx_pool_t  *p;  
    //   
    //     p = a->pool;  
    //   
    //     if ((u_char *) a->elts + a->size * a->nalloc == p->d.last) {  
    //         p->d.last -= a->size * a->nalloc;  
    //     }  
    //   
    //     if ((u_char *) a + sizeof(ngx_array_t) == p->d.last) {  
    //         p->d.last = (u_char *) a;  
    //     }  
    }

然后呢？怎么使用？一个伪列子如下：

    ngx_array_t* parray = ngx_array_create(NULL, 2, sizeof(int));  
    int* pTest = (int*)ngx_array_push(parray);  
    ngx_array_destroy(parray);

## 2\. 小结

好了，nginx的数组管理就分析到这里，个人感觉其不管是功能还是易用程度都与C++标准库中的vector不是一个

档次的， 打算在下一篇分析下STL种的vector作为对比。

posted @ 2010-07-04 18:58 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1770938) 收藏

##备注 
 @post in:2010-07-04 18:58