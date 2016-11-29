#  [Windows
内地管理](http://www.cnblogs.com/sld666666/archive/2011/01/15/1936419.html)

本文只是一篇读书笔记。

## 1\. 内存类型

分页内存： 虚拟内存可以交换到文件中的。

非分页内存：虚拟内存不可以交换到文件中。

当程序中断请求级别在DISPATCH_LEVEL之上是，只能使用非分页内存。

如：把函数放入分页或者非分页内存：

#define PAGEDCODE code_seg(“PAGE”)

#LOCKEDCODE code_seg()

将一个函数载入到分页内存中：

    1 #pragma PAGEDCODE  
    2 VOID someFunction()  
    3 {  
    4     PAGED_CODE();  
    5     // do something  
    6  }

PAGED_CODE(); 是DDK 提供的宏。

## 2\. 分配内核内存

windows 内核的栈空间不像应用程序这么大， 所以驱动程序不适合递归调用或者局部变量时大型的结构体。如果需要，
则需要在堆中申请。堆中申请的函数有以下几个：

**ExAllocatePool**

ExAllocatePoolWithTag

ExAllocatePoolWithQuota

ExAllocatePoolWithQuotaTag

这四个函数参数大同小异：

**IN POOL_TYPE** _[PoolType](ms-help://ms.wdk.v10.7600.091201/)_**,**   
**IN SIZE_T** _[NumberOfBytes](ms-help://ms.wdk.v10.7600.091201/)_**,**   
**IN ULONG** _[Tag](ms-help://ms.wdk.v10.7600.091201/)_

PoolType 是一个枚举变量，如果此值为NonPagedPool, 则分配非分页内存，如果为PagedPool, 则分配分页内存。

NumberOfBytes 是分配的内存大小， 注意最好是4的整数倍。

返回值： 如果成功则返回分配的内存，这个一定是内核模式地址， 如果失败，返回 0.

## 3\. 链表结构

DDK 提供了一个双向链表：

typedef struct _LIST_ENTRY {  
struct _LIST_ENTRY *[Flink](ms-help://ms.wdk.v10.7600.091201/);  
struct _LIST_ENTRY *[Blink](ms-help://ms.wdk.v10.7600.091201/);  
} LIST_ENTRY, *PLIST_ENTRY;

内存结构图：

![LIST_ENTRY](http://images.cnblogs.com/cnblogs_com/sld666666/201101/201101152
017491744.jpg)

使用：

    1 typedef struct _MYDATASTRUCT   
    2 {   
    3     ULONG number;   
    4     LIST_ENTRY ListEntry;   
    5 } MYDATASTRUCT, *PMYDATASTRUCT;

LIST_ENTRY 必须作为结构体的一部分。

初始化： InitializeListHead

是否为空： IsListEmpty

插入： 链表的插入分为两种， 从头部， 从尾部。

头部： InserHeadList(&head, &mydata->listEnty);

尾部： InsertTailList(&head,&mydata->listEnty)

删除： PLIST_ENTRY pEntry = RemoveHeadList(&head);

例子：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    extern "C" NTSTATUS DriverEntry (   
                IN PDRIVER_OBJECT pDriverObject,   
                IN PUNICODE_STRING pRegistryPath    )   
    {  
    #if DBG   
        _asm int 3   
    #endif  
        NTSTATUS status;   
        LIST_ENTRY linkListHead;  
        //初始化链表   
        InitializeListHead(&linkListHead);  
        PMYDATASTRUCT pData;   
        ULONG i = 0;   
        //在链表中插入10个元素   
        KdPrint(("Begin insert to link list"));   
        for (i=0 ; i<10 ; i++)   
        {   
            pData = (PMYDATASTRUCT)   
                ExAllocatePool(PagedPool,sizeof(MYDATASTRUCT));   
            pData->number = i;   
            InsertHeadList(&linkListHead,&pData->ListEntry);   
        }  
        //从链表中取出，并显示   
        KdPrint(("Begin remove from link list\n"));   
        while(!IsListEmpty(&linkListHead))   
        {   
            PLIST_ENTRY pEntry = RemoveTailList(&linkListHead);   
            pData = CONTAINING_RECORD(pEntry,   
                                  MYDATASTRUCT,   
                                  ListEntry);   
            KdPrint(("%d\n",pData->number));   
            ExFreePool(pData);   
        }  
        KdPrint(("DriverEntry end\n"));   
        return status;   
    }

如上， 用 ExAllocatePool 分配内存，用ExFreePool 释放内存， CONTAINING_RECORD宏得到一个结构体的首地址，
而LIST_ENTRY 实际上是首先声明 这样的链表头， 然后用MYDATASTRUCT 往上塞或者摘。

## 4\. Looksaide

频繁地申请或者释放内存，会产生内存碎片， 一般用来解决这样问题是内存池技术， ddk 中用Lookaside。

使用：

1）. 用 ExInitializeNPagedLookasideList()

或

ExInitializePagedLookasideList（)

来初始化Lookaside对象， 这两个函数分别对应与非分页和分页内存。

2）. 用ExAllocateFromNPagedLookasideList()

或

ExAllocateFromPagedLookasideList()

来从内存池申请内存。

3).用ExFreeToNPageLookasideList()

或

ExFreeToPageLookasideList()

往内存池释放内存。

4）. 用ExDeleteNPageLookasideList()

或

ExDeletePageLookasideList()

删除内存池

例子：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 VOID lookasideTest()   
     2 {   
     3     PAGED_LOOKASIDE_LIST pageList;   
     4     ExInitializePagedLookasideList(&pageList, NULL,NULL,   
     5         0, sizeof(MYDATASTRUCT),'1234', 0);  
     6  #define  ARRAY_NUMBER 50  
     7     PMYDATASTRUCT myArray[ARRAY_NUMBER];   
     8     int i;   
     9     for ( i =0; i < ARRAY_NUMBER; ++i)   
    10     {   
    11         myArray[i] = (PMYDATASTRUCT)ExAllocateFromPagedLookasideList(   
    12             &pageList);  
    13     }  
    14     for (i = 0; i < ARRAY_NUMBER; ++i)   
    15     {   
    16         ExFreeToPagedLookasideList(&pageList, myArray[i]);   
    17         myArray[i] = NULL;   
    18     }  
    19     ExDeletePagedLookasideList(&pageList);   
    20 }

## 5\. 内存操作函数

内存复制(不可重叠)： RtlCopyMemory()

内存间复制(可重叠)：RtlCopyMemory()

内存填充： RtlFillMemory()

内存比较： RtlCompareMemory, RtlEqualMemory.

检查内存可用性： ProbeForRead(), ProbeForWrite()

posted @ 2011-01-15 20:18 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1936419) 收藏

##备注 
 @post in:2011-01-15 20:18