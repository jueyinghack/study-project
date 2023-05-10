<template>
  <div>
      欢迎来到学习平台
  </div>
  <div>
      <el-button @click="logout" type="danger">退出登录</el-button>
  </div>
</template>

<script setup>
import {get} from "@/net";
import {ElMessage} from "element-plus";
import router from "@/router";
import {useStore} from "@/stores";

const store = useStore()
const logout = () =>{
    get('/api/auth/logout',()=>{
        router.push('/')
        store.auth.user = null;
        ElMessage.success("退出登录成功")
    })
}
</script>

<style scoped>

</style>
