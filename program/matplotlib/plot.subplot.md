
# subplot
一个绘图对象(figure)可以包含多个轴(axis)，在Matplotlib中用轴表示一个绘图区域，可以将其理解为子图.

  ```
  subplot(numRows, numCols, plotNum)
  ```
subplot将整个绘图区域等分为numRows行 * numCols列个子区域，然后按照从左到右，从上到下的顺序对每个子区域进行编号，左上的子区域的编号为1。如果numRows，numCols和plotNum这三个数都小于10的话，可以把它们缩写为一个整数，例如subplot(323)和subplot(3,2,3)是相同的

如：

    ```
    def draw(self, point1, point2):
      fig = plt.figure()
      ax1 = fig.add_subplot(2,2,1)
      ax2 = fig.add_subplot(2,2,4)
      plt.show()

    ```

![subplot_01](https://img.alicdn.com/imgextra/i2/46754672/TB2axaDeRyN.eBjSZFgXXXmGXXa_!!46754672.png)


利用 spines 函数可以控制坐标轴的中心位置：

  ```
  ax01.spines['left'].set_position('center')
  ax01.spines['bottom'].set_position('center')
  ```

设置刻度：

```
ax.set_xlim([-10, 11])
ax.set_ylim([-10, 11])
```
## Axes容器
matplotlib API包含有三层：

  1. backend_bases.FigureCanvas : 图表的绘制领域
  2. backend_bases.Renderer : 知道如何在FigureCanvas上如何绘图
  3. artist.Artist : 知道如何使用Renderer在FigureCanvas上绘图

FigureCanvas和Renderer需要处理底层的绘图操作，例如使用wxPython在界面上绘图，或者使用PostScript绘制PDF。Artist则处理所有的高层结构，例如处理图表、文字和曲线等的绘制和布局。通常我们只和Artist打交道，而不需要关心底层的绘制细节

直接使用Artists创建图表的标准流程如下：

  1. 创建Figure对象
  2. 用Figure对象创建一个或者多个Axes或者Subplot对象
  3. 调用Axies等对象的方法创建各种简单类型的Artists

    ```
    def draw(self, point1, point2):
      fig = plt.figure()
      ax01 = fig.add_axes([0.15, 0.1, 0.7, 0.3])
      plt.show()
    ```

    ![](https://img.alicdn.com/imgextra/i4/46754672/TB2NNEZdH1K.eBjSszbXXcTHpXa_!!46754672.png)
add_axes的参数是一个形如[left, bottom, width, height]的列表，这些数值分别指定所创建的Axes对象相对于fig的位置和大小，取值范围都在0到1之间

### Axis容器

Axis容器包括坐标轴上的刻度线、刻度文本、坐标网格以及坐标轴标题等内容. 可以认为真正作图都是在Axis上的。

```

import matplotlib.pyplot as plt
import numpy as np

class VectorAddition:

    def draw(self, point1, point2, point3):
        fig = plt.figure()
        ax01 = self._getAxes(fig)
        ax01.arrow(0,0,point1[0],point1[1], head_width=0.3, head_length=0.8, fc='k', ec='k')
        ax01.arrow(0,0,point2[0],point2[1], head_width=0.3, head_length=0.8, fc='k', ec='k')
        ax01.arrow(0,0,point3[0],point3[1], head_width=0.3, head_length=0.8, fc='k', ec='k')

        ax01.plot([point1[0], point3[0]], [point1[1], point3[1]], 'r--')
        ax01.plot([point2[0], point3[0]], [point2[1], point3[1]], 'r--')

        ax01.text(point3[0], point3[1]*3/5, "v")
        ax01.text(point3[0]*3/5, point3[1], "m")

        plt.show()

    def _getAxes(self, fig):
        ax = fig.add_subplot(111)
        ax.set_title('centered spines')
        ax.spines['left'].set_position('center')
        ax.spines['bottom'].set_position('center')

        ax.set_xlim([-5, 5])
        ax.set_ylim([-5, 5])

        axis = ax.xaxis
        print(axis.get_ticklocs())
        for label in axis.get_ticklabels():
            label.set_color("red")

        return  ax

if __name__=="__main__":
    additon = VectorAddition()
    v0 = np.array([-1, 2])
    v1 = np.array([4, 2])

    point0 = list(v0)
    point1 = list(v1)
    pointResult = list(v0 + v1)
    print(pointResult)
    additon.draw(point0, point1, pointResult)
    pass
```

![](https://img.alicdn.com/imgextra/i2/46754672/TB2LmySeQWM.eBjSZFhXXbdWpXa_!!46754672.png)
