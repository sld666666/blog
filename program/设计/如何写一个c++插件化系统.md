#  [如何写一个c++插件化系统](http://www.cnblogs.com/sld666666/p/3519467.html)

## 1.为什么需要插件化系统

“编程就是构建一个一个自己的小积木， 然后用自己的小积木搭建大系统”。

但是程序还是会比积木要复杂， 我们的系统必须要保证小积木能搭建出大的系统（必须能被组合），有必须能使各个积木之间的耦合降低到最小。

传统的程序结构中也是有模块的划分，但是主要有如下几个缺点：

a: c++二进制兼容

b: 模块对外暴露的东西过多，使调用者要关心的东西过多

c: 封装的模块只是作为功能的实现者封装，而不是接口的提供者

d: 可替换性和可扩展性差

而插件式的系统架构就是为了解决这样的问题。插件化设计的优点？插件化设计就是为了解决这些问题的，所以以上的缺点就是咱的优点

## 2.插件化系统的原理

指导性原则：“面向接口编程而不是实现编程”  
其接口的定义为interface, 其实转换一下的意思是面向纯虚类编程，当然也可以包装成面向服务和组件编程。  
如我可以这样定义一个接口(interface)

    interfacecptf IRole{
    　　virtual cptf ::ulong getHealth() = 0;
    　　virtual cptf ::ulong getHurt() = 0;
    　　virtual wstring getName() = 0;
    };

插件的目标就是实现IRole， 业务层的目标就是调用IRole， 业务层不知道IRole具体是如何实现的，而实现者也不用关心业务层是如何调用的。

## 3.插件化系统的目标

1). 使用者能通过规范，开发自己的插件，实用已有的插件，插件又能控制对外暴露的内容。  
2). 运行时候能动态安装、启动、停在、卸载  
3). 每一个插件提供一个或多个服务，其他插件是根据接口来获取服务提供者

## 4. 一个插件化系统应该是怎么构成的

OSGI，Java中影响力最大的插件化系统就是OSGI标准  
OSGI的定义：The dynamic module system for java  
借鉴osgi对插件系统的定义，我认为一个典型的插件系统应该有如下几个方面构成:  
“基础库+微内核+系统插件+应用插件”  
其中微内核 负责如下功能：

1、 负责插件的加载，检测，初始化。

2、 负责服务的注册。

3、 负责服务的调用。

4、 服务的管理。

##  5. 一个简单场景的随想

比如设计下如下的游戏场景：一个RPG游戏， 玩家控制一个英雄，在场景中有不同的怪物，而且随着游戏的更新，  
英雄等级的提升又会有不同的怪物出现， 这里就想把怪物设计为插件。  
首先工程是这样的布局的

