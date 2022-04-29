package com.hepengju.mockdata;

import com.hepengju.mockdata.common.DataConst;
import com.hepengju.mockdata.core.GeneratorMeta;
import com.hepengju.mockdata.generator.Generator;
import com.hepengju.mockdata.generator.gen100_date.DateGenerator;
import com.hepengju.mockdata.generator.gen100_date.DateTimeGenerator;
import com.hepengju.mockdata.generator.gen100_date.LocalDateGenerator;
import com.hepengju.mockdata.generator.gen100_date.LocalDateTimeGenerator;
import com.hepengju.mockdata.generator.gen200_number.AutoIncrementGenerator;
import com.hepengju.mockdata.generator.gen200_number.DoubleGenerator;
import com.hepengju.mockdata.generator.gen200_number.IntegerGenerator;
import com.hepengju.mockdata.generator.gen300_string.*;
import com.hepengju.mockdata.generator.gen400_custom.ScriptGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen410_name.*;
import com.hepengju.mockdata.generator.gen400_custom.gen420_password.Md5Generator;
import com.hepengju.mockdata.generator.gen400_custom.gen420_password.PasswordGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen420_password.Sha256Generator;
import com.hepengju.mockdata.generator.gen400_custom.gen430_phone.MobileGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen430_phone.TelephoneGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen440_computer.EmailGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen440_computer.IPv4Generator;
import com.hepengju.mockdata.generator.gen400_custom.gen440_computer.UUIDGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen450_card.IdentityCardGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen460_address.ChinaAddressGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen460_address.ChinaCityGenerator;
import com.hepengju.mockdata.generator.gen400_custom.gen460_address.ChinaProvinceGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 测试生成器
 *
 * @author hepengju
 */
public class GeneratorTest {

    int COUNT = 5;

    @Test
    public void testScriptGenerator() {
        String script =
                """
                function scriptGenerator() {
                    return Math.random() > 0.5 ? '+' : '-'
                }

                scriptGenerator();
                """
                ;

        ScriptGenerator gen = new ScriptGenerator();
        gen.setScript(script);
        System.out.println(gen.generate());
        System.out.println(gen.generate());
        System.out.println(gen.generate());
    }

    @Test
    public void testFormatPrefixSuffix() {
        DateTimeGenerator dt = new DateTimeGenerator();
        dt.setFormat("yyyyMMddHHmmss");
        dt.setPrefix("ORDER-");
        dt.setSuffix("-001");

        // 基本设置
        System.out.println(dt.generate());
        System.out.println(dt.generateString());
        System.out.println();
        printList(dt.generateList(COUNT));
        printList(dt.generateStringList(COUNT));

        // 相互转换
        GeneratorMeta generatorMeta = dt.toGeneratorMeta();
        System.out.println(generatorMeta);

        Generator generator = generatorMeta.toGenerator();
        System.out.println(generator);
    }


    @Test
    public void testGen100_date() {
        testGenerator(new DateGenerator());
        testGenerator(new DateTimeGenerator());
        testGenerator(new LocalDateGenerator());
        testGenerator(new LocalDateTimeGenerator());
    }

    @Test
    public void testGen200_number() {
        testGenerator(new IntegerGenerator());
        testGenerator(new DoubleGenerator());
        testGenerator(new AutoIncrementGenerator());
    }

    @Test
    public void testGen300_string() {
        testGenerator(new CodeGenerator());
        testGenerator(new NullGenerator());
        testGenerator(new RandomChineseGenerator());
        testGenerator(new RandomEmailGenerator());
        testGenerator(new RandomMobileGenerator());
        testGenerator(new RandomAlphabeticGenerator());
        testGenerator(new RandomNumberGenerator());
    }

    @Test
    public void testGen410_name() {
        testGenerator(new NameGenerator());
        testGenerator(new ChineseNameGenerator());
        testGenerator(new ChineseNamePinyinGenerator());
        testGenerator(new EnglishNameGenerator());
        testGenerator(new CompanyNameGenerator());
    }

    @Test
    public void testGen420_password() {
        testGenerator(new PasswordGenerator());
        testGenerator(new Md5Generator());
        testGenerator(new Sha256Generator());
    }

    @Test
    public void testGen430_phone() {
        testGenerator(new MobileGenerator());
        testGenerator(new TelephoneGenerator());
    }

    // 20200301 hepengju 生成的电话号码有负值, 原因是: 常量表中有不对的值 --> 修正完毕
    @Test
    public void testTelephone() {
        Set<Integer> lengthSet = new HashSet<>();
        for (String phone : DataConst.telePhoneArea) {
            lengthSet.add(phone.length());
        }
        System.out.println(lengthSet);
    }

    @Test
    public void testGen440_computer() {
        testGenerator(new EmailGenerator());
        testGenerator(new IPv4Generator());
        testGenerator(new UUIDGenerator());
    }

    @Test
    public void testGen450_card() {
        testGenerator(new IdentityCardGenerator());
    }

    @Test
    public void testGen460_address() {
        testGenerator(new ChinaAddressGenerator());
        testGenerator(new ChinaCityGenerator());
        testGenerator(new ChinaProvinceGenerator());
    }

    private void testGenerator(Generator gen) {
        System.err.println(gen.getClass());
//        System.out.println(gen.generate());
//        System.out.println(gen.generateString());
//        System.out.println();
//        System.out.println(gen.generateList(COUNT));
        System.out.println(gen.generateStringList(COUNT));


//        GeneratorMeta generatorMeta = gen.toGeneratorMeta();
//        System.out.println(generatorMeta);
//        Generator newGen = generatorMeta.toGenerator();
//        System.out.println(newGen);
//        System.out.println();
//
//        System.err.println(StringUtils.repeat("*", 100));
    }

    private void printList(List<? extends Object> objList) {
        objList.forEach(obj -> System.out.println(obj));
        System.out.println();
    }
}
