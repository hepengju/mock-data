package com.hepengju.mockdata.generator.gen300_string;


import com.hepengju.mockdata.generator.Generator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("空值生成器") @Order(301)
public class NullGenerator implements Generator<Object> {
    @Override
    public Object generate() {
        return null;
    }
}
