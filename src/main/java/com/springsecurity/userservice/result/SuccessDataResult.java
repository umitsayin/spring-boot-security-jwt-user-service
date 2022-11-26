package com.springsecurity.userservice.result;

public class SuccessDataResult<T> extends DataResult{
    public SuccessDataResult(T data){
        super(true,data);
    }
    public SuccessDataResult(String message,T data){
        super(true,message,data);
    }
}
