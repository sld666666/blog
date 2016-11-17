#  [Rendering Pipeline(1)----The Vertex Shader
Stage](http://www.cnblogs.com/sld666666/archive/2010/09/20/1832082.html)

The Vertex Shader
Stage(顶点着色阶段)是渲染管道的第二个阶段。它的主要工作职责是输入一个顶点，执行坐标变换、蒙皮（Skinning）和光照计算，然后输出这个顶点。

顶点着色是GPU为每一个顶点执行的，当然这非常快。

通过顶点着色，我们可以实现许多非常有用的效果（如： 坐标变换， 灯光， 位移映射）。

## 1\. Local Space and World Space

一个3D物体， 首先它有自己的坐标系，这个叫“Local Space”, 然后把它放入到一个世界坐标中，就会有了世界坐标系(World
Space)。这个很好理解。但是这就需要Local Space 和 World Space 之间的坐标转换了。把Local Space 到World
Space 之间的转换叫”World transform”,而对应的转换矩阵叫”world matirx”。把一个场景中的所有物体从Local Space
转换到World Space之后， 这个场景中所有的物体就在同一坐标系中了。

## 2\. View Space

为了形成一张2D图片，我们必须往场景中放置一个摄像头。该摄像机指定了我们能够看到什么样的2D图片。我们为摄像机建立一个Local Space, 这个
Local Space 叫”View space”或”eye space”或”camera space”.我们把从”world space”到”View
space”的转换叫”View transform”, 而对应的矩阵叫”view matirx”。

## 3\. Projection and Homogeneous Clip Space

前面我们已经介绍了一个摄像机在世界坐标系中的位置。 但是对于一个摄像机，还有一个重要的概念就是摄像机镜头能够拍摄的场景的大小。如图 1
所示。对于一个摄像机， 如果场景离摄像机越远，其能够拍摄的场景就越大。

![camera](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Re
nderingPipelineTheVertexShaderStage_13528/camera_thumb.png) 图 1

其中非常重要的一件事是我们要把3D的机会物体在2D的放映室中放映。这个过程叫”Projection (视口转换)”。

## 4\. 一个Vertex Shader的例子

下面我们来看一个简单的顶点着色器。

Shaders 是一种叫HLSL(High_Level Shading
Language)的语言描述的。Shaders通常是被.fx格式的文件保存。如下面几个一个HLSL语言的典型列子。

     1 cbuffer cbPerObject   
     2 {   
     3     float4x4 gWVP;   
     4 };  
     5  void VS(float3 iPosL  : POSITION,   
     6         float4 iColor : COLOR,   
     7         out float4 oPosH  : SV_POSITION,   
     8         out float4 oColor : COLOR)   
     9 {   
    10     // Transform to homogeneous clip space.   
    11      oPosH = mul(float4(iPosL, 1.0f), gWVP);   
    12     // Just pass vertex color into the pixel shader.   
    13      oColor = iColor;   
    14 }

这里就稍微解释下上面代码的意思， VS 就是顶点着色的函数。当然，我们可以给顶点着色函数任意的名字。这个顶点着色有4个参数，前面两个是输入参数，后面两个是输
出参数。HLSL没有引用和指针。所以要返回多个的内容，要么用结构体，要么用输出参数。

参数POSITION， 和参数COLOR分别对应于结构体：

    1 struct Vertex   
    2 {   
    3     D3DXVECTOR3 pos;   
    4     D3DXCOLOR   color;   
    5 };

中的 pos 和color。而后面两个参数（SV_POSITION和COLOR）会在下一个阶段用到。

顶点着色函数内部的语句就非常简单了。第一行代码的意思就是把这个顶点的位置从本地的坐标系到相应的裁剪坐标系中转换。这个转换时根据一个 4*4 矩阵 gWVP
来计算的。这个矩阵是描绘world view, projection的矩阵。float4(iPosL, 1.0f)构造了一个4D的向量， 其相等于：
float4(iPosL.x, iPosL.y, iPosL.z, 1.0f);内置函数 mul
是表示矩阵相乘。mul函数重载了矩阵的维数。如你可以把mul 用在3*3矩阵中， 也可以用在4*4 矩阵中，或者是1*3 向量中。

第二行的代码的意思就是简单的吧输入的color复制到输出地color上去， 这个表面颜色这个东西，在下一个阶段会被使用。

当然我们也可以把上面的HLSL的语言写成如下的形式：

     1 cbuffer cbPerObject   
     2 {   
     3     float4x4 gWVP;   
     4 };  
     5  struct VS_IN   
     6 {   
     7     float3 posL  : POSITION;   
     8     float4 color : COLOR;   
     9 };  
    10 struct VS_OUT   
    11 {   
    12     float4 posH : SV_POSITION;   
    13     float4 color : COLOR;   
    14 };  
    15 VS_OUT VS(VS_IN input)   
    16 {   
    17     VS_OUT output;  
    18     output.posH = mul(float4(input.posL, 1.0f), gWVP);   
    19     output.color = input.color;  
    20     return output;   
    21 }

Note： 如果没有几何着色器，
顶点着色器至少要做视口转换。因为硬件预期的点与定点着色器之间存在距离。但是如果有几何着色器，视口转换这个动作就可以推迟到几何着色器中进行。

Note: A vertex shader (or geometry shader)不会做perspective divide（不会翻译），
perspective divide 会在以后流程中体现。

## 5\. Constant Buffers

在上面的vertex shader中，我们有如下的代码：

    1 cbuffer cbPerObject   
    2 {   
    3     float4x4 gWVP;   
    4 };

上面的代码定义看cbuffer对象。这个对象是 constant buffer。 所谓constant
buffer就是一块能够存储不同变量然后把这些变量交由着色器访问。在上面的例子中，我们在Constant buffer
中存储了一个4*4的矩阵—gWVP,用以描绘world, view, projection 的组合，通过这个组合我们就可以把一个点从local space
到相应的clip space转换。在constant buffer中的数据不会为每一个点变化，但是通过effects framework（以后几章会讲到），
C++应用程序在运行时更新constant buffer的内容。它提供了C++应用程序代码和effect代码之间的交流联系。例如，world matirx
对于每一个物体是不同的，world,view,projection
matrix的组合也是不同的，因此，当使用上述的顶点着色器来渲染多个物体是，我们必须更新gWVP。

创建constant buffer的总的原则就是其更新的频率。如我们可创建如下的几个constant buffer。

     1 cbuffer cbPerObject   
     2 {   
     3     float4x4 gWVP;   
     4 };  
     5 cbuffer cbPerFrame   
     6 {   
     7     float3 gLightDirection;   
     8     float3 gLightPosition;   
     9     float4 gLightColor;   
    10 };  
    11 cbuffer cbRarely   
    12 {   
    13     float4 gFogColor;   
    14     float gFogStart;   
    15     float gFogEnd;   
    16 };

如上，我们创建了三个constant buffer。

第一个存储了world,view,prijection
矩阵的组合。它的值由不同的物体决定。所以对于任意一个，它的值都要改变。因此，如果我们每一帧在渲染100个物体，第一个constant
buffer的值将会被改变100次。

第二个存储了场景的灯光。这里我们假设灯光是动画形式的，所以这个constant buffer每一帧更新一次。

第三个用来控制雾效果，这里我们就可以认为这个constant就很少被更新了。

因为如果一个constant buffer 更新了以后，它里面的值也全部更新。因此，我们根据一个constant buffer更新的频率来划分
constant buffer是非常高效的。

posted @ 2010-09-20 21:39 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1832082) 收藏

##备注 
 @post in:2010-09-20 21:39