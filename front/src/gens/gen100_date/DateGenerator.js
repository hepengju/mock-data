import Generator from '../Generator.js'
import BaseConst from '../consts/BaseConst.js'
import UTIL from '../util.js'

export default class DateGenerator extends Generator {

    min = '1900-01-01'
    max = '2100-12-31'
    format = BaseConst.DATE_FORMAT

    // 提高效率
    minValue = UTIL.parse(this.min, this.format).getTime()
    maxValue = UTIL.parse(this.max, this.format).getTime()

    generate() {
        return UTIL.dateTruncate(new Date(UTIL.random(this.minValue, this.maxValue)))
    }

}