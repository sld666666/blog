# Integer类

Integer是对int的包装。但是做了缓存

```
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```
默认情况下缓存-128到128
所以以下为Ture
```
public static void main(String[] args) {
      Integer int1 = Integer.valueOf("100");
      Integer int2 = Integer.valueOf("100");
      System.out.println(int1 == int2);
  }
```
