import { createApp } from 'vue'
import { createRouter, createWebHashHistory } from 'vue-router';
import App from './App.vue'

import Home from './pages/Home.vue'
import Snow from './pages/Snow.vue'

const app = createApp(App)

//~~路由~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
const routes = [
    { path: '/', component: Home },
    { path: '/snow', component: Snow },
]

const router = createRouter({
    history: createWebHashHistory(),
    routes, 
})

app.use(router)
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
app.mount('#app')

