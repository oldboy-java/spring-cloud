package com.thoughtmechanix.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T>{
    private Integer code;
    private String msg;
    private T  data;

    public static   <T> ResponseResult success(T data){
       return  ResponseResult.builder().code(200).msg("success").data(data).build();
    }
    public static   <T> ResponseResult success(){
        return  ResponseResult.builder().code(200).msg("success").build();
    }

    public static   ResponseResult error(Integer code, String msg){
        return  ResponseResult.builder().code(code).msg(msg).build();
    }
}
