package com.sec.interceptor;


import com.sec.entity.AccountUser;
import com.sec.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorizeInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User user =(User)authentication.getPrincipal();
        String username = user.getUsername();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        AccountUser accountUser = userMapper.findAccountUserByUsernameOrEmail(username);
        request.getSession().setAttribute("account",accountUser);
        return true;
    }
}
