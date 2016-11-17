#  [STL
中的list](http://www.cnblogs.com/sld666666/archive/2010/09/28/1837864.html)

STL 中的list是对链表的封装。本次剖析的对象是SGI STL。

注：和原版的SGI STL 代码有出入，主要体现在：内存分配上， 变量的命名方式上,还有N多代码的省略上，所以本文章贴出的代码只适合参考一下。

## 1\. 链表的特点

\- O(1)级的插入和删除。

\- 其绝对位置未知，所以不能被索引化，不支持随机存储。

\- 查找其中某一元素的时候效率低下。

## 2\. list支持的操作

在STL中，list是对链表封装， 这里我列出list需要支持的功能，下面的源代码就按下列出的方式进行分析。

\- 提供了5个不用方式的构造函数

\- 在内部维护一个双向链表， 提供了 size, empty,这样的能力，这里与vector

不同的是没有提供capalcity 的能力，因为这样做全无必要。

\- 可认为list是一个全新的数据类型（同int等级别， 废话了， 哪一个class都可以），

所以必须重载= 运算符。

\- 支持迭代，（废话，每一种容器都是，这里写出来主要为了下面按这个来分析）

\- 提供push_back(), pop_back(), insert(), remove()，erase()等元素操作的能力。

## 3\. list的结构

在SGI STL中， list的内部结构是一个双向链表。其内存结构和一般的链表没什么两样。但是代码的组织形式却又很大的不同， 首先可以看下其对于节点的定义：

struct List_node_base{  
List_node_base* next;  
List_node_base* prev;  
};

template <class T>  
struct List_node : public List_node_base {  
T data;  
};

然后再声明了以基本 base_list

     1 // Base class   
     2      template <class T>   
     3     class List_base   
     4     {   
     5     protected:   
     6         List_node<T>*    m_node;  
     7     public:   
     8         List_base(){   
     9             m_node = get_node();   
    10             m_node->next = m_node;   
    11             m_node->prev = m_node;   
    12         }  
    13         ~List_base(){   
    14             clear();   
    15             put_node(m_node);   
    16         }  
    17         void clear()   
    18         {   
    19             //销毁内存，这里省略   
    20          }   
    21     protected: 
    　　　　　// 我们以作不普通的new 和 delete来管理内存  
    22         List_node<T>* get_node() {   
    23             List_node<T>* tmp = (List_node<T>*)(::operator new(1*sizeof(List_node<T>)));   
    24             return tmp;   
    25         }  
    26         void put_node(List_node<T>* p){   
    27             ::operator delete(p);   
    28         }   
    29     };

在类List_base中保存着对list的最基本的操作。成员变量的声明， 初始化…。 这里我们可以体验出SGI STL中把功能分割得足够细，
继承用得足够精巧。

哨兵：

ierator class 保持着当前节点的指针，链表的末尾是一个有效地地址，需要一个额外的节点来标示末尾。
这里我们可以在链表的开头创建一个额外的节点。sentinel node,header node, tail
node.这样做是为了在特简化特殊位置的节点的代码过程，比如说如果没有 sentinel node, 我们就必须为header node 进行特殊的处理。

SLT的链表结构就是拥有哨兵节点的。

## 4\. list中的的迭代器

list要提供++,--这样的功能， 而其支持这样的功能实现的时候又不能和vector一样，用普通的指针来寻址，所以迭代器的引入时必须的。我们如何实现lis
t的迭代如同普通指针的迭代一样呢？这就需要运算符重载出马了。迭代器的原理就是重载的++，—，*等这类运算符。下面我们看一下它是如何实现的。

首先声明一个List_iterator_base基类，在这个类中保存着对List_node_base最基本的操作。

     1 struct List_iterator_base   
     2     {  
     3        // 操作的是 List_node_base，这里可以想象一下为什么节点的声明都要用继承了   
     4          List_node_base*    m_node;  
     5         //构造函数  
     6          List_iterator_base(List_node_base* x): m_node(x){}   
     7         List_iterator_base(){}  
     8  //基本的操作  
     9  void incr() { m_node = m_node->next;}   
    10  void decr() { m_node = m_node->prev;}  
    11         bool operator==(const List_iterator_base& x) const   
    12         {   
    13             return m_node == x.m_node;   
    14         }  
    15         bool operator!=(const List_iterator_base& x) const   
    16         {   
    17             return m_node != x.m_node;   
    18         }   
    19     };

然后，派生一个类List_iterator，这个类是iterator实际的操作者，

     1 template<class Tp, class Ref, class Ptr>   
     2  struct List_iterator : public List_iterator_base   
     3 {  
     4     //我们可以看下iterator和const_iterator的定义。   
     5      typedef List_iterator<Tp, Tp&, Tp*>                iterator;   
     6     typedef List_iterator<Tp, const Tp&, const Tp*>    const_iterator;   
     7     typedef List_iterator<Tp, Ref, Ptr>                m_Self;  
     8     typedef    Tp value_type;   
     9     typedef Ptr pointer;   
    10     typedef Ref reference;   
    11     typedef List_node<Tp> Node;   
    12     //构造函数   
    13      List_iterator(Node* x) : List_iterator_base(x){}   
    14     List_iterator(){}   
    15     List_iterator(const iterator& x) : List_iterator_base(x.m_node){};  
    16     //最关键的运算符重载来了， 我们前面的主要工作就是为下面的服务的   
    17     reference operator*() const {return ((Node*)m_node)->data;}   
    18     m_Self& operator++()   
    19     {   
    20         this->incr();   
    21         return this;   
    22     }  
    23     m_Self operator++(int)   
    24     {   
    25         m_Self tmp = *this;   
    26         this->incr();   
    27         return tmp;   
    28     }  
    29     m_Self&    operator--()   
    30     {   
    31         this->decr();   
    32         return *this;   
    33     }  
    34     m_Self operator--(int)   
    35     {   
    36         m_Self tmp == *this;   
    37         this->decr();   
    38         return tmp;   
    39     }  
    40 };

如上，如果我们调用了++操作符，实际调用的是incr() 而最终调用的是m_node =
m_node->next;是不是有点简单的操作复杂化的味道了，呵呵，复杂是为了功能的强大。

## 5\. list中的构造函数

     1  在介绍list的构造函数之前，我们先看下器一大堆的typedef  
     2 class list: protected List_base<Tp>   
     3     {   
     4     //首先是一堆typedef   
     5     public:   
     6         typedef List_base<Tp/*, _Alloc*/> Base;//我这里省略了内存池，所以就不用手动得内存非配了  
     7         typedef Tp                        value_type;   
     8         typedef value_type*                pointer;   
     9         typedef const value_type*        const_pointer;   
    10         typedef value_type&                reference;   
    11         typedef const value_type&        const_reference;   
    12         typedef List_node<Tp>            Node;   
    13         typedef size_t                    size_type;   
    14         typedef ptrdiff_t                difference_type;  
    15     public:   
    16         typedef List_iterator<Tp,Tp&,Tp*>             iterator;   
    17         typedef List_iterator<Tp,const Tp&,const Tp*> const_iterator;  
    18     protected:  
    19         using Base::m_node; //这里使用List_node_base中的节点  
    20 }

然后我们再来看其实如何构造的

     1     //构造函数   
     2     public:   
     3         explicit list() : Base() {}//调用List_base的构造函数   
     4         // Ex: list<int> list0;  
     5         list(size_type n, const Tp& value): Base()   
     6         {   
     7             insert(begin(), n, value);   
     8         }   
     9         //Ex: list<int> list1(10, 1);  
    10         explicit list(size_type n): Base()   
    11         {   
    12             insert(begin(), n, Tp());   
    13         }   
    14         //Ex: list<int> list2(10);  
    15         list(const Tp* first, const Tp* last): Base()   
    16         {   
    17             this->insert(begin(), first, last);   
    18         }   
    19         //Ex: int a[10] = {0,1,2,3,4,5,6,7,8,9};   
    20         // list<int> list3(a, a+9);   
    21         list(const_iterator first, const_iterator last): Base()   
    22         {   
    23             this->insert(begin(), first, last);   
    24         }   
    25         //list<int> list4(list3.begin(), list4.end());

在上面的每一个构造函数下面，我都写了list构造相应的列子。恩，还是很简单的。

## 6\. list的元素操作能力

看完了list的构造函数，我们再来看下list是怎么操作元素的，在这里我只列出了典型的几个push_back(), pop_back(),
insert(), remove()，erase()。

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1   //基本操作   
     2     public:   
     3     iterator begin()             { return (Node*)(m_node->next); }   
     4     const_iterator begin() const { return (Node*)(m_node->next); }  
     5     iterator end()             { return m_node; }   
     6     const_iterator end() const { return m_node; }   
     7     void insert(iterator pos,const Tp* first, const Tp* last)   
     8     {   
     9         Node* tmp =  new Node;   
    10         while(first != last)   
    11         {   
    12             tmp->data = *first;   
    13             pos.m_node ->next= tmp;   
    14             tmp->prev = pos.m_node;   
    15             pos++;   
    16             first++;   
    17         }  
    18         delete tmp;  
    19     }   
    20     iterator insert(iterator pos, const Tp& x)   
    21     {   
    22         Node*    node = new List_node<Tp>;   
    23         node->next = pos.m_node;   
    24         node->prev = pos.m_node->prev;   
    25         pos.m_node->prev->next = node;   
    26         pos.m_node->prev = node;  
    27         return node;  
    28     }  
    29     void insert(iterator pos, size_type n, const Tp& x)   
    30     {   
    31         for (; n >0; --n)   
    32             insert(pos, x);   
    33     }  
    34     void push_back(const Tp& x)   
    35     {   
    36         insert(end(), x);   
    37     }  
    38     iterator erase(iterator pos)   
    39     {   
    40         List_node_base* next_node = pos.m_node->next;   
    41         List_node_base* prev_node = pos.m_node->prev;  
    42         Node*  node = (Node*)pos.m_node;   
    43         //...删除这块内存，省略之  
    44         return iterator((Node*)next_node);   
    45     }  
    46     void pop_back()   
    47     {   
    48         iterator tmp = end();   
    49         erase(--tmp);   
    50     }  
    51     void remove(const Tp& value)   
    52     {   
    53         iterator first = begin();   
    54         iterator last = end();  
    55         while (first != last)   
    56         {   
    57             iterator next = first;   
    58             //原版的SGI竟然把临时变量声明在循环中， 看来大牛也会有失误的   
    59             next++;   
    60             if (*first == value)   
    61                 erase(first);   
    62             first = next;   
    63         }   
    64         //看来remove一个特定值的节点还是很花时间的，因为有先查找的功能   
    65     }

这里再次指出的是list是有哨兵节点的双向链表，所以我们可以用m_node表示最后的节点，m_node->next表示头节点。

## 7\. 提供size, empty这样的能力

最后，提供szie,empty 对于容量测查询就非常简单了。

     1 bool empty() const   
     2      {   
     3          return m_node->next == m_node;   
     4      }  
     5 size_type size() const   
     6      {   
     7          size_type result = 0;   
     8          iterator tmp = (Node*)(m_node->next);   
     9          for (; tmp != end(); tmp++)   
    10              ++result;  
    11          return result;   
    12      }

好了， STL 中list的结构基本上是这样的，如果用C来表示一个普通的链表的话，基本功能的操作应该在100行代码之内，而且逻辑也没有这么复杂，但是还是那句
话：代码的复杂度和功能的强大时成正比的， list是作为一个基础的库来使用，而且对结构的封装又必须能被泛型算法所适用。

STL的源码，和一般工程的代码在命名方式，代码构造上有比较大的出入，所以在阅读的时候很不习惯。

posted @ 2010-09-28 21:01 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1837864) 收藏

##备注 
 @post in:2010-09-28 21:01