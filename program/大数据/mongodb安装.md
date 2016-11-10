

## windows 安装

1. 下载 http://www.mongodb.org/downloads
2.

## linux 安装

## 使用

### 启动服务器
1. md mongodb\data\db
2. mongod.exe  --dbpath mogodb\data\db

注意： 如果您的路径包含空格，请将路径用双引号引用

## 启动客户端

1. cd E:\Program Files\MongoDB\Server\3.2\bin
2. mongo.exe
3. 创建数据库： user book
4. 查看数据库: show dbs
5. 插入： db.book.insert({"name":"sld"})// 看这是mogodb的文档
6. 查找： db.book.find()， db.book.find({"name": "sld"})
7. 删除： db.book.remove({"name" : "sld"})
