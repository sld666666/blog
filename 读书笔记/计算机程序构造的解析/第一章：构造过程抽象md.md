#计算机程序构造的解析----构造过程抽象
##程序设计的基本元素
1. 基本表达形式
2. 组合的方法
3. 抽象的方法

### 表达式
表达式：表示基本过程的表达形式， 如： 486， "hello world"

组合式：把表达式组合起来， 如（+ 4 2）

运算符： 如上的“+”，当然也可以扩展， min， abs
###命名和变量
变量： (define size 2), 其中define是最简单的抽象方法。
###定义过程
格式(define (<name> <parameters>) (body)), 人员：(define (square x)(* x x))
###条件跳的是
格式是：
	
	（cond(<p1> <e1>)
		  (<p2> <e2>)）
如:
	
	((define (abs x)
  		(cond ((> x 0) x)
        ((= x 0) 0)
        ((< x 0) (- x))
        )))
也可以 （cond(<p1> <e1>)
		   ((else <e2>))）

当然还有其他关键字配合使用： and, or, not
###递归
	;阶层
	(define (factorial n)
	  (if (= n 1)
	      1
	      (* n (factorial (- n 1)))))
	
	(factorial 10)
##利用高阶函数做抽象
如果一个函数只能以数作为参数， 那显然严重地现在了我们的抽象能力。构造出这样的一个函数， 可以以函数作为参数或者以函数作为返回， 这样的函数称为高阶函数。
###过程作为参数
	; sum f(n) = f(0) + ... + f(n);
	(define (sum term a next b)
	  (if (> a b)
	      0
	      (+ (term a)
	         (sum (next a) b))))
	(define (inc n) (+ n 1))
	
	(define (sum-cubes-1 a b)
	  (sum cube a inc b))
	
	(sum-cubes 1 10)
###lambda构造过程
lambda又叫匿名函数。利用lambda我们就可以对 inc函数做改造：

	(lambda (n) (+ n 1))

	(define (sum-cubes-2 a b)
 	 (sum cube a (lambda(n)(+ n 1)) b))
	(sum-cubes 1 10)