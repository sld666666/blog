#  [Windows
内核编程初涉](http://www.cnblogs.com/sld666666/archive/2011/01/06/1929233.html)

windows 内核编程有自己的特点，下面是初涉其中的总结。文章的内容摘自《寒江独钓---windows内核安全编程》

### 1\. 基本数据类型

为了消除平台和编译器的差异，WDK定义了一套自己的数据类型。ULONG, UCHAR,UNIT VOID……

### 2\. 函数返回值

绝大部分内核API 都有一个返回值， 我在自己写的时候也需要：

     1 NTSTATUS myFunc()   
     2 {   
     3     NTSTATUS status;  
     4     status = ZwCreateFile();   
     5     if (!NT_SUCCESS(status))   
     6     {   
     7         return status;   
     8     }  
     9     return status;   
    10 }

### 3\. 字符串

WDK 中用UNICODE_STRING 表示字符串

    1 typedef struct _UNICODE_STRING {   
    2   USHORT  Length;   
    3   USHORT  MaximumLength;   
    4   PWSTR  Buffer;   
    5 } UNICODE_STRING, *PUNICODE_STRING;

如：

    1 UNICODE_STRING str = RTL_CONSTANT_STRING(L"first: hello, my salary");   
    2 DbgPrint("%wZ", &str);

其中 RTL_CONSTANT_STRING 宏是用来创建一个字符串的。

%wZ 是用来格式化输出字符串的， 和int 用%d, char用%c 同理。

### 4\. 重要的数据结构

windows内核采用的是面向对象的编程方式，但是使用的确实C语言。所以它的对象就是“伪对象”了。当然这个对象是指诸如一个驱动，一个设备，一个文件之类的东西
了。每一个对象都用一个结构体表示。

#### 4.1 驱动对象

当编写一个应用程序时候，windows 直接从main 函数开始执行生成一个进程。但是内核模块并不生成一个进程，只是填写了一组回调函数让windows调用。
而这个调用过程就需要驱动对象帮忙了。我看下驱动对象的定义：

     1 typedef struct _DRIVER_OBJECT {  
     2 // 结构体的类型和大小   
     3     CSHORT Type;   
     4     CSHORT Size;  
     5 //设备对象， 这里实际上是一个设备对象链表的开头，   
     6    PDEVICE_OBJECT DeviceObject;  
     7   
     8     ULONG Flags;  
     9 // 驱动的名字  
    10     UNICODE_STRING DriverName;  
    11     PUNICODE_STRING HardwareDatabase;  
    12 //快速分发函数  
    13     PFAST_IO_DISPATCH FastIoDispatch;  
    14     PDRIVER_INITIALIZE DriverInit;   
    15     PDRIVER_STARTIO DriverStartIo;  
    16 //卸载函数   
    17     PDRIVER_UNLOAD DriverUnload;  
    18 //普通分发函数   
    19     PDRIVER_DISPATCH MajorFunction[IRP_MJ_MAXIMUM_FUNCTION + 1];  
    20 } DRIVER_OBJECT;   
    21 typedef struct _DRIVER_OBJECT *PDRIVER_OBJECT;

#### 4.2 设备对象

windows 内核中，大部分消息都以请求(IRP)的方式传递，而设备对象(DEVICE_OBJECT)是唯一可以接收请求的实体。一个设备对象可以是一个硬盘
或者一个“管道”。

     1 typedef struct DECLSPEC_ALIGN(MEMORY_ALLOCATION_ALIGNMENT) _DEVICE_OBJECT {   
     2     CSHORT Type;//类型   
     3     USHORT Size;//大小   
     4     LONG ReferenceCount;//引用计数   
     5     struct _DRIVER_OBJECT *DriverObject;//这个设备所属的驱动对象   
     6     struct _DEVICE_OBJECT *NextDevice;//下一个设备对象，一个设备对象中有N个设备   
     7     struct _DEVICE_OBJECT *AttachedDevice;   
     8     struct _IRP *CurrentIrp;   
     9     PIO_TIMER Timer;   
    10     ULONG Flags;                                // See above:  DO_...   
    11     ULONG Characteristics;                      // See ntioapi:  FILE_...   
    12     __volatile PVPB Vpb;   
    13     PVOID DeviceExtension;   
    14     DEVICE_TYPE DeviceType;//设备类型   
    15     CCHAR StackSize;//IRP栈的大小   
    16     union {   
    17         LIST_ENTRY ListEntry;   
    18         WAIT_CONTEXT_BLOCK Wcb;   
    19     } Queue;   
    20     ULONG AlignmentRequirement;   
    21     KDEVICE_QUEUE DeviceQueue;   
    22     KDPC Dpc;  
    23     //   
    24     //  The following field is for exclusive use by the filesystem to keep   
    25     //  track of the number of Fsp threads currently using the device   
    26     //  
    27     ULONG ActiveThreadCount;   
    28     PSECURITY_DESCRIPTOR SecurityDescriptor;   
    29     KEVENT DeviceLock;  
    30     USHORT SectorSize;   
    31     USHORT Spare1;  
    32     struct _DEVOBJ_EXTENSION  *DeviceObjectExtension;   
    33     PVOID  Reserved;  
    34 } DEVICE_OBJECT;

从上可以看出，一个驱动对象可以包含多个设备对象。

ULONG Flags的含义:

  1. DO_BUFFEREND_IO : 读写操作使用缓冲方式。
  2. DO_EXCLUSIVE: 一次只允许一个线程打开设备句柄
  3. DO_DIRECT_IO: 读写操作直接使用
  4. DO_POWER_PAGABLE: 必须在PASSIVE_LEVEL 级上处理IRP请求
  5. DO_POWER_INRUSH: 设备上电期间需要最大电流

posted @ 2011-01-06 22:42 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1929233) 收藏

##备注 
 @post in:2011-01-06 22:42