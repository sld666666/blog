#  [gTest详解](http://www.cnblogs.com/sld666666/p/4514872.html)

## 1\. 安装使用

### 1.1 安装

在https://code.google.com/p/googletest/ 下载源码  
进入msvc， 注意编译方式， 如果是dll， 选择 gtest-md  
编译生成lib文件， 然后引入.文件即可使用

### 1.2 使用

    #include "gtest/gtest.h"
    int _tmain(int argc, _TCHAR* argv[])
    {
        testing::InitGoogleTest(&argc, argv);
        return RUN_ALL_TESTS();
    }

当然我们也可以输出到xml

    int _tmain(int argc, _TCHAR* argv[])
    {
        testing::GTEST_FLAG(output) = "xml:";
        testing::InitGoogleTest(&argc, argv);
        return RUN_ALL_TESTS();
    }

## 2\. 断言

断言的宏可以分为两类ASSERT系列和EXPECT系列。

    TEST(StringCmpTest, Demo)
    {
        EXPECT_EQ(3, add(1, 2));
        ASSERT_EQ(3, add(1, 2));
    }

我们再来看下所支持的宏

  * ASSERT_EQ
  * ASSERT_NE
  * ASSERT_LE
  * ASSERT_LT
  * ASSERT_GE
  * ASSERT_GT

* * *

  * EXPECT_EQ
  * EXPECT_NE
  * EXPECT_LE
  * EXPECT_LT
  * EXPECT_GE
  * EXPECT_GT

* * *

  * ASSERT_TRUE
  * ASSERT_FALSE

* * *

  * ASSERT_STREQ
  * ASSERT_STRNE
  * ASSERT_STRCASEEQ
  * ASSERT_STRCASENE

* * *

  * ASSERT_FLOAT_EQ
  * ASSERT_DOUBLE_EQ

* * *

直接返回成功还是失败

  * FAIL();
  * ADD_FAILURE();

* * *

Predicate Assertions

在使用EXPECT_TRUE或ASSERT_TRUE时，有时希望能够输出更加详细的信息，比如检查一个函数的返回值TRUE还是FALSE时，希望能够输出传入的
参数是什么，以便失败后好跟踪。因此提供了如下的断言：

  * ASSERT_PRED1(pred1, val1);
  * ASSERT_PRED2(pred2, val1, val2);

