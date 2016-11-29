#  [DirectX API 记录----
几个重要的结构体](http://www.cnblogs.com/sld666666/archive/2010/09/23/1833449.html)

这篇文章记录DirectX 10.0中几个重要的结构体。

## 1\. ID3D10Device：

Direct3D 10.0的适配器，用来准备渲染， 创建Direct3D 资源,创建shader。

## 2\. D3D10_DRIVER_TYPE：

Device的类型

    1 typedef enum D3D10_DRIVER_TYPE  
    2 {  
    3     D3D10_DRIVER_TYPE_HARDWARE = 0,//通过直接接口访问硬件  
    4      D3D10_DRIVER_TYPE_REFERENCE = 1,//通过直接接口访问创建的软件仿真设备  
    5      D3D10_DRIVER_TYPE_NULL = 2,          //使用参考设备，但不能用于渲染  
    6      D3D10_DRIVER_TYPE_SOFTWARE = 3,//留着以后用  
    7  } D3D10_DRIVER_TYPE;

## 3\. ID3D10RenderTargetView：

可以想象为一个画布，所有的东西都在其上面画。我们可以看下其继承关系：

![view](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Dire
ctXAPI_10E35/view_thumb.png)

## 4\. ID3D10Resource：

一个关于资源的接口。

广义地讲，Direct3D中的资源就是两种： Buffer 和 textures.

Buffer: 如 Vertex Buffer, Index buffer, Constant buffer

Textures: 用来存储纹理的结构体。

![resource](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/
DirectXAPI_10E35/resource_thumb.png)

## 5\. DXGI_SWAP_CHAIN_DESC：

描述一个swap chain, 总是用来在创建Deviece和swap chain的时候当设置参数的东西用。

补充：DXGI------DirectX Graphics Infrastructure(DirectX 图像基础设施)

DXGI 是以后Dirext 的后续版本的公共基础， 它封装了一些基本底层的常量和操作， 可以使以后的不同API共享一部分资源。

![DXGI](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Dire
ctXAPI_10E35/DXGI_thumb.png)

## 6\. D3D10_INPUT_ELEMENT_DESC

我们可以自己定义一个结构体来描述顶点，但是注意的是这些点是定义在系统内存中的。然后，当我们把这些点转化为GPU的Verter Buffer的时候，
我们只是提供一个一块线性的区域。GPU必须知道这些点是怎么分布的（提取争取的属性---
如何从结构体中提取各自的信息）。D3D10_INPUT_ELEMENT_DESC就是为了这个功能准备的。

ex:

    1 D3D10_INPUT_ELEMENT_DESC layout[] =   
    2 {  
    3 { L"POSITION", 0, DXGI_FORMAT_R32G32B32_FLOAT, 0, 0, D3D10_INPUT_PER_VERTEX_DATA, 0 },   
    4 };   
    5 UINT numElements = sizeof(layout)/sizeof(layout[0]);

## 7\. IDXGISwapChain

实现一个或多个surface在输出之前的存储渲染。

可以看下这个接口提供的的功能函数：

IDXGISwapChain::GetBuffer

取得后置缓存

IDXGISwapChain::GetContainingOutput

取得输出量.

IDXGISwapChain::GetDesc

取得swapchain的描述

IDXGISwapChain::GetFrameStatistics

取得渲染状态

IDXGISwapChain::GetFullscreenState

取得输出量(only for full-screen mode).

IDXGISwapChain::GetLastPresentCount

得到上一帧调用Present的数量

IDXGISwapChain::Present

Presents the frame buffer to the output

IDXGISwapChain::ResizeBuffers

改变后置缓存大小和格式

IDXGISwapChain::ResizeTarget

改变输出量属性

IDXGISwapChain::SetFullscreenState

改变输出量的窗口和全屏模式 （输出量IDXGIOutput 接口

## 8\. D3D10_BUFFER_DESC

描述一个 Buffer 资源

    1 typedef struct D3D10_BUFFER_DESC {  
    2     UINT ByteWidth;              //buffer的大小  
    3      D3D10_USAGE Usage;     //指定CPU或GPU读取资源的方式  
    4      UINT BindFlags;               //指定buffer的绑定方式  
    5      UINT CPUAccessFlags;     //指定CPU访问权限  
    6      UINT MiscFlags;               //指定其他buffer选项，比如是否共享  
    7 } D3D10_BUFFER_DESC;

## 9\. D3D10_SUBRESOURCE_DATA

为subresource指定数据， 主要为CreateBuffer服务。

    1 typedef struct D3D10_SUBRESOURCE_DATA   
    2     {   
    3     const void *pSysMem;  //要初始化的数据   
    4     UINT SysMemPitch;       //    
    5     UINT SysMemSlicePitch; //   
    6     }     D3D10_SUBRESOURCE_DATA;

## 10\. D3D10_INPUT_ELEMENT_DESC

D3D10一个明显的变化，全面取代固定流水线

    1 typedef struct D3D10_INPUT_ELEMENT_DESC {  
    2     LPCSTR SemanticName; //shader输入部分关联的语义  
    3     UINT SemanticIndex;  //语义编号  
    4     DXGI_FORMAT Format;  //输入部分的格式  
    5     UINT InputSlot;      //IA stage的位置  
    6     UINT AlignedByteOffset; //位置的偏移  
    7     D3D10_INPUT_CLASSIFICATION InputSlotClass;//输入的单顶点还是复合数据实例  
    8     UINT InstanceDataStepRate;//使用复合数据实例时的步进  
    9 } D3D10_INPUT_ELEMENT_DESC;

## 11\. D3D10_SUBRESOURCE_DATA

##  描述被复制到vertex buffer 的数据。通常的用法是：

    1 SimpleVertex vertices[] =   
    2 {   
    3     D3DXVECTOR3( 0.0f, 0.5f, 0.5f ),   
    4     D3DXVECTOR3( 0.5f, -0.5f, 0.5f ),   
    5     D3DXVECTOR3( -0.5f, -0.5f, 0.5f ),   
    6 };  
    7 ……  
    8 D3D10_SUBRESOURCE_DATA InitData;   
    9   InitData.pSysMem = vertices;

  

posted @ 2010-09-23 18:40 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1833449) 收藏

##备注 
 @post in:2010-09-23 18:40