#  [一个简单的makefile示例及其注释](http://www.cnblogs.com/sld666666/archive/2010/04/08/1
707789.html)

相信在unix下编程的没有不知道makefile的，刚开始学习unix平台  
下的东西，了解了下makefile的制作，觉得有点东西可以记录下。  
下面是一个极其简单的例子：  
现在我要编译一个Hello world，需要如下三个文件：  
1\. print.h  
#include<stdio.h>  
void printhello();

2\. print.c  
#include"print.h"  
void printhello(){  
printf("Hello, world\n");  
}

3\. main.c  
#include "print.h"  
int main(void){  
printhello();  
return 0;  
}

好了，很简单的程序了。如果我们想要编译成功需要哪些步骤呢？  
我认为在这里需要理解的就两步：  
# 为每一个 *.c文件生成 *o文件。  
# 连接每一个*o文件，生成可执行文件。  
下面的makefile 就是根据这样的原则来写的。

一：makefile 雏形：

  
#makefile的撰写是基于规则的，当然这个规则也是很简单的，就是：  
#target : prerequisites  
command //任意的shell 命令

实例如下：  
makefile:  
helloworld : main.o print.o #helloword 就是我们要生成的目标  
# main.o print.o是生成此目标的先决条件  
gcc -o helloworld main.o print.o#shell命令，最前面的一定是一个tab键

mian.o : mian.c print.h  
gcc -c main.c  
print.o : print.c print.h  
gcc -c print.c  
  
clean :  
rm helloworld main.o print.o  
OK，一个简单的makefile制作完毕，现成我们输入 make，自动调用Gcc编译了，  
输入 make clean就会删除 hellowworld mian.o print.o

  
二：小步改进：

  
在上面的例子中我们可以发现 main.o print.o 被定义了多处，  
我们是不是可以向C语言中定义一个宏一样定义它呢？当然可以：  
makefile:  
objects = main.o print.o #应该叫变量的声明更合适

helloworld : $(objects) //声明了变量以后使用就要$()了  
gcc -o helloworld$(objects)  
mian.o : mian.c print.h  
gcc -c main.c  
print.o : print.c print.h  
gcc -c print.c  
  
clean :  
rm helloworld $(objects)  
修改完毕，这样使用了变量的话在很多文件的工程中就能体现出方便性了。

  
三：再进一步：

  
再看一下，为没一个*.o文件都写一句gcc -c main.c是不是显得多余了，  
能不能把它干掉？而且 main.c 和print.c都需要print.h，为每一个都写上是  
不是多余了，能不能再改进？  
能，当然能了：  
makefile:  
objects = main.o print.o

helloworld : $(objects)  
gcc -o helloworld$(objects)  
  
$(objects) : print.h # 都依赖print.h  
mian.o : mian.c #干掉了gcc -c main.c 让Gun make自动推导了。  
print.o : print.c  
clean :  
rm helloworld $(objects)

好了，一个简单的makefile就这样完毕了，简单吧。

posted @ 2010-04-08 21:29 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1707789) 收藏

##备注 
 @post in:2010-04-08 21:29