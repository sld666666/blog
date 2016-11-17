#  [一个二叉搜索树的C++实现](http://www.cnblogs.com/sld666666/archive/2010/10/23/1859512
.html)

最近在找工作，复习了下二叉树。 这里用C++重复制造了一个二叉树的轮子和大家分享下。

本文章是对代码的解释， 源码的贴出放在最后。

在阅读本片代码的时候注意一下几点：

# 用了泛型技术：templeate <class T>

# const 引用 （C++程序员装B必备）

在阅读代码的时候可能会不太习惯。

## 1\. 二叉搜索树的结构

一个二叉树的如果不为空便是由一个根节点和左右两个只树构成。

二叉搜索树可以提供对数时间的插入和访问，其节点的放置规则是：任何一个节点的键值一定大于其左树节点的键值，而且小于其右树节点的值。

所以我们可以定义如下的节点：

     1 template <class T>   
     2  struct BinaryNode   
     3 {   
     4     T                element;   
     5     BinaryNode        *left;   
     6     BinaryNode        *right;  
     7     BinaryNode(const T& theElement,   
     8         BinaryNode *lt,   
     9         BinaryNode *rt):   
    10     element(theElement),   
    11         left(lt),   
    12         right(rt)   
    13     {  
    14     }   
    15 };

element 表示节点的键值；

left 表示指向左树节点的指针

right表示指向右树节点的指针

## 2\. 二叉搜索树类的属性、方法

在这里， 这个树类提供了如下几个方法：
findMin();findMax();contains();makeEmpty();insert();remove();printTree();
我们可以先来看下这个类的定义：

     1 template <class T>   
     2  class BinarySearchTree   
     3 {   
     4  private:  
     5     BinaryNode<T>            *m_root;  
     6  public:   
     7     BinarySearchTree();   
     8     BinarySearchTree(const BinarySearchTree& rhs);   
     9     ~BinarySearchTree();  
    10     const T& findMin() const;   
    11     const T& findMax() const;   
    12     bool contains(const T& x) const;   
    13     void printTree(ORDER_MODE eOrderMode = ORDER_MODE_PREV) const;  
    14     void makeEmpty();   
    15     void insert(const T& x);   
    16     void remove(const T& x);  
    17  private:   
    18     //因为树的方法用到了很多递归， 所以这里我们需要申明如下的私有成员函数   
    19      void insert(const T& x, BinaryNode<T>* &t) ;   
    20     void remove(const T& x, BinaryNode<T>* &t) ;   
    21     BinaryNode<T>* findMin( BinaryNode<T>* t) const;   
    22     BinaryNode<T>* findMax( BinaryNode<T>* t) const;   
    23     bool contains(const T& x, const BinaryNode<T>* t) const;   
    24     void makeEmpty(BinaryNode<T>* t);   
    25     void printTreeInPrev(BinaryNode<T>* t) const;   
    26     void printTreeInMid(BinaryNode<T>* t)const;   
    27     void printTreeInPost(BinaryNode<T>* t)const;   
    28 };

这里解释下为什么要重载这么多私有的成员函数。因为树的方法用到了很多递归， 所以这里我们需要重载这些私有成员函数，当我们写代码的时候，我们便可以这样做：

     1 template <class T>   
     2  bool  BinarySearchTree<T>::contains(const T& x) const   
     3 {   
     4     return contains(x, m_root);   
     5 }  
     6 template <class T>   
     7  bool BinarySearchTree<T>::contains(const T& x, const BinaryNode<T>* t) const   
     8 {  
     9     这里调用自己  
    10 }

下面解释各个方法的具体实现。

## 3.BinarySearchTree的构造、析构以及拷贝构造函数

类BinarySearchTree的构造、析构以及拷贝构造函数没什么特殊的地方。实现如下：

     1 template <class T>   
     2 BinarySearchTree<T>::BinarySearchTree()   
     3 {   
     4     m_root = NULL;   
     5 }  
     6 template <class T>   
     7 BinarySearchTree<T>:: BinarySearchTree(const BinarySearchTree& rhs)   
     8 {   
     9     m_root = rhs.m_root;   
    10 }  
    11 template <class T>   
    12 BinarySearchTree<T>:: ~BinarySearchTree()   
    13 {   
    14     makeEmpty();   
    15 }

