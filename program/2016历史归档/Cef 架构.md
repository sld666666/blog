#  [Cef 架构](http://www.cnblogs.com/sld666666/p/4138307.html)

cef支持各种语言和多种操作系统。在设计的时候充分考虑了性能和易用性。cef核心功能提供了c和c++的接口。cef提供了和主程序之间的通信能力(利用
custom plugins, protocols,javascrpit object 和 javascript
extensions)。主应用程序可以选择性的使用控制 资源的加载，切换， context menus， printing等。

## 依赖

  1. chromium
  2. webkit
  3. v8
  4. skia
  5. angle

## 线程注意事项

CEF线程有如下几种

    typedef enum {
        TID_UI,
        TID_DB,
        TID_FILE,
        TID_FILE_USER_BLOCKING,
        TID_PROCESS_LAUNCHER,
        TID_CACHE,
        TID_IO,
        TID_RENDERER,
    } cef_thread_id_t;

在使用线程的时候需要注意如下几点：

  1. 千万不要阻塞UI线程
  2. UI线程会任务是主线程，当 CefSettings.multi_threaded_message_loop = false的时候。
  3. 所有的webkit和V8的交互必须用 TID_RENDERER线程
  4. CefPostTask 方法可以再不同线程中进行异步调用

## 接口

  1. CefApp，此接口用来传递到CefInitialize()，和允许应用程序定制全局，如资源加载，代理。这些功能是由所有进程共享的，有些必须实现浏览器的过程中，必须在渲染过程中执行。见详情头文件的注释。
  2. CefClient，此接口用来传递到CefCreateBrowser() or CefCreateBrowserSync()，和充当单独CEF Browser事例和客户端应用程序的连接，也负责请求和显示处理的接口。请求处理，显示处理等额外的接口，通过这个接口暴露。
  3. CefBrowser，公开由浏览器提供的功能。包括前进后退导航，来源检索，加载请求等，一个CefBrowser可能有一到多个子类的CefFrame对象。在一个特定的过程或一个特定的线程必须调用一些方法，所以仔细阅读文档。
  4. CefBrowserHost - 公开有关运行browser进程中唯一可用的browser窗口的功能。例如，检索本地父窗口句柄，或销毁browser窗口。  
CefRenderProcessHandler - 公开WebKit和V8对渲染进程中应用程序的集成能力。通过CefApp返回此对象的一个​​实例。

## 进程注意事项

CEF3使用了很多不同的进程：

  1. Broser process- 这个进程可以认为是应该程序的主进程，当调用CefInitialize()的时候建立
  2. Render process- web容器(webkit和v8)在此进程中执行
  3. plugin process- 插件（如 Flash）
  4. GPU process-GPU渲染进程
  5. Utility process- 各种其他任务在这个进程中跑。

所有关于chrome 进程的资料可以在[这里](http://www.chromium.org/developers/design-documents
/multi-process-architecture)和[这里](http://www.chromium.org/developers/design-
documents/process-models)找到。

CefBrowser 和 CefFrame 在browser和render进程中都存在，并且传递一系列callbacks。
CefProcessMessage能够在browser进中利用CefBrowser::SendProcessMessage 函数发出，
并且在CefClient::OnProcessMessageRecieved
和CefRenderProcessHandler::OnProcessMessageRecieved 接收。

## 重要的细节

CEF3 有如下几个比较重要的类：

  * CefMainDelegate - 用于普通进程的逻辑过程
  * CefContentClient- 在普通进程 展现Content Api的回调。
  * CefContext-在browser进程中，展现全局的CEF上下文。 一个单独的CefContext对象时由CefInitialize()建立，有CefShutdown销毁。
  * CefBrowserMainParts- browser 进程的逻辑
  * CefContentBrowserClient-在browser 进程展现Content Api的回调。
  * CefBrowserHostImpl-是CefBrowserHost 的实现
  * CefContentRendererClient- 在render 进程中展现Content Api的回调
  * CefBrowserImpl- CefBrowser的实现者

posted @ 2014-12-02 19:43 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4138307) 收藏

##备注 
 @post in:2014-12-02 19:43