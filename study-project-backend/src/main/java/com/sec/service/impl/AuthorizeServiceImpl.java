package com.sec.service.impl;

import com.sec.entity.Account;


import com.sec.entity.RestBean;
import com.sec.mapper.UserMapper;
import com.sec.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AuthorizeServiceImpl implements AuthorizeService {
    @Resource
    private UserMapper userMapper;

    @Resource
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private StringRedisTemplate template;

    private final org.springframework.security.crypto.password.PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null){
            throw new UsernameNotFoundException("用户名不能为空");
        }
        Account account = userMapper.findAccountByUsernameOrEmail(username);
        if(account == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        return User.withUsername(account.getUsername())
                .password(account.getPassword())
                .roles("user")
                .build();
    }

    @Override
    public String sendValidateEmail(String email,String sessionId,boolean isHasAccount) {
        /**
         * 发送验证码
         * 1. 先生成验证码
         * 2. 把邮箱和对应的验证码放到Redis里
         * 只要剩余时间低于2分钟就可以再发送一次
         * 3. 发送验证码到指定邮箱
         * 4. 如果发送失败，把Redis里面的刚刚植入的验证码删除
         * 5. 用户再注册时，再从Redis里面取出对应键值对，然后查看是否一致
         */
        Account account = userMapper.findAccountByUsernameOrEmail(email);
        if (isHasAccount && account == null){
            return "没有此邮件地址的账户";
        }
        if(!isHasAccount && account != null){
            return "已经被注册";
        }

        String key = "email:"+sessionId + ":" + email + ":" + isHasAccount;
        if(Boolean.TRUE.equals(template.hasKey(key))){
            Long expire = Optional.ofNullable(template.getExpire(key,TimeUnit.SECONDS)).orElse(0L);
            if(expire > 120)
                return "请求频繁请稍后再试";
        }

        // 生成验证码
        Random random = new Random();
        int code = random.nextInt(899999)+100000;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证邮件");
        message.setText("验证码是："+code);
        try{
            mailSender.send(message);
            template.opsForValue().set(key,String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "注册失败";
        }
    }

    @Override
    public String validateAndRegister(String username, String password, String email, String code,String sessionId) {
        String key = "email:"+sessionId + ":" + email + ":false";
        if(Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if(s == null)
                return "验证码失效，请重新请求";
            if(s.equals(code)){

                password = encoder.encode(password);
                Account account = new Account();
                account.setUsername(username);
                account.setPassword(password);
                account.setEmail(email);
                userMapper.insert(account);
                template.delete(key);
                return null;
            }else{
                return "验证码错误，请检查后提交";
            }
        }else{
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:"+sessionId + ":" + email + ":true";
        if(Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if(s == null)
                return "验证码失效，请重新请求";
            if(s.equals(code)){

                template.delete(key);
                return null;
            }else{
                return "验证码错误，请检查后提交";
            }
        }else{
            return "请先请求一封验证码邮件";
        }
    }

    @Override
    public boolean resetPassword(String email,String password){
        password = encoder.encode(password);
        Integer integer = userMapper.resetPasswordByEmail(email, password);
        return integer > 0;
    }
}
