package com.xuezw.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/*
 * 该类的功能是检查请求的Request中是否包含了accessToken
 * @2017-12-14 by xuezw
 * */

public class AccessFilter extends ZuulFilter {

	//过滤器的具体逻辑
	@Override
	public Object run() {
		// TODO Auto-generated method stub
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request= ctx.getRequest();
		Object accessToken = request.getParameter("accessToken");
		if(accessToken == null){
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(401);
			return null;
		}
		return null;
	}

	//判断该过滤器是否需要被执行，实际运用中可以利用该函数指定过滤器的有效范围
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	//过滤器的执行顺序，数字越小优先级越高，具体数字意义查看相关资料
	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*过滤器的类型，它决定过滤器在请求的哪个声明周期中执行
	 * pre:可以在请求被路由前调用
	 * routing:在路由请求时被调用
	 * post:在routing和error过滤器之后被调用
	 * error:处理请求时发生错误时被调用
	 * */
	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
