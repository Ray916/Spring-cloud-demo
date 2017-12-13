## 分布式配置中心 Spring Cloud Config 

### 简介

1、为分布式系统中的基础设施和微服务应用提供**集中化的外部配置支持**（就是将各个工程的配置信息文件集中在一起，方便管理和修改）；

2、分为服务器和客户端两部分，配置中心为服务器，其他微服务应用为客户端；

3、本身也是一个微服务应用，可以注册到Eureka的注册中心；

4、可以连接配置仓库，默认为Git；

5、可以在其他任何语言运行的应用程序中使用；

### 构建服务器

1、pom.xml文件中引入以下依赖
```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
```

其中，Config-server为配置中心必须引入的依赖包，Eureka是为在注册中心注册引入，Security是为了服务器与客户端之间校验用的。

2、在工程主类中添加相关注解，开启功能
```
    @EnableDiscoveryClient
    @EnableConfigServer
```

3、在application.properties文件中进行config配置
```
    spring.cloud.config.server.git.uri=file:///d://workspace//config-repo
    spring.cloud.config.server.git.search-paths={application}
```
其中，没有配置git的远程仓库，而是只配置了本地仓库，本地仓库目录需要有.git文件，search-paths的配置参数为本地库config-repo下面的子目录，以微服务应用的名字命名。

eureka的配置
```
    eureka.client.service-url.defaultZone=http://localhost:1111/eureka/
```

security的配置
```
    security.user.name=abc
    security.user.password=abc123
```
客户端的配置文件中需同样进行上面的用户名和密码配置后才能正常获取其配置文件，简单的客户端校验。

加密解密配置
```
encrypt.key=abcdefg123w 
```
上述参数配置为对称加密，也可以设置非对称加密，主要是用于加密客户端微服务应用的配置文件中比较敏感的信息，如数据库密码等。

4、以上配置完成后，可以通过访问url：`http://localhost:7001/api-gateway/prod`，获取api-gateway的配置信息，也可以通过访问:`http://localhost:7001/api-gateway.properties`获取,其中api-gateway为应用名，prod为profile（可省略）。



