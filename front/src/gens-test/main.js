import DateGenerator from '../gens/gen100_date/DateGenerator'
import UTIL from '../gens/util'

gen = new DateGenerator()
console.log(gen.generate())
console.log(gen.generate())
console.log(gen.generate())
console.log(gen.generate())


console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))
console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))
console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))
console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))