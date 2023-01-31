import dayjs from "dayjs"
import numeral from "numeral"
import _ from 'lodash'
import BaseConst from "./consts/BaseConst.js"

export default {

    /**
     * 格式化: 日期、数字
     */
    format(value, format) {
        try {
            if (format) {
                if (value instanceof Date) {
                    // https://dayjs.gitee.io/docs/zh-CN/display/format
                    format = format || BaseConst.DATE_TIME_FORMAT
                    return dayjs(value).format(format)
                } else if (value instanceof Number) {
                    // http://numeraljs.com/#format
                    return numeral(value).format(format)
                }
            }
        } catch (error) {
            return value + ': ' + error.toString()
        }

        return value.toString()
    },

    /**
     * 解析: 日期、数字
     */
    parse(value, format) {
        try {
            return dayjs(value, format).toDate()
        } catch (error) {
            return value
        }
    },

    /**
     * 随机值: 产生一个包括 lower 与 upper 之间的数
     *
     * @see https://www.lodashjs.com/docs/lodash.random
     */
    random(lower = 0, upper = 1) {
        return _.random(lower, upper)
    },

    /**
     * 日期: 去掉时分秒，返回的还是Date格式
     */
    dateTruncate(date) {
        let str = ''
        if (date instanceof Date) {
            str = dayjs(date).format(BaseConst.DATE_TIME_FORMAT)
        } else {
            str = date.toString()
        }

        str = str.substring(0, BaseConst.DATE_FORMAT.length)

        return dayjs(str, BaseConst.DATE_FORMAT).toDate()
    },

    /**
     * 输出生成器N次
     */
    print(gen, num = 10) {
        console.log(gen.constructor)
        for (let i = 0; i < num; i++) {
            console.log(gen.generate())
        }
    },

    /**
     * 输出生成器N次
     */
    printString(gen, num = 10) {
        console.log(gen.constructor)
        for (let i = 0; i < num; i++) {
            console.log(gen.generateString())
        }
    }
}