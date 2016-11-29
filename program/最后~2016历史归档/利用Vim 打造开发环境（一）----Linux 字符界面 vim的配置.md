#  [利用Vim 打造开发环境（一）---->Linux 字符界面
vim的配置](http://www.cnblogs.com/sld666666/archive/2010/04/05/1704662.html)

要配置vim ，当然要修改.vimrc了。

一：.vimrc在哪呢？

输入命令： vim ,打开vim编辑器，然后敲入version，vim 会把此版本

的信息列出来，对我们有用的是如下信息：

system vimrc file： “$VIM/vimrc”

user vimrc file: "$Home/.vimrc"

................................

这两句话告诉我们，有两个地方保持了vimrc配置文件，安装的地方（root帐号）

\--(/usr/share/vim)和登录帐号的home目录下，vim对此的选择是：

如果 安装目录下下没有.vimrc，它会读取登录账户下 的的vimrc文件 。

好了我们进入登录帐号home下，比如我的帐号是sld,则：cd /home/sld,

打开.vimrc，： vim .vimrc,即可。

二： 下面是几个比较常用的设置：

syntax on " 语法高亮

set nocompatible "去掉讨厌的有关vi一致性模式  
  
set number "显示行号。  
  
set history=50 "设置命令历史记录为50条。  
  
set autoindent "使用自动对起，也就是把当前行的对起格式应用到下一行。  
  
set smartindent "依据上面的对起格式，智能的选择对起方式，对于类似C语言编。  
  
set tabstop=4 "设置tab键为4个空格。  
  
set shiftwidth=4 "设置当行之间交错时使用4个空格  

posted @ 2010-04-05 14:25 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1704662) 收藏

##备注 
 @post in:2010-04-05 14:25