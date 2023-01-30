import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router';
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
    history: createWebHistory(),
    routes, 
})

app.use(router)
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
app.mount('#app')

