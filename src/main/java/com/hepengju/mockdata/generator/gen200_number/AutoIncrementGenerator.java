package com.hepengju.mockdata.generator.gen200_number;


import com.hepengju.mockdata.generator.AbstractNumberGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicInteger;

@ApiModel("自增生成器") @Data @Order(203)
public class AutoIncrementGenerator extends AbstractNumberGenerator<Integer> {

    private int min = 1;
    private AtomicInteger atomic = new AtomicInteger(min);
    private String format = "#";

    @Override
    public Integer generate() {
        return atomic.getAndIncrement();
    }

    public void setMin(int min) {
        this.min = min;
        this.atomic = new AtomicInteger(min);
    }

    public AutoIncrementGenerator() {
        this.setMin(min);
    }

    public AutoIncrementGenerator(int min) {
        this.setMin(min);
    }
}
