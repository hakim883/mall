<template>
    <div class="container" :class="{'sign-up-mode':signUpMode}">
        <!-- form表单容器 -->
        <div class="forms-container">
            <div class="signin-signup">
                <!-- 登录 -->
                <el-form 
                :model="loginForm"
                label-width="100px" 
                
                class="registerForm">

                  <el-form-item label="Username">
                    <el-input v-model="loginForm.username" placeholder="Enter account..."></el-input>
                  </el-form-item>

                  <el-form-item label="Password">
                    <el-input v-model="loginForm.password" type="password" placeholder="Enter password..."></el-input>
                  </el-form-item>

                  <el-form-item>
                    <el-button @click="login()" type="primary" class="submit-btn">Login</el-button>
                  </el-form-item>

                </el-form>
                <!-- 注册 -->
            </div>
        </div>

        <!-- 左右切换动画 -->
        <div class="panels-container">
            <div class="panel left-panel">
                <div class="content">
                    <h3>Welcome to NEU Profits Manage System!</h3>
                    <p>For more details you can click</p>
                    <button @click="signUpMode = !signUpMode" class="btn transparent">Know More</button>
                </div>
                <img src="@/assets/image/log.svg" class="image"/>
            </div>

            <div class="panel right-panel">
                <div class="content">
                    <h3>The system is designed for NEU staff.</h3>
                    <p>Only internal registration is supported.</p>
                    <button @click="signUpMode = !signUpMode;" class="btn transparent">Back</button>
                </div>
                
                <img src="@/assets/image/register.svg" class="image"/>
            </div>
            
        </div>
    </div>
</template>

<script>
import { ref,getCurrentInstance } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
export default {
    setup(){
        const router = useRouter()
        const { ctx } = getCurrentInstance();
        const signUpMode = ref(false);
        const loginUser = ref({
          email:"",
          password:"",
        });

        const rules = ref({
          email: [
            { type:"email",
              message:"The account does not exist", 
              required:true,
              trigger:"blur",
            }
          ],
          password: [
            { 
              required:true,
              message:"Password could not be empty",
              trigger:"blur",
            },
            {
              min: 6,
              max: 30,
              message: "Password's length is too short.",
              trigger:"blur",
            }
          ]
        });

        //触发登录方法
        const handleLogin = (formName) => {
          ctx.$refs[formName].validate((valid) => {
          if (valid) {
            alert('submit!');
          } else {
            alert("Error submit!!")
            return false;
          }
        });
        };

        function gotoHome(){
          router.push("/Home");
        }

        return { rules,signUpMode,loginUser,handleLogin,gotoHome };
    },
    data(){
        return{
            loginFormVisible:false,
            loginForm:{
              username:'',
              password:'',
              grant_type:'password',
              client_id:'client_1',
              client_secret:'123456'
            },
        }
    },
    methods:{
        login(){
            console.log(this.loginForm);
            this.axios.post('http://localhost:14000/auth/oauth/token',null,{params:this.loginForm})
                .then((res)=>{
                    console.log(res.data);
                    var target = null;
                    target = res.data.access_token;
                    if(target != null){
                      window.sessionStorage.setItem('token',res.data.access_token);
                      window.sessionStorage.setItem('username',this.loginForm.username);
                      window.sessionStorage.setItem('password',this.loginForm.password);
                      ElMessage({showClose: true, message: 'Login Success!', type: 'success'})
                      this.getPositionId();
                      this.gotoHome();
                    }else{
                      ElMessage({showClose: true, message: 'Login Failed!', type: 'success'})
                    }
                                    
                    // if(res.data.access_token != null ){
                    //     this.emp_info = res.data.obj;
                    //     ElMessage({showClose: true, message: res.data.msg, type: 'success'})
                    //     this.gotoHome();
                    // }else{
                    //     if(res.data.status == 500){
                    //         ElMessage({showClose: true, message: res.data.msg, type: 'error'})
                    //     }
                    //     else{
                    //         ElMessage({showClose: true, message: 'Unknown error', type: 'error'})
                    //     }
                        
                    // }
                    
                })

        },

        getPositionId(){
          var target = window.sessionStorage.getItem('token');
          console.log(target);
          this.axios.post('http://localhost:14000/auth/oauth/check_token',null,{
            params:{token:target}
          }).then((res) => {
            console.log(res.data);
            window.sessionStorage.setItem('position',res.data.authorities[0]);
            console.log('position: '+ window.sessionStorage.getItem('position'));
          })
        }
    }
}
</script>

<style scoped>
.container {
  position: relative;
  width: 100%;
  background-color: #fff;
  min-height: 100vh;
  overflow: hidden;
}

