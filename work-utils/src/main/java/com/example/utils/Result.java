package com.example.utils;

import com.example.enums.ResultEnum;
import com.example.exception.ResultException;

public class Result {
    private int status;
    private String msg;
    private Object data;

    public Result(int status, String msg){
        this.status = status;
        this.msg = msg;
    }
    public Result(int status, String msg, Object data){
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static Result success(){
        ResultEnum resultEnum = ResultEnum.SUCCESS;
        return new Result(resultEnum.getStatus(), resultEnum.getMsg());
    }

    public static Result success(Object data){
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result fail(ResultException e){
        return new Result(e.getStatus(), e.getMessage());
    }

    public static Result fail(int status, ResultException e){
        return new Result(status, e.getMessage());
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
