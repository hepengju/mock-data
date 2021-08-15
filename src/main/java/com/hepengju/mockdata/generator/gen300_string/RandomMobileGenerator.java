package com.hepengju.mockdata.generator.gen300_string;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("随机手机号生成器") @Order(307)
public class RandomMobileGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return "1" + RandomUtil.randomNum(DataConst.numbers, 10);
	}

}
