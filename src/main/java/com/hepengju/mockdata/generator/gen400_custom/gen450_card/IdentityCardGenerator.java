package com.hepengju.mockdata.generator.gen400_custom.gen450_card;

import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.DateUtil;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 身份证号码
 * 
 * 1、号码的结构
 * 		公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
 * 2、地址码(前六位数）
 * 		表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
 * 3、出生日期码（第七位至十四位）
 * 		表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 * 4、顺序码（第十五位至十七位）
 * 		表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。
 * 5、校验码（第十八位数）
 * 		（1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
 * 			Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 	
 *      （2）计算模 Y = mod(S, 11)
 *      （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 */
@Data @ApiModel("身份证号") @Order(452 - 10000)
public class IdentityCardGenerator extends AbstractStringGenerator {

	private String min = "1900-01-01";
	private String max = "2100-12-31";

	private Date minDate;
	private Date maxDate;

	private List<String> areas = DataConst.idCardAreas;
	private static String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4","3", "2" };
	private static String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7","9", "10", "5", "8", "4", "2" };
	
	@Override
	public String generate() {
		StringBuffer result = new StringBuffer(18);
		result.append(RandomUtil.randomOne(areas))                                                                           //地区:前两位
		      .append(StringUtils.leftPad(RandomUtils.nextInt(0, 9999 + 1) + "", 4, '0'))                     //地区:后四位
		      .append(new SimpleDateFormat("yyyyMMdd").format(RandomUtil
					  .randomDate(minDate.getTime(), maxDate.getTime())))  //出生日期
		      .append(StringUtils.leftPad(RandomUtils.nextInt(0, 999 + 1) + "", 3, '0'))                      //顺序码
		      ;
		String verifyCode = getVerifyCode(result.toString());                                                                 //验证码
		return result.append(verifyCode).toString();
	}

	private static String getVerifyCode(String cardId) {
		int sum = 0;
		for (int i = 0; i < Wi.length; i++) {
			sum += Integer.parseInt(String.valueOf(cardId.charAt(i))) * Integer.parseInt(Wi[i]);
		}
		int modValue = sum % 11;
		String strVerifyCode = ValCodeArr[modValue];
		return strVerifyCode;
	}

	public IdentityCardGenerator() {
		this.setMin(this.min);
		this.setMax(this.max);
	}

	public void setMin(String min) {
		this.min = min;
		this.minDate = DateUtil.stringToDate(min);
	}

	public void setMax(String max) {
		this.max = max;
		maxDate = DateUtil.stringToDate(max);
	}

	public String getMin() { return min; }
	public String getMax() { return max; }
}
