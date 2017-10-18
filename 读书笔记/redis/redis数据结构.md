# redis数据结构

## string
string是redis最基本的类型，你可以理解成与Memcached一模一样的类型，一个key对应一个value。
string类型是二进制安全的。意思是redis的string可以包含任何数据。比如jpg图片或者序列化的对象 。
string类型是Redis最基本的数据类型，一个键最大能存储512MB

	set name "sld"
	get name

## Hash

Redis hash 是一个键值对集合。
Redis hash是一个string类型的field和value的映射表，hash特别适合用于存储对象。
		
	hmset user:1 name passworld
	hgetall user:1

##List

	lpush sld redis
	lpush sld db
	lrange sld 0 10

	"redis"
	"db"

## set
集合是通过hash表实现的， 所以查找的时间复杂度为o(1)

## zset
Redis zset 和 set 一样也是string类型元素的集合,且不允许重复的成员。
不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
zset的成员是唯一的,但分数(score)却可以重复。