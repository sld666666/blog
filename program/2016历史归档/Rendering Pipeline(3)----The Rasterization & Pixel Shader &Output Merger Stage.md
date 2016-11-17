#  [Rendering Pipeline(3)----The Rasterization & Pixel Shader &Output Merger
Stage](http://www.cnblogs.com/sld666666/archive/2010/09/21/1832744.html)

## 1\. The Rasterization Stage(光栅处理阶段)

The Rasterization Stage(光栅处理阶段) 是渲染管道的第四个阶段，这个阶段的主要工作是，根据摄像机镜头的区域裁剪几何体，
把3D几何体映射到屏幕上。

### 1\. 1 The Clipping Stage(裁剪阶段)。

裁剪过程发生在光处阶段。如图 1所示。一个摄像机的镜头区域是一个锥形体(frustum)，如果一个几何体，其完全在frustum外部，则需要完全地抛弃它，
如果部分在frustum外部， 则需要裁剪它。这个裁剪过程就是在Clipping stage 发生的。

![Clip](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Rend
eringPipeline1TheRasterizationPixelS_F463/Clip_thumb.jpg)图 1

### 1.2 Viewport Transform(视口转换)

当裁剪一个几何图形完毕以后，我们需要把这个图形从裁剪坐标系映射到设备坐标系中。一个点一旦位于设备坐标系中，其2D X坐标和Y坐标就会在back buffer
中转化在一个矩形区域内。这个过程叫做视口转换。在转换完毕之后。X 坐标和Y 坐标就在单位像素内。通常视口转不影响Z坐标。其值会被depth
buffer所使用，当然在视口转换中我们可以调用 D3D10_VIEWPORT结构体中的MinDepth和MaxDepth值。

### 1.3 Backface culling

一个三角形有两个面，我们把面对摄像机镜头的面称为front-facing，而被对镜头的面称为back-
facing。因为一个3D物体是有N多三角形形成的封闭的实体，摄像机是观察不到这个物体的back-facing的，所以这个back-
facing是不必要被渲染的。而所谓Backface cilling就是渲染管道丢弃back-facing渲染的过程。

### 1.4 Vertex Attribute Interpolation(顶点属性插值)

如果要画一个三角形，我们首先会定义一个点的结构体。这个结构体有关于点的位置,颜色，纹理坐标等相关的属性。但进行视口转换的时候，我们需要把这些属性全部转换到像
素中。顶点的深度值也需要得到这些插值，然后把这些插值用到depth buffer中。这个过程叫做_perspective correct
interpolation_这样我们就把顶点的属性转换为屏幕的显示了。

## 2\. The Pixel Shader Stage(像素着色阶段)

一个顶点的属性从顶点着色器(或者几何着色器)出来以后，被插值到三角形中， 然后这个插值会作为像素着色器的输入。

像素着色和顶点着色非常相似。它为每一个像素执行一次。像素着色器的主要功能是为每一个像素点计算颜色。需要注意的是一个顶点对应的像素点可能无法存在，因为这个像素
点可能被裁剪到或者被其他像素点覆盖。

如我们可以写下如下HLSL语言的像素着色器

    1 float4 PS(float4 posH : SV_POSITION,  
    2 float4 color : COLOR) : SV_TARGET  
    3 {  
    4 return color;  
    5 }

在上面的例子中，像素着色器只是返回了插值中的颜色值。

## 3\. The Output Merger Stage(输出合并阶段)

当像素被像素着色器处理了之后，其就会被输入到输出合成器中。在这个阶段有点像素点可能被丢弃。没有被丢弃的像素写入back
buffer中，Belending(混合)也是在这个阶段做的。

posted @ 2010-09-21 17:23 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1832744) 收藏

##备注 
 @post in:2010-09-21 17:23