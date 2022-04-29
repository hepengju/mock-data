package com.hepengju.mockdata.generator.gen100_date;

import com.hepengju.mockdata.common.BaseConst;
import com.hepengju.mockdata.generator.AbstractNumberGenerator;
import com.hepengju.mockdata.util.DateUtil;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.Date;

@ApiModel("时间戳生成器") @Data @Order(103)
public class TimestampGenerator extends AbstractNumberGenerator<Long> {

	private String min = "1900-01-01 00:00:00";
	private String max = "2100-12-31 23:59:59";
	private String format = BaseConst.DATE_TIME_FORMAT;

	private Date minDate;
	private Date maxDate;

	@Override
	public Long generate() {
		return RandomUtil.randomDate(minDate.getTime(), maxDate.getTime()).getTime();
	}

	public void setMin(String min) {
		this.min = min;
		this.minDate = DateUtil.stringToDate(min);
	}

	public void setMax(String max) {
		this.max = max;
		maxDate = DateUtil.stringToDate(max);
	}

	public TimestampGenerator() {
		this.setMin(min);
		this.setMax(max);
	}
	public TimestampGenerator(String min, String max) {
		this.setMin(min);
		this.setMax(max);
	}

}
