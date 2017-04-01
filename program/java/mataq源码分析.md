
# mataq源码分析

## 带着疑问看代码
作为一个消息中间件，他的事情就是消息的转发，对此有几个核心的疑问：

1. 如何收消息，如何发消息
2. 如何调度
3. 如何存储
4. 分布式事务？

## 流程

### 发消息
Metaq-client

MetaProducer producer
producer.start()
producer.send(Message msg)
producer.shutdown()


