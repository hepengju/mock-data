package com.hepengju.mockdata.generator.gen300_string;


import com.hepengju.mockdata.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

@ApiModel("随机字母生成器") @Data @Order(303)
public class RandomAlphabeticGenerator extends AbstractStringGenerator {

	private int min = 6;
	private int max = 6;

	@Override
	public String generate() {
		return RandomStringUtils.randomAlphabetic(min, max + 1);
	}
}
