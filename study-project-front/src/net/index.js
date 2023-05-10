import axios from 'axios'
import {ElMessage} from "element-plus";

const defaultError=()=> ElMessage.error('发生了一些错误，请联系管理员')
const defaultFailure=(message)=> ElMessage.warning(message)
function post(url,data,success,failure=defaultFailure,error=defaultError){
    axios.post(url,data,{
        headers:{
            'Content-Type':'application/x-www-form-urlencoded'
        },
        withCredentials:true
    }).then(({data})=>{
        if(data.code === 200){
            success(data.message,data.data)
        }else{
            failure(data.message,data.data)
        }
    }).catch(error)
}
function get(url,success,failure=defaultFailure,error=defaultError){
    axios.get(url,{
        withCredentials:true
    }).then(({data})=>{
        if(data.code === 200){
            success(data.message,data.data)
        }else{
            failure(data.message,data.data)
        }
    }).catch(error)
}

export {get,post}
