package com.xuezw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;

import com.netflix.zuul.FilterProcessor;
import com.xuezw.gateway.attributes.MyErrorAttributes;
import com.xuezw.gateway.filter.AccessFilter;
import com.xuezw.processor.ExtFilterProcessor;

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
		return new MyErrorAttributes();
	}
	
	//使用@RefreshScope注解来动态更新Zuul配置内容
//	@Bean
//	@RefreshScope
//	@ConfigurationProperties("zuul")
//	public ZuulProperties zuulProperties(){
//		return new ZuulProperties();
//	}
	
	public static void main(String[] args) {
		//调用自定义的核心处理器
		FilterProcessor.setProcessor(new ExtFilterProcessor());
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}
