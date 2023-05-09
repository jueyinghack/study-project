<template>
    <div style="text-align: center;margin: 30px 20px">
        <div>
            <el-steps
                :active="active"
                finish-status="success" align-center>
                <el-step title="验证电子邮件"/>
                <el-step title="重新设置密码"/>
            </el-steps>
        </div>
        <transition name="el-fade-in-linear" mode="out-in">
            <div style="margin-top: 150px;height: 100%" v-if="active === 0">
                <div style="font-size: 25px;font-weight: bold">
                    重置密码
                </div>
                <div style="font-size: 14px;color:grey">
                    请输入需要重置的电子邮件地址
                </div>
                <div style="margin-top: 50px">
                    <el-form
                            :model="form"
                            :rules="rules"
                            @validate="onValidate"
                            ref="formRef">
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
                <div style="margin-top: 70px">
                    <el-button style="width: 270px" type="danger" @click="startReset">
                        开始重置密码
                    </el-button>
                </div>
            </div>
        </transition >
        <transition name="el-fade-in-linear" mode="out-in">
            <div style="margin-top: 150px;height: 100%" v-if="active === 1">
                <div style="font-size: 25px;font-weight: bold">
                    重置密码
                </div>
                <div style="font-size: 14px;color:grey">
                    请填写您的新密码，务必牢记，防止丢失
                </div>
                <div style="margin-top: 50px">
                    <el-form
                        :model="resetForm"
                        :rules="rules2"
                        ref="formRef">
                        <el-form-item prop="password">
                            <el-input type="password" v-model="resetForm.password" placeholder="密码">
                                <template #prefix>
                                    <el-icon>
                                        <Lock/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password_repeat">
                            <el-input type="password" v-model="resetForm.password_repeat" placeholder="重复密码">
                                <template #prefix>
                                    <el-icon>
                                        <Lock/>
                                    </el-icon>
                                </template>
                            </el-input>
                        </el-form-item>
                    </el-form>
                </div>
                <div style="margin-top: 70px">
                    <el-button style="width: 270px" type="danger" @click="resetPassword">
                        立即重置密码
                    </el-button>
                </div>
            </div>
        </transition>
    </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {EditPen, Lock, Message} from "@element-plus/icons-vue";
import {post} from "@/net";
import {ElMessage} from "element-plus";

const form = reactive({
    email: '',
    code: ''
})

const resetForm = reactive({
    password: '',
    password_repeat:''
})
// 验证两次密码是否相同
const validatePasswordRepeat = (rule, value, callback) => {
    if (value === '') {
        callback(new Error('请输入重复密码'))
    } else if (!(value === resetForm.password)) {
        callback(new Error('请输入正确的重复密码'))
    } else {
        callback()
    }
}
// 步骤条进行到第几步
const active = ref(0)
// 邮箱验证是否通过
const isEmailValid = ref(false)

const onValidate = (prop, isValid) => {
    console.log(isValid);
    if (prop === 'email')
        isEmailValid.value = isValid
}
const formRef = ref()
const coldTime = ref(0);
const rules = {
    email: [
        {required: true, message: '请输入邮箱', trigger: ['blur', 'change']},
        {type: 'email', message: '请输入正确的邮箱', trigger: ['blur', 'change']}
    ],
    coed: [
        {required: true, message: '请输入邮箱', trigger: ['blur', 'change']},
    ]
}
const rules2 = {
    password: [
        {required: true, message: '请输入密码', trigger: ['blur', 'change']},
        {min: 6, max: 16, message: '密码长度必须在6-16个字符之间', trigger: ['blur', 'change']}
    ],
    password_repeat: [
        {validator: validatePasswordRepeat, trigger: ['blur', 'change']}
    ],
}
//发送验证码
const validateEmail = () => {
    post("/api/auth/valid-reset-email", {
        email: form.email
    }, (message) => {
        ElMessage.success(message)
        coldTime.value = 60
        setInterval(() => coldTime.value--, 1000);

    })
}

//重置密码
const resetPassword = () => {
    post("/api/auth/do-reset", {
        password: resetForm.password
    }, (message) => {
        ElMessage.success(message)
        coldTime.value = 60
        setInterval(() => coldTime.value--, 1000);
    })
}

// 开始重置
const startReset = () => {
    post("/api/auth/start-reset", {
        email: form.email,
        code: form.code
    }, (message) => {
        active.value=1;
        ElMessage.success(message)
        router.push("/")
    })
}

</script>

<style scoped>

</style>
