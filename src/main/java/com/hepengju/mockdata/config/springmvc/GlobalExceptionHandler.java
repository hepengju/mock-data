package com.hepengju.mockdata.config.springmvc;

import com.hepengju.mockdata.common.BaseConst;
import com.hepengju.mockdata.common.JsonR;
import com.hepengju.mockdata.util.StackUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局的异常处理器
 */
@RestControllerAdvice @Slf4j
public class GlobalExceptionHandler {

    /**
     * 其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public JsonR handleException(Exception e) {
        log.error(e.getMessage(), e);
        return JsonR.err(e.getMessage())
                .setErrCode(BaseConst.ErrorCode.UNKNOWN_ERROR)
                .setErrDetail(StackUtil.getStackTrace(e));
    }
}
