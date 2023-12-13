<template>
    <div>
      <navbar
        :min-nav="navbarMinimize"
        :toggle="toggleConfigurator"
        :class="isNavFixed ? navbarFixed : ''"
      />
    </div>
    <div
      class="mx-3 mt-3 border-radius-xl position-relative"
      :style="{
        backgroundImage: 'url(' + require('@/assets/img/vr-bg.jpg') + ')',
        backgroundSize: 'cover',
      }"
    >
      <sidenav
        :custom_class="mcolor"
        :class="isTransparent"
        class="fixed-start"
      />
      <main class="mt-1 main-content border-radius-lg">
        <div
          class="section min-vh-85 position-relative transform-scale-0 transform-scale-md-7"
        >
        <div class="Box">
          <div style="position: relative;">
              <!-- <el-progress type="circle"  :stroke-width="26" :width="550" :percentage="100" :show-text="false" status="success"></el-progress>
              <div class="name" v-loading="loading">
                  <div v-for="(item, index) in selectList" :key="index">
                      就决定是你了！
                      {{"\n"+ item }}！
                  </div>
                  <div v-if="selectList.length == 0">请开始抽奖吧!</div>
              </div> -->
              <LuckyWheel
                      ref="myLucky"
                      width="500px"
                      height="500px"
                      :prizes="prizes"
                      :blocks="blocks"
                      :buttons="buttons"
                      @start="startCallback"
                      @end="endCallback"
                    />
          </div>
          <!-- <el-button size="large" type="success" round style="margin-top: 20px;" @click="randomStudent();">开始抽奖！</el-button> -->
          <el-button size="large" type="success" round style="margin-top: 10px;" @click="get()">获取学生数据</el-button>
          <el-button size="large" type="success" round style="margin-top: 10px;" @click="back()">返回主界面</el-button>
        </div>
          <div class="container-fluid">
            <div class="pt-10 row">
              <div class="col-lg-8 col-md-11">
                <div class="d-flex">
                  <div class="ms-auto">
                  </div>
                </div>
                
              </div>
            </div>
          </div>
        </div>
      </main>
      
    </div>
    <app-footer class="py-3 bg-white border-radius-lg" />
  </template>
  
  <script>
  
  import AppFooter from "@/examples/Footer.vue";
  import Navbar from "@/examples/Navbars/Navbar.vue";
  import setTooltip from "@/assets/js/tooltip.js";
  import image1 from "@/assets/img/team-1.jpg";
  import image2 from "@/assets/img/team-2.jpg";
  import image3 from "@/assets/img/team-3.jpg";
  import image4 from "@/assets/img/team-4.jpg";
  
  import axios from "axios"
  import { ElMessage } from 'element-plus'
  import { useRouter } from "vue-router";

  
  const body = document.getElementsByTagName("body")[0];
  // const router = useRouter();
  
  import { mapMutations, mapState } from "vuex";
  
  export default {
    name: "VrInfo",
    components: {
      AppFooter,
      Navbar,
    },
    data() {
      return {
        image1,
        image2,
        image3,
        image4,
        students: [],
        selectList: [],//选中的学生数组
        selectedStudent: '',//当前选中的学生
        loading: false,
        Num: 1,
        index: 0,
        show: true,
        router: useRouter(),
        blocks: [
        { padding: '10px', background: '#67C23A' }
      ],
      prizes: [],
      buttons: [
        { radius: '40%', background: '#00BC00' },
        { radius: '35%', background: '#F6F5F4' },
        {
          radius: '30%', background: '#FEFE33',
          pointer: true,
          fonts: [{ text: '开始', top: '-10px' }]
        }
      ],
      
      };
    },
    computed: {
      ...mapState(["isTransparent", "isNavFixed", "navbarFixed", "mcolor"]),
    },
    mounted() {
      setTooltip(this.$store.state.bootstrap);
    },
    beforeMount() {
      this.$store.state.showNavbar = false;
      this.$store.state.showSidenav = false;
      this.$store.state.showFooter = false;
      body.classList.add("virtual-reality");
      this.$store.state.isTransparent = "bg-white";
    },
    beforeUnmount() {
      this.$store.state.showNavbar = true;
      this.$store.state.showSidenav = true;
      this.$store.state.showFooter = true;
      body.classList.remove("virtual-reality");
  
      if (this.$store.state.isPinned === false) {
        const sidenav_show = document.querySelector(".g-sidenav-show");
        sidenav_show.classList.remove("g-sidenav-hidden");
        sidenav_show.classList.add("g-sidenav-pinned");
        this.$store.state.isPinned = true;
      }
      this.$store.state.isTransparent = "bg-transparent";
    },
    methods: {
      ...mapMutations(["navbarMinimize", "toggleConfigurator"]),
      get(){
        axios.get('http://localhost:8080/checkroll/find_all').then(res=>{
            console.log(res);
            for (let i = 0; i <res.data.length; i++) {
                console.log(res.data[i].studentName);
                this.students.push(res.data[i].studentName);
                let data = {};
                if (i%2==0) {
                  data={
                    background: '#92E92D', fonts: [{ text: res.data[i].studentName }]
                  }
                }else{
                  data={
                    background: '#20B237', fonts: [{ text: res.data[i].studentName }]
                  }
                }
                this.prizes.push(data);
            }
        }).catch(err=>{
            console.log("获取数据失败" + err);
        })
      },
      back(){
        this.router.push("/Dashboard");
      },
      //抽取随机学生
      randomStudent() {
        if( this.students.length == 0){
            ElMessage.error('学生已全部遍历或列表为空，请获取学生数据')
        }else{
            this.selectList = []
            this.loading = true
            let arr = JSON.parse(JSON.stringify(this.students))  //深拷贝一份数组
            for (let i = 0; i < this.Num; i++) {
                let index = Math.floor(Math.random() * arr.length);
                this.selectList.push(arr[index]);
                arr.splice(index, 1);
            }
            this.students = arr;
            setTimeout(() => {
                this.loading = false
            }, 500)
        }
      },
      // 点击抽奖按钮会触发star回调
      startCallback () {
        // 调用抽奖组件的play方法开始游戏
        
        this.$refs.myLucky.play()
        // 模拟调用接口异步抽奖
        setTimeout(() => {
          if(this.prizes.length == 0){
            ElMessage.error('学生已全部遍历或列表为空，请获取学生数据');
          }else{
            this.index = Math.floor(Math.random() * this.prizes.length);
            this.$refs.myLucky.stop(this.index)
            //console.log(this.prizes[index].fonts.text)
            
            
          }
          
          //const index = 0
          // 调用stop停止旋转并传递中奖索引
          
        }, 3000)
        
        //this.prizes.splice(index,1)
      },
      // 抽奖结束会触发end回调
      endCallback (prize) {
        console.log(prize.fonts[0].text)
        this.prizes.splice(this.index,1)
      },
    },
  };
  </script>
  <style scoped>
    .Box {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        height: 450px;
        width: 100%;
        padding: 30px 20px;
        font-size: 50px;
        box-sizing: border-box;
    }
    
    .Box .name {
        position: absolute;
        top: 50%;
        left: 52%;
        width: 70%;
        transform: translate(-50%, -50%);
        display: flex;
        flex-direction: column;
        font-size: 50px;
        text-align:center;
        white-space: pre-wrap;
        transition: opacity .5s;
    }
    </style>