![](http://images.cnitblog.com/blog/124057/201401/141725114398.png)

首先要在做的是定义接口， 这里我需要一个英雄的接口，有需要一个怪物的接口。

    interfacecptf IHero : public cptf ::core:: IDispatch
                               , public IRole {
          virtual    cptf ::ulong attack() = 0;
    };

    interfacecptf IOgre : public cptf ::core:: IDispatch
                               , public IRole {

    };

然后作为插件我需要实现一个Hero， 和多个Ogre

    class Hero : public ServiceCoClass<Hero >
                    , public ObjectRoot <SingleThreadModel>
                    , public cptf ::core:: IDispatchImpl<IHero >{

    class Wolf : public ServiceCoClass<Wolf >
                               , public ObjectRoot<SingleThreadModel >
                               , public cptf::core ::IDispatchImpl< IOgre>

    class Tiger : public ServiceCoClass<Tiger >
                               , public ObjectRoot<SingleThreadModel >
                               , public cptf::core ::IDispatchImpl< IOgre>　

最后，在主工程用我要用到这些插件

    void BattleMannager ::run()
    {
          hero_ = static_cast<IHero *>(serviceContainer_. getService(Hero_CSID , IHero_IID));
          if (!hero_ )return;
          printHero(hero_ );

          list<IService *> services = serviceContainer_ .getServices( IOgre_IID);
          list<IOgre *> ogres = CastUtils::parentsToChildren <IService, IOgre>(services );
          for_each(ogres .begin(), ogres.end (), bind(&BattleMannager ::printOgre, _1));

          services = serviceContainer_ .getServices( IHumanOgre_IID);
          list<IHumanOgre *> hummanOgres = CastUtils::parentsToChildren <IService, IHumanOgre>(services );
          for_each(hummanOgres .begin(), hummanOgres.end (), bind(&BattleMannager ::printHumanOgre, _1));
    }

以上， 因为逻辑层和插件实现层都已经好了， 整个流程也已经跑通，但是还是的疑问：服务是怎么加载的？

## 6\. 如何进行插件的加载以及服务的注册

借鉴OSGI， 我这里把系统设计为bundle+service的组合。 bundle是service的容器，service是功能的具体实现者。

在windows下，bundle用dll来表示。

那bundle在windwos下加载就很简单了LoadLibrary Api就行了

但是再c++中dll的接口还必须要考虑的一个问题就是c++的二进制兼容性：现在没有标准的 C++ ABI。这意味着，不同编译器（甚至同一编译器的不同版本）会
编译出不同的目标文件和库。这个问题导致的最显而易见的问题就是，不同编译器会使用不同的名称改写算法。这样对插件的接口来说是致命的。当然我们可以用c api来作
为接口，但是这样势必会对整体的设计产生影响，而且作为一个装B的c++程序员，我们怎么能容忍要借用低级语言的特性来实现我们的功能呢。当然幸亏还有另外一种方式，
那就是虚表。当然不是所有的c++编译器对虚表的实现也是不一样的(好吧~~)，但是至少主流(多主流~~不能确定)的编译器虚表都是在对象的第一个位置。好吧，现在
决定用虚表来对插件接口的实现了，所以我们就可以用这样的方式来计算具体实现类的地址了

    #define  CPTF_PACKING 8
    #define cptf_offsetofclass (base, derived) \
         (( cptf::ulong )(static_cast< base*>((derived *)CPTF_PACKING))- CPTF_PACKING)

哇，好神奇的代码， 这个是为什么呢。 这个就需要对c++内存对象模型需要深入得了解了，可能需要拜读<c++内存对象模型>，这里篇幅有限这里就不解释了。但是如
果有看官想要问“你为什么这么天才能想出这样的写法？”，虽然我很想说我很天才，但是其实正是情况是我参考的atl中的源码，而且整个插件加载过程我都是山寨了atl
中的相关代码的。

但是还是有一个问题， 在GameMain中，认识的是IHero， 根本不知道有个Hero的实现，所有可能有这样的代码IHero* hero = New
Hero() 这样动作。  
那我们要如何进行这样的new动作。 当然我们说Hero是在Role dll中的， 在dll被加载的时候可以new Hero，
然后把hero对象的地址放到某个堆中，标志让GameMain使用。作为一个转换的伪设计人员， 我也是认为这样会有性能问题的， 我不仅要做到加载，
还要做到懒加载。  
那如何做到懒加载呢？  
感谢微软，在vc++中有机制帮我们做到，在其他的编译器中也会有其他的实现，但是这里我们只做了vc++中的实现。  
首先声明一个自己的段，段名可以叫cptf：

    #pragma section ("CPTF$__a", read, shared )
    #pragma section ("CPTF$__z", read, shared )
    #pragma section ("CPTF$__m", read, shared )

然后在编译的时候，把具体实现的类的Create函数地址放到这个段中

    #define CPTF_OBJECT_ENTRY_AUTO (class) \
    　　__declspec(selectany ) AutoObjectEntry __objMap_##class = {class::clsid (), class:: creatorClass_::createInstance }; \
    　　extern "C" __declspec( allocate("CPTF$__m" )) __declspec(selectany ) AutoObjectEntry* const __pobjMap_ ##class = &__objMap_ ##class; \
    　　CPTF_OBJECT_ENTRY_PRAGMA(class )

最后在加载的时候，变量这个段，如果csid命中，则调用Create方法

    inline bool cptfModuleGetClassObject( const CptfServiceEntities * cpfgModel
                                                         , const cptf::IID & csid
                                                         , const cptf::IID & iid
                                                         , void** rtnObj)
         {
                bool rtn (false);
                assert(cpfgModel );
                for (AutoObjectEntry ** entity = cpfgModel->autoObjMapFirst_
                    ; entity != cpfgModel ->autoObjMapLast_; ++entity)
               {
                     AutoObjectEntry* obj = *entity;
                     if (obj == NULL) continue;
                     if (obj ->crateFunc != NULL && csid == obj-> iid){
                          rtn  = obj ->crateFunc( iid, rtnObj );
                          break;
                    }
               }

                return rtn ;
         }

总结下流程：  
1\. GameMian使用的是IHero,  
2\. Hero是IHero的实现者，在编译的规程中，把Create Hero的方法编译到固定段中  
3\. GameMian进行new的时候其实调用的是Dll固定段中的函数地址  
4\. 利用 上面的cptf_offsetofclass 宏实现对IHero的

## 7\. 服务的管理

每一个服务都需要一个id来标志它， 这里就用guid， 命名为IID---interface id  
每一个服务的实现者也必须要有id来标志， 这也是一个guid， 命名为csid  
我们把服务和服务实现者的管理信息用配置文件管理起来，services.xml, 对Hero的定义

    <service>
              <bundle>Role.dll</bundle>
              <csid>500851c0-7c2a-11e3-8c28-bc305bacf447</csid>
              <description>hero</description>
              <name>Hero</name>
              <serviceId>99f9dd8f-7c1a-11e3-9f9d-bc305bacf447</serviceId>
              <serviceName>IHero</serviceName>
     </service>

当然一个插件的管理器也是必须的， 管理Service的注册，缓存，析构、获取，查询等。这里用ServiceContainer实现

## 8\. 基于插件的架构

基于插件系统的架构：

主要分三部分： 1. 使用其对象模型的主系统或主应用程序

2\. 插件管理器

3\. 插件

所有的插件但是从IService, 是参考Com中IUnkown

    interfacecptf IService{
                virtual    cptf ::ulong addRef() = 0;
                virtual cptf ::ulong release() = 0;
                virtual bool queryInterface( const cptf ::IID& iid, void**rntObj ) = 0;
         };

其实插件的内核并不复杂，复杂的是对插件接口的定义和封装，如何根据不同的业务场景抽象出不同的interface。

## 9\. 源代码

本文不是很水的理论，所有的理论都是经过代码验证的。

本文涉及到的代码在我的github上，<https://github.com/sld666666/cptf>

工程的目标是建立一个跨平台的c++插件开发框架， 现在的是一个能成功在vc++下运行demo的插件化framework

用了boost和stl，如果要深入了解core中的代码，还需要对模板有了解， 水深请勿轻易尝试

当然有的看官会对core中的代码非常熟悉，那可能你发现了， 我是山寨atl实现的

## 8\. 今后改进的方向

1\. service如何释放， 还在考虑是用野指针还是智能指针还是垃圾回收机制

2\. 错误处理

3\. 跨平台和跨编译器

posted @ 2014-01-14 17:49 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=3519467) 收藏

##备注
 @post in:2014-01-14 17:49
