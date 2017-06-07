# java8函数式编程

## 对函数式编程的定义

普通的函数是以数据为参数，传递的是数据，而函数式是对行为的封装，传递的是行为。

lambada是传递行为的一种很好的方式。

## lambada表达式

有四种形式：

1. 无参数： () ->System.out.println("hello world")
2. 一个参数: (x)->System.out.printlin(x)
3. 多个参数： (x, y) -> x + y, 注意不是两个数字相加的结果，返回的是一个函数。 或者也可以这样 (Long x, Long y) -> x + y

Lambda 表达式的类型依赖于上下文环境， 是由编译器推断出来的.


lambada表达式也称为闭包。未赋值的变量和周围环境隔离起来，然后绑定到一个新的值上去。如：

        ```
        String name = getUserName();
        name = formatUserName(name);
        button.addActionListener(event -> System.out.println("hi " + name));
        ```
这样是编译不通过的。

## steam
stream可以用来改善for循环。

```
public class StreamTestor {
    private List<Integer> datas = Arrays.asList(1, 2,3,4,5);

    public void normalIterator(){
        int counts = 0;
        Iterator<Integer> iter =  datas.iterator();
        while (iter.hasNext()){
            int tmp = iter.next();
            if (tmp > 3){
                ++counts;
            }
        }

        System.out.println(counts);
    }

    public void lambadaIterator(){
        long counts = this.datas.stream()
                .filter( input -> input > 3)
                .count();

        System.out.println(counts);
    }

    public static void main(String [] args){
        StreamTestor streamTestor = new StreamTestor();
        streamTestor.normalIterator();
        streamTestor.lambadaIterator();
    }
}
```
像 filter 这样只描述 Stream， 最终不产生新集合的方法叫作惰性求值方法； 而像 count 这样
最终会从 Stream 产生值的方法叫作及早求值方法。判断是不是惰性求值很简单， 只需要看返回的是不是Stream就可以了。


### collect

```
    public  void lambadeUpper(){
        List<String> collected = Stream.of("a", "b", "hello")
                                .map(str -> str.toUpperCase())
                                .collect(Collectors.toList());
        System.out.println(collected);
    }

```
 collect(toList()) 方法从 Stream 中生成一个列表

### map

```
     public void normalUpper(){
        List<String> collected = new ArrayList<>();
        for (String string : Arrays.asList("a", "b", "hello")) {
            String uppercaseString = string.toUpperCase();
            collected.add(uppercaseString);
        }

        System.out.println(collected);
    }

    public  void lambadeUpper(){
        List<String> collected = Stream.of("a", "b", "hello")
                                .map(str -> str.toUpperCase())
                                .collect(Collectors.toList());
        System.out.println(collected);
    }

```

 map接受一个参数，返回一个值。 如果看下map的入参， 我们可以发现参数必须是 Function 接口的实现。Function接口是包含一个参数的普通接口。

## min 和 max

 ```

    public  void testMin(){
        Integer min = this.datas.stream()
                .min(Comparator.comparing(x -> x)).get();
        System.out.println(min);
    }

    public  void testMax(){
        Integer max = this.datas.stream()
                .max(Comparator.comparing(x -> x)).get();
        System.out.println(max);
    }
 ```
求最大值和最小值是比较常用的操作，所以Stream封装了这样操作。值得一提的是， 我们看下comparing函数的实现：

    ```
        public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
            Function<? super T, ? extends U> keyExtractor)
    {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
            (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }
    ```
    
 他接受一个函数，返回另一个函数。
 

### 组合
函数式编程的一个重要好处是能把功能分解为很小的部分，但是非常易于组合，而且组合起来威力强大。
 ```
 Set<String> origins = album.getMusicians()
.filter(artist -> artist.getName().startsWith("The"))
.map(artist -> artist.getNationality())
.collect(toSet());
 ```   

### 高阶函数
以函数为参数或者返回为函数的都认为是高阶函数。


## SOLID 原则

SOLID 原则是设计面向对象程序时的一些基本原则。 原则的名字是个简写， 分别代表了
下 面 五 个 词 的 首 字 母： Single responsibility、 Open/closed、 Liskov substitution、 Interface
设计和架构的原则 ｜ 107
segregation 和 Dependency inversion。 这些原则能指导你开发出易于维护和扩展的代码。



