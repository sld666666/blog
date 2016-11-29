#
[union用法记录](http://www.cnblogs.com/sld666666/archive/2010/09/14/1825760.html)

union就是表示两个变量共用了同一块内存地址， 如果我们改变了其中一个，另一个也会随之改变。如现在我们想要一个3维的点，
我们既需要普通方式访问3个点，又需要索引方式访问。我们可以创建如下的结构体：

     1 struct point3{  
     2     union  
     3     {  
     4         struct{  
     5             float m_x, m_y,m_z;  
     6         };  
     7         float pPoint[3];  
     8     };  
     9       
    10   
    11     point3(){}  
    12     point3(float x, float y, float z) :   
    13         m_x(x), m_y(y), m_z(z)  
    14     {}  
    15 };

完成以后就有如下测试代码：

     1 int _tmain(int argc, _TCHAR* argv[])  
     2 {  
     3   
     4     point3 pt(1.0f, 2.0f, 3.0f);  
     5   
     6     cout<<pt.m_x<<' '<<pt.m_y<<' '<<pt.m_z<<endl;  
     7     cout<<pt.pPoint[0]<<' '<<pt.pPoint[1]<<' '<<pt.pPoint[2]<<endl;  
     8     pt.pPoint[0] = 3.0f;  
     9     pt.pPoint[1] = 2.0f;  
    10     pt.pPoint[2] = 1.0f;  
    11     cout<<pt.m_x<<' '<<pt.m_y<<' '<<pt.m_z<<endl;  
    12     cout<<pt.pPoint[0]<<' '<<pt.pPoint[1]<<' '<<pt.pPoint[2]<<endl;  
    13     return 0;  
    14 }

结果为： 1 2 3

1 2 3

3 2 1

3 2 1

posted @ 2010-09-14 11:50 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1825760) 收藏

##备注 
 @post in:2010-09-14 11:50