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
            method: "post",
            data:data
        })

    },
    loginWithAccount: data=>{
        return request({
            url: url+'/cn/loginWithUsername',
            method:"post",
            data:data
        })
    },
    loginWithPhone: data=>{
        return request({
            url: url+'/cn/loginWithPhone',
            method:"post",
            data:data
        })
    },
    applyCode: data=>{
        return request({
            url: url+'/api/ljh/apply-code',
            method:"post",
            data:data
        })
    },
}
