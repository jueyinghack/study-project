package com.sec.controller;

import com.sec.entity.AccountUser;
import com.sec.entity.RestBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping("/me")
    public RestBean me(@SessionAttribute("account")AccountUser user){
        return RestBean.success("success",user);
    }
}
