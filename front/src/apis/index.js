import axios from '../plugins/axios'

// 初始化, 获取所有生成器
export function getGenMap() {
  return axios.get('getGenMap')
}

// // 获取单个生成器的数据
// export function getData(params) {
//   return axios.get('getData', { params })
// }

// 获取单个生成器的数据 - 20220430 POST请求, 固定10个 (script的存在, get请求不合适)
export function fetchData(data) {
  return axios.post('fetchData', data)
}

// 刷新表格
export function refreshTable(data) {
  return axios.post('refreshTable', data)
}

// 下载数据
export function downTable(data) {
  return axios.post('downTable', data, {
    responseType: 'blob'
  })
}
