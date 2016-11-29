#  [Rendering Pipeline(2)----The Geometry Shader
Stage](http://www.cnblogs.com/sld666666/archive/2010/09/20/1832129.html)

The Geometry Shader Stage（几何着色阶段）是渲染管道的第三阶段。这个阶段是可选的。几何着色阶段的输出的是整一个图元。例如,如果我们在
渲染一系列三角形。经过了顶点着色之后，输入几何着色器的是三角形的3个顶点，而输出的就是三角形。(注意的是，这个三个顶点已经被顶点着色器转换过了)。利用几何着
色器的主要优点是它可以创建和销毁几何图形。例如，输入的图元和可以扩展成一个或多个图元。当然几何着色器可以选择不输出图元。

相对于顶点着色器，几何着色器可以创建顶点。一个典型的例子就是把一个点扩展成4个点，或者把一条线扩展成一4个点。

在Rendering Pipeline(0)中的图 1, 我们有一个步骤是“stream output”。这就是说几何渲染器可以输出顶点数据到
buffer中。

posted @ 2010-09-20 22:59 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1832129) 收藏

##备注 
 @post in:2010-09-20 22:59