## 4\. BinarySearchTree::contains(X)

此方法判断函数树是不是包含元素X. 可以看下具体实现：

     1 // return true if the x is found in the tree   
     2  template <class T>   
     3  bool  BinarySearchTree<T>::contains(const T& x) const   
     4 {   
     5     return contains(x, m_root);   
     6 }  
     7 template <class T>   
     8  bool BinarySearchTree<T>::contains(const T& x, const BinaryNode<T>* t) const   
     9 {   
    10     if (!t)   
    11         return false;   
    12     else if (x < t->element)   
    13         return contains(x, t->left);   
    14     else if (x > t->element)   
    15         return contains(x, t->right);   
    16     else   
    17         return true;   
    18 }

## 5\. BinarySearchTree::makeEmpty()

清空这个树。

     1 template <class T>   
     2  void  BinarySearchTree<T>::makeEmpty()   
     3 {   
     4     makeEmpty(m_root);   
     5 }  
     6 template <class T>   
     7 void  BinarySearchTree<T>::makeEmpty(BinaryNode<T>* &t)   
     8 {   
     9     if (t)   
    10     {   
    11         makeEmpty(t->left);   
    12         makeEmpty(t->right);   
    13         delete t;   
    14     }   
    15     t = NULL;   
    16 }

实现非常简单，就是遍历整颗树，delete BinaryNode，
这里需要注意的一点是makeEmpty的参数是一个指针的引用：BinaryNode<T>* &t

## 6\. BinarySearchTree::findMax() & BinarySearchTree::findMin().

findMax 和findMin的实现就是遍历树，比较相关的元素:

     1 template <class T>   
     2 BinaryNode<T>* BinarySearchTree<T>::findMax( BinaryNode<T>* t) const   
     3 {   
     4     //二叉树的一个特点就是左子叶的值比根节点小， 右子叶的比根节点的大   
     5     if (t != NULL)   
     6         while (t->right != NULL)   
     7             t = t->right;   
     8     return t;   
     9 }  
    10 template <class T>   
    11 BinaryNode<T>* BinarySearchTree<T>::findMin( BinaryNode<T>* t) const   
    12 {   
    13     //二叉树的一个特点就是左子叶的值比根节点小， 右子叶的比根节点的大   
    14     if (!t)   
    15         return NULL;   
    16     if (t->left == NULL)   
    17         return t;   
    18     else   
    19         return findMin(t->left);   
    20 }

## 7\. BinarySearchTree的插入和删除

二叉树的插入比较简单， 只要循环得比较某一直，如果此值比根节点的值小，插入左子树，如果大插入右子树，如果相等，什么也不做

     1 template <class T>   
     2 void BinarySearchTree<T>::insert(const T& x, BinaryNode<T>* &t)   
     3 {   
     4     if (t == NULL)   
     5         t = new BinaryNode<T>(x, NULL, NULL);//注意这个指针参数是引用   
     6     else if (x < t->element)   
     7         insert(x, t->left);   
     8     else if (x > t->element)   
     9         insert(x, t->right);   
    10     else   
    11         ;//do nothing   
    12 }

二叉树的删除就比较复杂了，我们先得判断这个需要删除的节点有几个之节点，以及考虑到删除这个节点后其子节点引起的连锁反应。

     1 template <class T>   
     2 void BinarySearchTree<T>::remove(const T& x, BinaryNode<T>* &t)   
     3 {   
     4     if (t == NULL)   
     5         return;   
     6     if (x < t->element)   
     7         remove(x, t->left);   
     8     else if (x > t->element)   
     9         remove (x, t->right);   
    10     else // now ==   
    11     {   
    12         if (t->left != NULL &&   
    13             t->right != NULL)//two child   
    14         {   
    15             t->element = findMin(t->right)->element;   
    16             remove(t->element, t->right);   
    17         }   
    18         else   
    19         {   
    20             BinaryNode<T> *oldNode = t;   
    21             t = (t->left != NULL) ? t->left : t->right;   
    22             delete oldNode;   
    23         }   
    24     }   
    25 }

