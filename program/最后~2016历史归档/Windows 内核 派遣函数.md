#  [Windows 内核
派遣函数](http://www.cnblogs.com/sld666666/archive/2011/01/15/1936463.html)

这只是一篇读书笔记。

派遣函数（Dispathc Funtion）是windows驱动中的重要概念。驱动程序的主要功能是负责处理I/O请求，其中大部分I/O请求是在派遣函数中处理
的。用户模式下所有对驱动程序的I/O请求。全部由操作系统转化为一个叫做IRP的数据结构，不同的IRP数据会派遣到不同的派遣函数中。

IRP（I/O Request Package） 和windows应用程序的消息处理机制相似。 IRP中一个MajorFunction, 其保存派遣的函数。

如：

    1 pDriverObject->DriverUnload = HelloDDKUnload;   
    2 pDriverObject->MajorFunction[IRP_MJ_CREATE] = HelloDDKDispatchRoutine;   
    3 pDriverObject->MajorFunction[IRP_MJ_CLOSE] = HelloDDKDispatchRoutine;   
    4 pDriverObject->MajorFunction[IRP_MJ_WRITE] = HelloDDKDispatchRoutine;   
    5 pDriverObject->MajorFunction[IRP_MJ_READ] = HelloDDKDispatchRoutine;

处理这些IRP最简单的方法是在相应的派遣函数中将IRP的状态设置为成功， 结束IRP请求（IoCompleteRequest）.

     1 NTSTATUS HelloDDKDispatchRoutin(IN PDEVICE_OBJECT pDevObj,   
     2                                  IN PIRP pIrp)   
     3 {   
     4     KdPrint(("Enter HelloDDKDispatchRoutin\n"));  
     5     //对一般IRP的简单操作，后面会介绍对IRP更复杂的操作   
     6      NTSTATUS status = STATUS_SUCCESS;   
     7     // 完成IRP   
     8      pIrp->IoStatus.Status = status;   
     9     pIrp->IoStatus.Information = 0;    // bytes xfered   
    10      IoCompleteRequest( pIrp, IO_NO_INCREMENT );  
    11     KdPrint(("Leave HelloDDKDispatchRoutin\n"));  
    12     return status;   
    13 }

如上中，如果将IRP 的完成状态设为STATUS_SUCCESS, 上层的调用函数（WriteFile）就会返回true.

我们这里可以下上层系统调用内核的流程。

![函数调用流程](http://images.cnblogs.com/cnblogs_com/sld666666/201101/2011011521424
28009.jpg)

(图一)

![------](http://images.cnblogs.com/cnblogs_com/sld666666/201101/2011011521424
32569.jpg)

(图二)

跟踪IRP 利器： IRPTrace

posted @ 2011-01-15 21:43 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1936463) 收藏

##备注 
 @post in:2011-01-15 21:43