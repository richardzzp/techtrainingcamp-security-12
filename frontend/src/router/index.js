import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            redirect: '/404'
        },
        {
            path: '/',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: { title: '404' }
                }
            ]
            },
            {
                path: '/login',
                component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
                meta: { title: '登录&注册' }
            },
        {
            path: '*',
            redirect: '/404'
        }
    ]
});
