#  [Python
实现文件复制、删除](http://www.cnblogs.com/sld666666/archive/2011/01/05/1926282.html)

用python实现了一个小型的自动发版本的工具。这个“自动发版本”有点虚， 只是简单地把debug
目录下的配置文件复制到指定目录，把Release下的生成文件复制到同一指定，过滤掉不需要的文件夹(.svn)，然后再往这个指定目录添加几个特定的文件。

这个是我的第一个python小程序。

下面就来看其代码的实现。

首先插入必要的库：

    1 import os   
    2 import os.path   
    3 import shutil   
    4 import time,  datetime

然后就是一大堆功能函数。第一个就是把某一目录下的所有文件复制到指定目录中：

     1 def copyFiles(sourceDir,  targetDir):   
     2     if sourceDir.find(".svn") > 0:   
     3         return   
     4     for file in os.listdir(sourceDir):   
     5         sourceFile = os.path.join(sourceDir,  file)   
     6         targetFile = os.path.join(targetDir,  file)   
     7         if os.path.isfile(sourceFile):   
     8             if not os.path.exists(targetDir):    
     9                 os.makedirs(targetDir)    
    10             if not os.path.exists(targetFile) or(os.path.exists(targetFile) and (os.path.getsize(targetFile) != os.path.getsize(sourceFile))):    
    11                     open(targetFile, "wb").write(open(sourceFile, "rb").read())   
    12         if os.path.isdir(sourceFile):   
    13             First_Directory = False   
    14             copyFiles(sourceFile, targetFile)

删除一级目录下的所有文件：

    1 def removeFileInFirstDir(targetDir):   
    2     for file in os.listdir(targetDir):   
    3         targetFile = os.path.join(targetDir,  file)   
    4         if os.path.isfile(targetFile):   
    5             os.remove(targetFile)

复制一级目录下的所有文件到指定目录：

    1 def coverFiles(sourceDir,  targetDir):   
    2         for file in os.listdir(sourceDir):   
    3             sourceFile = os.path.join(sourceDir,  file)   
    4             targetFile = os.path.join(targetDir,  file)   
    5             #cover the files   
    6             if os.path.isfile(sourceFile):   
    7                 open(targetFile, "wb").write(open(sourceFile, "rb").read())

复制指定文件到目录：

    1 def moveFileto(sourceDir,  targetDir):   
    2     shutil.copy(sourceDir,  targetDir)

往指定目录写文本文件：

    1 def writeVersionInfo(targetDir):   
    2     open(targetDir, "wb").write("Revison:")

返回当前的日期，以便在创建指定目录的时候用：

     1 def getCurTime():   
     2     nowTime = time.localtime()   
     3     year = str(nowTime.tm_year)   
     4     month = str(nowTime.tm_mon)   
     5     if len(month) < 2:   
     6         month = '0' + month   
     7     day =  str(nowTime.tm_yday)   
     8     if len(day) < 2:   
     9         day = '0' + day   
    10     return (year + '-' + month + '-' + day)

然后就是主函数的实现了：

     1 if  __name__ =="__main__":   
     2     print "Start(S) or Quilt(Q) \n"   
     3     flag = True   
     4     while (flag):   
     5         answer = raw_input()   
     6         if  'Q' == answer:   
     7             flag = False   
     8         elif 'S'== answer :   
     9             formatTime = getCurTime()   
    10             targetFoldername = "Build " + formatTime + "-01"   
    11             Target_File_Path += targetFoldername  
    12   
    13             copyFiles(Debug_File_Path,   Target_File_Path)   
    14             removeFileInFirstDir(Target_File_Path)   
    15             coverFiles(Release_File_Path,  Target_File_Path)   
    16             moveFileto(Firebird_File_Path,  Target_File_Path)   
    17             moveFileto(AssistantGui_File_Path,  Target_File_Path)   
    18             writeVersionInfo(Target_File_Path+"\\ReadMe.txt")   
    19             print "all sucess"   
    20         else:   
    21             print "not the correct command"

感觉是果然简单， 不过简单的原因是因为库函数丰富，语言基本特性的简单真没感觉出来。

posted @ 2011-01-05 14:08 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=1926282) 收藏

##备注 
 @post in:2011-01-05 14:08