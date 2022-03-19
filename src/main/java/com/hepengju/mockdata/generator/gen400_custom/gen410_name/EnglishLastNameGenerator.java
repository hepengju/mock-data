package com.hepengju.mockdata.generator.gen400_custom.gen410_name;


import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.generator.AbstractStringGenerator;
import com.hepengju.mockdata.util.RandomUtil;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;

@ApiModel("英文名生成器") @Data
public class EnglishLastNameGenerator extends AbstractStringGenerator {

    private ArrayList<String> lastNames = DataConst.lastNames;

    @Override
    public String generate() {
        return RandomUtil.randomOne(lastNames);
    }


}
