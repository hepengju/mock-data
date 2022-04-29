package com.hepengju.mockdata.generator.gen400_custom;

import com.hepengju.mockdata.generator.AbstractGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.core.annotation.Order;

/**
 * js脚本生成器
 *
 * @author hepengju
 */
@Data @ApiModel("js脚本生成器") @Order(499)
public class ScriptGenerator extends AbstractGenerator<Object> {

    // 动态脚本生成器
    private String script = """
            function scriptGenerator() {
                return Math.random() > 0.5 ? '+' : '-'
            }

            scriptGenerator();
            """;

    private Context context = Context.create();

    @Override
    public Object generate() {
        try {
            Value value = context.eval("js", this.getScript());
            return value.as(Object.class);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
