<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">抓到你啦！</div>
            <el-tabs type="border-card" v-model="activeName" :stretch="true" class="tabs" v-if="loginState">
                <el-tab-pane label="账号密码登录" name="account">
                    <el-form :model="accountParam" :rules="accountRules" ref="account_login" label-width="0px" class="ms-content">
                        <el-form-item prop="username">
                            <el-input v-model="accountParam.username" placeholder="username">
                                <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input
                                type="password"
                                placeholder="password"
                                v-model="accountParam.password"
                                @keyup.enter.native="LoginSubmit()"
                                :show-password="true"
                            >
                                <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                            </el-input>
                        </el-form-item>
                        <div class="register-text">
                            <el-link type="danger" @click="loginState = false">还没有账号？</el-link>
                        </div>
                        <div class="login-btn">
                            <el-button type="primary" @click="LoginSubmit()">登录</el-button>
                        </div>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="手机号登录" name="phone">
                    <el-form :model="phoneParam" :rules="phoneRules" ref="phone_login" label-width="0px" class="ms-content">
                        <el-form-item prop="phone">
                            <el-input v-model="phoneParam.phone" placeholder="phone number">
                                <el-button slot="prepend" icon="el-icon-phone"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="verifyCode">
                            <el-input
                                type="verifyCode"
                                placeholder="verifyCode"
                                v-model="phoneParam.verifyCode"
                                @keyup.enter.native="LoginSubmit()"
                            >
                                <el-button slot="prepend" icon="el-icon-key"></el-button>
                                <el-button slot="append" @click="getCode('login')" :disabled='codeDisabled'>获取验证码{{ codeText }}</el-button>
                            </el-input>
                        </el-form-item>
                        <div class="register-text">
                            <el-link type="danger" @click="loginState = false">还没有账号？</el-link>
                        </div>
                        <div class="login-btn">
                            <el-button type="primary" @click="LoginSubmit()">登录</el-button>
                        </div>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
            <el-tabs type="border-card" v-model="activeNameRegister" :stretch="true" class="tabs" v-else>
                <el-tab-pane label="账号手机号注册" name="first">
                    <el-form :model="registerParam" :rules="registerRules" ref="register" label-width="0px" class="ms-content">
                        <el-form-item prop="username">
                            <el-input v-model="registerParam.username" placeholder="username">
                                <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input type="password" placeholder="password" v-model="registerParam.password" :show-password="true">
                                <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="passwordConfirm">
                            <el-input type="password" placeholder="password" v-model="registerParam.passwordConfirm" :show-password="true">
                                <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="phone">
                            <el-input v-model="registerParam.phone" placeholder="phone number">
                                <el-button slot="prepend" icon="el-icon-phone"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="verifyCode">
                            <el-input type="verifyCode" placeholder="verifyCode" v-model="registerParam.verifyCode">
                                <el-button slot="prepend" icon="el-icon-key"></el-button>
                                <el-button slot="append" @click="getCode('register')" :disabled='codeDisabled'>获取验证码{{ codeText }}</el-button>
                            </el-input>
                        </el-form-item>
                        <div class="register-text">
                            <el-link type="primary" @click="loginState = true">返回登录</el-link>
                        </div>
                        <div class="login-btn">
                            <el-button type="primary" @click="registerSubmit()">注册</el-button>
                        </div>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
