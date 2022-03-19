import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

import iview from './plugins/iview'
import axios from './plugins/axios'

Vue.use(iview)
Vue.use(axios)

// 渲染
new Vue({
  el: '#app',
  render: h => h(App),
  beforeCreate() {
    Vue.prototype.$bus = this
  },
})
