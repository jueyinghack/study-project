<template>
    <div style="text-align: center;margin: 0 20px">
        <div style="margin-top: 150px">
            <div style="font-size: 25px;font-weight: bold">注册新用户</div>
            <div style="font-size: 14px;color: green">欢迎注册我们的平台，请在下方填写相关信息</div>
        </div>
        <div style="margin-top: 50px">
            <el-form
                    :model="form"
                    :rules="rules"
                    @validate="onValidate"
                    ref="formRef">
                <el-form-item prop="username">
                    <el-input type="text" v-model="form.username" placeholder="用户名">
                        <template #prefix>
                            <el-icon>
                                <User/>
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" v-model="form.password" placeholder="密码">
                        <template #prefix>
                            <el-icon>
                                <Lock/>
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password_repeat">
                    <el-input type="password" v-model="form.password_repeat" placeholder="重复密码">
                        <template #prefix>
                            <el-icon>
                                <Lock/>
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input type="text" v-model="form.email" placeholder="请输入电子邮箱">
                        <template #prefix>
                            <el-icon>
                                <Message/>
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="code">
                    <el-row :gutter="10" style="width: 100%">
                        <el-col :span="17">
                            <el-input placeholder="验证码"
                                      v-model="form.code"
                            >
                                <template #prefix>
                                    <el-icon>
                                        <EditPen/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-col>
                        <el-col :span="5">
                            <el-button @click="validateEmail"
                                       type="success"
                                       :disabled="!isEmailValid || coldTime > 0 ">
                                {{ coldTime > 0 ? '请等待' + coldTime + '秒' : '获取验证码' }}
                            </el-button>
                        </el-col>
                    </el-row>
                </el-form-item>
            </el-form>


        </div>
        <div style="margin-top: 10px;">

        </div>
        <div style="margin-top: 100px">
            <el-button @click="register" type="warning" style="width: 270px">
                立即注册
            </el-button>
            <div style="margin-top:20px;">
                <span style="font-size: 14px;line-height: 15px;color: grey">
                    已有账号?
                </span>
                <el-link type="primary" style="translate: 0-2px" @click="router.push('/')">立即登录</el-link>
            </div>
        </div>
    </div>

</template>

<script setup>

import {EditPen, Lock, Message, User} from "@element-plus/icons-vue";
import router from "@/router";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net";

const form = reactive({
    username: '',
    password: '',
    password_repeat: '',
    email: '',
    code: ''
})

const validateUsername = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入用户名'))
    } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)) {
        callback(new Error('用户名不能包含特殊字符,只能是中文或者英文'))
    } else {
        callback()
    }
}

const validatePasswordRepeat = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入重复密码'))
    } else if (!(value === form.password)) {
        callback(new Error('请输入正确的重复密码'))
    } else {
        callback()
    }
}
const isEmailValid = ref(false)

const onValidate = (prop, isValid) => {
    if (prop === 'email')
        isEmailValid.value = isValid
}

const formRef = ref()
const coldTime = ref(0);

const register = () => {
    formRef.value.validate((isValid) => {
        if (isValid) {
            post("/api/auth/register", {
                username: form.username,
                password: form.password,
                email: form.email,
                code: form.code
            }, (message) => {
                router.push("/")
                ElMessage.success(message);
            }, (message) => {
                ElMessage.error(message);
            })
        } else {
            ElMessage.warning("请填写完整表单")
        }
    })
}
const rules = {
    username: [
        {validator: validateUsername, trigger: ['blur', 'change']},
        {min: 2, max: 8, message: '用户名长度必须在2-8个字符之间', trigger: ['blur', 'change']}
    ],
    password: [
        {required: true, message: '请输入密码', trigger: ['blur', 'change']},
        {min: 6, max: 16, message: '密码长度必须在6-16个字符之间', trigger: ['blur', 'change']}
    ],
    password_repeat: [
        {validator: validatePasswordRepeat, trigger: ['blur', 'change']}
    ],
    email: [
        {required: true, message: '请输入邮箱', trigger: ['blur', 'change']},
        {type: 'email', message: '请输入正确的邮箱', trigger: ['blur', 'change']}
    ],
    coed: [
        {required: true, message: '请输入邮箱', trigger: ['blur', 'change']},
    ]
}
const validateEmail = () => {
    coldTime.value = 60
    post("/api/auth/valid-register-email", {
        email: form.email
    }, (message) => {

        ElMessage.success(message)

        setInterval(() => coldTime.value--, 1000);
    }, () => {
        coldTime.value = 0
    })
}

</script>


<style scoped>

</style>
