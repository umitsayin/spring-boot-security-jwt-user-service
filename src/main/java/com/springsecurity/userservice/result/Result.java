package com.springsecurity.userservice.result;

import lombok.Data;

@Data
public class Result {
    private boolean type;
    private String message;

    public Result(){

    }

    public Result(boolean type){
        this.type = type;
    }

    public Result(String message){
        this.message = message;
    }

    public Result(boolean type,String message){
        this.type = type;
        this.message = message;
    }

}
