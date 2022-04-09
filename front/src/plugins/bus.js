import mitt from 'mitt'

// 注意: 此处需要多个组件间使用的是同一个实例
const bus = mitt()
export default bus