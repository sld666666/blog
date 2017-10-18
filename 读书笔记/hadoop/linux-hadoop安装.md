# linux hadoop安装

本文介绍如何在Linux下安装伪分布式的hadoop开发环境。

在一开始想利用cgywin在 windows下在哪， 但是一直卡在ssh的安装上。所以最后换位虚拟机+ubuntu安装。

选择的版本是hadoop 2.7.2 安装步骤虽然简单，但是中间遇到的小问题还是比较多的，总结下安装hadoop主要分为3大部：

1. 安装ssh
2. 安装 jdk
3. 安装hadoop

## 安装ssh
1. sudo apt-get install ssh
2. mkdir /home/user/.ssh
3. cd /home/user/.ssh
4. ssh-keygen  一路next
5. cat id_dsa.pub >> authorized_keys 设置证书
6. ssh localhost  测试

注意几点： 

1. 安装Ubuntu的时候不要开启root 登陆
2. 注意.ssh文件夹的权限

不然localhost的时候报  permission denied

## 安装java
1. sudo add-apt-repository ppa:webupd8team/java
2. sudo apt-get update
3. sudo apt-get install oracle-java8-installer

### 设置JAVA_HOME
1. sudo vim /etc/profile
2. 在profile文件末尾加入： 
export JAVA_HOME=/usr/lib/jvm/java-8-oracle
export PATH=$JAVA_HOME/bin:$PATH 
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar 
3. 用echo ${JAVA_HOME}校验环境变量是否正确
4. java -version; javac -verison 校验jdk是否安装正确

## 安装hadoop
下载好hadoop，解压完毕。
在伪分布式下需要配置如下几个文件， 他们都在etc/hadoop下

### mapred-site.xml.template
	<configuration>
	　<property>
	　　　<name>mapred.job.tracker</name>
	　　　<value>localhost:9001</value>
	　</property>
	</configuration>

### core-site.xml

	<configuration>	
	    <property>
	        <name>hadoop.tmp.dir</name>
	       	<value>file:/home/sld/app/hadoop-2.7.2/tmp</value>
		<description>Abase for other temporary directories.</description>
	    </property>
	    <property>
		<name>fs.defaultFS</name>
		<value>hdfs://localhost:9000</value>	
	    </property>
	</configuration>

### hdfs-site.xml

	<configuration>
	  <property>
	    <name>dfs.replication</name>
	    <value>1</value>
	  </property>
	  <property>
	    <name>dfs.namenode.name.dir</name>
	    <value>file:/home/sld/app/hadoop-2.7.2/tmp/dfs/name</value>
	  </property>
	  <property>
	    <name>dfs.datanode.data.dir</name>
	    <value>file:/home/sld/app/hadoop-2.7.2/tmp/dfs/data</value>
	  </property>
	</configuration>

## hadoop-env.sh
export JAVA_HOME=“你的JDK安装地址”

注意一点的是就是在环境变量中设置的了JDK的路径这里还要设置
注意另外一点： 输出路径最好设置到hadoop目录下， 部分在格式化的时候没法格式化到

### 启动hadoop
在启动Hadoop前，需格式化Hadoop的文件系统HDFS(这点与Windows是一样的，重新分区后的卷总是需要格式化的)。进入Hadoop文件夹，输入下面的命令

	bin/hadoop namenode -format

然后启动

	sbin/start-all.sh

也可以只启动 hdfs

	sbin/start-hdfs.sh

如果要关闭hadoop可以用

	sbin/stop-hdfs.sh

#### 验证hadoop

利用jps命令查看java的进程

	SecondaryNameNode
	DataNode
	NameNode
	NodeManager
	ResourceMananger

如果缺失 DataNode或者 NameNode 说明 hdfs-site.xml 没有配置好。重新配置+格式化+启动

Hadoop 启动以后会有两个地址可以方位：

		http://localhost:50030 (MapReduce的Web页面)
	 http://localhost:50070 (HDFS的Web页面)

　　	 

然后
	
	curl http://localhost::50070 

也可以在虚拟机外面方位：

	http://192.168.20.129:50070

![](https://img.alicdn.com/imgextra/i4/46754672/TB2FUXunXXXXXadXXXXXXXXXXXX_!!46754672.jpg)
