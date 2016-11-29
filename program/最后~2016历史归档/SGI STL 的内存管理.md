#  [SGI STL
的内存管理](http://www.cnblogs.com/sld666666/archive/2010/07/01/1769448.html)

# 1\. 好多废话

在分析完nginx的内存池之后，也想了解一下C++的内存管理，于是就很自然得想到STL。

STL是一个重量级的作品，据说当时的出现，完全可以说得上是一个划时代意义的作品。

泛型、数据结构和算法的分离、底耦合、高复用… 啊，废话不多说了，再说下去让人感觉像

王婆卖瓜了。

啊，还忘了得加上两位STL大师的名字来聊表我的敬意了。泛型大牛Alexander Stepanov

和 Meng Lee（李梦--让人浮想的名字啊）。

# 2\. SLT 内存的分配

以一个简单的例子开始。

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) #include <vector>
    #include <algorithm>
    using namespace std;
    
    void print( int elem)
    {
            cout << elem <<  ' ';
    }
    
    
    int main()
    {
            vector<int> vec;
    	for (int i = 0; i != 10; ++i)
    		vec.push_back(i);
    
    	for_each(vec.begin(), vec.end(), print);
            //请允许我卖弄一点点小特性
    	cout << endl;
    
    	return 0;
    
    }

我们想知道的时候， 当vec声明的时候和push_back的时候，是怎么分配的。

其实对于一个标准的STL 容器，当Vetor<int> vec 的真实语句应该是 vetor<int, allocator<int>>vec，

allocator是一个标准的配置器，其作用就是为各个容器管理内存。这里需要注意的是在SGI STL中，有两个

配置器：allocator(标准的)和alloc(自己实现的，非常经典，这篇文章的主要目的就是为了分析它)。

# 3\. 一个标准的配置器

要写一个配置器并不是很难，最重要的问题是如何分配和回收内存。下面看下一个标准（也许只能称为典型）

的配置器的实现：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) #include <new>// for new
    #include <cstddef> //  size_t
    #include <climits> // for unit_max
    #include <iostream> // for cerr
    using namespace std;
    
    namespace SLD {
    template <class T>
    class allocator
    {
    public:
    	typedef T		value_type;
    	typedef T*		pointer;
    	typedef const T*	const_pointer;
    	typedef T&		reference;
    	typedef const T&	const_reference;
    	typedef size_t		size_type;
    	typedef ptrdiff_t	difference_type;
    
    	template <class U>
    	struct rebind
    	{
    		typedef allocator<U> other;
    	};
    
    	//申请内存
    	pointer allocate(size_type n, const void* hint = 0)
    	{
    		T* tmp = (T*)(::operator new((size_t)(n * sizeof(T))));
    		//operator new 和new operator是不同的
    		if (!tmp)
    			cerr << "out of memory"<<endl;
    		
    		return tmp;
    
    	}
    
    	//释放内存
    	void deallocate(pointer p)
    	{
    		::operator delete(p);
    	}
    	
    	//构造
    	void construct(pointer p, const T& value)
    	{
    		new(p) T1(value);
    	}
    	
    	//析构
    	void destroy(pointer p)
    	{
    		p->~T();
    	}
    	
    	//取地址
    	pointer address(reference x)
    	{
    		return (pointer)&x;
    	}
    	
    
    	const_pointer const_address(const_reference x)
    	{
    		return (const_pointer)&x;
    	}
    
    	size_type max_size() const 
    	{
    		return size_type(UINT_MAX/sizeof(T));
    	}
    };
    }

  
注：代码有比较大的改动，因为主要是为了理解。

在使用的时候， 只需这样vector<int, SLD::allocator<int>>vec; 即可。

vetor便会自动调用我们的配置器分配内存了。

要自己写个配置器完全可以以这个类为模板。 而需要做的工作便是写下自己的 allocate和deallocate即可。

其实SGI的allocator 就是这样直接调用operator new 和::operator delete实现的，不过这样做的话效率就很

差了。

# 4\. SGI STL中的alloc

## 4.1 SGI 中的内存管理

SGI STL默认的适配器是alloc，所以我们在声明一个vector的时候实际上是这样的

vetor<int, alloc<int>>vec. 这个配置器写得非常经典，下面就来慢慢分析它。

在我们敲下如下代码：

CSld* sld = new CSld;

的时候其实干了两件事情：（1） 调用::operator new 申请一块内存（就是malloc了）

（2） 调用了CSld::CSld();

