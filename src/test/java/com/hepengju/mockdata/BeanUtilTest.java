package com.hepengju.mockdata;

import com.hepengju.mockdata.generator.Generator;
import com.hepengju.mockdata.core.GeneratorMeta;
import org.junit.jupiter.api.Test;

public class BeanUtilTest {

    @Test
    public void testCopy(){
        GeneratorMeta meta = new GeneratorMeta();
        meta.setClassName("com.hepengju.mockdata.generator.gen200_number.IntegerGenerator");
        meta.setMin("10"); // 最大值, 最小值类型不一致, Spring的BeanUtils复制不上
        meta.setMax("100");
        meta.setFormat("#.00");
        Generator gen01 = meta.toGenerator();
        System.out.println(gen01);

        meta.setClassName("com.hepengju.mockdata.generator.gen100_date.DateGenerator");
        Generator gen02 = meta.toGenerator();
        System.out.println(gen02);
    }
}
