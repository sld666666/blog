
# Linux Tomcat 安装

## 第一步 安装jdk

1. 下载 http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. 解压： tar zxvf jdk-8u111-linux-x64.gz
3. 打印下当前目录： /root/soft/jdk1.8.0_111
4. 设置环境变量： vim /etc/profile ：在尾部添加：

    ```
    export JAVA_HOME=/root/soft/jdk1.8.0_111/
    export PATH=$JAVA_HOME/bin:$JAVA_HOME/jre/bin:$PATH
    export CLASSPATH=.:/root/soft/jdk1.8.0_111/lib:/root/soft/jdk1.8.0_111/jre/lib:$CLASSPATH
    ```
5. 刷新配置文件: source /etc/profile
6. 测试： java -version


## 第二步 安装Tomcat

1. 下载： http://tomcat.apache.org/download-90.cgi
2. 解压: tar zxvf apache-tomcat-9.0.0.M13.tar.gz
3. 配置环境变量:  vim /etc/profile, 末尾添加:

  ```
    export TOMCAT_HOME=/root/soft/apache-tomcat-9.0.0.M13/
  ```
4. 刷新: source /etc/profile
5. 启动： 进入 bin目录： bash catalina.sh start or  ../bin/configtest.sh  run
6. 测试： curl http://localhost:8080  

### 开启Manager
用是tomcat9 ,所有改动如下：
1. tomcat-users.xml:
    ```
    <role rolename="manager-gui"/>
    <role rolename="tomcat"/>
    <user username="sld" password="123456" roles="manager-gui,admin-gui,standard"/>
    ```
    注意xml有格式错误的化，登陆不成功.
2. 设置 <tomcat>/webapps/manager/META-INF/context.xml， 注释掉 <value>标签

    ```
    <Context antiResourceLocking="false" privileged="true" >
      <!--
        <Valve className="org.apache.catalina.valves.RemoteAddrValve"
       allow="127\.\d+\.\d+\.\d+|::1|0:0:0:0:0:0:0:1" />
      !-->
      </Context>
    ```

  
