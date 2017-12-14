package com.xuezw.processor;

import com.netflix.zuul.FilterProcessor;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/*
 * 创建一个FilterProcessor的子类，
 * 主逻辑不变，但是增加了异常捕获
 * 在异常处理中为请求上下文添加了failed.filter属性
 * 以存储抛出异常的过滤器实例
 * @2017-12-14 by xuezw
 */

public class ExtFilterProcessor extends FilterProcessor {
	
	@Override
	public Object processZuulFilter(ZuulFilter filter) throws ZuulException {
		try{
			return super.processZuulFilter(filter);
		}catch(ZuulException e){
			RequestContext ctx = RequestContext.getCurrentContext();
			ctx.set("failed.filter", filter);
			throw e;
		}
	}
}
