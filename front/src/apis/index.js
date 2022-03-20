import axios from './axios'

// 初始化, 获取所有生成器
export function getGenMap() {
  return axios.get('getGenMap')
}

// 获取单个生成器的数据
export function getData(name, sampleSize = 10) {
  return axios.get('getData', {
    params: {
      name,
      sampleSize
    }
  })
}

// 刷新表格
export function refreshTable(params) {
  return axios.post('refreshTable', { params })
}

// 下载数据
export function downTable(params) {
  return axios.post('downTable', { params })
}
