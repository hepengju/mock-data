import Generator from '../Generator.js'
import BaseConst from '../consts/BaseConst.js'
import UTIL from '../util.js'

export default class DateTimeGenerator extends Generator {

    min = '1900-01-01 00:00:00'
    max = '2100-12-31 23:59:59'
    format = BaseConst.DATE_TIME_FORMAT

    // 提高效率
    minValue = UTIL.parse(this.min, this.format).getTime()
    maxValue = UTIL.parse(this.max, this.format).getTime()

    generate() {
        return new Date(UTIL.random(this.minValue, this.maxValue))
    }

}