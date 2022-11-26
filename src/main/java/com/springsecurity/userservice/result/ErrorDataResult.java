package com.springsecurity.userservice.result;

public class ErrorDataResult<T> extends DataResult{
    public ErrorDataResult(T data){
        super(false,data);
    }
    public ErrorDataResult(String message,T data){
        super(false,message,data);
    }
}
