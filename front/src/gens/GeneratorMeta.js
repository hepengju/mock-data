export default class GeneratorMeta {

    constructor(min, max, code, codeMulti = false, format, prefix = '', suffix = '', script = '') {
        this.min = min               // 最小值
        this.max = max               // 最大值
        this.code = code             // 代码枚举
        this.codeMulti = codeMulti   // 代码多选
        this.format = format         // 格式
        this.prefix = prefix         // 前缀
        this.suffix = suffix         // 后缀
        this.script = script         // 脚本
    }


}