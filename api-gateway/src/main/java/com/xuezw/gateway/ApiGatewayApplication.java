package com.xuezw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;

import com.xuezw.gateway.attributes.DidiErrorAttributes;
import com.xuezw.gateway.filter.AccessFilter;

//@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class ApiGatewayApplication {

	@Bean
	public AccessFilter accessFilter(){
		return new AccessFilter();
	}
	
	@Bean
	public PatternServiceRouteMapper serviceRouteMapper(){
		return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)",
				"${version}/${name}");
	}
	
	@Bean
	public DefaultErrorAttributes errorAttributes(){
		return new DidiErrorAttributes();
	}
	
	//使用@RefreshScope注解来动态更新Zuul配置内容
//	@Bean
//	@RefreshScope
//	@ConfigurationProperties("zuul")
//	public ZuulProperties zuulProperties(){
//		return new ZuulProperties();
//	}
	
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}
