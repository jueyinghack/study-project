<template>
    <router-view/>
</template>

<script setup>
    import {get} from "@/net";
    import {ElMessage} from "element-plus";
    import {useStore} from "@/stores";
    import router from "@/router";
    const store = useStore()
    get("/api/user/me",(message,data)=>{
        ElMessage.success(message)
        store.auth.user = data;
    },(message)=>{
        store.auth.user = null;
        ElMessage.warning("未登录")
    })
</script>

<style scoped>

</style>
