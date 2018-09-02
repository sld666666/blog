## cpu过高问题排查

top 命令：然后出现如下结果：

Cpu(s):  0.0%us,  0.5%sy,  0.0%ni, 99.5%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st

各部分的含义如下：
1. us 用户空间占用CPU百分比
2. sy 内核空间占用CPU百分比
3. ni 用户进程空间内改变过优先级的进程占用CPU百分比
4. id 空闲CPU百分比
5. wa 等待输入输出的CPU时间百分比
6. hi 硬件中断
7. si 软件中断
8. st: 实时

CPU过高主要看两点： US和SY

## cup us
从三个方向依次进行：
1. CMS GC/FULL GC 频繁: jstat –gcutil [pid] 1000 10
2. 代码中出现非常耗时的操作: greys
3. 整体代码耗时: 通常很难搞定

## cpu sy
1. 锁竞争激烈： jstak
2. 线程主动切换频繁 : jstack
