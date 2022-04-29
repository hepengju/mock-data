package com.hepengju.mockdata.generator.gen400_custom.gen460_address;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("中国城市生成器") @Order(462)
public class ChinaCityGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.cities);
	}

}
