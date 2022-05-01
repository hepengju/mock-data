package com.hepengju.mockdata.generator.gen400_custom;

import com.hepengju.mockdata.generator.AbstractGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.core.annotation.Order;

/**
 * js脚本生成器
 *
 * @author hepengju
 */
@Data @ApiModel("JS脚本生成器") @Order(499) @Slf4j
public class ScriptGenerator extends AbstractGenerator<Object> {

    // 动态脚本生成器
    private String script = """
            /*
             * 服务端采用graalvm解析执行, 支持ECMAScript 2021
             * <p> 脚本直接评估出结果
             * @see https://www.graalvm.org/javascript/
             * /
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
            log.error("动态js脚本异常", e);
            return e.getMessage();
        }
    }
}
