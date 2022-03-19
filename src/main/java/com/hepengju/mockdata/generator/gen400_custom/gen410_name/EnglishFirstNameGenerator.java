package com.hepengju.mockdata.generator.gen400_custom.gen410_name;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;

@ApiModel("英文姓生成器") @Data
public class EnglishFirstNameGenerator extends AbstractStringGenerator {

	private ArrayList<String> firstNames = DataConst.firstNames;
	
	@Override
	public String generate() {
		return RandomUtil.randomOne(firstNames);
	}

}
