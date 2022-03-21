package com.hepengju.mockdata.generator.gen400_custom.gen420_password;


import cn.hutool.core.codec.Base64;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

/**
 * md5密码生成器
 *
 * @author hepengju
 */
@ApiModel("Base编码") @Order(424)
public class Base64Generator extends AbstractStringGenerator {

	@Override
	public String generate() {
		String password = RandomStringUtils.randomAlphabetic(6, 10);
		return Base64.encode(password);
	}

}
