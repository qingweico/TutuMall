import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../components/Login.vue'
import axios from "axios";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: Login
    },
    {
        path: '/home',
        component: () => import('../components/Home.vue'),
        redirect: '/welcome',
        children: [
            {path: '/welcome', component: () => import('../components/index/Welcome.vue')},
            {path: '/users', component: () => import('../components/user/Users.vue')},
            {path: '/rights', component: () => import('../components/power/Rights.vue')},
            {path: '/roles', component: () => import('../components/power/Roles.vue')},
            {path: '/categories', component: () => import('../components/goods/Cate.vue')},
            {path: '/params', component: () => import('../components/goods/Params.vue')},
            {path: '/goods', component: () => import('../components/goods/List.vue')},
            {path: '/goods/add', component: () => import('../components/goods/Add.vue')},
            {path: '/goods/edit', component: () => import('../components/goods/Edit.vue')},
            {path: '/orders', component: () => import('../components/order/Orders.vue')},
        ]
    }
]

const router = new VueRouter({
    routes
})

// 配置路由守卫
router.beforeEach((to, from, next) => {
    // 如果访问登陆页, 则放行
    if (to.path === '/login') {
        return next()
    }
    // 如果用户未登陆, 则跳转到/login
    const userInfo = JSON.parse(sessionStorage.getItem('userInfo'))
    if (!userInfo) {
        return next('/login')
    } else {
        // 校验token
        axios.get('checkToken',
            {headers: {token: userInfo.token}}).then((res) => {
            if (!res.data) {
                const _this = this;
                console.log("校验失败");
                this.$alert("登陆信息失效!", "提示", {
                    confirmButtonText: '确定'
                }).then(() => {
                    localStorage.removeItem("userInfo");
                    _this.$router.replace("/login");
                })
            }
        });
    }
    // 如果用户token有效，则放行
    next()
})

export default router
