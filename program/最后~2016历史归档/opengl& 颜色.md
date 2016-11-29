#  [opengl& 颜色](http://www.cnblogs.com/sld666666/p/3812425.html)

## 颜色

### 简介

颜色的显示模式分为两种：

  1. RGBA显示模式
  2. 颜色索引显示模式（使用颜色映射表，映射表提供了索引，可以混合基本的红，绿，蓝色值）。  
RGBA模式可以选择的颜色数量多于颜色索引模式。一般而言，尽量选择RGBA模式，而颜色索引模式主要用在纹理贴图以及光照，着色，雾和抗锯齿中。

### opengl指定颜色和着色模型

有如下伪代码：

    setColor(RED)
    drawItem(A)

这样画出来的A就是红色的了。  
RGBA模式下可以用glColor*()选择当前颜色：

  1. void glColor3(Type r, Type g, Type b);
  2. void glColor4(Type r, Type b, Type b, Type a);

颜色索引模式下可以用glIndex*(Type c)来指定颜色。

glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);

glClearColor(0.0, 0.0, 0.0, 0.0);

glShadeModel(GL_SMOOTH);

void TwoDDispaly::colorDisplay()  
{  
glClear(GL_COLOR_BUFFER_BIT);  
glBegin(GL_TRIANGLES);  
glColor3f(1.0, 0.0, 0.0);  
glVertex2f(5.0, 5.0);  
glColor3f(0.0, 1.0, 0.0);  
glVertex2f(25.0, 5.0);  
glColor3f(0.0, 0.0, 1.0);  
glVertex2f(5.0, 25.0);  
glEnd();  
glFlush();  
}

![opengl_color](http://sld666666bblogimage.qiniudn.com/opengl_color.png)

其中glShadeModel(GL_SMOOTH);指定着色模型， 其中GL_SMOOTH表示可以用多种颜色进行绘制，而GL_FLAT只能用单色。

posted @ 2014-06-27 17:26 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=3812425) 收藏

##备注 
 @post in:2014-06-27 17:26