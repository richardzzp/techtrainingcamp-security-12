import request from '../utils/request';

var url="http://127.0.0.1:9100"
export default{
    // getData: query =>{
    //     return request({
    //         url:"./EngineeringCalculationModuleTest.json",
    //         method: 'get',
    //         params: query
    //     });
    // }
    register: data=>{
        return request({
            url: url+'/cn/register',
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
            url: url+'/cn/loginWithUsername',
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
            url: url+'/cn/loginWithPhone',
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
            url: url+'/api/ljh/apply-code',
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
            url: url+'/cn/getUser',
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
            url: url+'/cn/getLoginRecord',
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
            url: url+'/cn/logout',
            headers:{
                'ip':data.environment.ip,
                'deviceId':data.environment.deviceId
            },
            method:"post",
            data:data
        })
    },
}
