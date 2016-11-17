#  [Directx----
Lighting](http://www.cnblogs.com/sld666666/archive/2010/08/25/1808487.html)

为了使渲染出来的东西更加真实， 灯光是必须的。当我们使用灯光的时候我们就可以不用指定顶点的颜色。

## 1\. 灯光的组成

(1) Ambient Light(环境光): 场景中所有的物体的表面会反射这种光源。

(2)Diffuse light(漫射光)：当其碰到一个物体时，用各个方向反射。因为满反射的光会朝着物体的各个方向

反射， 所以会到达一个场中的任何地方。 因此，使用diffuse light的时候

我们只需考虑光源发射的方向和物体表面的材质就好了。 这种光是我们用到的

主要光源。

(3)Specular light(镜面反射):在某一特定的方向上传播，当遇到物体时，从一个方向反射。

Specular 反射要求更多的计算。所以在Direct3D中，它是默认关闭的，

如果要打开它Device->SetRenderState(D3DRS_SPECULARENARLE, true)

## 2\. Material(材质)

在真实世界中， 如果一个物体它显示为红色， 是因为它反射的是红色（除了红色以外的全部被吸收了）。

在Direct3D中， 其原理也是一样的。材质反映了其表面对光反射的程度。在 D3D中， 用D3DMATERIAL9表示。

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

    1 typedef struct _D3DMATERIAL9 {   
    2     D3DCOLORVALUE   Diffuse;        /* Diffuse color RGBA */   
    3     D3DCOLORVALUE   Ambient;        /* Ambient color RGB */   
    4     D3DCOLORVALUE   Specular;       /* Specular 'shininess' */   
    5     D3DCOLORVALUE   Emissive;       /* Emissive color RGB */   
    6     float           Power;          /* Sharpness if specular highlight */   
    7 } D3DMATERIAL9;

比如， 如果我们要得到一个红色的球，我们只需定义这个球反射红色就可

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 D3DMATERIAL9 InitMtrlRed()   
     2 {   
     3     D3DMATERIAL9 red;   
     4     ::ZeroMemory(&red, sizeof(red));   
     5     red.Diffuse = D3DXCOLOR(1.0f, 0.0f, 0.0f, 1.0f);   
     6     red.Ambient = D3DXCOLOR(1.0f, 0.0f, 0.0f, 1.0f);   
     7     red.Specular = D3DXCOLOR(1.0f, 0.0f, 0.0f, 1.0f);   
     8     red.Emissive = D3DXCOLOR(0.0f, 0.0f, 0.0f, 1.0f);   
     9     red.Power = 5.0f;  
    10     return red;   
    11 }

要画一个球体，另外几处重要的相关代码如下：

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 ID3DXMesh*    sphere = 0;   
     2 D3DMATERIAL9 mtrlRed;   
     3 D3DXMATRIX    world;  
     4  bool Setup()   
     5 {  
     6 D3DXCreateSphere(Device, 1.0f, 20, 20, &sphere, 0);   
     7 mtrlRed = InitMtrlRed();   
     8 D3DXMatrixTranslation(&world, 0.0f, 0.0f, 0.0f);  
     9  return true;  
    10 }  
    11   
    12  void display()  
    13 {  
    14 Device->SetMaterial(&mtrlRed);   
    15 Device->SetTransform(D3DTS_WORLD, &world);   
    16 sphere->DrawSubset(0);  
    17 }

得到如下图：

![QQ截图未命名](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/D
irectxLighting_12112/QQ%E6%88%AA%E5%9B%BE%E6%9C%AA%E5%91%BD%E5%90%8D_thumb.png
)

## 3\. Light Sources(光源的种类)

Direct3D支持3种不同类型的光源。

(1). Point lights(点光源)--》在世界坐标系中的一点，然后向其四周发射。

(2). Directional lights(直射光)--》光源沿着直线传播。

(3). Spot lights(局部照明的光)--》和手电筒相似。

在D3D中， 灯光用D3DLIGHT9结构体表示。

下面列出3个例子来表示3中不用的光源：

(1). point lights

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 D3DLIGHT9 d3d::InitPointLight(D3DXVECTOR3* position, D3DXCOLOR* color)   
     2 {   
     3     D3DLIGHT9 light;   
     4     ::ZeroMemory(&light, sizeof(light));  
     5     light.Type      = D3DLIGHT_POINT;   
     6     light.Ambient   = *color * 0.6f;   
     7     light.Diffuse   = *color;   
     8     light.Specular  = *color * 0.6f;   
     9     light.Position  = *position;//光源的原始位置   
    10      light.Range        = 1000.0f;//光源的传播范围   
    11      light.Falloff      = 1.0f;   
    12     light.Attenuation0 = 1.0f;//随着光的传播，其被减弱的程度   
    13      light.Attenuation1 = 0.0f;   
    14     light.Attenuation2 = 0.0f;  
    15     return light;   
    16 }

![point](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/Dir
ectxLighting_12112/point_thumb.png)

(2). Directional lights

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 D3DLIGHT9 d3d::InitDirectionalLight(D3DXVECTOR3* direction, D3DXCOLOR* color)   
     2 {   
     3     D3DLIGHT9 light;   
     4     ::ZeroMemory(&light, sizeof(light));  
     5     light.Type      = D3DLIGHT_DIRECTIONAL;//光源的种类   
     6      light.Ambient   = *color * 0.6f;//光源发出的环境光   
     7      light.Diffuse   = *color; //光源发出的漫射光   
     8      light.Specular  = *color * 0.6f;//光源发出的镜面反射光   
     9      light.Direction = *direction;//光传播的方向  
    10      return light;   
    11 }

![Direct light](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWri
ter/DirectxLighting_12112/Direct%20light_thumb.png)

（3）Spot lights

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1 D3DLIGHT9 d3d::InitSpotLight(D3DXVECTOR3* position, D3DXVECTOR3* direction, D3DXCOLOR* color)   
     2 {   
     3     D3DLIGHT9 light;   
     4     ::ZeroMemory(&light, sizeof(light));  
     5     light.Type      = D3DLIGHT_SPOT;   
     6     light.Ambient   = *color * 0.0f;   
     7     light.Diffuse   = *color;   
     8     light.Specular  = *color * 0.6f;   
     9     light.Position  = *position;   
    10     light.Direction = *direction;   
    11     light.Range        = 1000.0f;   
    12     light.Falloff      = 1.0f;   
    13     light.Attenuation0 = 1.0f;   
    14     light.Attenuation1 = 0.0f;   
    15     light.Attenuation2 = 0.0f;   
    16     light.Theta        = 0.4f;//spot light 装用， 内部的圆锥弧度   
    17      light.Phi          = 0.9f;//spot light 装用， 外部的圆锥弧度  
    18      return light;   
    19 }

![spot light](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWrite
r/DirectxLighting_12112/spot%20light_thumb.png)

posted @ 2010-08-25 21:23 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1808487) 收藏

##备注 
 @post in:2010-08-25 21:23