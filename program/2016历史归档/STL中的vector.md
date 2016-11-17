#  [STL中的vector](http://www.cnblogs.com/sld666666/archive/2010/07/11/1775276.h
tml)

在上一篇中，我分析了nginx中的动态数组， 作为对比这里再分析一下SGI STL

中的动态数组(vector)。

在开篇之前想借此文大家一个问题，STL在大家的工程中用得多吗？本人现在的

公司C++的代码都是用MFC开发的，公司项目也算比较多的，但是在开发过程中用

上标准库的真的不太多！？

## 1\. vector 源代码分析

STL中的vector也可以认为是对array的包装和升级。

\- vector 在其内部保持着一个原始的数组(一块连续的内存区域)，但是提供了计算size,capacity，以及

动态扩展的能力。

\- vector 可以认为是一种全新的数据类型，所以必须重载=, []等运算符

\- 提供了push_back, pop_back，clear,erase, resize等操作元素的能力

\- 提供empty ， size,capacity等判断内部元素的能力

\- 支持迭代

### 1.1 vector的属性

在vector中定义了三个成员变量，代码如下：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) namespace SLD
    {
    	template <class T>
    	class vector
    	{
    	private:
    		T*		m_start;//使用空间的开头
    		T*		m_finish;//使用空间的末尾
    		T*		m_end_of_storage//可用空间的末尾
    	};
    }

其中 m_finish – m_start 就等于这个vector的size,

m_end_of_storage – m_start就等于这个vector的capacity。

### 1.2 vector的构造

vector支持五种类型的初始化化， 所以它就必须有5个构造函数：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) explicit vector():m_start(0), m_finish(0), m_end_of_storage(0) {}
    
    vector(size_t n, const T& value) 
    {
    	Base(n);
    	m_finish = uninitialized_fill_n(m_start, n, value);
    }
    
    explicit vector(size_t n)
    {
    	Base(n);
    	m_finish = uninitialized_fill_n(m_start, n, T());
    }
    		
    template <class p>
    vector(const SLD::vector<p>& x)
    {
    	Base(x.size());
    	uninitialized_copy(x.begin(), x.end(), m_start);
    }
    
    vector(T* first, T* last) 
    {
    	Base(last - first);
    	uninitialized_copy(first, last, m_start);
    
    }
    

在这里， 我为了代码的简单， 去掉了STL中从alloc分配内存的动作， 而直接用new delete，

这样管理内存，性能当然很差了， 但是用来说明vector却足够了， 所以uninitialized_copy

和uninitialized_fill_n 函数 我也仿造STL中从新写了一个， 不然的话光是内存分配的动作就能

拉出一大堆的代码。下面是我仿造的几个函数：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) private:
    //为了简便我在这里重新模拟了uninitialized_fill_n
    //在SLT中它是在stl_uninitialized.h中单独实现的
    T* uninitialized_fill_n(T* first, size_t n, const T& value)
    {
    	assert(first);
    	assert(n >= 0);
    			
    	for (int i = 0; i < n; ++i)
    		*(first + i) = value;
    
    	return first + n;//[m_start, m_finish)
    }
    
    void uninitialized_copy(T* source_begin, T* source_end, T* dest)
    {
    	for (int i = 0; i < (source_end - source_begin); ++i)
    	{
    		*(dest + i) = *(source_begin + i);
    	}
    }
    
    void Base (size_t n)
    {
    	//m_start = _M_allocate(__n);原本是从alloc中分配的，这里为了简单用malloc代替
    	m_start = new T[n*2];//这个是我写的， 用来代替原来的从alloc中分配的,
    						//这样做的原因是为了得到最少的能编译通过的代码
    	m_finish = m_start;
    	m_end_of_storage = m_start + 2*n;
    }

  
这样我们就可以写下如下的测试代码了：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) SLD::vector<int> test0;
    SLD::vector<int> test1(10,4);
    SLD::vector<int> test2(10);
    SLD::vector<int> test3(test1);
    SLD::vector<int> test4(test1.begin(), test1.end());

### 1.3 vector的元素的迭代

vector必须支持迭代功能，说到迭代功能，不得不说下STL种的迭代器。

迭代器中STL中扮演着很重要的角色. STL的核心思想就是：将数据结构和算法分离。

而迭代器就是作为数据结构和算法的粘合层而存在的。在STL中，每一个容器而有一个自己

的迭代器， 而vector的迭代器就是一个普通的指针。所以在vector中实现迭代功能就非常

的简单了。代码如下：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) T* begin(){return m_start;}
    T* end(){return m_finish;}

  
然后调用的使用只需：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) for (int* p = test1.begin(); p != test1.end(); ++p)
    {
    	std::cout<<*p<<std::endl;
    }

### 1.4 vector的元素判断

