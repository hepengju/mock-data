package com.hepengju.mockdata.generator.gen200_number;

import cn.hutool.core.util.IdUtil;
import com.hepengju.mockdata.generator.AbstractNumberGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("雪花主键生成器") @Order(204)
public class SnowIdGenerator extends AbstractNumberGenerator<Long> {
    @Override
    public Long generate() {
        return IdUtil.getSnowflakeNextId();
    }
}
