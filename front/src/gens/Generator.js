import UTIL from './util.js'

export default class Generator {

    /**
     * 生成数据（需要子类重写）
     * */
    generate();

    /**
     * 构造函数: 指定格式、前缀、后缀
     */
    constructor(format = '', prefix = '', suffix = '') {
        this.format = format
        this.prefix = prefix
        this.suffix = suffix
    }

    /**
     * 生成字符串
     */
    generateString() {
        const value = this.generate();
        if (!value) return "";

        let result = value.toString();

        // 格式化日期和数字
        if (this.format) {
            result = UTIL.format(this.value, this.format)
        }

        return this.prefix +  result + this.suffix
    }

    /**
     * 生成数据列表
     */
    generateList(size = 10) {
        const arr = []
        for (let i = 0; i < size; i++) {
            arr.push(this.generate())
        }
        return arr;
    }

    /**
     * 生成数据字符串列表
     */
    generateStringList(size = 10) {
        const arr = []
        for (let i = 0; i < size; i++) {
            arr.push(this.generateString())
        }
        return arr;
    }

}