.forms-container {
  position: absolute;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

.signin-signup {
  position: absolute;
  
  top: 50%;
  transform: translate(-50%, -50%);
  left: 75%;
  width: 44%;
  transition: 1s 0.7s ease-in-out;
  display: inline;
  z-index: 5;
}

/* 左右切换动画 */
.social-text {
  padding: 0.7rem 0;
  font-size: 1rem;
}

.social-media {
  display: flex;
  justify-content: center;
}

.social-icon {
  height: 46px;
  width: 46px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0 0.45rem;
  color: #333;
  border-radius: 50%;
  border: 1px solid #333;
  text-decoration: none;
  font-size: 1.1rem;
  transition: 0.3s;
}

.social-icon:hover {
  color: #4481eb;
  border-color: #4481eb;
}

.btn {
  width: 150px;
  background-color: #5995fd;
  border: none;
  outline: none;
  height: 49px;
  border-radius: 49px;
  color: #fff;
  text-transform: uppercase;
  font-weight: 600;
  margin: 10px 0;
  cursor: pointer;
  transition: 0.5s;
}

.btn:hover {
  background-color: #4d84e2;
}
.panels-container {
  position: absolute;
  height: 100%;
  width: 100%;
  top: 0;
  left: 0;
  display: grid;
  grid-template-columns: repeat(2, 1fr);
}

.container:before {
  content: "";
  position: absolute;
  height: 2000px;
  width: 2000px;
  top: -10%;
  right: 48%;
  transform: translateY(-50%);
  background-image: linear-gradient(-45deg, #4481eb 0%, #04befe 100%);
  transition: 1.8s ease-in-out;
  border-radius: 50%;
  z-index: 6;
}

.image {
  width: 100%;
  transition: transform 1.1s ease-in-out;
  transition-delay: 0.4s;
}

.panel {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-around;
  text-align: center;
  z-index: 6;
}

.left-panel {
  pointer-events: all;
  padding: 3rem 17% 2rem 12%;
}

.right-panel {
  pointer-events: none;
  padding: 3rem 12% 2rem 17%;
}

.panel .content {
  color: #fff;
  transition: transform 0.9s ease-in-out;
  transition-delay: 0.6s;
}

.panel h3 {
  font-weight: 600;
  line-height: 1;
  font-size: 1.5rem;
}

.panel p {
  font-size: 0.95rem;
  padding: 0.7rem 0;
}

.btn.transparent {
  margin: 0;
  background: none;
  border: 2px solid #fff;
  width: 130px;
  height: 41px;
  font-weight: 600;
  font-size: 0.8rem;
}

.right-panel .image,
.right-panel .content {
  transform: translateX(800px);
}

/* ANIMATION */

.container.sign-up-mode:before {
  transform: translate(100%, -50%);
  right: 52%;
}

.container.sign-up-mode .left-panel .image,
.container.sign-up-mode .left-panel .content {
  transform: translateX(-800px);
}

.container.sign-up-mode .signin-signup {
  left: 25%;
}

.container.sign-up-mode form.sign-up-form {
  opacity: 1;
  z-index: 2;
}

.container.sign-up-mode form.sign-in-form {
  opacity: 0;
  z-index: 1;
}

.container.sign-up-mode .right-panel .image,
.container.sign-up-mode .right-panel .content {
  transform: translateX(0%);
}

.container.sign-up-mode .left-panel {
  pointer-events: none;
}

.container.sign-up-mode .right-panel {
  pointer-events: all;
}

@media (max-width: 870px) {
  .container {
    min-height: 800px;
    height: 100vh;
  }
  .signin-signup {
    width: 100%;
    top: 95%;
    transform: translate(-50%, -100%);
    transition: 1s 0.8s ease-in-out;
  }

  .signin-signup,
  .container.sign-up-mode .signin-signup {
    left: 50%;
  }

  .panels-container {
    grid-template-columns: 1fr;
    grid-template-rows: 1fr 2fr 1fr;
  }

  .panel {
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
    padding: 2.5rem 8%;
    grid-column: 1 / 2;
  }

  .right-panel {
    grid-row: 3 / 4;
  }

  .left-panel {
    grid-row: 1 / 2;
  }

  .image {
    width: 200px;
    transition: transform 0.9s ease-in-out;
    transition-delay: 0.6s;
  }

  .panel .content {
    padding-right: 15%;
    transition: transform 0.9s ease-in-out;
    transition-delay: 0.8s;
  }

  .panel h3 {
    font-size: 1.2rem;
  }

  .panel p {
    font-size: 0.7rem;
    padding: 0.5rem 0;
  }

  .btn.transparent {
    width: 110px;
    height: 35px;
    font-size: 0.7rem;
  }

  .container:before {
    width: 1500px;
    height: 1500px;
    transform: translateX(-50%);
    left: 30%;
    bottom: 68%;
    right: initial;
    top: initial;
    transition: 2s ease-in-out;
  }

  .container.sign-up-mode:before {
    transform: translate(-50%, 100%);
    bottom: 32%;
    right: initial;
  }

  .container.sign-up-mode .left-panel .image,
  .container.sign-up-mode .left-panel .content {
    transform: translateY(-300px);
  }

  .container.sign-up-mode .right-panel .image,
  .container.sign-up-mode .right-panel .content {
    transform: translateY(0px);
  }

  .right-panel .image,
  .right-panel .content {
    transform: translateY(300px);
  }

  .container.sign-up-mode .signin-signup {
    top: 5%;
    transform: translate(-50%, 0);
  }
}

@media (max-width: 570px) {
  form {
    padding: 0 1.5rem;
  }

  .image {
    display: none;
  }
  .panel .content {
    padding: 0.5rem 1rem;
  }
  .container {
    padding: 1.5rem;
  }

  .container:before {
    bottom: 72%;
    left: 50%;
  }

  .container.sign-up-mode:before {
    bottom: 28%;
    left: 50%;
  }
}

/* 控制login & register显示 */
form {
  padding: 0rem 5rem;
  transition: all 0.2s 0.7s;
  overflow: hidden;
}

form.sign-in-form {
  z-index: 2;
}

form.sign-up-form {
  opacity: 0;
  z-index: 1;
}

/* register */
.registerForm {
  margin-top: 20px;
  background-color: #fff;
  padding: 40px 40px 20px 0px;
  border-radius: 5px;
  box-shadow: 0px 5px 10px #cccc;
}

.submit-btn {
  width: 100%;
}

</style>