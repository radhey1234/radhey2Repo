package com.iexpress.spring.resolver;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/*
 * Creating Resolver to resolve input jwtUser
 */
public class JwtUserRequestResolver  implements HandlerMethodArgumentResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtUserRequestResolver.class);
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return true;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
			LOGGER.info("Inside JwtUserRequestResolver ");
		   HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
	        return request.getAttribute("jwtUser");
	}
}
