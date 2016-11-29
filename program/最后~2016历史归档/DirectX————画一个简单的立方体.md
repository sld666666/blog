#  [DirectX————画一个简单的立方体](http://www.cnblogs.com/sld666666/archive/2010/08/22/
1805856.html)

通常，如果我们需要画一个立方体的话需要做什么？可以想象一下我们需要如下叫几个步骤：

(1). 得到一个画布

(2). 准备好需要画相关的点

(3). 准备好摄像机

(4).用2维图像来表现3维效果

(5). 连接这些点 画出一个立方体

# 1\. 得到画布

> D3D中， 所有画图的操作都是在IDirect3DDevice9 中的， 所以我们就可以

>

> 这样声明： IDirect3DDevice9* Device = 0;

>

> 出生化之(略)

# 2\. 准备好相关的点

D3D中， 花点用了两个Buffer， vertex buffer 用来保存相关的点的值，

index buffer 用来保存点之间连接的索引。

首先我们可以这样声明一个结构体：

    struct	Vertex
    {
    	Vertex(){}
    	Vertex(float x, float y, float z)
    	{
    		m_x = x; 
    		m_y = y;
    		m_z = z;
    	}
    
    	float m_x, m_y, m_z;
    	static const DWORD FVF;
    };
    
    const DWORD Vertex::FVF = D3DFVF_XYZ;

然后利用 CreateVertexBuffer 和 CreateIndexBuffer 创建 vertex buffer 和 index buffer

    	Device->CreateVertexBuffer(
    		8 * sizeof(Vertex), 
    		D3DUSAGE_WRITEONLY,
    		Vertex::FVF,
    		D3DPOOL_MANAGED,
    		&VB,
    		0);
    
    	Device->CreateIndexBuffer(
    		36 * sizeof(WORD),
    		D3DUSAGE_WRITEONLY,
    		D3DFMT_INDEX16,
    		D3DPOOL_MANAGED,
    		&IB,
    		0);

最后填充vertex buffer 和 index buffer。

我们得到了相关的vertex buffer 和index buffer之后， 我们就去为这些点赋值。

我们用Lock函数得到相应的内存位置，复制完成以后用Unlock 释放.

    	Vertex* vertices;
    	VB->Lock(0, 0, (void**)&vertices, 0);
    
    	// vertices of a unit cube
    	vertices[0] = Vertex(-1.0f, -1.0f, -1.0f);
    	vertices[1] = Vertex(-1.0f,  1.0f, -1.0f);
    	vertices[2] = Vertex( 1.0f,  1.0f, -1.0f);
    	vertices[3] = Vertex( 1.0f, -1.0f, -1.0f);
    	vertices[4] = Vertex(-1.0f, -1.0f,  1.0f);
    	vertices[5] = Vertex(-1.0f,  1.0f,  1.0f);
    	vertices[6] = Vertex( 1.0f,  1.0f,  1.0f);
    	vertices[7] = Vertex( 1.0f, -1.0f,  1.0f);
    
    	VB->Unlock();
    
    	// define the triangles of the cube:
    	WORD* indices = 0;
    	IB->Lock(0, 0, (void**)&indices, 0);
    
    	// front side
    	indices[0]  = 0; indices[1]  = 1; indices[2]  = 2;
    	indices[3]  = 0; indices[4]  = 2; indices[5]  = 3;
    
    	// back side
    	indices[6]  = 4; indices[7]  = 6; indices[8]  = 5;
    	indices[9]  = 4; indices[10] = 7; indices[11] = 6;
    
    	// left side
    	indices[12] = 4; indices[13] = 5; indices[14] = 1;
    	indices[15] = 4; indices[16] = 1; indices[17] = 0;
    
    	// right side
    	indices[18] = 3; indices[19] = 2; indices[20] = 6;
    	indices[21] = 3; indices[22] = 6; indices[23] = 7;
    
    	// top
    	indices[24] = 1; indices[25] = 5; indices[26] = 6;
    	indices[27] = 1; indices[28] = 6; indices[29] = 2;
    
    	// bottom
    	indices[30] = 4; indices[31] = 0; indices[32] = 3;
    	indices[33] = 4; indices[34] = 3; indices[35] = 7;
    
    	IB->Unlock();

# 3\. 准备好摄像机

    	D3DXVECTOR3 position(0.0f, 0.0f, -5.0f);
    	D3DXVECTOR3 target(0.0f, 0.0f, 0.0f);
    	D3DXVECTOR3 up(0.0f, 1.0f, 0.0f);
    	D3DXMATRIX V;
    	D3DXMatrixLookAtLH(&V, &position, &target, &up);
    
    	Device->SetTransform(D3DTS_VIEW, &V);
    

# 4\. 用2维图像来表现3维效果

设置 projection:

    	D3DXMATRIX proj;
    	D3DXMatrixPerspectiveFovLH(
    		&proj,
    		D3DX_PI * 0.5f, // 90 - degree
    		(float)Width / (float)Height,
    		1.0f,
    		1000.0f);
    	Device->SetTransform(D3DTS_PROJECTION, &proj);
    

设置渲染模式:

    Device->SetRenderState(D3DRS_FILLMODE, D3DFILL_WIREFRAME);

# 5\. 开始画了

当一切准备就绪之后就可以用vertex buffer 和index buf 中的内容画三角形了。

但是要画东西出来却需要遵循一下三步：

(1).把 vertex buffer 中的内容放到一个 stream 中， 这是stream会最终把几何图型渲染成为图像。

相应的函数是SetStreamSource

(2).设置点的格式， 利用SetFVF函数

(3).设置 index buffer， 调用的是函数 SetIndices

在开始之前， 我们把屏幕背景填充成白色：

    Device->Clear(0, 0, D3DCLEAR_TARGET | D3DCLEAR_ZBUFFER, 0xffffffff, 1.0f, 0);

然后就开始画 立方体：

    		Device->BeginScene();
    
    		Device->SetStreamSource(0, VB, 0, sizeof(Vertex));
    		Device->SetIndices(IB);
    		Device->SetFVF(Vertex::FVF);
    
    		// Draw cube.
    		Device->DrawIndexedPrimitive(D3DPT_TRIANGLELIST, 0, 0, 8, 0, 12);
    
    		Device->EndScene();
    		Device->Present(0, 0, 0, 0);

好了这样一个简单的立方体就出来了。

![](http://pic002.cnblogs.com/img/sld666666@gmail.com/201008/2010082217460527.
png)

posted @ 2010-08-22 17:33 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1805856) 收藏

##备注 
 @post in:2010-08-22 17:33