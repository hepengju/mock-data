package com.hepengju.mockdata.common;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;

import java.util.ArrayList;

/**
 * 常量
 * 
 * @author hepengju
 *
 */
public interface DataConst {

	// 从配置文件获取数据
	Dict dict = YamlUtil.loadByPath("const-dict.yaml");
	static ArrayList<String> list(String key) {
		try {
			return (ArrayList<String>) dict.get(key);
		} catch (Exception e) {
			return new ArrayList<>();
		}
	}

	// 常量设置
	ArrayList<String> surnames          = list("surnames")         ; //常见姓
	ArrayList<String> words             = list("words")            ; //常见汉字
	ArrayList<String> lastNames         = list("lastNames")        ; //常见英文姓
	ArrayList<String> firstNames        = list("firstNames")       ; //常见英文名
	ArrayList<String> provinces         = list("provinces")        ; //省
	ArrayList<String> cities            = list("cities")           ; //城市
	ArrayList<String> provinceCities    = list("provinceCities")   ; //省市列表
	ArrayList<String> idCardAreas       = list("idCardAreas")      ; //身份证区号
	ArrayList<String> mailSupplies      = list("mailSupplies")     ; //邮箱后缀
	ArrayList<String> mobileTypes       = list("mobileTypes")      ; //手机号码段
	ArrayList<String> numbers           = list("numbers")          ; //数字
	ArrayList<String> addressFlags      = list("addressFlags")     ; //地标
	ArrayList<String> telePhoneArea     = list("telePhoneArea")    ; //电话号码区号
	ArrayList<String> companyIndustries = list("companyIndustries"); //公司行业
	ArrayList<String> specialChar       = list("specialChar")      ; //特殊字符: ~`!@#$%^&*()_-+=\|]}[{'";:?/<>,.
}
