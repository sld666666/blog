#  [Effects](http://www.cnblogs.com/sld666666/archive/2010/09/21/1832833.html)

## 1.1 Effect Files

Effect file 就是一个.fx文件。constant
buffers可以用来保存”全局的”变量然后把这些变量交由着色器。而这个效果就是写在.fx文件中的。当然如果一个Effect
file要能够被着色器和constant buffer使用，其至少要包含一个technique。而一个technique至少包含一个通道(pass)。

. technique 10: 一个technique 包含了一个或多个通道来穿件特殊的渲染效果。在每一个通道里面，一个几何体被不同的渲染方式渲染。而各个通
道不同的效果能够组合最终被显示出来。例如，一个地形technique也许使用了多个通道纹理techique。需要注意的一点是多通道technique代价比较
昂贵，因为一个几何体在每一个通道中都被渲染了。

. pass: 一个通道由顶点着色器，几何着色器，像素着色器以及渲染的状态(render states)组成。

下面的代码及时一个effect files 的例子。

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
    15 float4 PS(float4 posH  : SV_POSITION,   
    16           float4 color : COLOR) : SV_Target   
    17 {   
    18     return color;   
    19 }  
    20 technique10 ColorTech   
    21 {   
    22     pass P0   
    23     {   
    24         SetVertexShader( CompileShader( vs_4_0, VS() ) );   
    25         SetGeometryShader( NULL );   
    26         SetPixelShader( CompileShader( ps_4_0, PS() ) );   
    27     }   
    28 }

## 2\. 创建一个effect

在D3D中，一个effect被ID3D10Effect接口描述被D3DX10CreateEffectFromFile创建。

     1     HRESULT D3DX10CreateEffectFromFile(   
     2     LPCTSTR pFileName,    //.fx文件的名字   
     3      CONST D3D10_SHADER_MACRO *pDefines,//未使用，至0   
     4      ID3D10Include *pInclude, // 未使用, 至0   
     5      LPCSTR pProfile,//fx的版本，10.0 是"fx_4_0",   
     6      UINT HLSLFlags,//标志.fx文件是怎么被编译的   
     7     UINT FXFlags, //未使用，至0   
     8     ID3D10Device *pDevice,//Device   
     9     ID3D10EffectPool *pEffectPool,//内存池   
    10     ID3DX10ThreadPump *pPump,//   
    11     ID3D10Effect **ppEffect,//这个就是我们创建的effct   
    12     ID3D10Blob **ppErrors);   
    13     HRESULT *pttResult

## 3\. 把Effect 和C++程序联系起来。

通常情况下，一个C++程序需要更新constant buffers。例如在effct 文件中我们有如下的constant buffer。

    1 cbuffer cbPerObject   
    2 {   
    3     float4x4 gWVP;   
    4     float4 gColor;   
    5     float gSize;   
    6     int gIndex;   
    7     bool gOptionOn;   
    8 };

通过ID3D10Effect 接口，我们能够获取指向constant buffer的指针。

     1 ID3D10EffectMatrixVariable* fxWVPVar;   
     2 ID3D10EffectVectorVariable* fxColorVar;   
     3 ID3D10EffectScalarVariable* fxSizeVar;   
     4 ID3D10EffectScalarVariable* fxIndexVar;   
     5 ID3D10EffectScalarVariable* fxOptionOnVar;   
     6 fxWVPVar      = mFX->GetVariableByName("gWVP")->AsMatrix();   
     7 fxColorVar    = mFX->GetVariableByName("gColor")->AsVector();   
     8 fxSizeVar     = mFX->GetVariableByName("gSize")->AsScalar();   
     9 fxIndexVar    = mFX->GetVariableByName("gIndex")->AsScalar();   
    10 fxOptionOnVar = mFX->GetVariableByName("gOptionOn")->AsScalar();

ID3D10Effect::GetVariableByName接口返回一个指向ID3D10EffectVariable形式的指针。但是ID3D10Effec
tVariable是一个通用的effct 形式。所以我们用As*****方法转化为具体的形式。

一旦我们得到了这些地址，我们就可以给他们赋值了。

    1 fxWVPVar->SetMatrix( (float*)&M ); // assume M is of type D3DXMATRIX   
    2 fxColorVar->SetFloatVector( (float*)&v ); // assume v is of type   
    3 // D3DXVECTOR4   
    4 fxSizeVar->>SetFloat( 5.0f );   
    5 fxIndexVar->SetInt( 77 );   
    6 fxOptionOnVar->SetBool( true );

technique是effect渲染几何图形的载体。我们可以按以下方式创建一个technique

    1 ID3D10EffectTechnique* mTech;  
    2 mTech = mFX->GetTechniqueByName("ColorTech");

## 4\. 利用effect 渲染

在利用technique渲染之前，我们必须constant buffers中的数据都被更新了。然后我们要做的就是循环所有的technique
pass，apply这个通道，然后花几何图形。

    1 D3D10_TECHNIQUE_DESC techDesc;   
    2 mTech->GetDesc( &techDesc );   
    3 for(UINT p = 0; p < techDesc.Passes; ++p)   
    4 {   
    5     mTech->GetPassByIndex( p )->Apply(0);  
    6     mBox.draw();   
    7 }

posted @ 2010-09-21 20:37 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1832833) 收藏

##备注 
 @post in:2010-09-21 20:37