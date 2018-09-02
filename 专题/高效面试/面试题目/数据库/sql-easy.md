# sql-easy

## Big Countries    
### 描述
表： World
|    name     | continent |  area  | population |   gdp    |
| ----------- | --------- | ------ | ---------- | -------- |
| Afghanistan | Asia      | 652230 | 25500100   | 20343000 |

找出 面积大于 3000000 或者认可大于 25000000 的国家
### 答案
```
  select name, population, area from World where area > 3000000
  UNION  
  select name, population, area from World where population > 25000000
```

## Swap Salary
### 描述
表 salary
| id  | name | sex | salary |
| --- | ---- | --- | ------ |
| 1   | A    | f   | 1000   |
| 2   | B    | m   | 8000   |

交换表中的所有 f为m, m为f
### 答案

```
  update salary set sex =
  case
   when sex ='f' then 'm'
   when sex = 'm' then 'f'
   else 'n'
  end
```
## Duplicate Emails
### 描述
表 Person
| id  |   Email   |
| --- | --------- |
| 1   | a@163.com |
| 2   | b@163.com |
| 3   | a@163.com |
找出表中邮件重复的邮箱
### 答案

```
select  Email from
(select Email ,count(Email) as num from Person group by Email) as statistic
where statistic.num > 1
```
## Combine Two Tables
### 描述
两个表，表结构如下：
Person
| Column Name |  Type   |
| ----------- | ------- |
| PersonId    | int     |
| FirstName   | varchar |
| LastName    | varchar |

Address
| Column Name |  Type   |
| ----------- | ------- |
| AddressId   | int     |
| PersonId    | int     |
| City        | varchar |
| State       | varchar |

如果 两个表中都有数据，则 找出所有的 FirstName, LastName, City, State
### 答案
  ```
  select Person.FirstName, Person.LastName, Address.City, Address.State from Person left outer join Address on  Person.PersonId = Address.PersonId
  ```

## Second Highest Salary
###描述
表 Employee：
| id  | Salary |
| --- | ------ |
| 1   | 1000   |
找出第二高的薪水，命名为SecondHighestSalary

### 答案

```
select (select Salary from  Employee   where id = 1000 limit 1) as SecondHighestSalary
```

## Employees Earning More Than Their Managers
### 描述
表： Employee

| Id  | Name | Salary | ManagerId |
| --- | ---- | ------ | --------- |
| 1   | A    | 10000  | 3         |
| 2   | B    | 2000   | 3         |
| 3   | C    | 3000   | null      |
取出薪水比他的管理者搞的名字
### 答案
```
select a.Name as 'Employee' from Employee as a, Employee as b
    where a.ManagerId = b.Id and a.Salary > b.Salary;
```
