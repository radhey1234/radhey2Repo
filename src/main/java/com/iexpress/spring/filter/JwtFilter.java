package com.iexpress.spring.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.jwt.JwtTokenUtil;
import com.iexpress.spring.jwt.JwtUser;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
	@Autowired 
	private JwtTokenUtil jwtTokenUtil;

	/*
	 * @Override public void doFilter(ServletRequest request, ServletResponse
	 * rResponse, FilterChain chain) throws IOException, ServletException { }
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse response,FilterChain filterChain) throws ServletException, IOException {
		LOGGER.info("Inside JwtFilter doFilter " + httpRequest.getMethod());
		allowCorsRequest(response);

		final String headerValue = httpRequest.getHeader("authHeader");
		if (headerValue != null) {

			try {
				final JwtUser user = jwtTokenUtil.buildJwtUserFromAuthHeader(headerValue);
				httpRequest.setAttribute("jwtUser", user);

				if (!jwtTokenUtil.validateToken(user)) {
					loginUnsuccessful(response);	
				}
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user,null, user.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (Exception e) {
				loginUnsuccessful(response);
			}	
			 
		}
		LOGGER.info("No issue at jwtFilter");
		filterChain.doFilter(httpRequest, response);

	}

	private void allowCorsRequest(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers","contentType,Content-Type,X-Requested-With,accept,Origin,authtoken,deviceId,authHeader");
	}

	private void loginUnsuccessful(HttpServletResponse response) throws IOException {
		LOGGER.info("Either AuthHeader is not present or it is invalid");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(200);
		ObjectMapper mapper = new ObjectMapper();
		ResponseEnvelop<ObjectNode> responseObject = new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(),
				" You are not logged in ! Please login !!", HttpStatus.UNAUTHORIZED.value());
		
		final String responseMsg = mapper.writeValueAsString(responseObject);
		response.getWriter().write(responseMsg);
		response.getWriter().flush();
		return;
	}

}
