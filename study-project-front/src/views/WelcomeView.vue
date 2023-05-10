<template>
    <div style="width: 100vw;height: 100vh;overflow: hidden;display: flex">
        <div style="flex: 1;background-color: aqua">
            <el-image style="width: 100%;height: 100%;" fit="cover"
                      src="https://img0.baidu.com/it/u=3597389780,549037104&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800"/>
        </div>

        <div class="welcome-title">
            <div style="margin-bottom: 10px;font-size: 30px;font-weight: bold">欢迎来到我们的学习平台</div>
            <div>在这里你可以学习Java的知识，如何搭建网站</div>

        </div>

        <div style="width: 400px;background-color:white;z-index: 1">
                <transition name="el-zoom-in-center" >
                    <router-view/>
                </transition>
        </div>

    </div>
</template>

<script setup>

</script>

<style scoped>
.welcome-title{
    position: absolute;
    bottom: 30px;
    left: 30px;
    color: white;
    text-shadow: 0 0 10px black;
}
</style>
