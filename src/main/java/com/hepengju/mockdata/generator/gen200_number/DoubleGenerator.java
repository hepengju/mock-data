package com.hepengju.mockdata.generator.gen200_number;

import com.hepengju.mockdata.generator.AbstractNumberGenerator;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.core.annotation.Order;

@ApiModel("小数生成器") @Data @NoArgsConstructor @AllArgsConstructor @Order(202)
public class DoubleGenerator extends AbstractNumberGenerator<Double> {

	private double min = 0.00;
	private double max = 1000.00;
	private String format = "#.00";

	@Override
	public Double generate() {
		return RandomUtils.nextDouble(min, max);
	}

}
