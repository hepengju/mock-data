package com.hepengju.mockdata.generator.gen300_string;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;

@ApiModel("随机汉字生成器") @Data @Order(306)
public class RandomChineseGenerator extends AbstractStringGenerator {
	
	private int min = 2;
	private int max = 4;
	private ArrayList<String> words = DataConst.words;

	@Override
	public String generate() {
		return RandomUtil.randomNum(words, min, max);
	}

}
