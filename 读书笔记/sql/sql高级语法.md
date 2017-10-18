# sql高级语法



## top
top 字句用户规定返回记录的数目， 不是所有数据库都支持的

## like
是作为条件判断的一种，可以用 "%"进行通用匹配， 如： like“name%”

## in

	where id not in(1,2)

## BETWEEN ... AND

	where id between 1 and 100

## 别名
可以指定列名
	
	SELECT  title AS NAME FROM auction_auctions ORDER BY seller_id

也可以指定表名

	select s.short_code, u.email from seller as s, user as u where s.id_seller < 10 and u.fk_seller = s.id_seller;

## join

join 可以把多个表关联起来， 有几种不同的join方式：

1. inner join
2. join
3. left join
4. right join
5. full jon



	JOIN USER ON user.fk_seller = s.id_seller  AND s.id_seller < 10  
ORDER BY s.id_seller 

## ALTER
修改表结构

 	ALTER TABLE table_name ADD column_name datatype

	ALTER TABLE table_name 
	DROP COLUMN column_name

	ALTER TABLE table_name
	ALTER COLUMN column_name datatype