#  [Direct3D学习笔记](http://www.cnblogs.com/sld666666/archive/2010/08/19/1803974.
html)

1\. 初始化

> a. 获得IDirect3D9 接口的指针

> b. 检查设备（显卡）的能力， D3DCAPS9

> c. 初始化D3DPRESENT_PARAMETERS结构，这个结构是为下一步服务的

> d. 基于D3DPRESENT_PARAMETERS结构，创建IDirect3DDevice9

相关代码：

     IDirect3DDevice9* Device = 0; 
        HRESULT hr = 0;
    
    	// Step 1: Create the IDirect3D9 object.
    
    	IDirect3D9* d3d9 = 0;
        d3d9 = Direct3DCreate9(D3D_SDK_VERSION);
    
        if( !d3d9 )
    	{
    		::MessageBox(0, _T("Direct3DCreate9() - FAILED"), 0, 0);
    		return false;
    	}
    
    	// Step 2: Check for hardware vp.
    
    	D3DCAPS9 caps;
    	d3d9->GetDeviceCaps(D3DADAPTER_DEFAULT, deviceType, &caps);
    
    	int vp = 0;
    	if( caps.DevCaps & D3DDEVCAPS_HWTRANSFORMANDLIGHT )
    		vp = D3DCREATE_HARDWARE_VERTEXPROCESSING;
    	else
    		vp = D3DCREATE_SOFTWARE_VERTEXPROCESSING;
    
    	// Step 3: Fill out the D3DPRESENT_PARAMETERS structure.
     
    	D3DPRESENT_PARAMETERS d3dpp;
    	d3dpp.BackBufferWidth            = 640;
    	d3dpp.BackBufferHeight           = 480;
    	d3dpp.BackBufferFormat           = D3DFMT_A8R8G8B8;
    	d3dpp.BackBufferCount            = 1;
    	d3dpp.MultiSampleType            = D3DMULTISAMPLE_NONE;
    	d3dpp.MultiSampleQuality         = 0;
    	d3dpp.SwapEffect                 = D3DSWAPEFFECT_DISCARD; 
    	d3dpp.hDeviceWindow              = hwnd;
    	d3dpp.Windowed                   = windowed;
    	d3dpp.EnableAutoDepthStencil     = true; 
    	d3dpp.AutoDepthStencilFormat     = D3DFMT_D24S8;
    	d3dpp.Flags                      = 0;
    	d3dpp.FullScreen_RefreshRateInHz = D3DPRESENT_RATE_DEFAULT;
    	d3dpp.PresentationInterval       = D3DPRESENT_INTERVAL_IMMEDIATE;
    
    	// Step 4: Create the device.
    
    	hr = d3d9->CreateDevice(
    		D3DADAPTER_DEFAULT, // primary adapter
    		D3DDEVTYPE_HAL,         // device type
    		hwnd,               // window associated with device
    		vp,                 // vertex processing
    	    &d3dpp,             // present parameters
    	    device);            // return created device
             d3dpp.Release();

上面代码的执行结果就是生成一个窗口， 得到了一个IDirect3DDevice9。

注：这里省略了Windows 32窗体的生成过程

2\. 画面的现实

> 在上面我们得到了一个IDirect3DDevice9, 我们就可以用此device进行画图等操作。

>  
>  
>     bool Display()

>     {

>       if( Device ) // Only use Device methods if we have a valid device.

>       {

>               // Instruct the device to set each pixel on the back buffer
black -

>               // D3DCLEAR_TARGET: 0x00000000 (black) - and to set each pixel
on

>               // the depth buffer to a value of 1.0 - D3DCLEAR_ZBUFFER:
1.0f.

>               Device->Clear(0, 0, D3DCLEAR_TARGET | D3DCLEAR_ZBUFFER,
0x00000000, 1.0f, 0);

>

>               // Swap the back and front buffers.

>               Device->Present(0, 0, 0, 0);

>       }

>       return true;

>     }

>

>  

这样一个可以画图的device就形成了

posted @ 2010-08-19 21:43 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1803974) 收藏

##备注 
 @post in:2010-08-19 21:43