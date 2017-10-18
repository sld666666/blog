# sql 基本语法

几点说明：

	1. SQL 对大小写不敏感
	2. 分号是在数据库系统中分隔每条 SQL 语句的标准方法
	3. SQL 分为两部分：DML（数据定义语言）DDL（数据操作语言）

DML:

	1. select
	2. update
	3. insert into 
	4. delete


DDL:

	1. create table
	2. alter table
	3. create table
	4. drop table
	5. create index
	6. drop index

## 基本语法

 1. select * from auction_autions
 2. select distinct seller_id from auction_sku

### 条件
 where

可以作为判断的是： =， <>, >, <, <=, <=, between, like

当然还可以加 and , or

	SELECT * FROM Persons WHERE (FirstName='Thomas' OR FirstName='William')
AND LastName='Carter'

### 排序
order by

	SELECT DISTINCT seller_id FROM auction_sku ORDER BY seller_id

	SELECT DISTINCT seller_id FROM auction_sku ORDER BY seller_id DESC,   auction_id ASC

### insert into 
	
	insert into table_name values(v1, v2);
	insert into table_name (column1, column2) values(v1, v2)

### update

	update table_name set column1 = v1

### delete

	delete from table_name

当然还有一个相同的：

	truncate table process_time;