这里实现的方法就是先拿出右树的最小值填充当前节点，以后节点依次干这件事情，然后删除最后的节点。

## 8\. BinarySearchTree的遍历输出

二叉树的遍历有三种：前序，中序，后序。

前序遍历的规律是：输出根结点，输出左子树，输出右子树；  
中序遍历的规律是：输出左子树，输出根结点，输出右子树；  
后序遍历的规律是：输出左子树，输出右子树，输出根结点；

在这里我先定义了一个枚举来表示着三种类型的遍历方式：

    1 enum ORDER_MODE   
    2 {   
    3     ORDER_MODE_PREV = 0,   
    4     ORDER_MODE_MID,   
    5     ORDER_MODE_POST   
    6 };

然后分别实现遍历：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 //Print tree  
     2  template <class T>  
     3  void BinarySearchTree<T>::printTree(ORDER_MODE eOrderMode /*= ORDER_MODE_PREV*/) const  
     4 {  
     5     if (ORDER_MODE_PREV == eOrderMode)  
     6          printTreeInPrev(m_root);  
     7     else if (ORDER_MODE_MID == eOrderMode)  
     8          printTreeInMid(m_root);  
     9     else if (ORDER_MODE_POST == eOrderMode)  
    10          printTreeInPost(m_root);  
    11     else   
    12         ;//do nothing  
    13  }  
    14 template <class T>  
    15  void BinarySearchTree<T>::printTreeInPrev(BinaryNode<T>* t) const  
    16 {  
    17     if (t)  
    18     {  
    19         cout << t->element;  
    20         printTreeInPrev(t->left);  
    21         printTreeInPrev(t->right);  
    22     }  
    23 }  
    24 template <class T>  
    25  void BinarySearchTree<T>::printTreeInMid(BinaryNode<T>* t) const  
    26 {  
    27     if (t)  
    28     {  
    29         printTreeInPrev(t->left);  
    30         cout << t->element;  
    31         printTreeInPrev(t->right);  
    32     }  
    33 }  
    34 template <class T>  
    35  void BinarySearchTree<T>::printTreeInPost(BinaryNode<T>* t) const  
    36 {  
    37     if (t)  
    38     {  
    39         printTreeInPost(t->left);  
    40         printTreeInPost(t->right);  
    41         cout << t->element;  
    42     }  
    43 }

到此这个类的介绍，就完毕了， 下面可以敲下如下的测试代码：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 #include "stdafx.h"  
     2 #include "BinarySearchTree.h"  
     3   
     4   
     5  int _tmain(int argc, _TCHAR* argv[])  
     6 {  
     7     BinarySearchTree<int> binaryTree;  
     8     binaryTree.insert(5);  
     9     binaryTree.insert(1);  
    10     binaryTree.insert(2);  
    11     binaryTree.insert(3);  
    12     binaryTree.insert(6);  
    13     binaryTree.insert(8);  
    14     bool b = binaryTree.contains(1);  
    15     int x = binaryTree.findMin();  
    16     cout << b << " "<< x <<endl;  
    17     x = binaryTree.findMax();  
    18     cout << x <<endl;  
    19     binaryTree.remove(2);  
    20     binaryTree.printTree(ORDER_MODE_PREV);  
    21     cout <<endl;  
    22     binaryTree.printTree(ORDER_MODE_MID);  
    23     cout <<endl;  
    24     binaryTree.printTree(ORDER_MODE_POST);  
    25     cout <<endl;  
    26     return 0;  
    27 }  
    28  

结果为：

![RTX截图未命名](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/
C_135A3/RTX%E6%88%AA%E5%9B%BE%E6%9C%AA%E5%91%BD%E5%90%8D_thumb.png)

源码下载地址 http://files.cnblogs.com/sld666666/BinarySearchTree.zip

posted @ 2010-10-23 22:01 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1859512) 收藏

##备注 
 @post in:2010-10-23 22:01