而在SGI中， 其内存分配把这两步独立出了两个函数：allocate 申请内存， construct 调用构造函数。

他们分别在<stl_alloc.h>, <stl_construct.h> 中。

SGI的内存管理比上面所说的更复杂一些， 首先看一些SGI内存管理的几个主要文件，如下图所示：

![SGI Memory](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWrite
r/SGISTL_10C9C/SGI%20Memory_thumb.jpg)

<图1. SGI 内存管理>

在stl_construct.h中定义了两个全局函数construct()和destroy()来管理构造和析构。

在stl_allo.h中定义了5个配置器， 我们现在关心的是malloc_alloc_template(一级)

和default_alloc_template(二级)。在SGI中，如果用了一级配置器，便是直接使用了

malloc()和free()函数，而如果使用了二级适配器，则如果所申请的内存区域大于128b,

直接使用一级适配器，否则，使用二级适配器。

而stl_uninitialized.h中，则定义了一下全局函数来进行大块内存的申请和复制。

是不是和nginx中的内存池很相似啊，不过复杂多了。

## 4.2一级配置器：__malloc_alloc_template

上面说过， SGI STL中， 如果申请的内存区域大于128B的时候，就会调用一级适配器，

而一级适配器的调用也是非常简单的， 直接用malloc申请内存，用free释放内存。

可也看下如下的代码：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) class __malloc_alloc_template {
    
    private:
      // oom = out of memroy,当内存不足的时候，我要用下面这两个函数
      static void* _S_oom_malloc(size_t);
      static void* _S_oom_realloc(void*, size_t);
    
    public:
    
      //申请内存
      static void* allocate(size_t __n)
      {
        void* __result = malloc(__n);
        //如果不足，我有不足的处理方法
        if (0 == __result) __result = _S_oom_malloc(__n);
        return __result;
      }
    
     //直接释放掉了
      static void deallocate(void* __p, size_t /* __n */)
      {
        free(__p);
      }
     //重新分配内存
      static void* reallocate(void* __p, size_t /* old_sz */, size_t __new_sz)
      {
        void* __result = realloc(__p, __new_sz);
        if (0 == __result) __result = _S_oom_realloc(__p, __new_sz);
        return __result;
      }
     //模拟C++的 set_new_handler,函数，
     //为什么要模拟，因为现在用的是C的内存管理函数。
      static void (* __set_malloc_handler(void (*__f)()))()
      {
        void (* __old)() = __malloc_alloc_oom_handler;
        __malloc_alloc_oom_handler = __f;
        return(__old);
      }
    
    };

  
好了， 很简单把，只是对malloc，free, realloc简单的封装。

## 4.3 二级配置器：__default_alloc_template

按上文所说的，SGI的 __default_alloc_template 就是一个内存池了。

我们首先来看一下它的代码：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) template <bool threads, int inst>
    class __default_alloc_template {
    
    private:
      // Really we should use static const int x = N
      // instead of enum { x = N }, but few compilers accept the former.
        enum {_ALIGN = 8};//小块区域的上界
        enum {_MAX_BYTES = 128};//小块区域的下降
        enum {_NFREELISTS = 16}; // _MAX_BYTES/_ALIGN，有多少个区域
    /*SGI 为了方便内存管理， 把128B 分成16*8 的块*/
    
    //将Byte调到8的倍数
      static size_t
      _S_round_up(size_t __bytes) 
        { return (((__bytes) + (size_t) _ALIGN-1) & ~((size_t) _ALIGN - 1)); }
    
    //管理内存的链表，待会会详细分析这个
      union _Obj {
            union _Obj* _M_free_list_link;
            char _M_client_data[1];    /* The client sees this.        */
      };
    private:
        //声明了16个 free_list, 注意 _S_free_list是成员变量
        static _Obj* __STL_VOLATILE _S_free_list[_NFREELISTS];
    
     //同了第几个free_list, 即_S_free_list[n],当然这里是更具区域大小来计算的
      static  size_t _S_freelist_index(size_t __bytes) {
            return (((__bytes) + (size_t)_ALIGN-1)/(size_t)_ALIGN - 1);
      }
    
      // Returns an object of size __n, and optionally adds to size __n free list.
      static void* _S_refill(size_t __n);
    
      // Allocates a chunk for nobjs of size size.  nobjs may be reduced
      // if it is inconvenient to allocate the requested number.
      static char* _S_chunk_alloc(size_t __size, int& __nobjs);
    
      // Chunk allocation state.
      static char* _S_start_free;//内存池的起始位置
      static char* _S_end_free;//内存池的结束位置
      static size_t _S_heap_size;//堆的大小
    
      /*这里删除一堆多线程的代码*/
    public:
    
       //分配内存，容后分析
      /* __n must be > 0      */
      static void* allocate(size_t __n);
    
       //释放内存，容后分析
      /* __p may not be 0 */
      static void deallocate(void* __p, size_t __n);
    
      //从新分配内存
      static void* reallocate(void* __p, size_t __old_sz, size_t __new_sz);
    
     }
    
      //下面是一些 成员函数的初始值的设定
    template <bool __threads, int __inst>
    char* __default_alloc_template<__threads, __inst>::_S_start_free = 0;
    
    template <bool __threads, int __inst>
    char* __default_alloc_template<__threads, __inst>::_S_end_free = 0;
    
    template <bool __threads, int __inst>
    size_t __default_alloc_template<__threads, __inst>::_S_heap_size = 0;
    
    template <bool __threads, int __inst>
    typename __default_alloc_template<__threads, __inst>::_Obj* __STL_VOLATILE
    __default_alloc_template<__threads, __inst> ::_S_free_list[] = 
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, };

  
我们最关心的有三点：1. 内存池的创建。2.内存的分配。 3. 内存的释放。

