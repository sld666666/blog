#  [Qt
中的多线程](http://www.cnblogs.com/sld666666/archive/2010/12/26/1917308.html)

这篇文章主要介绍Qt 中多线程.。多线程是一个很大的课题， 这里我介绍QThread 和QFuture的实现方式。

## 1\. QThread

用QThread创建线程非常简单， 只需要从QThread派生，然后重载 run成员函数即可。

     1 class MyThread : public QThread   
     2 {   
     3     Q_OBJECT  
     4  public:   
     5     MyThread();   
     6     ~MyThread();   
     7  protected:   
     8     void    run()   
     9     {   
    10         cout << "I run the thread" <<endl;   
    11     }  
    12 };

在主函数中我们可以这样调用：

    1 int main(int argc, char *argv[])   
    2 {   
    3     QCoreApplication a(argc, argv);   
    4     MyThread myThread;   
    5     myThread.start();  
    6     return a.exec();   
    7 }

## 2\. 线程同步

Qt 提供了QMute, QReadWriteLock, QSemaphore 和QWaitCondition来同步线程。

QMutex提供了一种保护一个变量或一段代码的方法。

使用互斥量的一个问题就是每次只有一个线程可以访问一个变量， 如果我们要尝试多个线程访问某一个变量的话就用QReadWriteLock。

## 3\. QFuture

QFuture表示异步计算的结果。用QtConcurrent::run
开始一个线程，用QFutureWatcher来监视整个线程。这里写了一耳光future类：

.h

    class Future : public QObject   
    {   
        Q_OBJECT   
    public:   
        Future(QObject *parent = NULL);   
        ~Future();  
        public slots:   
            void displayFinished()   
            {   
                cout << "finshed thread" <<endl;   
            }   
    private:   
        void run_thread();   
    private:   
        QFuture<void> *future_;   
        QFutureWatcher<void> *watcher_;  
    };

.cpp

     1 void my_func()   
     2 {   
     3     double sum(0.0);  
     4     for (int i=0; i < 1000; i++)   
     5     {   
     6         for (int j = 0; j < 1000; j++)   
     7         {   
     8             sum = sum + i + j;   
     9         }   
    10     }  
    11     cout << sum << endl;;   
    12 }  
    13 Future::Future(QObject *parent)   
    14     : QObject(parent)   
    15 {   
    16     future_ = new QFuture<void>;   
    17     watcher_ = new QFutureWatcher<void>;   
    18     run_thread();   
    19     cout << "now here" <<endl;  
    20     QObject::connect(watcher_, SIGNAL(finished()),   
    21         this, SLOT(displayFinished()));   
    22 }  
    23 Future::~Future()   
    24 {  
    25 }  
    26  void Future::run_thread()   
    27 {   
    28     *future_ = QtConcurrent::run(my_func);   
    29     watcher_->setFuture(*future_);   
    30 }

当Future future的时候就会开辟一个线程来执行my_func 函数。

posted @ 2010-12-26 19:02 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1917308) 收藏

##备注 
 @post in:2010-12-26 19:02