如果对这样的输出不满意的话，还可以自定义输出格式化

  * ASSERT_PRED_FORMAT1(pred_format1, val1);`
  * ASSERT_PRED_FORMAT2(pred_format2, val1, val2);

### 例子

如果我们有这样一个类Arithmetic  
我们只需要新建一个ArithmeticUnit.cpp文件，然后写下如下代码：

    #include "stdafx.h"
    #include "Arithmetic.h"
    #include "gtest/gtest.h"
    TEST(Arithmetic, add){
        Arithmetic arith;
        int a(1), b(2);
        EXPECT_EQ(3, arith.add(1, 2));
    }

## 3\. 深入解析gTest

首先从TEST宏入手， 我们看下宏的定义

    //1
     define TEST(test_case_name, test_name) GTEST_TEST(test_case_name, test_name)
    //2
    #define GTEST_TEST(test_case_name, test_name)\
    GTEST_TEST_(test_case_name, test_name, \
              ::testing::Test, ::testing::internal::GetTestTypeId())
    //3
    #define GTEST_TEST_(test_case_name, test_name, parent_class, parent_id)\
    class GTEST_TEST_CLASS_NAME_(test_case_name, test_name) : public parent_class {\
    public:\
        GTEST_TEST_CLASS_NAME_(test_case_name, test_name)() {}\
    private:\
        virtual void TestBody();\
        static ::testing::TestInfo* const test_info_ GTEST_ATTRIBUTE_UNUSED_;\
        GTEST_DISALLOW_COPY_AND_ASSIGN_(\
        GTEST_TEST_CLASS_NAME_(test_case_name, test_name));\
    ....
    所以
    1. 最终展开的宏是继承自testing::Test类
    2. 我们最终写的代码是放在TestBody()中的
    3. 通过静态变量test_info_，调用MakeAndRegisterTestInfo对测试案例进行注册。

看下MakeAndRegisterTestInfo 是如何实现的

    TestInfo* MakeAndRegisterTestInfo(
        const char* test_case_name,
        const char* name,
        const char* type_param,
        const char* value_param,
        TypeId fixture_class_id,
        SetUpTestCaseFunc set_up_tc,
        TearDownTestCaseFunc tear_down_tc,
        TestFactoryBase* factory) {
      TestInfo* const test_info =
          new TestInfo(test_case_name, name, type_param, value_param,
                       fixture_class_id, factory);
      GetUnitTestImpl()->AddTestInfo(set_up_tc, tear_down_tc, test_info);
      return test_info;
    }

TestInfo对象主要用于包含如下信息：

  1. 测试案例名称（testcase name）
  2. 测试名称（test name）

  3. 该案例是否需要执行

  4. 执行案例时，用于创建Test对象的函数指针
  5. 测试结果

我们还看到，TestInfo的构造函数中，非常重要的一个参数就是工厂对象  
internal::TestFactoryBase* factory  
它主要负责在运行测试案例时创建出Test对象

    new ::testing::internal::TestFactoryImpl<\
            GTEST_TEST_CLASS_NAME_(test_case_name, test_name)>)

我们再来看下 TestFactoryImpl 是如何实现的

    template <class TestClass>
    class TestFactoryImpl : public TestFactoryBase {
     public:
      virtual Test* CreateTest() { return new TestClass; }
    };

我靠， 这也能算是工厂吗~

不过总之流程是， 我们要创建一个测试对象的时候，先调用factory的CreateTest()方法 创建TestInfo对象， 再通过
GetUnitTestImpl()->AddTestInfo(set_up_tc, tear_down_tc,
test_info);对TestInfo对象进行注册

UnitTest 是单例  
UnitTestImpl 是实现

    void AddTestInfo(Test::SetUpTestCaseFunc set_up_tc,
                   Test::TearDownTestCaseFunc tear_down_tc,
                   TestInfo * test_info) {
    // 获取或创建了一个TestCase对象，并将testinfo添加到TestCase对象中。
    GetTestCase(test_info->test_case_name(),
                test_info->test_case_comment(),
                set_up_tc,
                tear_down_tc)->AddTestInfo(test_info);
    }

这里TestCase对象就出来了

  1. TEST宏中的两个参数，第一个参数testcase_name，就是TestCase对象的名称，第二个参数test_name就是Test对象的名称。而TestInfo包含了一个测试案例的一系列信息。

  2. 一个TestCase对象对应一个或多个TestInfo对象。

总结一下gtest里的几个关键的对象：

  1. UnitTest 单例，总管整个测试，包括测试环境信息，当前执行状态等等
  2. Test 我们自己编写的，或通过TEST，TEST_F等宏展开后的Test对象，管理着测试案例的前后事件，具体的执行代码TestBody。
  3. TestCase 测试案例对象，管理着基于TestCase的前后事件，管理内部多个TestInfo。
  4. TestInfo 管理着测试案例的基本信息，包括Test对象的创建方法。

一个简单的UML图如下  
![](http://img03.taobaocdn.com/imgextra/i3/46754672/TB2WMiIcVXXXXXPXXXXXXXXXXX
X_!!46754672.png)

## 4\. 撸一个山寨的

撸了一个山寨的，<https://github.com/sld666666/TestUnit>

posted @ 2015-05-19 16:24 [sld666666](http://www.cnblogs.com/sld666666/)
阅读(...) 评论(...) [编辑](https://i.cnblogs.com/EditPosts.aspx?postid=4514872) 收藏

##备注 
 @post in:2015-05-19 16:24