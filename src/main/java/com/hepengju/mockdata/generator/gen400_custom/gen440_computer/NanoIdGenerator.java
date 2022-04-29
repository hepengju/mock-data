package com.hepengju.mockdata.generator.gen400_custom.gen440_computer;

import cn.hutool.core.util.IdUtil;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("NanoId生成器") @Order(444)
public class NanoIdGenerator extends AbstractStringGenerator {
    @Override
    public String generate() {
        return IdUtil.nanoId();
    }
}
