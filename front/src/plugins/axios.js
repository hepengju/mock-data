import Vue from 'vue'
import axios from "axios";
import { Modal } from 'view-design';

function showModal(msg) {
    Modal.warning({
        title: '系统提示',
        content: msg,
    });
}

axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.interceptors.response.use(function (response) {
    // 下载文件的处理
    if (response.config.responseType == 'blob') {
        // 正常下载服务器返回的是此响应头
        if (response.headers['content-type'] == 'application/octet-stream') {
            let fileName = response.headers['original-filename-encode']
            let blob = new Blob([response.data]);
            if (window.navigator.msSaveOrOpenBlob) {
                try {
                    window.navigator.msSaveOrOpenBlob(blob, fileName);
                } catch (e) { } // 兼容ie11
            } else {
                let downEle = document.createElement('a');
                downEle.download = decodeURIComponent(fileName);
                downEle.href = window.URL.createObjectURL(blob);
                downEle.click();
                downEle.remove();
                window.URL.revokeObjectURL(blob);
            }
        } else {
            // 下载过程中发生错误, 返回的响应头是 'application/json'
            let errBlob = new Blob([resp.data], { type: 'application/json' });
            let fr = new FileReader();
            fr.readAsText(errBlob);
            fr.onload = function () {
                let jsonR = JSON.parse(this.result);
                showModal(jsonR.errMsg)
            }
        }
        // 出现错误的处理
    } else if (response.headers['content-type'] == 'application/json' && response.data.errCode != 0) {
        showModal(response.data.errMsg);
    }
    return response
}, function (error) {
    showModal("服务器连接失败！")
    return Promise.reject(error);
});

export default {
    install() {
        Vue.prototype.$axios = axios
    }
};