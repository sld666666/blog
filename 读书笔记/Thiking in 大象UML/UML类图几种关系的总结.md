
# UML类图几种关系的总结

在Uml类图中， 有几种常见的关系：
- association  关联
- aggregation 聚合
- composition	组合
- dependency	依赖
- generalization 泛化
- Realization 实现


## generalization
泛化关系是一种继承关系。 如老虎是动物的一种。如下图所示：
![](https://img.alicdn.com/imgextra/i4/46754672/TB27MQFgpXXXXaDXpXXXXXXXXXX_!!46754672.jpg)
## Realization
实现是接口类和实现类的关系，还是以老虎和动物为例子。如下图所示：
![](https://img.alicdn.com/imgextra/i1/46754672/TB2iAEWgpXXXXaVXXXXXXXXXXXX_!!46754672.jpg)
## Association
聚合跟组合其实都属于关联，提出一个概念是为了和依赖做区分
##Composition
组合，强关联， 整体和部分的关系， 部分不能离开整体而存在。 如大雁的翅膀和大雁
![](https://img.alicdn.com/imgextra/i3/46754672/TB2mw3vgpXXXXcBXpXXXXXXXXXX_!!46754672.jpg)

## Aggregation
聚合，弱关联， 非紧密的聚合。比如大雁和雁群，大雁可以离开雁群独立存在。
![](https://img.alicdn.com/imgextra/i2/46754672/TB2n8ASgpXXXXbPXXXXXXXXXXXX_!!46754672.jpg)

## Dependency
依赖， 一个类中可能要用到另一个类的方法， 但是这种关系是临时的、脆弱的。 代码表现为成员变量或者入参数。如有一个终结者，他可以毁灭地球。 在设计的时候地球是作为参数传入的。
![](https://img.alicdn.com/imgextra/i2/46754672/TB2uSgTgpXXXXbuXXXXXXXXXXXX_!!46754672.jpg)

