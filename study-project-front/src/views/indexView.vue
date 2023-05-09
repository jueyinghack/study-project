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

const logout = () =>{
    get('/api/auth/logout',()=>{
        ElMessage.success("退出登录成功")
        router.push('/')
    })
}
</script>

<style scoped>

</style>
