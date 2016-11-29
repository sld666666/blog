#  [nginx源码剖析(2)----从main()函数开始](http://www.cnblogs.com/sld666666/archive/2010
/05/05/1728417.html)

首先说下感受吧。

这个是对nginx的main()函数的简单的分析了，大致看了下nginx.c的代码，发现有如下几个特点：

一：高度模块化，唯一把多个模块串起来的就是ngx_cycle_s这个结构体。

二：重新定义，不管是数据类型，还是库函数，还是系统函数，其都给它们包装了下，

unix网络编程里面就提倡这种做法，并把之称之为包裹函数。

三：注释太少，虽说里面有些代码是自明的，但是并不是所有的代码都能直接表现出其意义。

也许是我水平不太高的原因吧。

四：代码干净，简洁干练，这个只是我的主观的观点，呵呵，没有论证。

  

在这里，只是总体浏览了一下，没深入到代码细节上，如果以上或以下有错误的观点，望指教。

Let's begin!  

在开始分析main函数的代码以前必  须了解一下一个典型的服务器的运行方式。

一个简单而有代表性的服务器程序的伪代码如下:

![](http://images.cnblogs.com/OutliningIndicators/ContractedBlock.gif)![](http
://images.cnblogs.com/OutliningIndicators/ExpandedBlockStart.gif)代码

     1  struct sockaddr_in servaddr cliaddr;  
     2   
     3  int listenfd = socket(AF_INET, SOCK_STREAM,0);  
     4 bind(listenfd, (struct sockaddr_in*)(&servaddr), sizeof(servaddr));  
     5   
     6  listen(listenfd, 22);  
     7  for (;;) {  
     8  int clielen = sizeof(cliaddr);  
     9  int connfd = accept(listenfd, (strct sockaddr_in)(&cliaddr), & clielen );  
    10   
    11  if( 0 == fork())  
    12     I_will_do_something();  
    13 }  
    14     
    15   
    16  

nginx作为一个http服务器当然不能免俗了。整个mian()算上空行200行左右，有效代码当然更少了。

好吧，现在来看看mian函数干了什么了。

     1   main(int argc, char *const *argv)  
     2   {  
     3       ngx_int_t              i;  
     4       ngx_log_t             *log;  
     5       ngx_cycle_t          *cycle, init_cycle;  
     6       ngx_core_conf_t   *ccf;  
     7     
     8       ngx_get_options(argc, argv); /*根据参数，选定一实现什么功能，在此函数里面有对几个全局变量的                    处理*/  
     9       if (ngx_show_version) {/*这个ngx_show_version就是函数ngx_get_options的处理结果了*/  
    10          I_will_show_information;  
    11      }  
    12    
    13        ngx_time_init();/*服务器时间的初始化*/  
    14       ngx_pid = ngx_getpid();/*得到进程id*/  
    15       log = ngx_log_init(ngx_prefix);/*服务器怎么能不写log呢，初始化之*/  
    16    
    17      ngx_memzero(&init_cycle, sizeof(ngx_cycle_t));/*meset(init_cycle, 0 , sizeof(ngx_cycle_t))*/  
    18      init_cycle.log = log;  
    19      ngx_cycle = &init_cycle;  
    20      ngx_pagesize = 1024;  
    21      init_cycle.pool = ngx_create_pool(1024, log);  
    22      /*初始化init_cycle, init_cycle 是一个非常庞大struct，里面包含了内存池，log，array,list，event，
    套节字，主机名 等等一系列信息*/       
    25           ngx_add_inherited_sockets(&init_cycle);/*这里就是前面说的服务器接听套节字过程的封装了*/  
    26      
    27      ngx_os_status(cycle->log);/*还是写log*/  
    28       ngx_cycle = cycle;  
    29       ccf = (ngx_core_conf_t *) ngx_get_conf(cycle->conf_ctx, ngx_core_module);  
    30       ngx_create_pidfile()/*还是往文件写log信息*/  
    31   
    32      /*这里省下几个错误处理的动作*/  
    33    
    34      ngx_single_process_cycle(cycle)/*我要处理东西了*/  
    35    
    36      return 0；  
    37  }

posted @ 2010-05-05 22:25 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1728417) 收藏

##备注 
 @post in:2010-05-05 22:25