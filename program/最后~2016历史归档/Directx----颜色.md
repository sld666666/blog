#  [Directx----
颜色](http://www.cnblogs.com/sld666666/archive/2010/08/24/1807559.html)

## 1\. 颜色的定义

在D3D中， 颜色使用RGB形式表现的。有两种结构来表现颜色D3DCOLOR和D3DCOLORVALUE。

    typedef DWORD D3DCOLOR;
    D3DCOLOR brightRed = D3DCOLOR_ARGB(255, 255, 0, 0);
    typedef struct D3DCOLORVALUE 
    { 
        float r; // the red component, range 0.0-1.0 
        float g; // the green component, range 0.0-1.0 
        float b; // the blue component, range 0.0-1.0 
        float a; // the alpha component, range 0.0-1.0 
    } D3DCOLORVALUE;

## 2\. 定点的颜色

一个面的颜色是有其相应点的颜色决定的， 所以我们顶一个点的时候就得为其增加颜色变量。

    struct colorVertex 
    { 
        colorVertex(){}
        colorVertex(float x, float y, float z, D3DCOLOR c) 
        { 
            _x = x;     _y = y;  _z = z;  _color = c; 
        }
        float _x, _y, _z; 
        D3DCOLOR _color;
        static const DWORD FVF; 
    };
    const DWORD colorVertex::FVF = D3DFVF_XYZ | D3DFVF_DIFFUSE;

## 3\. 渲染

有两种渲染方式： flat shading 和Gourud shding.

比如我们定义这样3个点的三角形

    colorVertex* vertices; 
    VB->Lock(0, 0, (void**)&vertices, 0);
    // vertices of a unit cube 
    vertices[0] = colorVertex(-1.0f, 0.0f, 2.0f, D3DCOLOR_XRGB(255,   0,   0)); 
    vertices[1] = colorVertex( 0.0f, 1.0f, 2.0f, D3DCOLOR_XRGB(  0, 255,   0)); 
    vertices[2] = colorVertex( 1.0f, 0.0f, 2.0f, D3DCOLOR_XRGB(  0,   0, 255));
    VB->Unlock();

然后用如下方式画出：

    D3DXMATRIX WorldMatrix; 
    // draw the triangle to the left with flat shading 
    D3DXMatrixTranslation(&WorldMatrix, -1.25f, 0.0f, 0.0f); 
    Device->SetTransform(D3DTS_WORLD, &WorldMatrix); 
    Device->SetRenderState(D3DRS_SHADEMODE, D3DSHADE_FLAT); 
    Device->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 1);
    // draw the triangle to the right with gouraud shading 
    D3DXMatrixTranslation(&WorldMatrix, 1.25f, 0.0f, 0.0f); 
    Device->SetTransform(D3DTS_WORLD, &WorldMatrix); 
    Device->SetRenderState(D3DRS_SHADEMODE, D3DSHADE_GOURAUD); 
    Device->DrawPrimitive(D3DPT_TRIANGLELIST, 0, 1);

得到的图形是这样的：

![QQ截图未命名](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/D
irectx_FEFB/QQ%E6%88%AA%E5%9B%BE%E6%9C%AA%E5%91%BD%E5%90%8D_thumb.png)

左图是用 flat shading: 三角形的颜色就第一个点的颜色

右图是用 Gouraud shading: 通过差值算法算出。

posted @ 2010-08-24 19:36 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1807559) 收藏

##备注 
 @post in:2010-08-24 19:36