### 4.3.1 SGI内存池的结构

在分析内存池的创建之前我们首先需要看下SGI内存池的结构。

在__default_alloc_template 内部，维护着这样一个结构体：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif)   union _Obj {
            union _Obj* _M_free_list_link;
            char _M_client_data[1];    /* The client sees this.        */
      };
    static _Obj*  _S_free_list[]; //我就是这样用的 

其实一个free_list 就是一个链表，如下图所示：

![link](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/SGIS
TL_10C9C/link_thumb.jpg)

<图2. free_list的链表表示>

这里需要注意的有两点：

一：SGI 内部其实维护着16个free-list，对应管理的大小为8,16,32……128.

二：_Obj是一个union而不是sturct, 我们知道，union中的所有成员的引用在内存中的位置都是

相同的。这里我们用union就可以把每一个节点需要的额外的指针的负担消除掉。

### 4.3.2 二级配置器的内存分配：allocate

比如现在我要申请一块30B的空间，我要怎么申请呢？

首先会呼叫二级配置器， 调用 allocate，在allocate函数之内， 从对应的32B的链表中拿出空间。

如果对应的链表空间不足，就会先用填充至32B，然后用refill()冲洗填充该链表。

相应的代码如下：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif)   static void* allocate(size_t __n)
      {
        void* __ret = 0;
    
        if (__n > (size_t) _MAX_BYTES) {
       //如果大于128B， 直接调用一级配置器
          __ret = malloc_alloc::allocate(__n);
        }
        else {
          //找出 16个free-list 中的一个
          _Obj* __STL_VOLATILE* __my_free_list
              = _S_free_list + _S_freelist_index(__n);
    
          _Obj* __RESTRICT __result = *__my_free_list;
          if (__result == 0)
         //如果满了,则我refill整一个链表
            __ret = _S_refill(_S_round_up(__n));
          else {
            *__my_free_list = __result -> _M_free_list_link;
            __ret = __result;
          }
        }
    
        return __ret;
      };

下面画了一张图来帮助理解：

![GetMemory](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter
/SGISTL_10C9C/GetMemory_thumb.jpg)

<图3. GetMemory>

### 4.3.3 二级配置器的内存释放：allocate

有内存的分配，当然得要释放了，下面就来看看是如何释放的：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif)   static void deallocate(void* __p, size_t __n)
      {
        if (__n > (size_t) _MAX_BYTES)
        //如果大于128,直接释放
          malloc_alloc::deallocate(__p, __n);
        else {
        //找到对应的链表
          _Obj* __STL_VOLATILE*  __my_free_list
              = _S_free_list + _S_freelist_index(__n);
          _Obj* __q = (_Obj*)__p;
        //回收，该链表
          __q -> _M_free_list_link = *__my_free_list;
          *__my_free_list = __q;
          // lock is released here
        }
      }

### 4.3.4 二级配置器的内存池：chunk_alloc

前面说过，在分配内存时候如果空间不足会调用_S_refill函数，重新填充空间（ps:如果这是第一个的话，

就是创建了）。而_S_refill最终调用的又是chunk_alloc函数从内存池中提取内存空间。

