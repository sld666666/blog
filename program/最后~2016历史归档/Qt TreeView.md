#  [Qt
TreeView](http://www.cnblogs.com/sld666666/archive/2011/02/15/1955179.html)

这篇文章讲述如何实现如下的一个treeView.

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201102/20110215142318
9945.png)

首先设置treeView的外框， 大小， model等

     1 QStandardItemModel *goodsModel = new QStandardItemModel(0, 4,this);  
     2   
     3 ui.treeView_->setColumnWidth(0,50);   
     4 ui.treeView_->setColumnWidth(1,200);   
     5     ui.treeView_->setColumnWidth(2,200);   
     6     ui.treeView_->setColumnWidth(3,200);   
     7     goodsModel->setHeaderData(0, Qt::Horizontal, tr("No"));   
     8     goodsModel->setHeaderData(1, Qt::Horizontal, tr("name"));   
     9     goodsModel->setHeaderData(2, Qt::Horizontal, tr("value1"));   
    10     goodsModel->setHeaderData(3, Qt::Horizontal, tr("value2"));  
    11   
    12     ui.treeView_->setModel(goodsModel);

然后把数据填充到item中

     1 for (int i = 0; i < 4; ++i)   
     2     {  
     3   
     4         QList<QStandardItem *> items;   
     5         for (int i = 0; i < 3; ++i)   
     6         {   
     7             QStandardItem *item = new QStandardItem(QString("item %0").arg(i));  
     8   
     9             if (0 == i)   
    10                 item->setCheckable(true);   
    11             items.push_back(item);   
    12         }   
    13         goodsModel->appendRow(items);  
    14   
    15         for (int i = 0; i < 4; ++i)   
    16         {   
    17             QList<QStandardItem *> childItems;   
    18             for (int i = 0; i < 3; ++i)   
    19          {   
    20              QStandardItem *item = new QStandardItem(QString("%0").arg(i));              
    21              if (0 == i)   
    22                  item->setCheckable(true);  
    23   
    24              childItems.push_back(item);   
    25          }   
    26             items.at(0)->appendRow(childItems);      
    27         }  
    28   
    29     }

第三步设置treeview 能够被多选

    1 ui.treeView_->setSelectionMode(QAbstractItemView::ExtendedSelection);

第三步设置右键选择菜单

    1 void TreeView::contextMenuEvent(QContextMenuEvent *event)   
    2 {   
    3     QMenu *pMenu = new QMenu(ui.treeView_);   
    4     QAction* buildItem = pMenu->addAction(tr("build"));   
    5     pMenu->exec(QCursor::pos());   
    6 }

posted @ 2011-02-15 14:23 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1955179) 收藏

##备注 
 @post in:2011-02-15 14:23