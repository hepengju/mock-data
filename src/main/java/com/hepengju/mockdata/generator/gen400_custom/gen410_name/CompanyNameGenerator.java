package com.hepengju.mockdata.generator.gen400_custom.gen410_name;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("公司名称生成器") @Order(414)
public class CompanyNameGenerator extends AbstractStringGenerator {

	@Override
	public String generate() {
		StringBuilder result = new StringBuilder();
		
		String city = RandomUtil.randomOne(DataConst.cities).replace("省|市|区", "");
		result.append(city)
		      .append(RandomUtil.randomNum(DataConst.words, 2,4))
		      .append(RandomUtil.randomOne(DataConst.companyIndustries))
		      .append("有限公司");
		return result.toString();
	}

}
