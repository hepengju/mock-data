import axios from "axios";
import { Modal } from 'view-design';

function showModal(msg) {
    Modal.warning({
        title: '系统提示',
        content: msg,
    });
}

axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.interceptors.response.use(res => {
    // 下载文件的处理
    if (res.config.responseType == 'blob') {
        // 正常下载服务器返回的是此响应头
        if (res.headers['content-type'] == 'application/octet-stream') {
            let fileName = res.headers['original-filename-encode']
            let blob = new Blob([res.data]);
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
            let errBlob = new Blob([res.data], { type: 'application/json' });
            let fr = new FileReader();
            fr.readAsText(errBlob);
            fr.onload = function () {
                let jsonR = JSON.parse(this.result);
                showModal(jsonR.errMsg)
            }
        }
        return

    // 出现错误的处理
    } else if (res.headers['content-type'] == 'application/json' && res.data.errCode != 0) {
        showModal(res.data.errMsg)
        return Promise.reject(res.data.errMsg);
    } 

    // 拆了数据包再返回
    return res.data.data
}, err => {
    showModal("服务器连接失败")
    return Promise.reject(err);
});

// 为了统一所有接口到API, 这里就不安装在vue身上了
// export default {
//     install() {
//         Vue.prototype.$axios = axios
//     }
// };
export default axios