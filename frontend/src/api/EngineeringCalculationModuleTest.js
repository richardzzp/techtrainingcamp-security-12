import request from '../utils/request';

export default{
    getData: query =>{
        return request({
            url:"./EngineeringCalculationModuleTest.json",
            method: 'get',
            params: query
        });
    }
}
