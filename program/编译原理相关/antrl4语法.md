# antrl4语法

## 背景
介绍如何用antrl4的语法实现普通的程序语言的逻辑控制的模式。


## 模式： 序列
如一个n维度向量: [1 2 3 4], 后面是可能会出现多个INT，我们用*号来表示：

```
grammar Vector;
prog: '[' value ']';
value: (INT)*;
INT : [0-9]+;
```
![grammar_vector](https://img.alicdn.com/imgextra/i2/46754672/TB2W2yyX4BmpuFjSZFDXXXD8pXa_!!46754672.png)

如我们还可以这样来识别java中的多个句子

```
stats : (stats ';') *
```

可以用这样来识别java中的函数入参:

```
argsList : args(',' args)*
```

##　模式：　选择
用　｜　表示or: 

```
field : INT | STRING

type : 'void' | 'float' | 'int'
```

## 模式： token 一定

定义了一个token可以在其他地方依赖他。

```
func: (args)*
args: str
```

## 模式： 递归

```
stat : 'while (' args ')''{' stat* '}''
```

## 优先级，左递归，结合律

## 认识语法结构

## 解析json
