package com.sec.mapper;

import com.sec.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;



public interface UserMapper {
    Account findAccountByUsernameOrEmail(@Param("text")String text);
}

