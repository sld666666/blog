
## HashMap 内部实现

HashMap 内部是用哈希表实现的，哈希表又称散列表。把key通过hash函数转换为一个整数型数组，然后把这个数字对数组长度取余作为下标。整个算法时间复杂度为 O(1)

哈希碰撞冲突 用拉链法。



```
    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                       boolean evict) {
            Node<K,V>[] tab; Node<K,V> p; int n, i;
            if ((tab = table) == null || (n = tab.length) == 0)
                n = (tab = resize()).length;
            if ((p = tab[i = (n - 1) & hash]) == null)
            //如果通过hash计算数组的第i给位置没有值就直接放入
                tab[i] = newNode(hash, key, value, null);
            else {
            //如果发现有就在数组后面加个链表， 哈希碰撞冲突， 拉链法
                Node<K,V> e; K k;
                if (p.hash == hash &&
                    ((k = p.key) == key || (key != null && key.equals(k))))
                    e = p;
                else if (p instanceof TreeNode)
                    e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
                else {
                    for (int binCount = 0; ; ++binCount) {
                        if ((e = p.next) == null) {
                            p.next = newNode(hash, key, value, null);
                            if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                                treeifyBin(tab, hash);
                            break;
                            .....
       }   
                         
                         
    final Node<K,V> getNode(int hash, Object key) {
            Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
            if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
                //如果能取到第一个直接用第一个
                if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k))))
                    return first;
                 //如果有hash碰撞冲突了就要遍历他后面的链表了
                if ((e = first.next) != null) {
                    if (first instanceof TreeNode)
                        return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                    do {
                        if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k))))
                            return e;
                    } while ((e = e.next) != null);
                }
            }
            return null;
        }
```

## HashMap，Hashtable 区别

1. Hashtable是线程同步的，而 HashMap不是
2. HashMap能接受null key和value
3. HashMap是 fail-fast迭代器 迭代器， 当有其他线程改变结构时候，会抛出ConcurrentModificationException异常。

异步情况下都用HashMap，当在多线程时候选择 ConcurrentHashMap

Hashtable是对整个 put方法加 synchronize， 太简单粗暴了

     public synchronized V put(K key, V value) 

## ConcurrentHashMap

在HashMap的基础上， putval的时候增加了synchronize关键字。

    synchronized (f) {
                        if (tabAt(tab, i) == f) {
                            if (fh >= 0) {
                                binCount = 1;
                                for (Node<K,V> e = f;; ++binCount) {
                                    K ek;
                                    if (e.hash == hash &&
                                        ((ek = e.key) == key ||
                                         (ek != null && key.equals(ek)))) {
                                        oldVal = e.val;
                                        if (!onlyIfAbsent)
                                            e.val = value;
                                        break;
                                    }
                                    
## JVM内存模型

jvm 把内存分为堆和线程栈。

1. 线程栈： 只有线程可见， 维护当前执行信息。 原始类型本地变量和引用类型本地变量
2. 堆： 对象的内存分配， 静态变量

## java中的集合

1.  List: ArrayList - 数组， LinkedList--链表
2. Set:

       2.1 HashSet :  LinkedHashSet
       2.2 TreeSet: 内部使用了TreeMap
       2.3 EnumSet：抽象类
3. Queue：
       3.1 PriorityQueue
       3.2 ArrayDeque
4. Map：
       4.1 HashMaphashCode

       4.2 LinkedHashMap
       4.3 Hashtable
       4.4 TreeMap： 红黑树
       4.5 EnumMap

## 乐观锁和悲观锁
1. 悲观锁： 只有一个现实失败或者成功以后下一个才能进行.悲观并发控制实际上是“先取锁再访问”的保守策略，为数据处理的安全提供了保证。但是在效率方面，处理加锁的机制会让数据库产生额外的开销，还有增加产生死锁的机会；另外，在只读型事务处理中由于不会产生冲突，也没必要使用锁，这样做只能增加系统负载；还有会降低了并行性，一个事务如果锁定了某行数据，其他事务就必须等待该事务处理完才可以处理那行数 -- 如synchronized
2. 乐观锁:它假设多用户并发的事务在处理时不会彼此互相影响，各事务能够在不产生锁的情况下处理各自影响的那部分数据。在提交数据更新之前，每个事务会先检查在该事务读取数据后，有没有其他事务又修改了该数据。如果其他事务有更新的话，正在提交的事务会进行回滚。  -- 如 AtomicInteger

## java object类有哪些方法
- getClass
- hashCode
- equals
- toString
- wait

## java关键字
- native :是用native代码实现的
- defualt
- static 
- final


