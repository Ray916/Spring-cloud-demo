## API网关服务 Spring Cloud Zuul

### 简介

1、整个微服务架构系统的门面，统一入口，所有的外部客户端访问都需要经过它来进行调度和过滤；

2、Zuul将自己注册为Eureka服务治理下的应用，同时从Eureka获得所有其他微服务的实例信息；

3、默认通过服务名application作为ContextPath来创建路由映射；

4、使签名校验和登录校验独立出来，在API网关服务上统一调用来对微服务接口做前置过滤；

### 构建网关

1、在pom.xml中引入zuul, config, eureka的依赖；
```
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zuul</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-eureka</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
```
zuul的依赖中还包含了hystrix, ribbon, actuator这些重要的依赖。

2、在应用主类中添加相关注解；
```
    @EnableDiscoveryClient
    @EnableZuulProxy
```

3、由于我们使用了config来统一管理配置信息，所以在`src/main/resources`下面的文件是bootstrap.properties而不是application.properties或者application.yml;
```
    spring.application.name=api-gateway
    server.port=5555

    eureka.client.service-url.defaultZone=http://localhost:1111/eureka/

    spring.cloud.config.discovery.enabled=true
    spring.cloud.config.discovery.service-id=config-server

    spring.cloud.config.profile=
    spring.cloud.config.label=master

    spring.cloud.config.username=abc
    spring.cloud.config.password=abc123
```
上面这些属性必须配置在bootstrap.properties中，这样config-server中的配置信息才能被正确加载，具体可查看spring boot对配置文件的具体加载顺序；

4、在config-server的仓库config-repo中我们创建了一个api-gateway的文件夹，里面包含api-gateway的相关配置文件，主要是路由配置信息；
```
    zuul.routes.api-a.path=/api-a/**
    zuul.routes.api-a.service-id=hello-service
```
客户端应用在获取外部配置文件后加载到客户端的ApplicationContext实例，该配置内容的优先级高于客户端jar包内部的配置内容；

### 服务路由

#### 默认规则

1、Eureka为每个服务自动创建一个默认的路由规则，这些规则的path使用serviceId配置的服务名作为请求前缀；

2、如果某些服务不想对外开放而被路由，可以通过`zuul.ignored-services`参数来设置一个服务名匹配表达式定义不自动创建路由的规则。

#### 自定义路由映射规则

在程序中增加如下Bean的创建即可，
```
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper(){
        return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)",
                "${version}/${name}");
    }
```
1、构造函数的第一个参数用来匹配服务名称是否符合该自定义的正则表达式，第二个参数用来定义根据服务名的内容转换出的路径表达式；

2、符合第一个参数的服务名优先使用该实现构造的路径表达式，不符合的使用默认路径映射规则；

3、上面正则表达式的规则为：将服务名为`userservice-v1`的服务名转换成`/v1/userservice`;

#### 其他路由规则

1、通过`zuul.ignored-patterns`可以用来设置不希望被网关进行路由的url表达式；

2、路由前缀，本地跳转，Cookie与头信息，重定向问题，Hystrix和Ribbon支持等。

### 请求过滤（访问权限的设置）

1、通过自定义**过滤器**来实现对请求的拦截和过滤，实现方法：继承ZuulFilter抽象类并实现它定义的4个抽象函数（具体请查看AccessFilter类中的注释）。

2、`com.xuezw.gateway.filter`中的`AccessFilter.java`类实现了在请求被路由前检查Request中是否有accessToken。

3、核心过滤器中并没有实现error阶段的过滤器，因此需要我们自己实现，具体如：`com.xuezw.gateway.filter`中的`ErrorFilter.java`。

4、为了能够处理post阶段抛出的异常信息，我们新建了一个error过滤器，其继承自post阶段的SendErrorFilter过滤器，复用其run方法，但是在shouldFilter判断中只允许处理post阶段抛出的异常信息。

具体如: `com.xuezw.gateway.filter`中的`ErrorExtFilter.java`。

5、自定义核心过滤器，如:`com.xuezw.processor`的`ExtFilterProcessor.java`用于在异常处理中为请求上下文添加failed.filter属性。

6、自定义异常信息，只需要我们编写一个自定义的ErrorAttributes接口实现类，并创建它的实例替代默认实现即可。具体参考：`com.xuezw.gateway.attributes`中的`MyErrorAttributes.java`实现，不将原来结果中的exception属性返回给客户端。

7、禁用过滤器，动态加载路由，动态加载过滤器等。





