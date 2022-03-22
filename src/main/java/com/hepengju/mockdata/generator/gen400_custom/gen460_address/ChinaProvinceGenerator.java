package com.hepengju.mockdata.generator.gen400_custom.gen460_address;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;

@ApiModel("中国省份生成器") // @Order(463)
public class ChinaProvinceGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.provinces);
	}

}
