#  [cef 介绍](http://www.cnblogs.com/sld666666/p/4137356.html)

## 介绍

cef 是一个基于google chromiun的简单的框架。 它主要是作为一个内嵌浏览器嵌入到客户端应用程序中。

可以再 <http://cefbuilds.com> 下载最新的编译版本。

## 总体框架预览

  1. CEF 使用了多进程。主进程是“browser”进程。 而子进程是由rederes, plugins, GPU, 等组件创建。
  2. 在ECF的所有进程中，都可以有多线程。CEF提供了函数和接口在不同的线程中来传递任务。
  3. 一些回调方法和函数只能在特定线程和进程中使用。在使用API之前请确保仔细阅读注释。

## 源代码

cefsimple 工程初始化CEF并创建了一个简单的浏览器窗口。

  1. 系统在入口点函数中（man或者wWinMain）函数中开启browser进程
  2. 入口点函数： 
    1. 创建SimpleApp的实例，在这个类中保存process-level callbacks.
    2. 初始化CEF并开启消息循环。
  3. 当CEF初始化完毕以后， SimpleApp::OnContextInitialized()会被调用。在这个方法中： 
    1. 创建一个单例的SimpleHandler
    2. 由CefBrowserHost::CreateBrowserSync()创建一个浏览器窗口
  4. 所有的浏览器共享同一个在SimpleHandler。SimpleHandler负责定制浏览器的行为并保存browser-related callbacks（loading状态，标题行为等）
  5. 当浏览器窗口被关闭的时候， SimpleHandler::OnBeforeClose() 被调用。当所有浏览器窗口被关闭，CEF消息循环退出。

可以看下如下代码：

    int APIENTRY wWinMain(HINSTANCE hInstance
                      , HINSTANCE hPrevInstance
                      , LPTSTR lpCmdLine
                      , int nCmdShow)
    {
        CefMainArgs args(hInstance);
        CefRefPtr<MySimpleApp> app(new MySimpleApp);
        int exitCode = CefExecuteProcess(args, app, NULL);
        if(exitCode >= 0) return exitCode;
        CefSettings settings;
        CefInitialize(args, settings, app, NULL);
        CefRunMessageLoop();
        CefShutdown();
        return 0;
    }

此时进程开启，但是没有任何窗口， 如果我们需要建立窗口需要如下

    CefWindowInfo winInfo;
    winInfo.SetAsPopup(NULL, "myCefSimple");
    CefBrowserSettings browser_settings;
    CefRefPtr<CefClient> client(new MySimpleClient());
    std::string url = "file:///D:/project/github/cptf/resource/binding.html";
    CefBrowserHost::CreateBrowser(winInfo
        , client.get()
        , url
        , browser_settings
        , NULL);

这样就显示出来窗口了。  
但是我们发现在关闭的时候进程没有关掉， 所以我们要做如下动作。

    class MySimpleClient : public CefClient
                    , public CefLifeSpanHandler
    void MySimpleClient::OnBeforeClose(CefRefPtr<CefBrowser> browser) {
        CEF_REQUIRE_UI_THREAD();
        CefQuitMessageLoop();
    }

## 总结

App和Client是CEF中最重要的两个类。CefExecuteProcess 和 CefInitialize 是建立app的两个最重要方法。  
而CreateBrowser 是创建浏览器窗口的最重要方法。

posted @ 2014-12-02 14:30 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4137356) 收藏

##备注 
 @post in:2014-12-02 14:30