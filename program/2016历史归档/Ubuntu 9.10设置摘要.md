#  [Ubuntu
9.10设置摘要](http://www.cnblogs.com/sld666666/archive/2010/04/04/1704459.html)

一： Root 账户

Ubuntu默认是不开启Root账户的，要开启的话可以先登录一个用户。然后，输入如下命令：

sudo passwd root

密码：××××××

二：控制台分辨率：

默认的分辨率是800×600，可以用如下方式修改：

1.切换到Root账户

2.打开/etc/default/grub

3.修改其中GRUB_CMDLINE_LINUX="" 为 GRUB_CMDLINE_LINUX="vga=791"//vga791为1024×768

4.输入命令update-grub2，重新生成grub配置文件

三：启动时直接进入控制台

只需要编辑/etc/X11/default-display-manager文件即可。

root登录， cd etc/X11,在此文件夹下有default-display-manager文件，

vim default-display-manager 编辑之，改 “**/usr/sbin/gdm**”为**“false”**即可。

如果想反过来，这改 **“false“**为“**/usr/sbin/gdm**”，reboot计算机，完成设置。

当然此时如果想要进入图形界面的话，需要输入startx命令了

posted @ 2010-04-04 23:52 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1704459) 收藏

##备注 
 @post in:2010-04-04 23:52