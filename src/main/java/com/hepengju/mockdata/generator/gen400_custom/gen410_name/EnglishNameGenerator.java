package com.hepengju.mockdata.generator.gen400_custom.gen410_name;


import com.hepengju.mockdata.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

/**
 * 英文姓名生成器
 *
 * @author hepengju
 */
@ApiModel("英文姓名生成器")
@Data
@Order(414)
public class EnglishNameGenerator extends AbstractStringGenerator {

    private EnglishFirstNameGenerator firstName = new EnglishFirstNameGenerator();
    private EnglishLastNameGenerator lastName = new EnglishLastNameGenerator();

    @Override
    public String generate() {
        double chance01 = Math.random();
        if (chance01 < 0.3) return firstName.generate();
        if (chance01 < 0.6) return lastName.generate();
        return firstName.generate() + " " + lastName.generate();
    }
}
