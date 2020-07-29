package com.example.exception;

import com.example.enums.ResultEnum;

public class ResultException extends RuntimeException {
    private ResultEnum resultEnum;
    public ResultException(String msg){
        super(msg);
    }

    public ResultException(ResultEnum resultEnum){
        this.resultEnum = resultEnum;
    }

    @Override
    public String getMessage(){
        if (resultEnum == null){
            return super.getMessage();
        }
        return resultEnum.getMsg();
    }

    public int getStatus(){
        return resultEnum.getStatus();
    }
}
