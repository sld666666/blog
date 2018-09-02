# CheckStyle工具

## 代码格式化
mac下 idea快捷键： Option + Command + L

## 代码格式检查工具

1. idea 安装插件 :Preferences -> plugin -> CheckStyle-IDEA
网络有问题可以 到这里下载本地安装： https://plugins.jetbrains.com/plugin/1065-checkstyle-idea
2. 设置 baiwang-checkstyle.xml ， file->defualt setting->other setting ->CheckStyle, 新建规则，设置aiwang-checkstyle.xml. 可以在这里 [下载](http://192.168.0.247/baiwang/document/blob/master/%E7%BC%96%E7%A0%81%E8%A7%84%E8%8C%83/baiwang-checkstyle.xml) 百望的代码规范检查配置
3. 右键 ->check currrent file
Â
CheckStyle 插件工具的好处是，你能够直接在 IDE 中对项目中的某个 Java 文件检查规范，并且从错误信息处直接定位到相关代码位置，从而很方便地作出修改。还有一个好处是，你可以在编写代码时，实时得到 CheckStyle 的检测提示


而 CheckStyle 插件工具不好的地方在于，其不能做到对多个文件批量检查。比如，我想知道某个项目或者某个目录下的 Java 文件是否符合规范，插件就做不到。这个时候，你就可以使用命令行工具了。

下载 CheckStyle 提供的 Jar 包及 xml 规则文件，然后打开命令行工具，输入检查命令即可。如：

java -jar ~/tools/checkstyle-7.8.2-all.jar -c ~/tools/google_checks.xml MainActivity.java

命令行工具的弊端在于没有定位源码的功能，你只能看到当前文件哪里是否符合规范。
