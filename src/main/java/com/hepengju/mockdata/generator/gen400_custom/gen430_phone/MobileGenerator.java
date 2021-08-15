package com.hepengju.mockdata.generator.gen400_custom.gen430_phone;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

/**
 * 手机号
 * 
 * @author hepengju
 *
 */
@ApiModel("手机号生成器") @Order(431)
public class MobileGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		return RandomUtil.randomOne(DataConst.mobileTypes) + RandomUtil.randomNum(DataConst.numbers, 8);
	}

}
