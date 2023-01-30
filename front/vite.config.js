import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// element-plus 按需导入
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// let server_url = 'https://www.hepengju.com/'
server_url = 'http://localhost:8080'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        AutoImport({ resolvers: [ElementPlusResolver()], }),
        Components({ resolvers: [ElementPlusResolver()], }),
    ],
    server: {
        proxy: {
            '/getGenMap': server_url,
            '/fetchData': server_url,
            '/refreshTable': server_url,
            '/downTable': server_url,
            '/evalScript': server_url,
        }
    }
})
