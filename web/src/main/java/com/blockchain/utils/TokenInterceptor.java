package com.blockchain.utils;

import com.blockchain.service.UserService;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TokenInterceptor implements HandlerInterceptor
{
	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object o) throws Exception
	{
		if (!(o instanceof HandlerMethod))
		{
			return true;
		}
		HandlerMethod handlerMethod = ((HandlerMethod) o);

		Method method = handlerMethod.getMethod();

		//获取方法上的Authorization注解
		Authorization tokenValid = method.getAnnotation(Authorization.class);
		//httpServletResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("origin"));
		//如果方法上有Authorization注解
		if (tokenValid != null)
		{
			//获取请求上传的token信息
			String token = httpServletRequest.getHeader("token");
			var user = AESToken.verifyToken(token);

			//如果token合法
			if (user != null)
			{
				var u = userService.getUserInfoByEmail(user.getString("email"));
				//将user 添加到 request中，以便后续操作获取user
				httpServletRequest.setAttribute("user", u);
				return true;
			} else
			{
				throw new Exception("登录信息错误");
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView)
			throws Exception
	{

	}

	@Override
	public void afterCompletion(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception
	{

	}
}
