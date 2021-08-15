package com.hepengju.mockdata.generator.gen400_custom.gen410_name;

import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.PinyinUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("姓名拼音生成器") @Order(412)
public class ChineseNamePinyinGenerator extends AbstractStringGenerator {

    private ChineseFirstNameGenerator firstName = new ChineseFirstNameGenerator();
    private ChineseLastNameGenerator  lastName  = new ChineseLastNameGenerator();
    @Override
    public String generate() {
        return PinyinUtil.toPinyin(firstName.generate()) + " " + PinyinUtil.toPinyin(lastName.generate());
    }
}
