# NIO
三种IO:
- BIO : 面向流. File , inputStream, OutputStream, Reader, Write
- NIO : 面向buffer,  buffer, Chnanal, Selector
- AIO : 异步NIO

在操作系统层面有五种IO:
 - 同步IO: 等待返回
 - 异步IO: 立刻返回,轮询结果
 - IO复用: select/ epoll。 select/poll函数不断轮训负责的fd,当有数据返回时候，通知用户进程。 调用时候会阻塞，直到监控的fd列表有数据准备就绪，然后遍历fdset，找到就绪的描述符。优势是可以处理更多的连接。  epoll 采用回调机制
 - 事件驱动,异步回调
 - 异步IO

java的nio是select 模式。

## FileChannel transferTo 为什么快？
Zero-Copy: 系统内核态和用户态会进行文件的负责。 零拷贝就可以直接通过内核态发生文件。
FileChannal.transferTo()就是利用整个实现的。

## 例子
```
public  void readFile(String fileName){
     try {
         RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, "rw");
         FileChannel fileChannel = randomAccessFile.getChannel();
         ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
         int byteBufferRead = fileChannel.read(byteBuffer);
         while (byteBufferRead != -1){
             byteBuffer.flip();
             while(byteBuffer.hasRemaining())
             {
                 System.out.print((char)byteBuffer.get());
             }
             byteBuffer.compact();
             byteBufferRead = fileChannel.read(byteBuffer);
         }
     } catch (FileNotFoundException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
```

```
Selector selector = null;
 ServerSocketChannel ssc = null;
 try{
     selector = Selector.open();
     ssc= ServerSocketChannel.open();
     ssc.socket().bind(new InetSocketAddress(PORT));
     ssc.configureBlocking(false);
     ssc.register(selector, SelectionKey.OP_ACCEPT);
     while(true){
         if(selector.select(TIMEOUT) == 0){
             System.out.println("==");
             continue;
         }
         Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
```
