package com.hepengju.mockdata.generator.gen100_date;

import com.hepengju.mockdata.common.BaseConst;
import com.hepengju.mockdata.generator.Generator;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneId;

@ApiModel("LocalDateTime生成器") @Data
public class LocalDateTimeGenerator implements Generator<LocalDateTime> {

    private String min = "1900-01-01 00:00:00";
    private String max = "2100-12-31 23:59:59";
    private String format = BaseConst.DATE_TIME_FORMAT;

    private DateTimeGenerator dateTimeGenerator;

    @Override
    public LocalDateTime generate() {
        return dateTimeGenerator.generate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTimeGenerator() {
        this.dateTimeGenerator = new DateTimeGenerator(this.min, this.max);
    }

    public LocalDateTimeGenerator(String min, String max) {
    	this.min = min;
    	this.max = max;
        this.dateTimeGenerator = new DateTimeGenerator(min, max);
    }
}
