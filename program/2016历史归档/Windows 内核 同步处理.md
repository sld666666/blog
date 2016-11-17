#  [Windows 内核
同步处理](http://www.cnblogs.com/sld666666/archive/2011/01/16/1936700.html)

这只是一篇读书笔记。

1\. 中断请求级

中断请求(IRQ)可以分为软件中断和硬件中断，这些中断都映射成不同级别的中断请求级(IRQL).同步处理机制很大程度上依赖于中断请求级。

传统PC中，一般可以产出16个中断信号，每一个中断信号对应于一个中断号。硬件中断可以分为不可屏蔽和可屏蔽，分别由CPU得两根引脚NMI和INTR来接收。

X86机器基本上涌高级可编程控制器(APIC)来代替NMI和INTR。APIC把IRQ的数量增加到了24个。 我们可以看下图：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201101/20110116152424
7007.png)

图一

windows 将中断的概念进行了扩展，提出中断请求级(IRQL)的概念。规定一共有32个中断请求级，其中0~2为软件中断，3~31为硬件中断（包括了API
C的24个中断）。Windows大部分时间运行在软件中断级别中，只有当设备中断来临时，操作系统将IRQL提升至硬件中断级别。

2\. 自旋锁

自旋锁一般是为了各个派遣函数之间的同步。用KSPIN_LOCK 表示。

使用：

  1. 用 KeInitializeSpinLock初始化，一般在DriverEntry或AddDevice中。

  2. 用KeAcquireSpinLock 申请自旋锁。

  3. 用KeReleaseSpinLock释放自旋锁。

  4. KeWaiteForSingleObject 和KeWaitForMultipleObjects 用于内核模式下的对象同步。

  5. PsCreateSystenThread 创建新线程。

posted @ 2011-01-16 15:24 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1936700) 收藏

##备注 
 @post in:2011-01-16 15:24