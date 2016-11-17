#  [docker 介绍](http://www.cnblogs.com/sld666666/p/5444142.html)

# docker 介绍

## 安装

  1. sudo apt-get install docker.io
  2. sudo docker info 查看是否安装成功

## hello world

    sodu docker run hello-world

执行这个命令的时候干了如下几个事情：

  1. docker引擎 CLI client 和 daemon 引擎发生交互
  2. daemon 从docker hub 拉“hello-world” 镜像
  3. daemon 创建一个新的容器来作为执行环境
  4. deamon 告诉 CLI client 相关的执行返回，让其在终端打印出来

我们可以在 <https://hub.docker.com> 找到更多镜像。

以上我们有几种概念：

  1. docker client, 用户界面， 支持永和和docker daemon的同学
  2. docker daemon 运行主机上， 出来服务器请求
  3. docker index 中央registry， 支持共有和私用的docker容器镜像备份

三个要素：

  1. 容器
  2. 镜像
  3. DockerFile

## 镜像和容器

在上面例子中， 我们指出了两个概念： 容器 和镜像

容器： 负责应用程序的运行，包括操作系统、用户添加的文件以及元数据。  
镜像： 运行在容器上。

一个镜像可以是非常简单的一个命令，也可以是非常复杂的软件系统(如数据库，tomcat), 这些镜像可以是别人创建的，然后你在使用。

很多人在创建镜像， 我们可以在https://hub.docker.com知道镜像。

    docker run tomcat

就可以运行tomcat,如果本地不存在，则先从hub.docker中下载。

用： docker images 就可以查看本地安装了多少镜像

## 创建你自己的镜像

我们用

    docker run docker/whalesay cowsay boo

其中 cowsay 是要运行的命令，后面的 boo 是参数

来使用whalesay的镜像，现在我们以之为基础来创建我们自己的镜像。

### 第一个 写Dokcerfile

Dokcerfile 用来描述镜像的信息。按如下步骤就可以使用镜像：

  1. mkdir mydockerbuild
  2. cd mydockerbuild
  3. vim Dockerfile
  4. 写下如下, 保存并关闭

FROM tomcat:latest  
RUN apt-get -y update && apt-get install -y fortunes  
CMD /usr/games/fortune -a | cowsay

其中

  1. FROM 说明这个镜像是基于哪个镜像的
  2. RUN 增加 “fortunes ”到镜像中， “fortunes ”能够打印出更友好的话。

### 从 Dockerfile中编译出镜像

docker build -t docker-whale .

这样就可以使用自己的新镜像了，当然我们可以把他Push到hub.docker中。

## 总结

很久以前， 我就想一个软件配置玩了以后是不是可以直接在其他电脑中使用。 但是很不幸，自动化的配置非常的困然。 现在docker
竟然用虚拟化的技术实现了软件的配置像代码一样， git， push,get ，share。 真是爽。

posted @ 2016-04-28 20:31 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=5444142) 收藏

##备注 
 @post in:2016-04-28 20:31