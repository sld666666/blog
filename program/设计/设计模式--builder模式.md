## 设计模式----builder模式

一个典型的场景

```
    package builder.normal;

    /**
     * Created by diwu.sld on 2017/3/28.
     */
    public class Job{
        private String name;
        private String group;

        public void setName(final String name){
            this.name = name;
        }

        public  void setGroup(final String group){
            this.group = group;
        }

        public void run(){
            System.out.println(this.name);
            System.out.println(this.group);
        }


        public  static void main(String [] args){
            Job job = new Job();
            job.setName("name_abc");
            job.setGroup("group1");
            job.run();
        }
    }

```

以上代码粗看起来问题不大，因为逻辑简单。但是其潜在的一个问题是run方法必须要在setName和setGroup之后。我们在创建Job的时候没法约束先设置在运行。 这时候就要借用builder模式来改造了。

```
    package builder.design;

    /**
     * Created by diwu.sld on 2017/3/28.
     */
    public class JobBuilder {
        private String name;
        private String group;


        protected JobBuilder(){

        }

        public static JobBuilder newJob(){
            return  new JobBuilder();
        }

        public  JobBuilder setName(String name){
            this.name = name;
            return  this;
        }

        public JobBuilder setGroup(String group){
            this.group = group;
            return  this;
        }

        public Job builder(){
            Job job = new Job();
            job.setGroup(this.group);
            job.setName(this.name);

            return  job;
        }

        public  static  void main(String [] args){
            Job job = JobBuilder.newJob()
                    .setName("name_abc")
                    .setGroup("group1")
                    .builder();
            job.run();
        }
    }

```

包了一个builder类来封装Job的生成，其带来了额外的复杂度。 带来的唯一好处是约束了Job的生成逻辑。 设计就是一种约束。