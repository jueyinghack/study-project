package com.sec.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizeService extends UserDetailsService {
    // 发送验证码
    String sendValidateEmail(String email,String sessionId,boolean isHasAccount);
    // 注册
    String validateAndRegister(String username,String password,String email,String code,String sessionId);
    //开始重置密码
    String validateOnly(String email,String code,String sessionId);
    // 重置密码
    boolean resetPassword(String email,String password);
}
