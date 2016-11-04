

# 利用git+hugo+markdown  搭建一个静态网站

一直想要有一个自己的文档管理系统：
  1. 可以很方便书写，而且相应的文档很容易被分享
  2. 很方便的存储、管理、历史记录
  3. 比较方面的浏览和查询

第一点用Makrdown来写文档是一个非常好的选择，第二点自然想到了git，
第三点用一个静态的网站来浏览和管理是一个不错的选择，这里选择了hugo。

Hugo是由Go语言实现的静态网站生成器。 注意试试生成器。他虽然自带webserver，但是没有Nigix强大了。
他能非常方便的把markdown文件转换为html。


## 如何搭建

首先必须有一台服务器，我选择了阿里云。然后：

### 第一步： 安装hugo

1. 首先检查系统的版本：cat /proc/version
2. 直接用 sudo yum install hugo 发现不行，只能字节选择本机安装了
3. 当然发现少了一个能上传的客户端： yum install lrzsz
4. 因为是radhat，用hugo-0.16-2.el6.x86_64.rpm 包，然后sudo yum install hugo-0.16-2.el6.x86_64.rpm

### 第二步： 开始创建站点

1. hugo new site  hub_site
2. cd hub_sit
3. 安装皮肤： cd themes； git clone https://github.com/key-amb/hugo-theme-bootie-docs.git themes/bootie-docs
 也可以本地上传
4. 编辑： config.toml
5. 加一个主页： _index.md
6. 加一个 about页面： hugo new post/about.md
7. 启动：hugo server --buildDrafts -p 8080 --bind 100.69.199.93 -b http://100.69.199.93：8080/

这时候我们就可以看到第一个页面了。

### 第三步:和github结合起来
利用git来管理文档是一个非常好的方式。这里直接想到用github来存储文档。gitbhu支持收到push请求的时候调用固定的地址http。
 所以我们可以用这个来实现完美的功能。

 首先在github上配置请求：settings->Webhooks

 然后我们要在外面的服务器上搭建一个Http服务器来接受这个请求， 这里选择用python的import http.server来大家，简单方便：

```
class EntranceHttpRequestHandler(http.server.CGIHTTPRequestHandler):

    def do_POST(self):
        print('begin')

  if __name__=='__main__':
      handler = EntranceHttpRequestHandler.EntranceHttpRequestHandler
      httpd = socketserver.TCPServer(("", 8001), handler)
      httpd.serve_forever()
```
## 第四步 用python把整个链路连接起来

```
import http.server
import Convertor
import os
import _thread

TargetPath = "/root/root/site/content/post/blog"
GitSrcPath = "/root/root/site/blog"
HugeSitePath = "/root/root/site/"
HugeStatCommond = r'hugo server --buildDrafts -p 80 --bind 115.28.83.94 -b http://115.28.83.94/'
class EntranceHttpRequestHandler(http.server.CGIHTTPRequestHandler):

    def do_POST(self):
        print('begin')

        self.gitpull(GitSrcPath)

        self.stopHugo()

        convert = Convertor.Convertor()
        convert.excute(GitSrcPath,TargetPath)
        self.startHugo()
        print("finished")
        self.wfile.write(b"msg finished")


    def gitpull(self,  filePath):
        os.chdir(filePath)
        command = "git pull "
        os.system(command)

    def startHugo(self):
        _thread.start_new_thread(self.doStartHugo, ())

    def doStartHugo(self):
        os.chdir(HugeSitePath)
        output = os.system(HugeStatCommond)
        print(output)
        print('sartHugo finished')

    def stopHugo(self):
        command = 'kill -9 $(pidof hugo)'
        os.system(command)
        print('stopHugo finished')

```
其中Convert 是对文档做一些分类和tag的转换不想起介绍。
到现在一个完整的网站就搭建完成了，每一次只要在本地push文档，就能在网站上自动更新。
