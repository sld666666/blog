#  [第一个QT程序](http://www.cnblogs.com/sld666666/archive/2010/12/03/1895846.html)

换了份新工作，然后发现新公司竟然用QT的，爽。

这篇文章是我对QT的学习总结。

## 1\. QT 安装

现在安装就是简单，只需鼠标点击一下就好了。

1\. 安装好 VS2008 + SP1补丁

2\. 下载安装qt-win-opensource-4.6.2-vs2008.exe

3\. 下载安装 qt-vs-addin-1.1.4.exe

Oh, It is so easy.

## 2\. 第一个QT程序。

QT 用起来太简单了， 直接上代码：

     1 #include <QApplication>  
     2 #include <QHBoxlayout>  
     3 #include <QSlider>  
     4 #include <QSpinBox>  
     5   
     6  int main(int argc, char *argv[])  
     7 {  
     8     QApplication app(argc, argv);  
     9   
    10     QWidget *window =  new QWidget;  
    11     window->setWindowTitle("Enter your Age");  
    12   
    13     QSpinBox *spinBox = new QSpinBox;  
    14     QSlider *slider = new QSlider(Qt::Horizontal);  
    15   
    16     QObject::connect(spinBox, SIGNAL(valueChanged(int)),  
    17                     slider, SLOT(setValue(int)));  
    18   
    19     QObject::connect(slider, SIGNAL(valueChanged(int)),  
    20                     spinBox, SLOT(setValue(int)));  
    21     spinBox->setValue(35);  
    22   
    23     QHBoxLayout * layout = new QHBoxLayout;  
    24     layout->addWidget(spinBox);  
    25     layout->addWidget(slider);  
    26     window->setLayout(layout);  
    27   
    28     window->show();  
    29       
    30     return app.exec();  
    31 }  
    32  

运行结果如下图所示：

![](http://pic002.cnblogs.com/images/2010/124057/2010120321232691.jpg)

以下是几个注意点：

1\. Qt基本上所有的UI类都是由QWidget继承出来。

2\. QT new一个控件不用释放，有种拉了屎不用擦屁股的快感。

3\. QT用QObject::connect函数来实现消息映射(QT的专业叫法叫：信号和槽)。

在 QObject::connect(spinBox, SIGNAL(valueChanged(int)),

slider,SLOT(setValue(int)));

中spinBox 发送valueChanged 的singal,而slider有slot来介绍这个消息，这个槽内部嵌的是setValue(int)。

4\. 类QHBoxLayout是专门用来水平布局用的。在QT中， 我们创建一个button的时候不能直接放

在widget上而是需要通过Qwidget::setLayout()来设置。

再附上程序中几个类的结构体：

![](http://pic002.cnblogs.com/images/2010/124057/2010120321393577.jpg)

## 3\. 小结

这个星期好累， 不想写东西， 这些不能算文章的只是习惯性的总结，记录一下而已。

posted @ 2010-12-03 21:30 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1895846) 收藏

##备注 
 @post in:2010-12-03 21:30