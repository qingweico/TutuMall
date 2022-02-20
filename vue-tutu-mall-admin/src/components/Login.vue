<template>
  <div class="login-container">
    <div class="login-left">
      <img src="../assets/imgs/login_img.png" alt="...">
    </div>
    <div class="login-box">
      <!-- 头像 -->
      <div class="avatar-box">
        <img src="../assets/imgs/login_logo.png" alt="...">
      </div>
      <!-- 标题 -->
      <div class="title">
        图图商城管理系统
      </div>
      <!-- 登陆表单 -->
      <el-form ref="loginFormRef" class="login-form" :model="loginForm" :rules="loginFormRules">
        <el-form-item prop="username">
          <el-input placeholder="请输入用户名" prefix-icon="iconfont icon-user" v-model="loginForm.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" show-password prefix-icon="iconfont icon-3702mima"
                    v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="login">登陆</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 表单的数据绑定对象
      loginForm: {
        username: 'admin',
        password: '111',
        code: ''
      },
      // 表单的校验规则
      loginFormRules: {
        username: [
          {required: true, message: '请输入登陆账户', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入登陆密码', trigger: 'blur'},
          {min: 1, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    login() {
      // 判断是否校验成功
      this.$refs.loginFormRef.validate(async valid => {
        if (!valid) {
          return
        }

        // 发送请求判断是否登陆成功
        const {data: res} = await this.$http.get('login', {params: this.loginForm})

        if (res.status !== 200) {
          return this.$message.error(res.msg);
        }
        this.$message.success(res.msg)
        // 将用户信息保存到sessionStorage中
        sessionStorage.setItem('userInfo', JSON.stringify(res.data))
        await this.$router.push('/home')
      })

    }
  }
}
</script>

<style lang="less" scoped>
.login-container {
  background-color: whitesmoke;
  background-image: url("../assets/imgs/background.svg");
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-left {
    margin-right: 100px;
  }

  .login-box {
    width: 400px;
    height: 350px;
    background: #fff;
    padding: 50px;
    border-radius: 6px;
    box-sizing: border-box;
    position: relative;

    .avatar-box {
      width: 120px;
      height: 120px;
      border: 5px solid #93defe;
      border-radius: 100px;
      text-align: center;
      line-height: 110px;
      position: absolute;
      top: -60px;
      right: 140px;
      background: #fff;
    }

    .title {
      position: absolute;
      top: 100px;
      left: 50%;
      transform: translate(-50%);
      font-size: 18px;
      color: #444;
    }

    .login-form {
      position: absolute;
      bottom: 0;
      left: 50%;
      transform: translate(-50%);
      padding: 0 20px;
      width: 80%;
      box-sizing: border-box;

      .login-btn {
        width: 100%;
      }
    }
  }
}
</style>