##cef使用教程
### 目录介绍
我们下载源码， 用vs打开工程， 发现会有如下三个工程：

1. cefsimple
2. cefclient
3. lib_dll_wrapper

其中cefsimple和cefclient是两个例子。 而lib_dll_wrapper是一个c++借口。 cef本身对外提供的是capi， 而lib_dll_wrapper是把只转换为c++ api

###链接不同的运行库
lib_dll_wrapper 编译的时候用的是mt方式。 让我们的主程序用Md方式的时候就会有运行库错误， 所以我们需要重新用Md的方式编译lib_dll_wrapper。改动的时候注意4点：
1. 常规->配置类型 改为动态库
2. c++->代码生成->运行库 改为mdd
3. 引入libcef.lib
4. 在工程上右键 -> 添加 -> 新建项 -> 选"模块定义文件(.def)" -> 随便输入个名字 -> 添加 这样才能产生lib文件， 不然只有一个dll,疑似为微软bug

