package com.hepengju.mockdata.generator.gen400_custom.gen440_computer;

import cn.hutool.core.util.IdUtil;
import com.hepengju.mockdata.generator.AbstractNumberGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("SnowflakeId生成器") @Order(446)
public class SnowIdGenerator extends AbstractNumberGenerator<Long> {
    @Override
    public Long generate() {
        return IdUtil.getSnowflakeNextId();
    }
}
