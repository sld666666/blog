# numpy 和矩阵
python 中 numpy中包含很多关于矩阵运算的包。 这篇文章是对其中矩阵运算的用法的介绍。我们可以认为numpy就是计算器。

## 初始化

    ```
    import numpy as np

    def getData():
        m = np.matrix([[1,-2,3],[0,4,5],[7,8,-9]])
        return m
    ```
## 解一元线性方程
```
x + 2y +  z = 7
2x -  y + 3z = 7
3x +  y + 2z =18
```

```
def test_linalg_solve():
    A = np.array([[1, 2, 1], [2, -1, 3], [3, 1, 2]])
    B = np.array([7, 7, 18])
    x = np.linalg.solve(A, B)
    print(x)
```

```
[ 7.  1. -2.]
```
## 矩阵加法
 只有唯独相同的才可以加法

    ```
 def test_add():
    A =np.matrix([[ 4 , 0 , 5],[-1 , 3 , 2]])

    B = np.matrix([[1, 1, 1],[3, 5, 7]])

    C = np.matrix([[2, -3],[0, 1]])
    print ("A:")
    print(A)
    print("B")
    print(B)
    print("A+B:")
    print(A + B)

    print("A+C:")
    try:
        print(A+C)
    except Exception as e:
        print(e)

    ``` 

    结果：

    ```
    A:
    [[ 4  0  5]
     [-1  3  2]]
    B
    [[1 1 1]
     [3 5 7]]
    A+B:
    [[5 1 6]
     [2 8 9]]
    A+C:
    operands could not be broadcast together with shapes (2,3) (2,2) 
    ```  

## 矩阵乘法

矩阵的点积(AB)和矩阵乘积(A*B)是两回事情.
    
```
def test_mult():
    print("CB:")
    print(np.dot(C, B))

    print("A*D")
    print(A*D)
```

```
CB:
[[ -7 -13 -19]
 [  3   5   7]]
A*D
[[23 -7]
 [ 4  8]]

```
## 矩阵的转置
用A(T)表示。
```
def test_t():
    print(A)
    print(A.T)
    print(A.T.T)
```

```
[[ 4  0  5]
 [-1  3  2]]
[[ 4 -1]
 [ 0  3]
 [ 5  2]]
[[ 4  0  5]
 [-1  3  2]]

```

## 矩阵的逆
AC=I， 如果I为单位矩阵，则称为C为A的逆。
```
def test_inv():
    print(C)
    tmp = np.linalg.inv(C)
    print(tmp)

    print(np.dot(C, tmp))
```

```
[[ 2 -3]
 [ 0  1]]
[[ 0.5  1.5]
 [ 0.   1. ]]
[[ 1.  0.]
 [ 0.  1.]]
```