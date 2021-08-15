package com.hepengju.mockdata.core;

import com.hepengju.mockdata.generator.Generator;
import com.hepengju.mockdata.generator.gen300_string.RandomAlphabeticGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解版配置
 *
 * @see com.hepengju.mockdata.meta.GeneratorMeta 前端传入版配置
 * @see com.hepengju.mockdata.util.GeneratorUtil#getGeneratorList(java.lang.Class)
 *
 * @author hepengju
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratorAnno {

    Class<? extends Generator> value() default RandomAlphabeticGenerator.class;
    String min()        default ""; // 最小值 (或最小长度)
    String max()        default ""; // 最大值 (或最大长度)
    String code()       default ""; // 代码值: 逗号分隔

    boolean codeMulti() default false; // 20200210 代码值, 可以选择是否多选
    String format()     default "" ; // 20200222 格式化(针对日期和数字的格式化)
    String prefix()     default "" ; // 20200222 前缀
    String suffix()     default "" ; // 20200222 后缀
}
