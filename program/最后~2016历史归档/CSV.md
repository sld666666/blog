#  [CSV](http://www.cnblogs.com/sld666666/archive/2011/02/23/1962121.html)

## 1\. **什么是CSV格式文档**

所谓“CSV”，是Comma Separated
Value（逗号分隔值）的英文缩写，通常都是纯文本文件。通常CSV文件开头是不留空的，以行为单位，每行记录多项数据，每项数据用逗号
来分隔（标准英文逗号）。如用记事本写下：

name_1, num_1, file_1  
name_2, num_1, file_2  
name_3, num_3, file_3

保存为.csv 用excel 打开就是这样的：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201102/20110223114621
3196.png)

## 2\. 利用Qt写入CSV 文件

Qt创建一个.csv文件就非常简单了：

     1 #include <QtCore/QCoreApplication>   
     2 #include <QFile>   
     3 #include <QString>  
     4   
     5  const QString FILE_PATH("E:\\test.csv");  
     6   
     7  int main(int argc, char *argv[])   
     8 {   
     9     QCoreApplication a(argc, argv);  
    10   
    11     QString line_0("0, aaa, 000\n");   
    12     QString line_1("1, bbb, 111\n");   
    13     QString line_2("2, ccc, 222\n");   
    14     QFile csvFile(FILE_PATH);  
    15   
    16     if (csvFile.open(QIODevice::ReadWrite))   
    17     {   
    18         csvFile.write(line_0.toAscii());   
    19         csvFile.write(line_1.toAscii());   
    20         csvFile.write(line_2.toAscii());   
    21         csvFile.close();   
    22     }   
    23     return a.exec();   
    24 }

结果为：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201102/20110223114621
4591.png)

so easy.

## 3\. QT读取CSV 文件

     1 const QString FILE_PATH("E:\\test.csv");  
     2   
     3  int main(int argc, char *argv[])   
     4 {   
     5     QCoreApplication a(argc, argv);  
     6   
     7     QFile csvFile(FILE_PATH);   
     8     QStringList CSVList;   
     9     CSVList.clear();  
    10   
    11     if (csvFile.open(QIODevice::ReadWrite))   
    12     {   
    13         QTextStream stream(&csvFile);   
    14         while (!stream.atEnd())   
    15         {   
    16             CSVList.push_back(stream.readLine());   
    17         }   
    18         csvFile.close();   
    19     }   
    20     Q_FOREACH(QString str, CSVList)   
    21     {   
    22         qDebug() << str<<"\n";   
    23     }   
    24     return a.exec();   
    25 }

结果：

![image](http://images.cnblogs.com/cnblogs_com/sld666666/201102/20110223114621
5671.png)

## 4\. 一个简易的CSV解析器

基于以上的介绍，实现一个CSV Parser就非常简单了。

.h

     1 #pragma once   
     2 #include <QStringList>   
     3 #include <QFile>   
     4 #include <boost/shared_ptr.hpp>  
     5   
     6  class CSVParser   
     7 {   
     8  public:   
     9     CSVParser(QString filePath);   
    10     ~CSVParser(void);  
    11   
    12  public:   
    13     bool            setCSVData(const QStringList& CSVList);   
    14     QStringList        getCSVData();  
    15   
    16  private:   
    17     boost::shared_ptr<QFile>    CSVFile_;   
    18 };

.cpp

     1 #include <QTextStream>  
     2   
     3 #include "CSVParser.h"  
     4   
     5 CSVParser::CSVParser(QString filePath)   
     6 {   
     7     CSVFile_ = boost::shared_ptr<QFile>(new QFile(filePath));   
     8     CSVFile_->open(QIODevice::ReadWrite);   
     9 }  
    10   
    11 CSVParser::~CSVParser(void)   
    12 {   
    13     CSVFile_->close();   
    14 }  
    15   
    16  // 注意， QStringList 的参数必须符合csv格式： 逗号分隔，\n结尾   
    17 bool CSVParser::setCSVData(const QStringList& CSVList)   
    18 {   
    19     if (CSVList.empty())   
    20         return false;  
    21   
    22     if (!CSVFile_->isOpen())   
    23         return false;  
    24   
    25     if (!CSVFile_->resize(0))   
    26         return false;  
    27   
    28     Q_FOREACH(QString str, CSVList)   
    29     {   
    30         CSVFile_->write(str.toAscii());   
    31     }   
    32     CSVFile_->flush();   
    33     return true;   
    34 }   
    35 QStringList CSVParser::getCSVData()   
    36 {   
    37     QStringList CSVList;   
    38     CSVList.clear();  
    39   
    40     QTextStream stream(CSVFile_.get());  
    41   
    42     while (!stream.atEnd())   
    43     {   
    44         CSVList.push_back(stream.readLine());   
    45     }  
    46   
    47     return CSVList;  
    48   
    49 }

posted @ 2011-02-23 11:46 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1962121) 收藏

##备注 
 @post in:2011-02-23 11:46