#  [MapReduce 简介](http://www.cnblogs.com/sld666666/p/5388840.html)

# 2\. MapReduce 简介

MapReduce 实际上是分为两个过程

  1. map 过程 ： 数据的读取
  2. reduce 过程 ： 数据的计算

并行计算是一个非常复杂的过程， mapreduce是一个并行框架。

在Hadoop中，每个MapReduce任务都被初始化为一个Job，每个Job又可以分为两种阶段：map阶段和reduce阶段。这两个阶段分别用两个函数表示
，即map函数和reduce函数

我们可以看下典型的官方列子

## 开发

用idea 开发开发

pom.xml 添加依赖

    <dependencies>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>2.7.2</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-core</artifactId>
        <version>1.2.1</version>
    </dependency>
    </dependencies>

写代码：

    import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.IntWritable;  
import org.apache.hadoop.io.LongWritable;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapred.*;

import java.io.IOException;  
import java.util.Iterator;  
import java.util.StringTokenizer;

/**

  * Created by diwu.sld on 2016/4/13.  
*/  
public class WordCount{

public static class CountMap extends MapReduceBase  
implements Mapper{  
private final static IntWritable one = new IntWritable(1);  
private Text word = new Text();

        public void map(LongWritable longWritable,
                    Text text,
                    OutputCollector<Text, IntWritable> outputCollector,
                    Reporter reporter) throws IOException {
        String line = text.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while(tokenizer.hasMoreTokens()){
            word.set(tokenizer.nextToken());
            outputCollector.collect(word, one);
        }
    }

}

public static class CountReduce extends MapReduceBase implements  
Reducer {  
public void reduce(Text key, Iterator values,  
OutputCollector output, Reporter reporter)  
throws IOException {  
int sum = 0;  
while (values.hasNext()) {  
sum += values.next().get();  
}  
output.collect(key, new IntWritable(sum));  
}  
}

public static void main(String[] args) throws Exception {  
JobConf conf = new JobConf(WordCount.class);  
conf.setJobName("wordcount");

        conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(IntWritable.class);
    conf.setMapperClass(CountMap.class);
    conf.setCombinerClass(CountReduce.class);
    conf.setReducerClass(CountReduce.class);
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);
    FileInputFormat.setInputPaths(conf, new Path(args[0]));
    FileOutputFormat.setOutputPath(conf, new Path(args[1]));
    JobClient.runJob(conf);

}  
}

然后打好包 HadoopDemo:

    1. Project Sturcture->Artifacts->+
    2. Build Artifacts

放到 hadoop 目录下运行

## 运行

  1. bin/hadoop fs -mkdir -p input
  2. bin/hadoop fs -copyFromLocal README.txt input
  3. bin/hadoop jar demos/HadoopDemo.jar WorldCount input output
  4. bin/hadoop fs -cat output/* 或者bin/hadoop fs -ls output
  5. bin/hadoop fs -cat output/part-r-00000

## 总结

如果有N个文件，和对这个N个文件的计算，我们可以用并行来提高运行效率。但是文件有大有小， 计算量有多又少， 如何进行并行和分配任务是一个非常繁琐的事情。
所以有了Hadoop这个并行框架来解决我们的问题。

Hadoop 主要分为两大块： 分布式文件存储和分布式计算。

在分布式文件存储中，他会把文件分割为想多相同的小块。

posted @ 2016-04-13 21:23 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=5388840) 收藏

##备注 
 @post in:2016-04-13 21:23