<template>
    <div style="text-align: center;margin: 0 20px">
        <div style="margin-top: 150px">
            <div style="font-size: 25px;font-weight: bold">登录</div>
            <div style="font-size: 14px;color: green">在进入系统之前请先登录</div>
        </div>
        <div style="margin-top: 50px;">
            <el-input type="text" v-model="form.username" placeholder="用户名/邮箱">
                <template #prefix>
                    <el-icon><User /></el-icon>
                </template>
            </el-input>
            <el-input style="margin-top: 10px" v-model="form.password" type="password" placeholder="密码">
                <template #prefix>
                    <el-icon><Lock /></el-icon>
                </template>
            </el-input>
        </div>
        <div style="margin-top: 10px;">
            <el-row >
                <el-col :span="12" style="text-align: left">
                    <el-checkbox v-model="form.remember" label="记住我" size="small"/>
                </el-col>

                <el-col :span="12" style="text-align: right">
                    <el-link @click="router.push('/forget')">忘记密码？</el-link>
                </el-col>
            </el-row>
        </div>
        <div style="margin-top: 40px">
            <el-button type="success" @click="login" style="width: 270px" plain>
                登录
            </el-button>
        </div>
        <el-divider>没有账号</el-divider>
        <div style="margin-top: 30px">
            <el-button type="warning" @click="router.push('/register')" style="width: 270px" plain>
                注册
            </el-button>
        </div>

    </div>
</template>

<script setup>
import {Lock, User} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import {get, post} from "@/net";
import router from "@/router";
import {reactive} from "vue";


const form = reactive({
    username:'',
    password:'',
    remember:false
})
const login = () =>{
    if(!form.username || !form.password){
        ElMessage.warning("请填写用户名和密码")
    }else{
        post('/api/auth/login',{
            username:form.username,
            password:form.password,
            remember:form.remember
        },(message)=>{
            ElMessage.success(message)
            router.push('/index')
        });
    }
}


</script>

<style scoped>

</style>
