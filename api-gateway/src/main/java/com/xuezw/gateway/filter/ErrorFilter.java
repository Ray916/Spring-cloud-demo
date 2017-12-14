package com.xuezw.gateway.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/*
 * 这是一个error类型的过滤器
 * 通过创建该类型的过滤器用来捕获请求声明周期的pre、route、post三个阶段中抛出的异常信息
 * 并根据这些异常信息再请求上下文中注入需要返回给客户端的错误描述
 * @2017-12-14 by xuezw
 * */

@Component
public class ErrorFilter extends ZuulFilter {

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		Throwable throwable = ctx.getThrowable();
		ctx.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		ctx.set("error.exception", throwable.getCause());
		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "error";
	}

}
