package com.sec.controller;

import com.sec.entity.RestBean;
import com.sec.service.AuthorizeService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.Pattern;

@RestController
@Validated
@RequestMapping("/api/auth")
public class AuthorizeController {

    @Resource
    private AuthorizeService authorizeService;

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private final String USERNAME_REGEX = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";
    @PostMapping("/valid-register-email")
    public RestBean validateRegisterEmail(@Pattern(regexp = EMAIL_REGEX)
                                      @RequestParam("email")String email,
                                        HttpSession session){

        String msg = authorizeService.sendValidateEmail(email, session.getId(), false);
        if(msg == null){
            return RestBean.success("邮件发送成功请注意查收");
        }
        return RestBean.failure(400,msg);
    }

    @PostMapping("/valid-reset-email")
    public RestBean validateResetEmail(@Pattern(regexp = EMAIL_REGEX)
                                  @RequestParam("email")String email,
                                  HttpSession session){

        String msg = authorizeService.sendValidateEmail(email, session.getId(), true);
        if(msg == null){
            return RestBean.success("邮件发送成功请注意查收");
        }
        return RestBean.failure(400,msg);
    }

    @PostMapping("/register")
    public RestBean register(@Pattern(regexp = USERNAME_REGEX)@Length(min=2,max=8)@RequestParam("username")String username,
                             @Length(min=6,max=16)@RequestParam("password")String password,
                             @Pattern(regexp = EMAIL_REGEX )@RequestParam("email")String email,
                             @Length(min=6,max=6)@RequestParam("code")String code,
                             HttpSession session){
        String msg = authorizeService.validateAndRegister(username,password,email,code,session.getId());
        if(msg == null){
            return RestBean.success("注册成功");
        }else{
            return RestBean.failure(400,msg);
        }
    }

    @PostMapping("/start-reset")
    public RestBean startRest(@Pattern(regexp = EMAIL_REGEX )@RequestParam("email")String email,
                              @Length(min=6,max=6)@RequestParam("code")String code,
                              HttpSession session){
        String msg = authorizeService.validateOnly(email, code, session.getId());
        if(msg == null){
            session.setAttribute("reset-password",email);

            return RestBean.success("验证成功");
        }else{
            return RestBean.failure(400,msg);
        }
    }

    @PostMapping("/do-reset")
    public RestBean resetPassword(@Length(min=6,max=16)@RequestParam("password")String password,
                                  HttpSession session){
        String email = (String)session.getAttribute("reset-password");
        if (email == null){
            return RestBean.failure(400,"请先完成邮箱验证");
        }else if(authorizeService.resetPassword(email,password)){
            session.removeAttribute("reset-password");
            return RestBean.success("邮件重置成功");
        }else{
            return RestBean.failure(400,"内部错误,请联系管理员");
        }

    }
}
