import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './plugins/element.js'

// 导入全局样式表
import './assets/css/global.css'
// 导入字体图标
import './assets/fonts/iconfont.css'

// 导入NProgress进度条
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

import axios from 'axios'

axios.defaults.baseURL = 'http://localhost/admin'
// 拦截所有请求，为请求头添加token认证
axios.interceptors.request.use(config => {
    // 请求时开启进度条
    NProgress.start()

    // console.log(config)
    const userInfo = JSON.parse(sessionStorage.getItem('userInfo'))
    config.headers.Authorization = userInfo ? userInfo.token : ''
    return config
})

axios.interceptors.response.use(config => {
    // 响应时结束进度度
    NProgress.done()
    return config
})

Vue.prototype.$http = axios

// 全局注册第三方表格组件
import ZkTable from 'vue-table-with-tree-grid'

Vue.component('tree-table', ZkTable)

// 工具库
import utils from './libs/utils'

Vue.prototype.$utils = utils

// 导入富文本编辑器
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css' // import styles
import 'quill/dist/quill.snow.css' // for snow theme
import 'quill/dist/quill.bubble.css' // for bubble theme
Vue.use(VueQuillEditor)

Vue.config.productionTip = false

new Vue({
    router,
    render: h => h(App)
}).$mount('#app')
