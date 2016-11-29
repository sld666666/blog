#  [利用RadASM
写汇编程序](http://www.cnblogs.com/sld666666/archive/2010/11/14/1877260.html)

本文介绍利用RedASM写一个messageBox “hello world”.

RadAsm 是我用过最爽的一个汇编Ide了， 代码高亮，智能提示……

RadASM支持多种不同的汇编编译器，这里我们选择MASM.既然是IDE了，创建工程就很简单了, 文件->Win32 App(no
res)->名字“MessageBox,”一路Next.

![QQ截图未命名](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/R
adASM_14215/QQ%E6%88%AA%E5%9B%BE%E6%9C%AA%E5%91%BD%E5%90%8D_thumb.png)

然后呢，我们写下如下的汇编程序：

     1 Title: Hello world   
     2 ;Author: sld6666666@gmail.com      
     3 ;Data: 2010-11-05   
     4 ;Description: Assemble hello world  
     5 .386                ;该程序对CPU的最低要求是intel 386   
     6 .model flat, stdcall        ; 平坦内存模型， stacll 函数调用方式   
     7 .stack 4096            ;栈的大小为4096B  
     8 option casemap:none  
     9 ;include 头文件， 连接库   
    10 include windows.inc   
    11 include kernel32.inc   
    12 include user32.inc   
    13 includelib kernel32.lib   
    14 includelib user32.lib  
    15 .data                    ;数据段  
    16     sztitle db "hello",0        ;0 很重要， 因为是0结尾   
    17     szText db "Hello world",0   
    18 .code                    ;这里是代码区域  
    19 main proc            ;proce 表示程序的开始   
    20     invoke MessageBox,NULL,    offset szText, ;invoke 关键字表明这个是从其他库导入的   
    21                 offset sztitle,   
    22                 MB_OK   
    23     invoke ExitProcess,NULL  
    24     ret            ;程序返回  
    25 main endp            ;endp表示proc过程的结束  
    26 end main            ;end 表明该行是汇编程序的最后一行

就OK了。

这里附上效果图，供大家参观：

![QQ截图未命名2](http://images.cnblogs.com/cnblogs_com/sld666666/WindowsLiveWriter/
RadASM_14215/QQ%E6%88%AA%E5%9B%BE%E6%9C%AA%E5%91%BD%E5%90%8D2_thumb.png)

这里再补一个hello world 的控制台程序

     1 .386                ;该程序对CPU的最低要求是intel 386   
     2  .model flat, stdcall        ; 平坦内存模型， stacll 函数调用方式   
     3  .stack 4096            ;栈的大小为4096B  
     4  option casemap:none  
     5   
     6 include windows.inc  
     7 include kernel32.inc  
     8 includelib kernel32.lib  
     9   
    10 .data  
    11   
    12   inchar     DB ?  
    13   numwritten DD ?  
    14   numread    DD ?  
    15   outhandle  DD ?  
    16   inhandle   DD ?  
    17   strHello    DB "Hello World!"  
    18   
    19   
    20 .code  
    21  main proc  
    22       invoke  AllocConsole  
    23         invoke  GetStdHandle,STD_OUTPUT_HANDLE  
    24         mov [outhandle],eax  
    25         invoke  GetStdHandle,STD_INPUT_HANDLE  
    26         mov [inhandle],eax  
    27         invoke  WriteConsole,[outhandle], offset strHello,12,numwritten,0  
    28         invoke  ReadConsole,[inhandle],inchar,1,numread,0  
    29         invoke  ExitProcess,0  
    30   
    31 main endp  
    32 end main 

来一个更简单的：

     1 .386  
     2 .model flat, stdcall  
     3 option casemap :none  
     4   
     5 include windows.inc  
     6 include kernel32.inc  
     7 include masm32.inc  
     8   
     9 includelib kernel32.lib  
    10 includelib masm32.lib  
    11   
    12 .data  
    13     helloWorld db "hello world",0  
    14       
    15 .code  
    16     start:  
    17         invoke StdOut, addr helloWorld  
    18         invoke ExitProcess,0  
    19       
    20     end start

posted @ 2010-11-14 23:04 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1877260) 收藏

##备注 
 @post in:2010-11-14 23:04