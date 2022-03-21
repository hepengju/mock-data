package com.hepengju.mockdata.config.springmvc;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.hepengju.mockdata.common.JsonR;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.hepengju.mockdata.common.BaseConst.ErrorCode.UNKNOWN_ERROR;

/**
 * 全局异常处理器
 */
@RestControllerAdvice @Slf4j
public class GlobalExceptionHandler {

    /**
     * 其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public JsonR handleException(Exception e) {
        log.error(e.getMessage(), e);
        return JsonR.err(StrUtil.blankToDefault(e.getMessage(), e.toString()))
                .setErrCode(UNKNOWN_ERROR)
                .setErrDetail(ExceptionUtil.stacktraceToString(e));
    }
}
