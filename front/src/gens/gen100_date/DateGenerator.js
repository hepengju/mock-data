import Generator from '../Generator.js'
import BaseConst from '../consts/BaseConst.js'
import UTIL from '../util.js'

export default class DateGenerator extends Generator {

    constructor(min = '1900-01-01', max = '2100-12-31', format = BaseConst.DATE_FORMAT) {
        super()
        this.min = min
        this.max = max

        this.minValue = UTIL.parse(min, format).getTime()
        this.maxValue = UTIL.parse(max, format).getTime()
    }

    generate() {
        return UTIL.dateTruncate(new Date(UTIL.random(this.minValue, this.maxValue)))
    }

}