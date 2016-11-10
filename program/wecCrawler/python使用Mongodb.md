
# python 使用mongodb

## 安装
 pip install Pymongo

 好吧下不下来， 选择用源码安装

 [这里](https://github.com/mongodb/mongo-python-driver)下载
然后python setup.py install

## 使用

###数据库连接

```
def init(self, ip, host, dbname):
      client =  pymongo.MongoClient(ip,host)
      self.db = client[dbname]
```
