#  [设计模式----
单件模式（c++）](http://www.cnblogs.com/sld666666/archive/2011/05/27/2060256.html)

单件模式是设计模式中最简单的模式了。

定义： 确保一个类只有一个实例，并提供一个全局的访问点。

  * 把一个类设计成自己管理的一个单独实例，同时避免其他类再自行生成实例(所以构造函数用protect或privite)
  * 也提供全局的访问点。public函数

看下代码的实现：

1 class Singleton  
2 {  
3 protected:  
4 Singleton(){};  
5 public:  
6 ~Singleton(){};  
7  
8 static shared_ptr<Singleton> getInstance()  
9 {  
10 if (!pInstance_.get())  
11 {  
12 pInstance_ = shared_ptr<Singleton>(new Singleton());  
13 return pInstance_;  
14 }

else

{

return pInstance_;

}  
15 }  
16 private:  
17 static shared_ptr<Singleton> pInstance_;  
18 };

shared_ptr<Singleton> Singleton::pInstance_ = shared_ptr<Singleton>();

单件的继承//add in 201205-15

对于单件模式， 因为内存模型的关系， 理论上是不能被继承的，但是可以利用c++模板技术实现单件模式的继承：

    #include <QObject>
    
    template <typename T>
    class ISingleton : public QObject
    {
    private:
    	struct ObjectCreator
    	{
    		ObjectCreator() 
    		{ 
    			ISingleton<T>::instance();
    		}
    
    		inline void do_nothing() const { }
    	};
    	static ObjectCreator objCreator_;
    
    protected:
    	ISingleton(){};
    public:
    	static T & instance()
    	{
    		static T obj;
    		objCreator_.do_nothing();
    		return obj;
    	}
    };
    
    template <typename T>  
    typename ISingleton<T>::ObjectCreator ISingleton<T>::objCreator_;

此段代码从 boost的单件中修改而来。

当需要有一个新的单件类的时候，可以用如下方法：

    class TcpCommandControl : public ISingleton<TcpCommandControl>
    {
    	Q_OBJECT
    
    public:
    	~TcpCommandControl();
    
    	void	execute(shared_ptr<TcpRequestBase> req, shared_ptr<TcpResponseBase> resp);
    
    private:
    	TcpCommandControl();
    
    	friend class ISingleton<TcpCommandControl>;
    
    private slots:
    	void	responseReceiveSlot(QByteArray const& packet);
    
    private:
    	shared_ptr<PacketTcpSocket>		tcpSocket_;
    	shared_ptr<TcpResponseBase>		response_;
    };

posted @ 2011-05-27 20:47 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=2060256) 收藏

##备注 
 @post in:2011-05-27 20:47