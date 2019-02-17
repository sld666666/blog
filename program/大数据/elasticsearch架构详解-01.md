# elasticsearch架构详解

## 安装
./bin/elasticsearch

curl 'http://localhost:9200/?pretty'

http://localhost:9200/megacorp/employee

./bin/kibana
http://localhost:5601/app/sense

## 概念性介绍
存储面向文档 --> json


Relational DB -> Databases -> Tables -> Rows -> Columns
Elasticsearch -> Indices   -> Types  -> Documents ->

举个例子：

```
curl -X PUT "localhost:9200/megacorp/employee/1" -H 'Content-Type: application/json' -d'
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}
'
```
- megacorp 索引名称
- employee 类型名称
- 1 雇员id

查询
```
curl -X GET "localhost:9200/megacorp/employee/_search?q=last_name:Smith"
```
