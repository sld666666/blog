#  [Directx----
Meshes](http://www.cnblogs.com/sld666666/archive/2010/09/10/1823572.html)

前面已经介绍过ID3DXMesh接口了， 但是只是简单的用了D3DXCreate 来创建简单的几何体。这篇文章详细地介绍ID3DXMesh接口。

## 1\. Geometry Info

ID3DXMesh 是从ID3DXBaseMesh继承下来的。ID3DXBaseMesh包含了vertex buffer 和 index
buffer。通过这两者的组合我们可以画出任意三角形及任意三角形的组合。我们可以使用如下方式把vertex buffer 和 index
buffer从ID3DXBaseMesh提取出来。

HRESULT ID3DXMesh::GetVertexBuffer(LPDIRECT3DVERTEXBUFFER9* vertexBuf);

HRESULT ID3DXMesh::GeIndexBuffer(LPDIRECT3DindexBUFFER9* indexBuf);

ID3DXMesh 自己也定义了一套方法：

(1). GetFVF(): 返回顶点的FVF

(2). GetNumVertices(): 返回有多少个顶点。

(3). GetNumBytesPreVertex(): 返回每个顶点有多少字节

(4). GetNumFaces()： 返回有多少个三角形。

## 2\. .x File

在3D图形学中，网格是由一系列共面的多边形组成。在Direct3D中，
提供了好几个接口来处理网格，其中最常见的一个就是ID3DXMesh.我们可以使用该接口来装载网格数据，获得网格的各种信息.

一架飞机，一个花瓶，这样的实体就叫做模型，而生成这种模型的过程叫建模。Direct3D利用.x文件来保存模型。

## 3\. 显示一个模型

显示一个.x 文件，其实很简单： 导入.x 文件， 得到.x文件中关于Material 和 Texture的信息， 然后利用这些信息渲染物体。

I: 首先声明几个渲染需要的变量：网格， 材质， 纹理

    1 ID3DXMesh*                      Mesh = 0;   
    2 std::vector<D3DMATERIAL9>       Mtrls(0);   
    3 std::vector<IDirect3DTexture9*> Textures(0);

  

II. 然后导入一个.x 文件。 可以利用D3DXLoadMeshFromX接口， 得到Mesh

     1 HRESULT hr = 0;  
     2   
     3  // Load the XFile data.   
     4  ID3DXBuffer* adjBuffer  = 0;   
     5 ID3DXBuffer* mtrlBuffer = 0;   
     6 DWORD        numMtrls   = 0;  
     7 hr = D3DXLoadMeshFromX(    
     8     _T("bigship1.x"),   
     9     D3DXMESH_MANAGED,   
    10     Device,   
    11     &adjBuffer,   
    12     &mtrlBuffer,   
    13     0,   
    14     &numMtrls,   
    15     &Mesh);

  

III: 利用上得出的东西， 设置好Material 和 Texture

     1 if( mtrlBuffer != 0 && numMtrls != 0 )   
     2     {   
     3         D3DXMATERIAL* mtrls = (D3DXMATERIAL*)mtrlBuffer->GetBufferPointer();  
     4         for(int i = 0; i < numMtrls; i++)   
     5         {   
     6             // the MatD3D property doesn't have an ambient value set   
     7             // when its loaded, so set it now:   
     8              mtrls[i].MatD3D.Ambient = mtrls[i].MatD3D.Diffuse;  
     9             // save the ith material   
    10              Mtrls.push_back( mtrls[i].MatD3D );  
    11             // check if the ith material has an associative texture   
    12              if( mtrls[i].pTextureFilename != 0 )   
    13             {   
    14                 // yes, load the texture for the ith subset   
    15                 IDirect3DTexture9* tex = 0;   
    16                 LPCWSTR lpTmp = 0;   
    17                 lpTmp = ConvetASCIIToUnicode(mtrls[i].pTextureFilename);   
    18                 D3DXCreateTextureFromFile(   
    19                     Device,   
    20                     lpTmp,   
    21                     &tex);  
    22                 // save the loaded texture   
    23                 Textures.push_back( tex );   
    24             }   
    25             else   
    26             {   
    27                 // no texture for the ith subset   
    28                 Textures.push_back( 0 );   
    29             }   
    30         }  
    31 

  

IIII： 设置好做不系，摄像头，渲染参数等之后就可以显示模型了

    for(int i = 0; i < Mtrls.size(); i++)   
    {   
        Device->SetMaterial( &Mtrls[i] );   
        Device->SetTexture(0, Textures[i]);   
        Mesh->DrawSubset(i);   
    }   

  

posted @ 2010-09-10 20:34 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1823572) 收藏

##备注 
 @post in:2010-09-10 20:34