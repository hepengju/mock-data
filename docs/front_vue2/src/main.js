import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

import iview from './plugins/iview'
Vue.use(iview)

// 渲染
new Vue({
  el: '#app',
  render: h => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this
  },
})
