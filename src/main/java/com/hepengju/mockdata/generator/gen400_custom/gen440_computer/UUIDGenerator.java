package com.hepengju.mockdata.generator.gen400_custom.gen440_computer;


import cn.hutool.core.util.IdUtil;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

/**
 * UUID生成器
 * 
 * @author hepengju
 *
 */
@ApiModel("UUID生成器") @Order(443)
public class UUIDGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		// return UUID.randomUUID().toString().replace("-","");
		return IdUtil.fastSimpleUUID();
	}

}
