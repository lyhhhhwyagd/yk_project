<template>
  <main class="mt-0 main-content main-content-bg">
    <section>
      <div class="page-header min-vh-75">
        <div class="container">
          <div class="row">
            <div class="mx-auto col-xl-4 col-lg-5 col-md-6 d-flex flex-column">
              <div class="mt-8 card card-plain">
                <div class="pb-0 card-header text-start">
                  <h3 class="font-weight-bolder text-success text-gradient">
                    移动互动课堂
                  </h3>
                  <p class="mb-0">输入邮箱和密码后进行登录</p>
                </div>
                <div class="card-body">
                  <form role="form" class="text-start">
                    <label>邮箱</label>
                    <soft-input
                        id="email"
                        type="email"
                        placeholder="Email"
                        name="email"
                        v-model="userCredentials.email"
                    />
                    <label>密码</label>
                    <soft-input
                        id="password"
                        type="password"
                        placeholder="Password"
                        name="password"
                        v-model="userCredentials.password"
                    />
                    <soft-switch id="rememberMe" name="rememberMe" checked>
                      记住账号密码
                    </soft-switch>
                    <div class="text-center">
                      <soft-button
                          class="my-4 mb-2"
                          variant="gradient"
                          color="success"
                          full-width
                          @click.prevent="submitForm"
                      >登录
                      </soft-button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
            <div class="col-md-6">
              <div
                  class="top-0 oblique position-absolute h-100 d-md-block d-none me-n8"
              >
                <div
                    class="bg-cover oblique-image position-absolute fixed-top ms-auto h-100 z-index-0 ms-n6"
                    :style="{
                    backgroundImage:
                      'url(' +
                      require('@/assets/img/curved-images/curved9.jpg') +
                      ')',
                  }"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>
</template>

<script>
import axios from "axios";
import Navbar from "@/AAA_Ding/Components/Navbar.vue";
import AppFooter from "@/AAA_Ding/Components/Footer.vue";
import SoftInput from "@/AAA_Ding/Components/SoftInput.vue";
import SoftSwitch from "@/AAA_Ding/Components/SoftSwitch.vue";
import SoftButton from "@/AAA_Ding/Components/SoftButton.vue";
const body = document.getElementsByTagName("body")[0];
import { mapMutations } from "vuex";

export default {
  name: "SignIn",
  data() {
    return {
      user:[],
      userCredentials: {
        userEmail: '',
        password: '',
      }
    };
  },
  components: {
    Navbar,
    AppFooter,
    SoftInput,
    SoftSwitch,
    SoftButton,
  },
  created() {
    this.toggleEveryDisplay();
    this.toggleHideConfig();
    body.classList.remove("bg-gray-100");
  },
  beforeUnmount() {
    this.toggleEveryDisplay();
    this.toggleHideConfig();
    body.classList.add("bg-gray-100");
  },
  methods: {
    ...mapMutations(["toggleEveryDisplay", "toggleHideConfig"]),
    submitForm() {
      if (this.userCredentials.email === '' || this.userCredentials.password === '') {
        alert('账号或密码不能为空');
      }else{
        axios.post('http://localhost:8080/loginIn',this.userCredentials)
            .then(response => {
              if (response.data.code === 200) {
                this.user = response.data.data;
                this.$store.commit('setUserID', this.user.userID);
                this.$router.push({ name: '论坛主页', query: { userID: this.user.userID } });
              } else {
                window.alert("账号密码错误");
                console.error('Error fetching rewards: ' + response.data.message);
              }
            })
            .catch(error => {
              console.error(error);
            });
      }
    },
  },
};
</script>
