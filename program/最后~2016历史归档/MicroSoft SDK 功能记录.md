#  [MicroSoft SDK
功能记录](http://www.cnblogs.com/sld666666/archive/2010/08/01/1789939.html)

1\. 判断某一按键是否按下或者弹起

函数 GetAsyncKeyState， 在按键不按下返回为0X0， 即0000 0000 0000 0000 0000 0000 0000 0000

在按下时返回0xffff8001 即1111 1111 1111 1111 1000 0000 0000 0001

0x8000 即0000 0000 0000 0000 1000 0000 0000 0000

GetAsyncKeyState(VK_LSHIFT) & 0x8000 返回0x1 即0000 0000 0000 0000 1000 0000 0000
0000

0x8000 的加入是为了屏蔽掉其他可能的状态

所以就有了如下代码：

    //key down
    inline bool KeyDown( int VK_Code)
    {
    	return (GetAsyncKeyState(VK_Code) & 0x8000) ? true : false;
    }
    
    //key up
    inline bool KeyUp(int VK_Code)
    {
    	return (GetAsyncKeyState(VK_Code) & 0x800) ? false : true;
    }
    2. 得到当前目录
    WCHAR path[MAX_PATH] = {0};
    GetModuleFileName(NULL,path,MAX_PATH); 
    CStringszPath = path;
    szPath = szPath.Left(szPath.ReverseFind(_T('\\')));

posted @ 2010-08-01 15:20 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1789939) 收藏

##备注 
 @post in:2010-08-01 15:20