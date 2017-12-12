package com.xuezw.gateway.attributes;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

/*
 * 自定义的ErrorAttributes接口实现类
 * 不将exception属性返回客户端*/

public class DidiErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(
			RequestAttributes requestAttributes, boolean includeStackTrace){
		Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
		result.remove("exception");
		return result;
	}
}
