#  [Directx----
Blending](http://www.cnblogs.com/sld666666/archive/2010/08/28/1811481.html)

## 1\. Blending 的定义

Blending 直接看的意思就是混合了， 那什么是混合呢？

假设考虑这么一个情况：如果一个玻璃球放在一个木板上， 这样就必须能透过球看到后面的

木板，用什么方法实现这种效果呢？这里就介绍Blending这种方法。看下面两张图片：

![1](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Directx
Blending_11466/1_thumb.png) ![2](http://images.cnblogs.com/cnblogs_com/sld6666
66/WindowsLiveWriter/DirectxBlending_11466/2_thumb.png)

图一 图二

图片一是未使用Blending的，图片二是使用了Blending的.

这里可以非常清楚地看出两者的区别。

而所谓混合也就是指一个前面的像素的颜色和后面像素的颜色的混合。

## 2\. Blend Factiors(影响混合的因素)

利用不同原物体和目标物体（前面的物体和后面的物体）的组合， 我们可以混合出非常多的效果。

在D3D中我们可以在设置渲染方式的时候利用参数：D3DRS_SRCBLEND和

D3DRS_DESTBLEND 来设置blending。如：

    1 Device->SetRenderState(D3DRS_SRCBLEND, Source);  
    2 Device->SetRenderState(D3DRS_SESTBLEND, Destination);

在这里Source 和 Destination 可以为以下的方式：

D3DBLEND_ZERO: blendFactor = (0,0,0,0);

D3DBLEND_ONE: blendFactor = (1,1,1,1);

D3DBLEND_SRCCOLOR: blendFactor = (r,g,b,a)

D3DBLEND_INVSRCCOLOR: blendFactor = (1-r,1-g,1-b,1-a);

D3DBLEND_SRCALPHA: blendFactor = (a,a,a,a);

D3DBLEND_INVSRCALPHA: blendFactor = (1-a,1-a,1-a, 1-a);

......

Source和Destination的默认方式分别为D3DBLEND_SRCALPHA和

D3DBLEND_INVSRCALPHA。

如：如果我们要创建如上图二所示的图片是我们只需：

（1） 创建一个长方形，作为目标物体， 为其画上木头的材质，画出。

代码略

（2） 创建一个球体，作为原物体， 为其赋予白色的材质， 设置透明度为0.5

    1 //Create sphere   
    2  sphereMtrl = d3d::WHITE_MTRL;   
    3 sphereMtrl.Diffuse.a = 0.5f; // set alpha to 50% opacity   
    4  D3DXCreateSphere(Device, 1.0f,20, 20, &sphere, 0);

然后设置渲染参数即可

    1 // set blending factors so that alpha component determines transparency   
    2  Device->SetRenderState(D3DRS_SRCBLEND, D3DBLEND_SRCALPHA);   
    3 Device->SetRenderState(D3DRS_DESTBLEND, D3DBLEND_INVSRCALPHA);

## 3\. Transparency(透明度)

一个32的颜色的组成如下： color = (r, g, b,a), 我们除了要表示红绿蓝三种颜色以外，

还可以用来表示透明度。

因为我们用8bit来表示alpha， 所以透明度的范围在[0,255],或者转化成相应的[0%， 100%]；

因此0表示完全透明， 128（50%）表示半透明。

为了能使用透明效果，在设置渲染参数的时候必须使用Source = D3DBLEND_SRCALPHA；

Destination = D3DBLEND_INVSRCALPHA； 也就是上面所说的默认的参数设置。

## 4\. Alpha channels(Alpha 通道)

所谓 Alpha通道就是 在24位真彩色的基础上，外加了8位的Alpha数值来描述物体的透明程度。

要渲染一个球体为透明的， 可以有两种方式：

(1).物体本身定义为透明。

(2).在进行纹理贴图的时候用 alpha 通道。

当纹理贴图的时候，如果使用了alpha 通道， Alpha 通道的alpha值一起渲染到了物体表面上。

那么alpha 通道的值是哪里来的呢？在默认方式下，如果一个材质有 alpha 通道，

渲染的时候alpha通道的值将会从材质的alpha通道得。如果当前没有得到任何alpha通道的值，

alpha通道的值会从物体的顶点中得到。

当然我们可以用如下方式手动指定我们aplha通道的值

    1 // use alpha channel in texture for alpha   
    2  Device->SetTextureStageState(0, D3DTSS_ALPHAARG1, D3DTA_TEXTURE);   
    3 Device->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_SELECTARG1);

或

    1 // use alpha channel from diffuse  
    2  Device->SetTextureStageState(0, D3DTSS_ALPHAARG1, D3DTA_DIFFUSE);   
    3 Device->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_SELECTARG1);

如我们用两种材质的方式画一个立方体，

(1) 导入的是一张普通的图片

    1 D3DXCreateTextureFromFile(   
    2     Device,   
    3     _T("normal.bmp"),   
    4     &CrateTex);

(2) 导入是一个带有alpha 通道的图片

    1 D3DXCreateTextureFromFile(   
    2     Device,   
    3     _T("normal.bmp"),   
    4     &CrateTex);

然后设置 渲染的alpha通道值从图片中读取。

其他设置合一般的渲染都一样。

    1 // use alpha channel in texture for alpha   
    2  Device->SetTextureStageState(0, D3DTSS_ALPHAARG1, D3DTA_TEXTURE);   
    3 Device->SetTextureStageState(0, D3DTSS_ALPHAOP, D3DTOP_SELECTARG1);

然后我们看一下两张图片

![1](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Directx
Blending_11466/1_thumb_1.png) ![2](http://images.cnblogs.com/cnblogs_com/sld66
6666/WindowsLiveWriter/DirectxBlending_11466/2_thumb_1.png)

图三 图四

图三是的材质图片是没有alpha通道的， 图四的图片是带alpha通道的， 这里可以明显

看出两者的不同。

posted @ 2010-08-28 21:35 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1811481) 收藏

##备注 
 @post in:2010-08-28 21:35