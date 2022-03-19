package com.hepengju.mockdata.config.springmvc;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import com.hepengju.mockdata.common.JsonR;
import com.hepengju.mockdata.web.GeneratorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.Set;

/**
 * 全局响应处理器
 */
@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {

    // 此处默认方法名 和 @RequestMapping的api是一致的, 反正我一直遵守的!
    private Set<String> apis = ClassUtil.getDeclaredMethodNames(GeneratorController.class);

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // swagger文档等不能全局处理, 即仅仅处理自己定义的Controller
        String path = request.getURI().getPath();
        if (!apis.contains(path)) return body;
        if (body instanceof JsonR) return body;
        return JsonR.ok().addData(body);
    }
}
