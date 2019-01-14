package com.linewell.oa.gateway.domail;

import lombok.Builder;
import lombok.Data;

/**
 * @author Blysi
 * @since 2019-01-11
 */
@Data
@Builder
public class PostResult {
    private int status;
    private boolean result;
    private String message = "";
    private Object data;

    public static PostResult success(Object data){
        return PostResult.builder().result(true).data(data).build();
    }

    public static PostResult success(){
        return PostResult.builder().result(true).build();
    }

    public static PostResult error(){
        return PostResult.builder().result(false).build();
    }

    public static PostResult error(String msg){
        return PostResult.builder().result(false).message(msg).build();
    }

    public static PostResult error(int status,String msg){
        return PostResult.builder().result(false).status(status).message(msg).build();
    }
}
