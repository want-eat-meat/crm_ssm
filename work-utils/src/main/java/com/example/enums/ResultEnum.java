package com.example.enums;

public enum ResultEnum {
    SUCCESS(200, "SUCCESS"),
    SUCCESS_NODATA(200, "无数据"),
    FAIL(1001, "操作失败");


    private int status;
    private String msg;

    ResultEnum(int status, String msg){
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
