#  [Qt
多级menu](http://www.cnblogs.com/sld666666/archive/2011/01/25/1944554.html)

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201101/20110125140021
6106.png)

实现如上的一个多级menu， Qt 中实现非常简单。 首先明确一个概念， QMenu表示“容器”， QAction表示具体的项。
所有上图中”其他”，“发票”等都要是QMenu， 而代充类型、有发票都要是QAction， 所以代码实现就非常简单了。

1\. 构造

    1 pMenu_ = new QMenu(tbaTableView_);   
    2 pBatchEditMenu_ = new QMenu(pMenu_);   
    3 pBatchEditOtherMenu_ = new QMenu(pBatchEditMenu_);   
    4 batchOtherBillMenu_ = new QMenu(pBatchEditMenu_);   
    5 batchOtherWarrantyMenu_ = new QMenu(pBatchEditMenu_);   
    6 batchOtherAutoSendMenu_ = new QMenu(pBatchEditMenu_);   
    7 batchOtherRecommendMenu_ = new QMenu(pBatchEditMenu_);   
    8 batchOtherDiscountMenu_ = new QMenu(pBatchEditMenu_);

2.析构

    1     delete batchOtherBillMenu_;   
    2     delete batchOtherWarrantyMenu_;   
    3     delete batchOtherAutoSendMenu_;   
    4     delete batchOtherRecommendMenu_;   
    5     delete batchOtherDiscountMenu_;   
    6     delete pBatchEditOtherMenu_;   
    7     delete pBatchEditMenu_;   
    8     delete addItemsTimer_;   
    9     delete pMenu_;

注意顺序。

3\. 代码实现

    1   pBatchEditOtherMenu_->setTitle(tr("其他(Q)"));   
    2     batchOtherBillMenu_->setTitle(tr("发票"));   
    3     batchOtherBillMenu_->addAction(tr("有发票"),this,SLOT(haveBillBatchEdit()));   
    4     batchOtherBillMenu_->addAction(tr("无发票"),this,SLOT(noBatchEdit()));   
    5     batchOtherRecommendMenu_->setTitle(tr("橱窗推荐"));   
    6     batchOtherDiscountMenu_->setTitle(tr("会员打折"));   
    7     pBatchEditMenu_->addMenu(pBatchEditOtherMenu_);   
    8     pBatchEditOtherMenu_->addMenu(batchOtherBillMenu_);

4\. 快捷键以及分割线

    1 QAction* openDetailPage = pMenu_->addAction(tr("打开宝贝页面"));   
    2     openDetailPage->setShortcut(QKeySequence(tr("Ctrl+B")));   
    3     pMenu_->addSeparator();

posted @ 2011-01-25 14:00 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1944554) 收藏

##备注 
 @post in:2011-01-25 14:00