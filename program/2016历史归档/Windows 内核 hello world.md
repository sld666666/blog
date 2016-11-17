#  [Windows 内核 hello
world](http://www.cnblogs.com/sld666666/archive/2011/01/09/1931419.html)

这边文章参考至《windows驱动开发技术详解》一书。其中代码90%是摘抄的。

我们以hello world 来开始windows内核的旅程吧。

要输出一个hello world, ddk 中需要如下几个步骤

1\. 打开一个创建一个设备对象 这个驱动对象是我们一系列操作的载体(**IoCreateDevice**)

2\. 实现分发函数（MajorFunction）， 在分发函数中， 做输出“hello world ”的功能。

3\. 删除创建的设备对象。

1\. 入口函数

当编写一个应用程序时候，windows 直接从main
函数开始执行生成一个进程。但是内核模块并不生成一个进程，只是填写了一组回调函数让windows调用，这个调用过程就用DriverEntry 实现，
因为习惯问题， 我把DriverEntry函数也叫成主函数，我们看下DriverEntry需要做什么：

     1 NTSTATUS DriverEntry(IN OUT PDRIVER_OBJECT   driverObject,   
     2                      IN PUNICODE_STRING      registryPath)   
     3 {   
     4 #if DBG   
     5     _asm int 3   
     6 #endif   
     7         NTSTATUS status;  
     8     DbgPrint("ENTER driverEntry\n");  
     9     driverObject->DriverUnload = helloDDKUnLoad;   
    10     driverObject->MajorFunction[IRP_MJ_CREATE] = helloDDKDispathcRoutine;   
    11     driverObject->MajorFunction[IRP_MJ_CLOSE] = helloDDKDispathcRoutine;   
    12     driverObject->MajorFunction[IRP_MJ_WRITE] = helloDDKDispathcRoutine;   
    13     driverObject->MajorFunction[IRP_MJ_READ] = helloDDKDispathcRoutine;  
    14     status = createDevice(driverObject);  
    15     DbgPrint("driverEntry end\n");  
    16     return STATUS_SUCCESS;   
    17 }

在主函数中， 我们做的功能很简单： 填充PDRIVER_OBJECT ，
我们用我们自己写的helloDDKUnLoad填充PDRIVER::DriverUnload , 用helloDDKDispathcRoutine
填充分发函数。

注意：

#if DBG  
_asm int 3  
#endif  
是设置一个软件断点， 当我们启动服务的时候， windbg 会停在这里以方便我们调试。

2.创建设备对象

createDevice()创建一个设备对象：

     1 NTSTATUS createDevice(IN PDRIVER_OBJECT driverObj)   
     2 {   
     3     NTSTATUS status;   
     4     PDEVICE_OBJECT devObj;   
     5     PDEVICE_EXTENSION devExt;  
     6     //create the device object   
     7     UNICODE_STRING devName;   
     8     RtlInitUnicodeString(&devName, L"\\Device\\MyDDKDevice");  
     9     status = IoCreateDevice(driverObj, sizeof(DEVICE_EXTENSION),   
    10         (PUNICODE_STRING)&devName, FILE_DEVICE_UNKNOWN, 0, true, &devObj);  
    11     if (!NT_SUCCESS(status))   
    12         return status;  
    13     devObj->Flags |= DO_BUFFERED_IO;   
    14     devExt = (PDEVICE_EXTENSION)devObj->DeviceExtension;   
    15     devExt->deviceObj = devObj;   
    16     devExt->deviceName = devName;  
    17     //create the sym link name   
    18     UNICODE_STRING symLinkName;   
    19     RtlInitUnicodeString(&symLinkName, L"\\??\\HelloDDK");   
    20     devExt->symLinkName = symLinkName;  
    21     status = IoCreateSymbolicLink(&symLinkName, &devName);   
    22     if (!NT_SUCCESS(status))   
    23     {   
    24         IoDeleteDevice(devObj);   
    25         return status;   
    26     }  
    27     return status;   
    28 }

设备对象的建立利用了IoCreateDevice()API.在创建设备对象的同时，需要建立一个符号链接。

这里解释下为什么需要符号链接： 驱动程序虽然有了设备的名称， 但是这个设备的名称是只能在内核态可见的，对应用程序是不可见的，因此需要暴露一个符号链接。

3\. 卸掉设备对象。

卸御设备对象就非常简单了：遍历驱动对象的所有设备对象，调用IoDeleteDevice()卸御之。

     1 VOID helloDDKUnLoad(IN PDRIVER_OBJECT driverObj)   
     2 {   
     3     PDEVICE_OBJECT nextObj;   
     4     DbgPrint("Enter DriverUnLoad\n");  
     5     nextObj = driverObj->DeviceObject;  
     6     while (NULL != nextObj)   
     7     {   
     8         PDEVICE_EXTENSION devExt = (PDEVICE_EXTENSION)   
     9             nextObj->DeviceExtension;  
    10         UNICODE_STRING lineName = devExt->symLinkName;   
    11         IoDeleteSymbolicLink(&lineName);   
    12         nextObj = nextObj->NextDevice;   
    13         IoDeleteDevice(devExt->deviceObj);   
    14     }   
    15 }  
    16 4. 实现分发函数  
    17 NTSTATUS helloDDKDispathcRoutine(IN PDEVICE_OBJECT devObj,   
    18                                  IN PIRP pIrp)   
    19 {   
    20     DbgPrint("Enter helloDDKDispathcRoutine");   
    21     NTSTATUS status = STATUS_SUCCESS;  
    22     // finish IRP   
    23     pIrp->IoStatus.Status = status;   
    24     pIrp->IoStatus.Information = 0;   
    25     IoCompleteRequest(pIrp, IO_NO_INCREMENT);   
    26     DbgPrint("hello world");  
    27     return status;   
    28 }

因为这我只是简单得在windbg下输出”hello world”， 所以不用操作设备对象链，只是简单地得：DbgPrint("hello world");

5\. windbg调试运行

设置windbg 的symbol path:

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201101/20110109204817
8067.png)

在虚拟机中装好驱动，我把此驱动的名字设为”first“

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201101/20110109204817
4969.png)

运行，windbg 捕到断点：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201101/20110109204818
5459.png)

下面调试或者运行就随便了。

posted @ 2011-01-09 20:48 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1931419) 收藏

##备注 
 @post in:2011-01-09 20:48