vector 提供了计算大小， 容量， 是不是空 等一系列的判断，实现起来也非常简单：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) size_t size(){return size_t(m_finish - m_start);}
    
    size_t capacity() const 
    {
    	return size_t(m_end_of_storage - m_start);
    }
    
    bool empty()const {return begin() == end()}

### 1.5 vector的运算符

在使用vector的时候，可以吧它认为是一种全新的数据类型， 所以[], ==, != , <= 等运算符

的重载是必须的。首先我们看下它是如何进行[]和= 的：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) T&	operator[](size_t n) {return *(begin() + n);}
    		
    template <class T1>
    vector<T1>& operator=(const vector<T1>& x)
    {
    	if (&x != this)
    	{
    		const size_t xlen = x.size();
    		if (xlen > capacity())
    		{
    			//从新分配内存
    			delete [] m_start;
    			Base[xlen];
    
    		}
    		else (size() >= xlen )
    		{
    			T1*p = copy(x.begin(), x.end(), begin());
    
    			delete []  p; //删除多余的
    		}
    		else
    		{
    			copy(x.begin(), x.begin() + size(), m_start);
    			uninitialized_copy(x.begin() + size(), x.end(), m_finish);
    		}
    				
    		m_finish = m_start + xlen;
    	}
    
    	return *this;
    }

有了实现我们就可以这样使用它了：

test0 = test1;  
std::cout<<test0[2]<<endl;

然后我们在来看下几个比较运算符的实现：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) 	template <class T>
    	inline bool operator==(const vector<T>& x, const vector<T>& y)
    	{
    		return (x.size() == y.size()) &&
    			equal(x.begin(), x.end(), y.begin(), y.end());
    		//这里直接用了algorithm中的equal算法
    	}
    	
    	template <class T>
    	inline bool operator< (const vector<T>& x, const vector<T>& y)
    	{
    		return lexicographical_compare(x.begin(), x.end(), 
    										y.begin(), y.end());
    		//还是借用了algorithm中的算法
    	}
    
    	template <class T>
    	inline bool operator!= (const vector<T>& x, const vector<T>& y)
    	{
    		return !(x == y);
    	}
    
    	template <class T>
    	inline bool operator>= (const vector<T>& x, const vector<T>& y)
    	{
    		return !(x <y);
    	}

实现起来非常简单了。

### 1.6 vector的元素的操作

vector的元素的操作非常多， 这里我就选取其中典型的几个来分析下：

    ![](http://www.cnblogs.com/Images/OutliningIndicators/ContractedBlock.gif)![](http://www.cnblogs.com/Images/OutliningIndicators/ExpandedBlockStart.gif) 		void push_back(const T& x)//在末尾增加一个
    		{
    			if (m_finish != m_end_of_storage)
    			{
    				construct(m_finish, x);
    				++m_finish;
    			}
    			//else
    				// _M_insert_aux(end(), __x);重新分配内存，这里就不继续了
    		}
    
    		void pop_back()//在末尾减去一个
    		{
    			--m_finish;
    			destroy(m_finish);
    		}
    
    		T* erase(T* position) //将 postition 位置上的元素移除,返回下一个元素
    		{
    			if (position + 1 != end())
    				copy(position + 1, m_finish, position);
    			--m_finish;
    			destroy(m_finish);
    			return position;
    		}
    		
    		T* erase(T* first, T* last)//移除区间[first）中的元素， 返回下一个元素
    		{
    			T* i = copy(last, m_finish, first);
    			destroy(i, m_finish);
    			m_finish = m_finish - (last - first);
    			return first;
    		}
    
    		void clear() { erase(begin(), end()); }
    
    //然后这里是在class vector 之外了， 定义了连个函数：
    	template<class _T1,class _T2> 
    	inline void construct(_T1 _FARQ *_Ptr, const _T2& _Val)
    	{	// construct object at _Ptr with value _Val
    		void _FARQ *_Vptr = _Ptr;
    		::new (_Vptr) _T1(_Val);
    	}
    
    	template<class _Ty>
    	inline void destroy(_Ty _FARQ *_Ptr)
    	{	// destroy object at _Ptr
    		_DESTRUCTOR(_Ty, _Ptr);
    	}

  
这几个函数的实现也比较简单， 以 push_back 为例子， 首先它会判断此容器的容量是不是满了， 如果慢了

则需要重新申请内存了， 如果还没有满 则增加一个值吗， 然后调整m_finish的位置。而pop_back， 则刚好

相反，减少一个值，然后改变m_finish的位置。

## 2\. 小结

vector的原理简单， 但是实现起来就复杂了。复杂度和功能是成正比的， STL的野心是很庞大的，导致了

内部实现的代码就复杂了。 不过话说回来，C 语言的哲学理念是：Kiss（keep it simple stupid）(武断，错误，

或者应该更正为simple,但是这里不改了)。 但是C++ 的 哲学理念是什么呢？Perfect？Complex？All-purpose？

posted @ 2010-07-11 17:18 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1775276) 收藏

##备注 
 @post in:2010-07-11 17:18