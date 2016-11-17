#  [QT
正则表达式](http://www.cnblogs.com/sld666666/archive/2011/03/16/1985715.html)

## 1\. 常用正则表达式

表达式

说明

\r, \n

代表回车和换行符

\t

制表符

\\\

代表 "\" 本身

\^

匹配 ^ 符号本身

\$

匹配 $ 符号本身

元字符

说明

.

匹配除了换行符以外的任意字符

\w

匹配字母、数字、下划线、汉字

\s

匹配任意的空白符

\b

单词的开始或结尾

\~

匹配字符串的开始

$

匹配字符串的结束

如：

\ba\w*\b ：匹配以字母a开头的单词——先是某个单词开始处(\b)，然后是字母a,然后是任意数量的字母或数字(\w*)，最后是单词结束处(\b)。

\d+ ：匹配1个或更多连续的数字。这里的+是和*类似的元字符，不同的是*匹配重复任意次(可能是0次)，而+则匹配重复1次或更多次。

\b\w{6}\b： 匹配刚好6个字符的单词。

表达式

说明

[ ]

包含一系列字符

[^ ]

包含之外一系列字符

[ab5@]: 匹配 "a" 或 "b" 或 "5" 或 "@"

[^abc]： 包含abc之外的任意字符

[f-k]: f-k之间的任意字符

表达式

说明

{n}

表达式重复n次，比如：["\w{2}" 相当于
"\w\w"](http://www.regexlab.com/zh/workshop.asp?pat=\\w{2}&txt=ab+c6)；["a{5}"
相当于 "aaaaa"](http://www.regexlab.com/zh/workshop.asp?pat=a{5}&txt=bbaaaaaddee)

{m,n}

表达式至少重复m次，最多重复n次，比如：["ba{1,3}"可以匹配 "ba"或"baa"或"baaa](http://www.regexlab.com/z
h/workshop.asp?pat=ba{1,3}&txt=a,baaa,baa,b,ba)

{m,}

表达式至少重复m次，比如：["\w\d{2,}"可以匹配 "a12","_456","M12344"...](http://www.regexlab.com
/zh/workshop.asp?pat=\\w\\d{2,}&txt=b1,a12,_456,_4AA,M12344,123465465474465345
43543)

?

匹配表达式0次或者1次，相当于 {0,1}，比如：["a[cd]?"可以匹配 "a","ac","ad"](http://www.regexlab.com/
zh/workshop.asp?pat=a\[cd\]%3F&txt=a,c,d,ac,ad)

+

表达式至少出现1次，相当于 {1,}，比如：["a+b"可以匹配 "ab","aab","aaab"...](http://www.regexlab.com
/zh/workshop.asp?pat=a%2Bb&txt=a%2Cb%2Cab%2Caab%2Caaab)

*
表达式不出现或出现任意次，相当于 {0,}，比如：["\^*b"可以匹配 "b","^^^b"...](http://www.regexlab.com/zh
/workshop.asp?pat=%5C%5E*b&txt=%5E%2Cb%2C%5E%5E%5Eb%2C%5E%5E%5E%5E%5E%5E%5Eb)

## 2.Qt 中使用正则表达式

Qt 用QRegExp 来封装正则表达式。如： QRegExp rx("^[0-9]+(\\\\.\\\d+)?$");

例如我们又这个一个有规则的字符串：价格:数量:商家编码:属性名:属性值;属性名:属性值;
价格:数量:商家编码:属性名:属性值;属性名:属性值;属性名:属性值; 价格:数量:商家编码:属性名:属性值;

想要分成：

价格:数量:商家编码:属性名:属性值;属性名:属性值;  
价格:数量:商家编码:属性名:属性值;属性名:属性值;属性名:属性值;  
价格:数量:商家编码:属性名:属性值;

首先选择合适的正则表达式：(\w*:){3}(\w*:\w*;)+， 然后：

1 QRegExp rx(tr("(\\\w*:){3}(\\\w*:\\\w*;)+"));  
2 int pos(0);  
3  
4 while ((pos = rx.indexIn(str, pos)) != -1)  
5 {  
6 strList.push_back(rx.capturedTexts().at(0));  
7 pos += rx.matchedLength();  
8 }

这里 strList 就是想要的结果了。

    QRegExp

的更多用法见Qt的文档。

posted @ 2011-03-16 10:13 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1985715) 收藏

##备注 
 @post in:2011-03-16 10:13