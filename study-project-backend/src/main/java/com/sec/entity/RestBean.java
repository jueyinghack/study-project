package com.sec.entity;

import lombok.Data;

@Data
public class RestBean {
    private int code;
    private String message;
    private Object data;

    public RestBean(int code,String message,Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static RestBean success(){
        return new RestBean(200,"success",null);
    }

    public static RestBean success(Object data){
        return new RestBean(200,"success",data);
    }

    public static RestBean failure(int code){
        return new RestBean(code,"failure",null);
    }

    public static RestBean failure(int code,String msg){
        return new RestBean(code,msg,null);
    }

}
