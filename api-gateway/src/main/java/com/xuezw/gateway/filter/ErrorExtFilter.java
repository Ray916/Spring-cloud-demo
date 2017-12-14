package com.xuezw.gateway.filter;

import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/*
 * 这是一个error类型的过滤器，但是继承了Post阶段的SendErrorFilter
 * 复用了SendErrorFilter的run方法
 * 其目的是处理post阶段抛出的异常信息
 * @2017-12-14 by xuezw
 */

@Component
public class ErrorExtFilter extends SendErrorFilter {
	
	@Override
	public String filterType(){
		return "error";
	}
	
	@Override
	public int filterOrder(){
		return 30;
	}
	
	@Override
	public boolean shouldFilter(){
		//判断：仅处理来自POST过滤器引起的异常
		RequestContext ctx = RequestContext.getCurrentContext();
		ZuulFilter failedFilter = (ZuulFilter) ctx.get("failed.filter");
		if(failedFilter != null && failedFilter.filterType().equals("post")){
			return true;
		}
		return false;
	}

}
