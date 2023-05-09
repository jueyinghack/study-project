package com.sec.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sec.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;



public interface UserMapper extends BaseMapper<Account> {
    // 通过用户名或者邮箱查询用户
    Account findAccountByUsernameOrEmail(@Param("text")String text);

    Integer resetPasswordByEmail(@Param("email")String email,@Param("password")String password);


}

