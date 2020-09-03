import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/Home.vue'
import SimpleEdit from '@/views/SimpleEdit.vue'
import StanderEdit from '@/views/StanderEdit.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/simpleEdit',
    name: 'simpleEdit',
    component: SimpleEdit
  },
  {
    path: '/standerEdit',
    name: 'standerEdit',
    component: StanderEdit
  }
]

const router = new VueRouter({
  // mode: 'history',
  mode: 'hash',
  // linkActiveClass: 'active', // 路由高亮样式，默认值为'router-link-active'
  base: process.env.BASE_URL,
  routes
})

export default router
