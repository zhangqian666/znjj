package com.zack.znjj.common.configuration;


import com.zack.znjj.common.restful.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(annotations = RestController.class)
@ResponseBody
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus
    public ServerResponse runtimeExceptionHandler(Exception e, HttpServletRequest httpServletRequest) {
        log.error("{} Exception", httpServletRequest.getRequestURI(), e);
        return ServerResponse.createByErrorMessage("接口异常,详情请查看服务端日志的异常信息" + e.toString());
    }
}
