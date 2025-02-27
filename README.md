<h1 align="center">develop-plus</h1>

> 开发工具集合

## 全局统一版本

- spring-boot：3.2.4
- spring-cloud：2023.0.1
- spring-cloud-alibaba：2023.0.1.0
- mybatis-plus：3.5.9

## 现包含的模块

- develop-plus-core：核心（其他模块都会引用，若引入其他模块，则该模块无需再次引用）
- develop-plus-web：web工程
    - 内置：
        - 通用对象（PageInfo、PageParam、BaseEntity等）
        - 自动封装
        - 全局异常捕获
        - Swagger
        - MyBatis-plus配置：
            - 分页插件【分页最大数10000】
            - 非法SQL拦截器
            - 防止全表更新与删除插件
        - 工具类：
            - RedisUtils：redis工具类（bean注入）
            - ChineseCharToEnUtils：中文转为拼音工具类
            - CollectionStringUtils：集合与字符串互转工具类
            - ExcelUtils：excel工具类
            - IPUtils：IP工具类
            - PageDataUtils：物理分页工具类
            - XmlObjectUtils：xml与object互转工具类
- develop-plus-feign：feign工程（用于feign接口无需自动封装配置）
    - 内置jar：
        - spring-cloud-starter-openfeign
        - spring-boot-starter-validation