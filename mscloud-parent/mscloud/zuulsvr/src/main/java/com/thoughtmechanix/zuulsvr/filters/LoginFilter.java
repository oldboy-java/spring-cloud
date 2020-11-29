package com.thoughtmechanix.zuulsvr.filters;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 用户登录验证
 */
public class LoginFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;     // 需要执行过滤器
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		
		
		
		return null;
	}

	@Override
	public String filterType() {
		return FilterUtils.PRE_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		return 0;    //同类型过滤器中，数字越小优先级越高
	}

}
