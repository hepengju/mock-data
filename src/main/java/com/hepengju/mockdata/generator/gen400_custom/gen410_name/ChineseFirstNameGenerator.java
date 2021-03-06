package com.hepengju.mockdata.generator.gen400_custom.gen410_name;

import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;

/**
 * 中文姓
 * 
 * 
 * <br> 1.中国最常见的100个姓氏(https://baike.baidu.com/item/%E4%B8%AD%E5%9B%BD%E4%BA%BA%E6%9C%80%E5%B8%B8%E8%A7%81%E7%9A%84100%E7%A7%8D%E5%A7%93%E6%B0%8F/5500469?fr=aladdin)
 * <br> 2.追加常见复姓(https://baike.baidu.com/item/%E4%B8%AD%E5%8D%8E%E5%A7%93%E6%B0%8F%E5%A4%A7%E5%85%A8/1689641?fr=aladdin)
 * <br>      司马 上官 欧阳 夏侯 诸葛  东方 皇甫 尉迟 公孙 仲孙 轩辕 令狐 爱新觉罗
 * 
 * @author hepengju
 *
 */
@ApiModel("中文姓生成器") @Data
public class ChineseFirstNameGenerator extends AbstractStringGenerator {
	
	private ArrayList<String> surnames = DataConst.surnames;
	
	@Override
	public String generate() {
		return RandomUtil.randomOne(surnames);
	}
}
