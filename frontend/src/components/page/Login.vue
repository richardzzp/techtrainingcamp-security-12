<template>
    <div class="login-wrap">
        <div class="ms-login">
            <div class="ms-title">抓到你啦！</div>
            <el-tabs type="border-card" v-model="activeName" :stretch="true" class="tabs">
                <el-tab-pane label="账号密码登录" name="account">
                    <el-form :model="accountParam" :rules="accountRules" ref="account_login" label-width="0px" class="ms-content">
                        <el-form-item prop="username">
                            <el-input v-model="accountParam.username" placeholder="username">
                                <el-button slot="prepend" icon="el-icon-lx-people"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="password">
                            <el-input type="password" placeholder="password" v-model="accountParam.password" @keyup.enter.native="LoginSubmit()">
                                <el-button slot="prepend" icon="el-icon-lx-lock"></el-button>
                            </el-input>
                        </el-form-item>
                        <div class="register-text">
                            <el-link type="danger" @click="test">还没有账号？</el-link>
                        </div>
                        <div class="login-btn">
                            <el-button type="primary" @click="LoginSubmit()">登录</el-button>
                        </div>
                    </el-form>
                </el-tab-pane>
                <el-tab-pane label="手机号登录" name="phone">
                    <el-form :model="phoneParam" ref="phone_login" label-width="0px" class="ms-content">
                        <el-form-item prop="phone">
                            <el-input v-model="phoneParam.phone" placeholder="phone number">
                                <el-button slot="prepend" icon="el-icon-phone"></el-button>
                            </el-input>
                        </el-form-item>
                        <el-form-item prop="verifyCode">
                            <el-input type="verifyCode" placeholder="verifyCode" v-model="phoneParam.verifyCode" @keyup.enter.native="LoginSubmit()">
                                <el-button slot="append">获取验证码</el-button>
                            </el-input>
                        </el-form-item>
                        <div class="register-text">
                            <el-link type="danger" @click="test">还没有账号？</el-link>
                        </div> 
                        <div class="login-btn">
                            <el-button type="primary" @click="LoginSubmit()">登录</el-button>
                        </div>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </div>
    </div>
</template>

<script>
export default {
    data: function () {
        return {
            activeName: 'account',
            accountParam:{},
            phoneParam:{},
            accountRules: {
                username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
                password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
            }
        };
    },
    methods: {
        LoginSubmit() {
            this.$refs.account_login.validate((valid) => {
                if (valid) {
                    console.log(this.accountParam)
                    this.$message.success('登录成功');
                    localStorage.setItem('ms_username', this.accountParam.username);
                    this.$router.push('/');
                } else {
                    this.$message.error('请输入账号和密码');
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        test() {
            console.log(1);
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
    padding: 30px 30px;
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