#  [OpenGl And 视图](http://www.cnblogs.com/sld666666/p/3810991.html)

# OpenGl And 视图

标签（空格分隔）： game

## 简介

本文主要介绍坐标系的观念， 以及在openGL中的视图及其相关的变换。

## 大纲

  * 视图、模型、投影变换概念
  * Opengl中对各种变换的支持

## 视图变换

在一个三维坐标系中， 一个模型的展示并不只有一个视图就可以了。而是存在视图变换，投影变换，模型变换的过程。

    顶点坐标---》（模型视图矩阵）----》视觉坐标---》（投影矩阵）----》裁剪坐标----》（透视坐标）----》规范化设备坐标----》视口变换----》窗口坐标

在opengl中主要通过以下三种变换达到最终控制屏幕输出：

  1. 视图变换： 视图是指摄像机能看到到区域，所以视图变换就是移动摄像机使其说能展示的区域发生变换
  2. 模型变换：是场景中的模型动起来，主要是针对模型的
  3. 投影变换： 决定了模型最终能在屏幕中的现实， 比如通过投影变换， 可以只让模型只显示一半。

为了进行视图，模型，投影变换， 可以创建一个4*4的矩阵M然后让他与顶点对应的坐标v相乘，以实现变换：  
$$v' = Mv$$

## OpenGl 中对视图、模型、投影变换的支持

### gult库

gl库只是一个图形库， 在我们写的窗口程序的过程中，我必须写窗口程序已经消息处理，我们可以自己写
，当然也可以用gult库。glut库中的api都已glut开头，最典型的现实一个窗口如下：

       glutInit(&argc, argv);
       glutInitDisplayMode (GLUT_DOUBLE | GLUT_RGB);
       glutInitWindowSize (500, 500); 
       glutInitWindowPosition (100, 100);
       glutCreateWindow (argv[0]);
       glutDisplayFunc(display); 
       glutReshapeFunc(reshape);
       glutKeyboardFunc(keyboard);
       glutMainLoop();

代码很简单，而我们要关注的是三个函数：

  1. diplay 模型的展示， 内部主要做模型变换
  2. reshage 视口的控制， 主要做视图和投影变换
  3. keyboard 对键盘输入的支持

### 引出

我们可以用如下代码创建一个球体

        void PlanteDisplay::excute()
        {
          glClear(GL_COLOR_BUFFER_BIT);
          glColor3f(1.0, 1.0, 1.0);
          drawSun();
        }
        void PlanteDisplay::drawSun()
        {
          glutWireSphere(1.0, 20, 16);   /* draw sun */
          glutSwapBuffers();
        }

我们可以加上一些初始化的动作

        void init()
        {
          glClearColor(0.0, 0.0, 0.0, 0.0);
          glShadeModel(GL_FLAT);
        }

我们会发现有如下所示的球体：

![nomral_sphear](http://sld666666bblogimage.qiniudn.com/nomral_sphear.png)

我们创见好了物体， 但是还没有设置好摄像机，opengl默认情况下回把摄像机正对着屏幕，而摄像机的镜头设置就在reshage中实现，在该函数中，
我们主要控制视图变换和投影变换。

### 视口变换

视口变换决定了场景所映射的有效屏幕区域的形状。利用函数glViewport来实现。

最简单的变换如下：

          void cubeReshape(int w, int h)
          {
            glViewport(0, 0, (GLsizei)w, (GLsizei)h);
          }

![glviewport](http://sld666666bblogimage.qiniudn.com/opengl_and_view_noview.jp
g)

我们改变屏幕的裁剪区域(投影变换)：

        glViewport(50, 0, (GLsizei)w/2, (GLsizei)h/2)

会发现如下图显示：

![glviewport_half](http://sld666666bblogimage.qiniudn.com/opengl_and_view_glVi
ewport_half.jpg)

### 模型变换

调用变换函数之前，需要确定自己想修改的模型视图矩阵还是投影视图矩阵，可以用glMatrixMode() 选择矩阵，并用glLoadIdentity()
把当前可修改矩阵清除为单位矩阵。

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();

在OpenGl中，有三个函数用于执行模型变换：

  1. glTranslate*()
  2. glRotate*()
  3. glScale*()

如：

    glTranslatef(0.2, .2, 0);
    glScalef(1.5, 2.0, 1);
    glutWireSphere(0.4, 50, 8);

![model_view_scale](http://sld666666bblogimage.qiniudn.com/model_view_sacale.p
ng)

### 视图变换

视图 变换拥有膝盖观察点的位置和方向。可以由两种方法来实现：

  1. 反方向移动物体
  2. 使用gluLookAt()函数

正常如图所示：

![teampot_normal](http://sld666666bblogimage.qiniudn.com/teapot_normal.png)

    gluLookAt(0.0, 0.0, 0.0
            , 0.0, 0.0, -1.0
            , -1.0, -1.0, 0.0);

![teapot_y=-1](http://sld666666bblogimage.qiniudn.com/teapot_y=-1.png)

    gluLookAt(0.0, 0.0, 0.0
            , 0.0, 0.0, -1.0
            , 0.0, -1.0, 0.0);

![teaapot_y=-1x=-1](http://sld666666bblogimage.qiniudn.com/teampot_y=-1x=-1.pn
g)

### 投影变换

投影变换最终控制线上屏幕的裁剪区域， opengl中可以用：

  1. glOrtho
  2. glFrustum

来进行投影变换。

#### glOrtho 和 glFrustum 的区别

  1. glOrtho:创建一个正交平行的视景体。 一般用于物体不会因为离屏幕的远近而产生大小的变换的情况。比如，常用的工程中的制图等。需要比较精确的显示
  2. glFrustum 产生一个透视投影。这是一种模拟真是生活中，人们视野观测物体的真实情况。例如：观察两条平行的火车到，在过了很远之后，这两条铁轨是会相交于一处的。还有，离眼睛近的物体看起来大一些，远的物体看起来小一些。
    glFrustum(GLdouble left
            , GLdouble right
            , GLdouble bottom
            , GLdouble top
            , GLdouble near
            , GLdouble far)

![glFrustum_view]()  
其中： (left, botton, -near)表示近侧裁剪平面左上角的坐标，(right, top,
-near)表示右下角坐标，near和far分别表示观察点到近侧和远侧的距离。

posted @ 2014-06-26 22:34 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=3810991) 收藏

##备注 
 @post in:2014-06-26 22:34