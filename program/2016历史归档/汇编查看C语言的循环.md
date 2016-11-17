#
[汇编查看C语言的循环](http://www.cnblogs.com/sld666666/archive/2010/10/23/1858828.html)

这篇文章主要描述C语言中循环的反汇编。

## 1\. for

在汇编中，可以利用cmp 和jp系列质量控制指令的次数。我们可以看下如下例子：

    1 int _tmain(int argc, _TCHAR* argv[])   
    2 {  
    3     int a = 0;  
    4     for (int i = 0; i < 50; i++)   
    5     {   
    6         a++;   
    7     }  
    8     return 0;  
    9 }

这里首先贴出for循环的代码

     1     for (int i = 0; i < 50; i++)   
     2 012E13C5  mov         dword ptr [i],0    
     3 012E13CC  jmp         wmain+37h (12E13D7h)    
     4 012E13CE  mov         eax,dword ptr [i]    
     5 012E13D1  add         eax,1    
     6 012E13D4  mov         dword ptr [i],eax    
     7 012E13D7  cmp         dword ptr [i],32h    
     8 012E13DB  jge         wmain+48h (12E13E8h)    
     9     {   
    10         a++;   
    11 012E13DD  mov         eax,dword ptr [a]    
    12 012E13E0  add         eax,1    
    13 012E13E3  mov         dword ptr [a],eax    
    14     }   
    15 012E13E6  jmp         wmain+2Eh (12E13CEh) 

for循环中，指令的循序是：

# 定义变量i （012E13C5 ）

# 比较（i<50）012E13D7

# 如果相等，则跳出循环， 不得则执行（a++）

# 执行完a++后， jmp到012E13CE ，执行i++

# 重复第二步

## 2\. do…while循环

此循环的汇编形式比较简单，可以归为：

cmp <循环变量>，<限制变量>

jl <循环开始的>

我们可以看下如下的列子：

    1 do   
    2 {   
    3     a++;   
    4 } while (a < 50);

汇编代码如下：

    1  do   
    2     {   
    3         a++;   
    4 00C213C5  mov         eax,dword ptr [a]    
    5 00C213C8  add         eax,1    
    6 00C213CB  mov         dword ptr [a],eax    
    7     } while (a < 50);   
    8 00C213CE  cmp         dword ptr [a],32h    
    9 00C213D2  jl          wmain+25h (0C213C5h)  //关键还是这里的一跳

恩，很简单。

## 3\. while循环

while 循环也是一种非常常用的循环，其共由三条指令实现：

cmp <循环变量>，<限制变量>

jpe b

……

jmp A

我们看下如下的列子：

    1 while(a < 50)   
    2 {   
    3     a++;   
    4 }

下面是相应的汇编源码：

    1 00FD13C5  cmp         dword ptr [a],32h    
    2 00FD13C9  jge         wmain+36h (0FD13D6h)  //如果a ==50, 不执行下面的了   
    3      {   
    4         a++;   
    5 00FD13CB  mov         eax,dword ptr [a]    
    6 00FD13CE  add         eax,1    
    7 00FD13D1  mov         dword ptr [a],eax    
    8     }   
    9 00FD13D4  jmp         wmain+25h (0FD13C5h) 

一切好像很简单哦。

posted @ 2010-10-23 01:54 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1858828) 收藏

##备注 
 @post in:2010-10-23 01:54