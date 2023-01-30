import DateGenerator from '../gens/gen100_date/DateGenerator.js'
import UTIL from '../gens/util.js'

const gen = new DateGenerator()
console.log(gen.generate())
console.log(gen.generate())
console.log(gen.generate())
console.log(gen.generate())


console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))
console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))
console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))
console.log(UTIL.format(gen.generate(),'YYYY-MM-DD'))

// console.log(UTIL.parse('2100-12-31', 'YYYY-MM-DD'))