import Fingerprint2 from 'fingerprintjs2';
import Aips from '../../api/account';
export default {
    data: function () {
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.registerParam.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            verifyCode:"",
            expireTime:"",
            decisionType:"",
            //发送验证码按钮禁用
            codeDisabled:false,
            //60秒倒计时用
            codeText: '',
            codeTime: 0,
            activeName: 'account',
            activeNameRegister: 'first',
            loginState: true,
            accountParam: {},
            phoneParam: {},
            registerParam: {},
            accountRules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
            },
            phoneRules: {
                phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
                verifyCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
            },
            registerRules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
                passwordConfirm: [{ validator: validatePass2, trigger: 'blur' }],
                phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
                verifyCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
            }
        };
    },
    created() {
        console.log(localStorage.getItem('ip'));
        localStorage.setItem('ip', returnCitySN['cip']);
        //获取设备id
        Fingerprint2.get(function (components) {
            const values = components.map(function (component, index) {
                if (index === 0) {
                    //把微信浏览器里UA的wifi或4G等网络替换成空,不然切换网络会ID不一样
                    return component.value.replace(/\bNetType\/\w+\b/, '');
                }
                return component.value;
            });
            // 生成最终id murmur
            const murmur = Fingerprint2.x64hash128(values.join(''), 31);
            console.log('浏览器指纹码：' + murmur);
            localStorage.setItem('deviceID', murmur);
        });
    },
    methods: {
        LoginSubmit() {
            if (this.activeName == 'account') {
                //账号登录
                this.$refs.account_login.validate((valid) => {
                    if (valid) {
                        var data = {
                            username: this.accountParam.username,
                            password: this.accountParam.password,
                            environment: {
                                ip: localStorage.getItem('ip'),
                                deviceId: localStorage.getItem('deviceID')
                            }
                        };
                        Aips.loginWithAccount(data).then((res) => {
                            if (res.code == 0) {
                                //成功
                                this.$message.success(res.message);
                                localStorage.setItem('sessionId', res.data.sessionId);
                                console.log(res.data.sessionId);
                                this.$message.success('登录成功');
                                localStorage.setItem('userName', this.accountParam.username);
                                this.accountParam = {};
                                this.$router.push('/');
                            } else {
                                this.$message.error(res.message);
                            }
                        });
                        console.log(this.accountParam);             
                    } else {
                        this.$message.error('请输入账号和密码');
                        console.log('error submit!!');
                        return false;
                    }
                });
            }
        },
        registerSubmit() {
            this.$refs.register.validate((valid) => {
                //验证码检查错误
                if(!this.validateCode(this.registerParam.verifyCode)){
                    return 0
                }
                if (valid) {
                    var data = {
                        username: this.registerParam.username,
                        password: this.registerParam.password,
                        phoneNumber: this.registerParam.phone,
                        verifyCode: this.registerParam.verifyCode,
                        environment: {
                            ip: localStorage.getItem('ip'),
                            deviceId: localStorage.getItem('deviceID')
                        }
                    };
                    Aips.register(data).then((res) => {
                        if (res.code == 0) {
                            //成功
                            this.$message.success(res.message);
                            localStorage.setItem('sessionId', res.data.sessionId);
                            console.log(res.data.sessionId);
                            this.registerParam = {};
                            this.loginState = true;
                        } else {
                            this.$message.error(res.message);
                        }
                    });
                    console.log(data);
                } else {
                    this.$message.error('请检查注册信息');
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        getCode(type) {
            var data = {
                environment: {
                    ip: localStorage.getItem('ip'),
                    deviceId: localStorage.getItem('deviceID')
                }
            };
            if(type=='login'){
                if(this.checkPhone(this.phoneParam.phone)){
                    data.phoneNumber=this.accountParam.phone
                }else{
                    return 0
                }
            }else{
                if(this.checkPhone(this.registerParam.phone)){
                    data.phoneNumber=this.registerParam.phone
                }else{
                    return 0
                }
            }
            Aips.applyCode(data).then(res=>{
                if (res.code == 0) {
                    //成功
                    this.decisionType=res.data.decisionType
                    if(this.decisionType==0){
                        this.$message.success(res.message);
                        this.verifyCode=res.data.verifyCode
                        this.expireTime=res.data.expireTime
                        console.log(res.data)
                    }     
                } else {
                    this.$message.error(res.message);
                }
            })
            //按钮字改变，禁用
            this.codeTime = 60;
            this.codeDisabled=true
            this.codeText = '(' + this.codeTime + ')';
            this.time = setInterval(this.timer, 1000);
        },
        timer() {
            this.codeTime -= 1;
            if (this.codeTime != 0) {
                this.codeText = '(' + this.codeTime + ')';
            } else {
                this.codeText = '';
                this.codeDisabled=false
                clearInterval(this.time);
            }
        },
        checkPhone(phone){ 
            if(!(/^1[34578]\d{9}$/.test(phone))){
                this.$message.error('手机号格式错误') 
                return false; 
            } 
            return true
        },
        //检验验证码
        validateCode(value){
            ////////////////////后面要删除123456
            if(value=="123456"){
                return true
            }
            if (value === '') {
                this.$message.error('请输入验证码')
                return false
            } else if (new Date(this.expireTime).getTime()<new Date().getTime()) {
                this.$message.error('验证码已过期')
                return false
            } else if(this.verifyCode!=value){
                this.$message.error('验证码错误')
                return false
            } 
            return true
        }
    }
};
</script>

<style scoped>
.register-text {
    text-align: right;
    font-size: 14px;
    padding-bottom: 10px;
    color: tomato;
}
.register-back {
    text-align: left;
    font-size: 14px;
    padding-bottom: 10px;
}
.login-wrap {
    position: relative;
    width: 100%;
    height: 100%;
    background-image: url(../../assets/img/bg1.jpg);
    background-size: 100%;
}
.ms-title {
    width: 100%;
    line-height: 50px;
    text-align: center;
    font-size: 20px;
    color: #409eff;
    border-bottom: 1px solid #ddd;
}
.ms-login {
    position: absolute;
    left: 50%;
    top: 50%;
    width: 350px;
    margin: -190px 0 0 -175px;
    border-radius: 5px;
    background: rgb(255, 255, 255);
    overflow: hidden;
}
.ms-content {
}
.login-btn {
    text-align: center;
}
.login-btn button {
    width: 100%;
    height: 36px;
    margin-bottom: 10px;
}
.login-tips {
    font-size: 12px;
    line-height: 30px;
    color: #fff;
}
</style>