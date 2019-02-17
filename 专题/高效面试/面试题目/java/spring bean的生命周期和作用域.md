# spring bean的生命周期和作用域

每次都是解析xml或者扫描注解(ClassPathBeanDefinitionsScanner中scan)， 由SpringApplicaiton创建 SrpingApplicaitonContex,
再由BeanFactory中负责new instance。 其实真正的注册在DefaultSingletonBeanRegistry中，注册好了的对象会放到其中的map中Map<String, Object> singletonObjects.

其实最关键的过程在 AbstractApplicationContext中
```
// Allows post-processing of the bean factory in context subclasses.
      postProcessBeanFactory(beanFactory);

      // Invoke factory processors registered as beans in the context.
      invokeBeanFactoryPostProcessors(beanFactory);

      // Register bean processors that intercept bean creation.
      registerBeanPostProcessors(beanFactory);

      // Initialize message source for this context.
      initMessageSource();

      // Initialize event multicaster for this context.
      initApplicationEventMulticaster();

      // Initialize other special beans in specific context subclasses.
      onRefresh();

      // Check for listener beans and register them.
      registerListeners();

      // Instantiate all remaining (non-lazy-init) singletons.
      finishBeanFactoryInitialization(beanFactory);

      // Last step: publish corresponding event.
      finishRefresh();
```

销毁，都一样scope=prototype的对象，父销毁以后其也销毁
