const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    proxy: 'http://ali.hepengju.com:8080/'
    // proxy: 'http://localhost:8080/'
  },
  productionSourceMap: false
})
