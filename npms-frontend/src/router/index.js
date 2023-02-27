import {createRouter, createWebHashHistory} from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import Layout from "@/layout/Layout"

const routes = [
    {
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        name: '登录',
        meta: {title: "登录", requireAuth: false},
        component: () => import('@/views/Login.vue')
    },
    {
        path: '/main',
        component: Layout,
        icon: "el-icon-s-home",
        meta: {title: "首页", requireAuth: true},
        redirect: '/home',
        children: [
            {
                path: "/home",
                name: "首页",
                icon: "el-icon-s-home",
                meta: {title: "首页", requireAuth: true},
                component: () => import('@/views/Home.vue')
            }
        ]
    },
    {
        path: "/customer",
        name: "客户管理",
        component: Layout,
        icon: "el-icon-s-shop",
        meta: {title: "客户管理", requireAuth: true},
        children: [
            {
                path: "/customer-list",
                name: "客户列表",
                icon: "el-icon-s-data",
                meta: {title: "客户列表", requireAuth: true},
                component: () => import("@/views/CustomerList.vue")
            },
            {
                path: "/customer-handover",
                name: "客户交接",
                icon: "el-icon-document-add",
                meta: {title: "客户交接", requireAuth: true},
                component: () => import("@/views/CustomerHandover.vue")
            }
        ]
    },
    {
        path: "/opp",
        name: "机会管理",
        component: Layout,
        icon: "el-icon-s-shop",
        meta: {title: "机会管理", requireAuth: true},
        children: [
            {
                path: "/opp-list",
                name: "机会维护",
                icon: "el-icon-s-data",
                meta: {title: "机会维护", requireAuth: true},
                component: () => import("@/views/Chances.vue")
            },
            {
                path: "/opp-approve",
                name: "机会审批",
                icon: "el-icon-s-data",
                meta: {title: "机会审批", requireAuth: true},
                component: () => import("@/views/Approval.vue")
            }
        ]
    },
    {
        path: "/log",
        name: "报表管理",
        component: Layout,
        icon: "el-icon-s-order",
        meta: {title: "报表管理", requireAuth: true},
        children: [
            {
                path: "/log-worklog",
                name: "销售日报撰写",
                icon: "el-icon-s-data",
                meta: {title: "销售日报撰写", requireAuth: true},
                component: () => import("@/views/workLog.vue")
            },
            {
                path: "/log-worklog-search",
                name: "销售日报查询",
                icon: "el-icon-s-data",
                meta: {title: "销售日报查询", requireAuth: true},
                component: () => import("@/views/workLogSearch.vue")
            },
            {
                path: "/log-relation-search",
                name: "客户关联关系查询",
                icon: "el-icon-document-copy",
                meta: {title: "客户关联关系查询", requireAuth: true},
                component: () => import("@/views/relationLog.vue")
            },
            {
                path: "/log-handover-search",
                name: "客户交接查询",
                icon: "el-icon-tickets",
                meta: {title: "客户交接查询", requireAuth: true},
                component: () => import("@/views/handoverLog.vue")
            }
        ]
    },
    {
        path: '/404',
        name: '404',
        meta: {title: "404", requireAuth: false},
        component: () => import('@/views/404.vue')
    },
    {
        path: '/500',
        name: '500',
        meta: {title: "500", requireAuth: false},
        component: () => import('@/views/500.vue')
    },
]


// createRouter 
const router = createRouter({
   
    history: createWebHashHistory(),
    routes,
})

NProgress.inc(0.2)
NProgress.configure({ easing: 'ease', speed: 600, showSpinner: false })

// title
router.beforeEach((to, from, next) => {
    
    NProgress.start()

    if (to.meta.title) {
        document.title = to.meta.title
    } else {
        document.title = "默存"
    }
    next()
})

router.afterEach(() => {
    NProgress.done()
})

//  main.js 
export default router