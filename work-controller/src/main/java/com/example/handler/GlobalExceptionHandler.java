package com.example.handler;

import com.example.exception.ResultException;
import com.example.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest request, Exception e){
        if(e instanceof ResultException){
            ResultException resultException = (ResultException) e;
            try{
                resultException.getStatus();
                return Result.fail(resultException);
            }catch (Exception exp){
                return Result.fail(1004, resultException);
            }
        }else{
            e.printStackTrace();
            return null;
        }
    }
}
