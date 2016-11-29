#  [WDK
常用的几个函数](http://www.cnblogs.com/sld666666/archive/2011/01/06/1929230.html)

首先是几个内存分配的函数

Ex_系列函数

功能简述

ExAllocatePool

内存分配

ExFreePool

释放内存

ExAcquireFastMutex

获取一个互斥体

ExRekeaseFastMutex

释放一个互斥体

ExRasiseStatus

抛出一个异常

下面是一组文件操作的函数

Zw-系列函数

功能简述

ZwCreateFile

打开文件(设备)

ZwWriteFile

写文件

ZwReadFile

读

ZwQueryDirectoryFile

目录查询

ZwDeviceloControFile

发出设备控制请求

ZwCreateKey

打开一个注册表键

ZwQueryValueKey

提取一个注册表的值

在进行字符串操作时，要用到大量的Rtl-函数。

Rtl-系列函数

功能简述

RtlInitUnicodeString

初始化一个字符串

RtlCopyUnicodeString

拷贝字符串

RtlAppendUnicodeToString

append

RtlStringCbPrintf

sprintf

RtlCopyMemory

内存数据拷贝

RtlMoveMemory

内存数据移动

RtlZeroNeniry

内存数据清零

RtlCompareMemory

比较内存

RtlGetVersion

获得当前windows的版本

Io-系列函数非常重要，涉及到了io管理

Io-系列函数

功能简述

IoCreateFile

打开文件，比ZwCreate更底层

IoCreateDevice

创建一个设备对象

IoCallDriver

发送请求

IoCompleteRequest

完成请求(通知IO管理器这个请求已经完成)

IoCopyCurrentIrpStackLocationToNext

将当前IRP的栈空间拷贝到下一个栈空间

IoSkipCurrentIrpStackLocationToNext

跳过当前的IRP栈空间

IoGetCurrentIrpStackLocation

获得IRP当前栈空间的指针

Ps-系列函数大多数和进程线程相关

Ndis-系列是网络驱动相关的

非WDK 中的函数只要不涉及内存管理，都可以在内核程序中调用，但是MS不提出这么做。

posted @ 2011-01-06 22:39 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1929230) 收藏

##备注 
 @post in:2011-01-06 22:39