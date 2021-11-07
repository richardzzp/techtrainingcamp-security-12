import request from '../utils/request';

var url="http://127.0.0.1:9100"
export default{
    register: data=>{
        return request({
            url: url+'/auth/register',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method: "post",
            data:data
        })

    },
    loginWithAccount: data=>{
        return request({
            url: url+'/auth/loginWithUsername',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
    loginWithPhone: data=>{
        return request({
            url: url+'/auth/loginWithPhone',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
    applyCode: data=>{
        return request({
            url: url+'/verifyCode/applyCode',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
    getUser: data=>{
        return request({
            url: url+'/auth/getUser',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
    getLoginRecord: data=>{
        return request({
            url: url+'/auth/getLoginRecord',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
    logout: data=>{
        return request({
            url: url+'/auth/logout',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
}
