
# markdown使用数学公式

markdown用来写文章非常方便，但是碰到数学公式就麻烦了。 一般情况下用截图，但是太麻烦，这里介绍一个更坚定的方法： 使用MathJax引擎

## 使用MathJax引擎
MathJax使用起来非常方法只需要：

```
<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=default"></script>
```

如：

<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=default"></script>

```
$$x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}$$
\\(x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}\\)
```

$$x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}$$
\\(x=\frac{-b\pm\sqrt{b^2-4ac}}{2a}\\)


采用Tex写公式。$$公式$$表示行间公式，本来Tex中使用\(公式\)表示行内公式，但因为Markdown中\是转义字符，所以在Markdown中输入行内公式使用\\(公式\\)

## lex 常用数学公式
段落中的数学表达式应该置于 \( 和 \)， $ 和 $ 之间。

```
$$a$$ 
$c^{2} = a ^{2} + b^{2}$
```

$$a$$ 
$$c^{2} = a ^{2} + b^{2}$$

定义了各种数学函数：

```
$$
\lim_{n \to \infty}
\sum_{k=1}^n \frac{1}{k^2}
= \frac{\pi^2}{6}
$$
```

$$
\\lim_{n \\to \\infty}
\\sum_{k=1}^n \\frac{1}{k^2}
= \\frac{\pi^2}{6}
$$

```
$$
x^{2}\geq 0
$$
```

$$
x^{2}\\geq 0
$$