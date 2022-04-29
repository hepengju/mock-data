package com.hepengju.mockdata.generator.gen400_custom.gen440_computer;

import cn.hutool.core.util.IdUtil;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("ObjectId生成器") @Order(445)
public class ObjectIdGenerator extends AbstractStringGenerator {
    @Override
    public String generate() {
        return IdUtil.objectId();
    }
}
