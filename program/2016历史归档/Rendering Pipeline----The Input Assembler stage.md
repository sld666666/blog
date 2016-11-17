#  [Rendering Pipeline----The Input Assembler
stage](http://www.cnblogs.com/sld666666/archive/2010/09/18/1830273.html)

(最近在看龙书10.0， 这篇文章是是关于Rendering Pipeline的The Input Assembler stage的总结)

Rendering pipeline(渲染管道)提供了一整套从摄像机视口的3D场景转化为2D图片的步骤。图 1显示了 渲染管道的不同时期。

![Rendering Pipeline](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsL
iveWriter/RenderingPipeline_11D54/Rendering%20Pipeline_thumb.png)

图 1

下面的文章以及以后会有几篇逐步介绍Direct 10.0 Rendering Pipeline渲染管道的不同时期。

## The Input Assembler stage

The Input Assembler stage(输入装配器阶段)读取几何数据(vertex buffer 和 index
buffer),然后用它们来装配几何图形(线，三角形).

## 1\. vertex 的定义

一个vertex 位置，颜色，纹理坐标等属性。Direct3D 可以使我们灵活地创建vertex的格式或者说Direct3D允许我们定义一个vertex的组
成。如我们就可以创建下面的结构体来描述一个点的位置,颜色和纹理坐标。

    1 struct vertex1  
    2 {  
    3     D3DXVECTOR3 pos;  
    4     D3DXCOLOR color;  
    5     D3DVECTOR2 texc  
    6 }  
    7  

## 1.1. D3D10_INPUT_ELEMENT_DESC

建立如上的vertex结构体以后， 我们还需要把这个结构体声明的点全部联系起来，Direct3D
用ID3D10InputLayout接口来实现这样的功能。这个接口被D3D10_INPUT_ELEMENT_DESC
描述。D3D10_INPUT_ELEMENT_DESC 中的每一个元素描述了一个vertex 结构体中的属性。所以如果vertex如果有3个属性的话，
就必须是D3D10_INPUT_ELEMENT_DESC layout[3]。

我们先看下这个结构体是如何被定义的：

     1 typedef struct D3D10_INPUT_ELEMENT_DESC   
     2     {   
     3     LPCSTR SemanticName;   
     4     UINT SemanticIndex;   
     5     DXGI_FORMAT Format;   
     6     UINT InputSlot;   
     7     UINT AlignedByteOffset;   
     8     D3D10_INPUT_CLASSIFICATION InputSlotClass;   
     9     UINT InstanceDataStepRate;   
    10     }     D3D10_INPUT_ELEMENT_DESC;

然后再解析下D3D10_INPUT_ELEMENT_DESC 结构体每一个参数的含义：

SemanticName： 一个与元素相关的字符串。标识这个layout的名字。

SemanticIndex： 一个Vectex的结构体可能可能有多个纹理。我们就可一个用SemanticIndex来标识它，
当其值为0的时候表示没有索引被定义。

Format：DXGI_FORMAT枚举中的一个，表明了元素的格式， 如DXGI_FORMAT_R32G32B32_UINT。

InputSlot：指定这些元素是从哪里来的， Direct3D 支持16个input slots(0-15)。例如，如果一个vertex
结构体有位置，颜色等，我们即可以用同一个input slots 来装配这个两个属性，也可以分别为这两个属性设置不同的input
slot。然后，Direct3D会利用不同Input slot的元素来装配vectex。

AlignedByteOffset： 表明一个input slot其位置到vertex结构体的距离。如：

    1 struct Vertex2  
    2 {  
    3     D3DXVECTOR3 pos; // 0-byte offset  
    4      D3DXVECTOR3 normal; // 12-byte offset  
    5      D3DXVECTOR2 texC; // 24-byte offset  
    6  };

InputSlotClass： 这里指定D3D10_INPUT_PER_VERTEX_DATA。

InstanceDataStepRate： 这里指定0。

如对于 Vertex2, 我们可以这样设置 Input Layout:

    1 D3D10_INPUT_ELEMENT_DESC desc2[] =  
    2 {  
    3 {"POSITION", 0, DXGI_FORMAT_R32G32B32_FLOAT, 0, 0, D3D10_INPUT_PER_VERTEX_DATA, 0},  
    4 {"NORMAL", 0, DXGI_FORMAT_R32G32B32_FLOAT, 0, 12, D3D10_INPUT_PER_VERTEX_DATA, 0},  
    5 {"TEXCOORD", 0, DXGI_FORMAT_R32G32_FLOAT, 0, 24, D3D10_INPUT_PER_VERTEX_DATA, 0}  
    6 };

## 1.2 ID3D10Device::CreateInputLayout

当input layout 设置完毕之后，我们用ID3D10Device::CreateInputLayout来获得一个指向ID3D10InputLayou
t接口的指针，这个指针就是用来描述 input layout的。

    1 HRESULT ID3D10Device::CreateInputLayout(   
    2     const D3D10_INPUT_ELEMENT_DESC *pInputElementDescs,   
    3     UINT NumElements,   
    4     const void *pShaderBytecodeWithInputSignature,   
    5     SIZE_T BytecodeLength,   
    6     ID3D10InputLayout **ppInputLayout);

pInputElementDescs： 1.1 所述的layout的数组

NumElements： layout数组的大小(数量)

pShaderBytecodeWithInputSignature: 一个指向顶点着色器的签名的字节码。

BytecodeLength：以byte为单位的顶点着色器的签名的数据。

ppInputLayout： 返回一个指向需要创建的input layout的指针前一个参数。

第三个参数需要一些定制。一个顶点渲染器会为每一个顶点判断一其格式。Vertex
结构体重的每一个元素(顶点的属性)都必须映射到其相应的输入通道中。当在创建input layout 的时候，
通过传递顶点渲染器的签名，Direct3D能够从vetex 结构体到渲染器输入通道的映射。一个input
layout能够被不同的渲染器重复使用，并且提供输入签名在不同的时间。

下面是一个创建 input layout的实例：

     1   ID3D10Effect* mFX;   
     2     ID3D10EffectTechnique* mTech;   
     3     ID3D10InputLayout* mVertexLayout;  
     4     /* ...create the effect... */  
     5     mTech = mFX->GetTechniqueByName("ColorTech");   
     6     D3D10_PASS_DESC PassDesc;   
     7     mTech->GetPassByIndex(0)->GetDesc(&PassDesc);   
     8     HR(md3dDevice->CreateInputLayout(vertexDesc, 3,   
     9         PassDesc.pIAInputSignature, PassDesc.IAInputSignatureSize,   
    10         &mVertexLayout));

这里解释下 D3D10_PASS_DESC ，首先提出一个概念就是 effect，
effect封装了顶点渲染器签名的传递过程，每一个顶点渲染器与这个过程相关联。所以从effect中，
我们可以通过D3D10_PASS_DESC结构体得到一个顶点渲染器的输入签名。

## 1.3 IASetInputLayout

当一个input layout被创建以后，它还没有绑定到一个device中， 调用IASetInputLayout函数可以把它绑定一个divece中，如：

    1 md3dDevice->IASetInputLayout(mVertexLayout);

## 2 Vertex Buffers

为了使GPU能够得到我们定义的点的数组的入口点， 我们必须定义一个专门的资源结构体(buffer)来描述这些点，
D3D中用ID3D10Buffer接口来描述这个buffer。如果这个buffer是用来储存vertex的就叫 vertex
buffer。为了创建一个vertex buffer，我们需要做如下步骤：

(1) 填充D3D10_BUFFER_DESC结构体，这个结构体是用来描述 buffer的

(2) 填充D3D10_SUBRESOURCE_DATA结构体， 这个结构体指定我们需要初始化的数据。

(3) 调用ID3D10Device::CreateBuffer来创建 buffer。

(4)调用ID3D10Device:IASetVertexBuffers把vertex buffer 设入device中。

## 3 Primitive Topology

一个vertex buffer只是在内存中存储了一系列内存点。它没有描述这些点是怎么组合在一起从而形成几何图形的。我们用 Primitive
Topology 告诉Direct3D用点形成几何图形的方式。

     1 void ID3D10Device::IASetPrimitiveTopology(   
     2     D3D10_PRIMITIVE_TOPOLOGY Topology);  
     3 typedef enum D3D10_PRIMITIVE_TOPOLOGY   
     4 {   
     5     D3D10_PRIMITIVE_TOPOLOGY_UNDEFINED = 0,   
     6     D3D10_PRIMITIVE_TOPOLOGY_POINTLIST = 1,   
     7     D3D10_PRIMITIVE_TOPOLOGY_LINELIST = 2,   
     8     D3D10_PRIMITIVE_TOPOLOGY_LINESTRIP = 3,   
     9     D3D10_PRIMITIVE_TOPOLOGY_TRIANGLELIST = 4,   
    10     D3D10_PRIMITIVE_TOPOLOGY_TRIANGLESTRIP = 5,   
    11     D3D10_PRIMITIVE_TOPOLOGY_LINELIST_ADJ = 10,   
    12     D3D10_PRIMITIVE_TOPOLOGY_LINESTRIP_ADJ = 11,   
    13     D3D10_PRIMITIVE_TOPOLOGY_TRIANGLELIST_ADJ = 12,   
    14     D3D10_PRIMITIVE_TOPOLOGY_TRIANGLESTRIP_ADJ = 13,   
    15 } D3D10_PRIMITIVE_TOPOLOGY;

## 3.1 Point List

Point List 被D3D10_PRIMITIVE_TOPOLOGY_POINTLIST 定义。如果定义了Point List， Vertex
buffer中的每一个点作为一个单独的点。如下图 2 a 所示。

![Point_List](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWrite
r/RenderingPipeline_11D54/Point_List_thumb.png)

图 2

## 3.2 Line Strip

D3D10_PRIMITIVE_TOPOLOGY_LINESTRIP 定义了 Line Strip.如果定义了Line Strip， vertex
buffer中的每一个点都会依次连接。所以如果有n+1个点，就会有n 条线。如图 2， b 所示。

## 3.3 Line List

D3D10_PRIMITIVE_TOPOLOGY_LINELIST 定义了 Line List. 如果定义了 Line List， 如图2 c
所示，每两个点会形成一条线。

## 3.4 Triangle strip

D3D10_PRIMITIVE_TOPOLOGY_TRIANGLESTRIP定义了 Triangle Strip. 如果定义了Triangle
strip,如图2 d 所示。

## 3.5 Triangle List

D3D10_PRIMITIVE_TOPOLOGY_TRIANGLELIST定义了 Triangle List。如果定义了Triangle
List，每三个点会形成一个三角形。如图 2. a 所示。

![Triangle_List](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWr
iter/RenderingPipeline_11D54/Triangle_List_thumb.png)

图 3

## 3.6 Primitives with Adjacency

上面提到， 一个 triangle List
形成的是一个一个的三角形。如果把这些三角形连成一片的话，就必须包含他们的临近的三角形。我们可以用“_adjacent
triangles_”技术来实现这个功能。 如图 3 . b 所示。为了使几何着色器能知道这些临近的三角形，这些三角形就必须向渲染管道提交
vertex/index buffer .并且D3D10_PRIMITIVE_TOPOLOGY_TRIANGLELIST_ADJ必须被定义。

## 4 Indices and Index buffer

三角形式形成几何图形的基础。如果我们要画如 图 4 所示的几何图形， 我们可以用如下代码

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    Vertex quad[6] = {   
        v0, v1, v2, // Triangle 0   
        v0, v2, v3, // Triangle 1   
    };  
    Vertex octagon[24] = {   
        v0, v1, v2, // Triangle 0   
        v0, v2, v3, // Triangle 1   
        v0, v3, v4, // Triangle 2   
        v0, v4, v5, // Triangle 3   
        v0, v5, v6, // Triangle 4   
        v0, v6, v7, // Triangle 5   
        v0, v7, v8, // Triangle 6   
        v0, v8, v1  // Triangle 7   
    };

![Index](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Ren
deringPipeline_11D54/Index_thumb.png)

图 4

但是我们代码中，我们发现， 有好多点是重复定义的。这里有两个原因需要去除这些重复定义的点：

(1). 减少所需内存空间的大小。

(2). 减少图形硬件对顶点的渲染。

如果定义Triangle strips ， 是可以渲染连续的点从而形成三角形的。但是triangle lists却更合适。所以这就有必要为triangle
list 移去完全一样的点。这就需要 index buffer 显身手了。如： 如果我们想渲染如图 4 a 所示的四边形。我们可以先定义一个Vertex
buffer:

    1 Vertex v[4] = {v0, v1, v2,v3};

然后再定义一个 index buffer :

    1 DWORD indexList[6] = {0, 1, 2, // Triangle 0  
    2                                      0, 2, 3}; // Triangle 1

## 4.1 Index buffer 的创建

因为Index buffer要被GPU接受， 所以我们在使用Index buffer的时候就必须如同一般资源一样被创建。EX：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

## 4.2 Index buffer 的绑定

当创建完 index buffer 之后， 我们必须把index buffer
绑定到渲染管道中。我们可用ID3D10Device::IASetIndexBuffer接口来干这件事情。Ex:

    1 md3dDevice->IASetIndexBuffer(mIB, DXGI_FORMAT_R32_UINT, 0);

##  4.4 用Index buffer 画几何图形

最后，当我们使用 index buffer之后， 我们用DrawIndex()方法来渲染几何图形，而不是Draw()方法。Ex:

    1 md3dDevice->DrawIndexed(numSphereIndices, 0, 0);

posted @ 2010-09-18 16:59 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1830273) 收藏

##备注 
 @post in:2010-09-18 16:59