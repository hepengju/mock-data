import axios from './axios'

// 初始化, 获取所有生成器
export function getGenMap() {
  return axios.get('getGenMap')
}

// 获取单个生成器的数据
export function getData(params) {
  return axios.get('getData', { params })
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
