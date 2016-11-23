
# plot.hist参数详解

hist函数用来放直方图。 文档比较详细，但是对各个参数的含义缺乏背景感念， 总结记录下。

host的定义如下

```
def hist(x, bins=10, range=None, normed=False, weights=None, cumulative=False,
         bottom=None, histtype='bar', align='mid', orientation='vertical',
         rwidth=None, log=False, color=None, label=None, stacked=False,
         hold=None, data=None, **kwargs):
```
比较复杂，下面来一个一个解释：

## x
是一个一维或者二维数组。 表示每一个柱形起始点的位置。如：

  ```
  x = [1, 2, 3,4,5]
  plt.hist(x, len(x))
  ```
  ![hist_x_define](https://img.alicdn.com/imgextra/i4/46754672/TB2No9behaK.eBjSZFAXXczFXXa_!!46754672.png)
## bins
有多少个柱子。 他们的间隔。 每隔柱子放哪些值。

  ```
  x = [1, 2,4]
  bins = [0, 1, 2, 4, 10]
  weights = [1,2,3]
  plt.hist(x, bins = bins, weights=weights)
  plt.show()
  ```
  ![hist_bins_define](https://img.alicdn.com/imgextra/i1/46754672/TB2_T9Jek1M.eBjSZPiXXawfpXa_!!46754672.png)
## range
柱子的上下范围。,数组

## weights
柱子的y值

  ```
  x = [1, 2, 3,4,5]
  weights  = [1, 1,2,3,3]
  plt.hist(x, len(x), weights= weights)
  plt.show()
  ```
 ![hist_weight_define](https://img.alicdn.com/imgextra/i4/46754672/TB2TgOseiKO.eBjSZPhXXXqcpXa_!!46754672.png)

 ## bottom
 从哪里开始，一般不用设置

 ## histtype
 {‘bar’, ‘barstacked’, ‘step’, ‘stepfilled’}, optional
