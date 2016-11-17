#  [Directx----
Texturing](http://www.cnblogs.com/sld666666/archive/2010/08/26/1809457.html)

纹理映射（或者说材质）：把一张图片映射到一个三角形中。这样的话就能使一个物体有更多的细节和真实感。

在Direct3D中， 与纹理相关的接口是IDirect3DTexture9.

## 1\. Texture Coordinates(纹理坐标系)

D3D使用的纹理坐标系包括：u_axis(水平坐标轴) 和v_axis（垂直坐标轴）。

## 2.使用材质

一个材质可以认为是一张图片， 它会被导入到IDirect3DTexture9来作为一个材质来使用。

    1 IDirect3DTexture9*      Tex  = 0;  
    2 D3DXCreateTextureFromFile(  
    3     Device,  
    4     _T("dx5_logo.bmp"),  
    5     &Tex);  
    6 Device->SetTexture(0, Tex);

D3DXCreateTextureFromFile 函数能够load 任何格式的图片。

## 3.Filters（过滤器？???）

通常情况下，一个材质的大小和其要赋予的物体的大小是不一样的。

a. 当材质比物体小的时候， 扩大材质适应物体。

b. 当材质比物体大时，减小材质适应物体

D3D使用SetSamplerState来设filter.

Filtering（过滤？我不知道是不是正确），D3D中使用这种技术使这个适应过程更平滑。

D3D使用了3种过滤器：

(1).Nearest point sampling: 默认的过滤方式，最差的效果，但是最快的速度。

Ex :Device->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_POINT);

(2).Linear filtering:效果相当的好， 速度比较快。

Ex :Device->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_LINEAR);

(3).Anisotropic filtering:最好的效果， 但是最长的时间

Ex :Device->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_ANISOTROPIC);

## 4\. Mipmaps Filter

注：MIPMAP可以在内存容量充足的情况下实现较低的纹理读取带宽需求做filter操作，

本身是一堆对原始纹理作pre-filter产生的较小分辨率纹理。

比如我有一张10*10的图片， 我要贴到5*5的平面上，此时我会创建10*10， 9*9， 8*8…

一系列的图片，只要碰到合适的就用各自级别上的图片进行贴图操作。

在D3D中，如果设备支持mipmaps, D3DXCreateTextureFromFile 会得到一个mipmap的链，

然后，就会自动的进行选择和贴图了。

     

## 5\. Address modes

通常情况下，纹理坐标系的范围是[0,1]，当然也可以超出这个范围，有4种方式

来表示超出范围以后的贴图方式： wrap, border color, clamp mirror.

## 6\. 实例

假设现在要花一个箱子， 我们的原材料有一个Cube 类，一个材质如下图

                      ![crate](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Texturing_135B5/crate_thumb.jpg) 

在D3D中我们要一个箱子一个箱子需要以下步：

(1). 初始化， 这里把之封装在Setup()函数中

(2). 画图， 这里把之封装在Display函数中。

首先来看下Setup 函数是如何实现的：

一： 创建立方体

    1 //Create cube  
    2  Box = new Cube(Device);

二：设置灯光

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 D3DLIGHT9 light;  
     2 ::ZeroMemory(&light, sizeof(light));  
     3 light.Type      = D3DLIGHT_DIRECTIONAL;  
     4 light.Ambient   = D3DXCOLOR(0.8f, 0.8f, 0.8f, 1.0f);  
     5 light.Diffuse   = D3DXCOLOR(1.0f, 1.0f, 1.0f, 1.0f);  
     6 light.Specular  = D3DXCOLOR(0.2f, 0.2f, 0.2f, 1.0f);  
     7 light.Direction = D3DXVECTOR3(1.0f, -1.0f, 0.0f);  
     8 Device->SetLight(0, &light);  
     9 Device->LightEnable(0, true);  
    10 Device->SetRenderState(D3DRS_NORMALIZENORMALS, true);  
    11 Device->SetRenderState(D3DRS_SPECULARENABLE, true);

三：创建材质纹理

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    1 //create texture  
    2  D3DXCreateTextureFromFile(Device,   
    3                         _T("crate.jpg"),  
    4                         &Tex);  
    5  //Set Texture Filter states  
    6  Device->SetSamplerState(0, D3DSAMP_MAGFILTER, D3DTEXF_LINEAR);  
    7 Device->SetSamplerState(0, D3DSAMP_MINFILTER, D3DTEXF_LINEAR);  
    8 Device->SetSamplerState(0, D3DSAMP_MIPFILTER, D3DTEXF_LINEAR);

四：设置摄像头

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    1 //Set the camera  
    2  D3DXVECTOR3 position( 0,2,3);  
    3 D3DXVECTOR3 target(0.0f, 0.0f, 0.0f);  
    4 D3DXVECTOR3 up(0.0f, 1.0f, 0.0f);  
    5 D3DXMATRIX V;  
    6 D3DXMatrixLookAtLH(&V, &position, &target, &up);  
    7 Device->SetTransform(D3DTS_VIEW, &V);

五：设置放映室

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    1 D3DXMATRIX proj;  
    2 D3DXMatrixPerspectiveFovLH(  
    3     &proj,  
    4     D3DX_PI * 0.5f, // 90 - degree  
    5      (float)Width / (float)Height,  
    6     1.0f,  
    7     1000.0f);  
    8 Device->SetTransform(D3DTS_PROJECTION, &proj);

到这里Setup()函数完成，下面来看下它是如何display的

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 voidDisplay()  
     2 {  
     3         Device->Clear(0, 0, D3DCLEAR_TARGET | D3DCLEAR_ZBUFFER, 0xffffffff, 1.0f, 0);  
     4         Device->BeginScene();  
     5         Device->SetMaterial(&d3d::WHITE_MTRL);  
     6         Device->SetTexture(0, Tex);  
     7         Box->draw(0, 0, 0);  
     8         Device->EndScene();  
     9         Device->Present(0, 0, 0, 0);   
    10 }

到这里，一个箱子就出来了：

![RTX截图未命名](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/
Texturing_135B5/RTX%E6%88%AA%E5%9B%BE%E6%9C%AA%E5%91%BD%E5%90%8D_thumb.png)

好简单啊，不是吗？

posted @ 2010-08-26 20:09 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1809457) 收藏

##备注 
 @post in:2010-08-26 20:09