首先我们看一下它的源代码：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) /* We allocate memory in large chunks in order to avoid fragmenting     */
    /* the malloc heap too much.                                            */
    /* We assume that size is properly aligned.                             */
    /* We hold the allocation lock.                                         */
    template <bool __threads, int __inst>
    char*
    __default_alloc_template<__threads, __inst>::_S_chunk_alloc(size_t __size, 
                                                                int& __nobjs)
    {
        char* __result;
        size_t __total_bytes = __size * __nobjs;//申请的总内存空间
        size_t __bytes_left = _S_end_free - _S_start_free;//内存池剩余的内存空间
    
        if (__bytes_left >= __total_bytes) {
         //如果你能满足我
            __result = _S_start_free;
            _S_start_free += __total_bytes;
            00ff">return(__result);
        } else if (__bytes_left >= __size) {
        //如果能满足我一块或一块以上，参考__Obj这个联合体（free_list）
            __nobjs = (int)(__bytes_left/__size);
            __total_bytes = __size * __nobjs;
            __result = _S_start_free;
            _S_start_free += __total_bytes;
            return(__result);
        } else {
        //如果连一块都给不出
            size_t __bytes_to_get = 
    	  2 * __total_bytes + _S_round_up(_S_heap_size >> 4);
            // Try to make use of the left-over piece.
            if (__bytes_left > 0) {
                _Obj* __STL_VOLATILE* __my_free_list =
                            _S_free_list + _S_freelist_index(__bytes_left);
    
                ((_Obj*)_S_start_free) -> _M_free_list_link = *__my_free_list;
                *__my_free_list = (_Obj*)_S_start_free;
            }
         .//从堆空间重新分配内存
            _S_start_free = (char*)malloc(__bytes_to_get);
            if (0 == _S_start_free) {
          //连堆都没有内存了
                size_t __i;
                _Obj* __STL_VOLATILE* __my_free_list;
    	    _Obj* __p;
                // Try to make do with what we have.  That can't
                // hurt.  We do not try smaller requests, since that tends
                // to result in disaster on multi-process machines.
                for (__i = __size;
                     __i <= (size_t) _MAX_BYTES;
                     __i += (size_t) _ALIGN) {
                    __my_free_list = _S_free_list + _S_freelist_index(__i);
                    __p = *__my_free_list;
                    if (0 != __p) {
                        *__my_free_list = __p -> _M_free_list_link;
                        _S_start_free = (char*)__p;
                        _S_end_free = _S_start_free + __i;
                        return(_S_chunk_alloc(__size, __nobjs));
                        // Any leftover piece will eventually make it to the
                        // right free list.
                    }
                }
    	    _S_end_free = 0;	// In case of exception.
                //调用一级配置器，主要是为了调用_S_oom_malloc压榨出内存来
                _S_start_free = (char*)malloc_alloc::allocate(__bytes_to_get);
                // This should either throw an
                // exception or remedy the situation.  Thus we assume it
                // succeeded.
            }
            //更改一下内存池
            _S_heap_size += __bytes_to_get;
            _S_end_free = _S_start_free + __bytes_to_get;
            return(_S_chunk_alloc(__size, __nobjs));
        }
    }

  
区间[_S_start_free, _S_end_free)便是内存池的总空间（参考类：__default_alloc_template的定义）。

当申请一块内存时候，如果内存池总内存量充足，直接分配，不然就各有各的处理方法了。

下面举一个例子来简单得说明一下：

1\. 当第一次调用chunk_alloc(32,10)的时候，表示我要申请10块__Obje(free_list)， 每块大小32B，

此时，内存池大小为0，从堆空间申请32*20的大小的内存，把其中32*10大小的分给free_list[3]（参考图3）。

2\. 我再次申请64*5大小的空间，此时free_list[7]为0， 它要从内存池提取内存，而此时内存池剩下320B，

刚好填充给free_list[7]，内存池此时大小为0。

3\. 我第三次神奇一耳光72*10大小的空间，此时free_list[8]为0，它要从内存池提取内存，此时内存池空间

不足，再次从堆空间申请72*20大小的空间，分72*10给free_list用。

整一个SGI内存分配的大体流程就是这样了。

# 5\. 小结

SIG的内存池比nginx中的复杂多了。简单得分析一下+写这篇文章花了我整整3个晚上的时间。

啊，我的青春啊。

posted @ 2010-07-01 19:52 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1769448) 收藏

##备注 
 @post in:2010-07-01 19:52