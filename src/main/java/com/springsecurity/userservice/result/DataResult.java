package com.springsecurity.userservice.result;

import lombok.Data;

@Data
public class DataResult<T>{
    private boolean type;
    private String message;
    private T data;

    public DataResult(){

    }

    public DataResult(boolean type,T data){
        this.type = type;
        this.data = data;
    }

    public DataResult(boolean type,String message,T data){
        this.type = type;
        this.message = message;
        this.data = data;
    }
}
