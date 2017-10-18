# netstat

netstat用来显示各种网络信息


## 参数
- -a (all)显示所有选项，默认不显示LISTEN相关
- -t (tcp)仅显示tcp相关选项
- -u (udp)仅显示udp相关选项
- -n 拒绝显示别名，能显示数字的全部转化成数字。
- -l 仅列出有在 Listen (监听) 的服務状态

- -p 显示建立相关链接的程序名
- -r 显示路由信息，路由表
- -e 显示扩展信息，例如uid等
- -s 按各个协议进行统计
- -c 每隔一个固定时间，执行该netstat命令。

提示1：LISTEN和LISTENING的状态只有用-a或者-l才能看到

提示2: 各种参数的组合威力无穷

## 例子

**列出所有 tcp 端口 netstat -at**

```
ctive Internet connections (including servers)
Proto Recv-Q Send-Q  Local Address          Foreign Address        (state)
tcp4       0      0  bogon.61793            203.208.43.79.https    ESTABLISHED
tcp4      69      0  bogon.61788            m12-8.163.com.imaps    CLOSE_WAIT
```

**只显示监听端口 netstat -l**

**统计本地有多少http连接**
netstat -nat|grep -i "80"| grep ESTABLISHED|wc -l
