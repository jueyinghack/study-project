import { createRouter, createWebHistory } from 'vue-router'
import {useStore} from "@/stores";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path:"/",
      name:"welcome",
      component:()=> import('@/views/WelcomeView.vue'),
      redirect:"/login",
      children:[
        {
          path:'/login',
          name:'welcome-login',
          component:() => import('@/components/welcome/LoginPage.vue')
        },{
          path:'/register',
          name:'welcome-register',
          component:() => import('@/components/welcome/RegisterPage.vue')
        },
        {
          path: '/forget',
          name: 'forget',
          component:()=>import("@/components/welcome/ForgetPage.vue")
        }
      ]
    },
    {
      path:'/index',
      name:'index',
      component:()=> import('@/views/indexView.vue')
    }
  ]
})


export default router
