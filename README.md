base_vue_system_core
===============

基础框架【Vue】

此分支为开发版本，不能用于实际项目


---
#### 一、注意事项

项目ORM框架使用mybatis-plus。

#### 二、教程
- [MyBatis-Plus](https://mp.baomidou.com/)

#### 三、开发建议
    
1. 实体类中不能使用基本数据类型，必须使用包装类，因为基本数据类型会存在默认值，会对部分操作有影响。
   例如在Controller中的分页查询接口，如果使用基本类型boolean，默认值为false，则会将其作为查询条件。
   
2. ...待完善
