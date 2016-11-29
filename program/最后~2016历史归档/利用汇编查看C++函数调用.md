#  [利用汇编查看C++函数调用](http://www.cnblogs.com/sld666666/archive/2010/10/11/1848159
.html)

这篇文章的内容是一个老生常谈的问题----> 函数是如何被调用的。

本文用汇编代码研究函数调用的过程，参数调用的方式，函数值的返回。

## 1\. 函数是如何实现调用的

函数的调用是用call 和 ret 指令实现的。这里首先简单说明下这call指令的作用：call指令与跳转指令相似，但是不同的是保持返回信息，
即将下一个指令入栈，当遇到ret的时候，返回到这个地址。

呵呵，有点抽象， 下面就用实例来说明，我们写下如下代码：

     1 int add(int a, int b)  
     2 {  
     3     return a+b;  
     4 }  
     5   
     6  int _tmain(int argc, _TCHAR* argv[])  
     7 {  
     8     int a(1), b(2);  
     9     b = add(a,b);  
    10     return 0;  
    11 }

很简单的代码，这里我们关注的是add函数是如何被调用的，下面我们看下其汇编代码的实现

     1 //int a(1), b(2);  
     2  004113FE mov     dword ptr [a],1  
     3  00411405 mov     dword ptr [b],2  
     4  //b = add(a,b);  
     5  0041140C mov     eax,dword ptr [b]  
     6 0041140F push    eax  
     7  00411410 mov     ecx,dword ptr [a]  
     8  00411413 push    ecx  
     9 00411414 call    add (41109Bh)  
    10  00411419 add     esp,8  
    11 0041141C mov     dword ptr [b],eax  
    12  //return 0;  
    13  0041141F xor     eax,eax

如上，红色的部分为调用的add函数的代码， 当调用add函数时相关寄存器的情况是

EIP = 0041109B

ESP = 0012FE78

EBP = 0012FF68

其中EIP是下一个指令的地址，而ESP就是保存了add函数的地址了。我们看以在memory窗口看下其值：

0x0012FE78 19 14 41 00

根据小字节存储顺序 其值应该为 00411419， 刚好为下一个指令的地址。

再来看下add函数内部的调用情况：

    1 int add(int a, int b)  
    2 {  
    3 004113A0 push     ebp  
    4 004113A1 mov      ebp,esp  
    5  //……一大堆对esp的操作  
    6  004113C7 mov      esp,ebp  
    7 004113C9 pop      ebp  
    8 004113CA ret  
    9 }

这里我们需要注意的是 如上列出的指令就是所谓的堆栈平衡。首先ebp入栈， 然后把esp的值赋给ebp， 中间有一大堆对esp的操作，
最后把ebp的刚才保存的值赋给esp, ebp出栈。这样做的原因是为了中间对栈的操作不影响ebp的值，当函数返回时，其值依旧是刚进入此函数的ebp的值。

## 2\. 函数参数的调用方式

一．堆栈方式

二．寄存器方式

三．全局变量

### 2.1 堆栈方式

如果函数通是堆栈方式的调用就必须要做如下两个方面:

1． 对应堆栈的顺序

2． 由谁来平衡堆栈(调用者还是子程序)

所以我们必须约定函数参数调用的方式。常见的调用约定由如下几个：

调用约定

_cdecl（C/C++的默认方式）

pascal

Stdcall(win32 API的默认方式)

参数传递顺序

从右到在

从左到右

从右到在

平衡堆栈着

调用者

子程序

子程序

在这里我们看下_cdecl调用时如何实现的。还是本文的第一段代码，这里我们只观察其参数的传递：

    1 0041140C mov     eax,dword ptr [b]  
    2 0041140F push    eax            // ESP = 0012FE84  
    3  00411410 mov     ecx,dword ptr [a]  
    4 00411413 push    ecx  
    5 00411414 call    add (41109Bh) //ESP = 0012FE7C  
    6 00411419 add     esp,8         //ESP = 0012FE84

如上， 首先push b, 然后push a, 一切执行完之后在 00411419 平衡堆栈

这里需要指出的一点是栈是向下增长的，也就是说每一次push的时候，ESP的值会减少。

### 2.2 寄存器方式

寄存器传递函数参数的方式没有一个统一的标准，所以不同编译器的实现各有不同。这里只选取MSVC来说明。让在MSVC中采用_fasetcall来传递参数是，最左
边的不大于4字节的参数分别放在ecx和edx中，寄存器用完以后就按照从右到左的顺序人栈。

我们可以看下如下的例子：

     1 int _fastcall add(int a, int b, int c)  
     2 {  
     3 return a+b;  
     4 }  
     5   
     6 int _tmain(int argc, _TCHAR* argv[])  
     7 {  
     8 int a(1), b(2),c(3);  
     9 c = add(a,b,c);  
    10 return 0;  
    11 }

下面再来看下汇编代码

     1 004113FE mov       dword ptr [a],1  
     2 00411405 mov       dword ptr [b],2  
     3 0041140C mov       dword ptr [c],3  
     4 // c = add(a,b,c);  
     5 00411413 mov eax,  dword ptr [c]  
     6 00411416 push      eax                //第三个参数，入栈  
     7 00411417 mov       edx,dword ptr [b] //第二个参数，放入edx中  
     8 0041141A mov       ecx,dword ptr [a] //第一个参数，放入ecx中  
     9 0041141D call      add (4111D1h)  
    10 00411422 mov       dword ptr [c],eax

如上，我们可以比较出参数调用的不同。其调用时第一个参数是放在ecx中，第二个参数是放入edx中。

## 3\. 函数的返回值

函数的返回方式有两种：

1\. Return 直接返回

2\. 以引用方式返回

第一种方式，函数的返回值是放在eax中的，如果返回值的长度大于32，其高位会放在eax中。这个比较简单，这里不着重讨论。

我们这里着重看的是以引用方式返回， 我们敲下如下代码：

     1 void swap(int& a, int& b)  
     2  {  
     3      int tmp = a;  
     4      a = b;  
     5      b = tmp;  
     6  }  
     7   
     8  int _tmain(int argc, _TCHAR* argv[])  
     9  {  
    10      int a(1), b(2);  
    11      swap(a,b);  
    12      return 0;  
    13 }

然后我们再来看其汇编对于add函数的实现：

     1 0041140C  lea         eax,[b] //压地址入栈  
     2 0041140F  push        eax    
     3 00411410  lea         ecx,[a]   
     4 00411413  push        ecx    
     5 00411414  call        swap (4111D6h)   
     6 00411419  add         esp,8  
     7   
     8     //int tmp = a;  
     9 004113BE  mov         eax,dword ptr [a]   
    10 004113C1  mov         ecx,dword ptr [eax]   
    11 004113C3  mov         dword ptr [tmp],ecx   
    12     //a = b;  
    13 004113C6  mov         eax,dword ptr [a]   
    14 004113C9  mov         ecx,dword ptr [b]   
    15 004113CC  mov         edx,dword ptr [ecx]   
    16 004113CE  mov         dword ptr [eax],edx //指针的地址改变了  
    17     //b = tmp;  
    18 004113D0  mov         eax,dword ptr [b]   
    19 004113D3  mov         ecx,dword ptr [tmp]   
    20 004113D6  mov         dword ptr [eax],ecx  
    21 

如上， 以16 行为列， 引用来作为函数的参数只是用地址来代替普通的值， 其他和一般值参数没有区别。

posted @ 2010-10-11 19:23 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1848159) 收藏

##备注 
 @post in:2010-10-11 19:23