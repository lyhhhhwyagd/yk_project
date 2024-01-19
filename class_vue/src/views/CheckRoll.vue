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
        </div>
        <div class="Box">
          <el-row  :gutter="20">
            <el-col :span="24" :offset="10">
              <el-button size="large" type="success" round style="margin-top: 10px;" @click="get()">获取学生数据</el-button>
              <el-button size="large" type="success" round style="margin-top: 10px;" @click="back()">返回主界面</el-button>
            </el-col>
            <el-col :span="12">
              <h3>全部名单</h3>
              <el-table
                id="out-table"
                :data="filterstudents"
                height="250"
                style="width: 100%"
              >
                <el-table-column prop="name" label="姓名" sortable width="180" />
                <el-table-column prop="id" label="学号" sortable />
                <el-table-column align="right">
                  <template #header>
                    <el-input v-model="search" size="small" placeholder="请输入信息" />
                  </template>
                  <template #default="scope">
                    <el-button type="success"  size="small" @click="dialogVisible1 = true,handleEdit(scope.$index, scope.row)"
                      >编辑</el-button
                    >
                    <el-button
                      size="small"
                      type="danger"
                      @click="handleDelete(scope.$index, scope.row)"
                      >删除</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
              <el-col :span="24" :offset="10">
                <el-button size="large" type="success" round style="margin-top: 10px;" @click="dialogVisible = true">添加学生</el-button>
                <el-button size="large" type="success" round style="margin-top: 10px;" @click="exportExcel">导出名单</el-button>
                <el-button size="large" type="success" round style="margin-top: 10px;" @click="print">打印名单</el-button>
              </el-col>
            </el-col>
            <el-col :span="12">
              <h3>已抽学生</h3>
              <el-table
                :data="selectList"
                style="width: 100%"
              >
              <el-table-column prop="name" label="姓名" sortable width="180" />
              <el-table-column prop="id" label="学号" sortable />
              </el-table>
            </el-col>
          </el-row>
        </div>
      </div>
      </main>
      <el-dialog
        v-model="dialogVisible"
        title="添加信息"
        width="30%"
        :before-close="handleClose"
      >
        <!-- <span>This is a message</span> -->
        <el-form :model="addForm" label-width="120px">
          <el-form-item label="姓名">
            <el-input v-model="addForm.name" />
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="addForm.id" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="success" @click="dialogVisible = false, handleAdd()">
              确认
            </el-button>
          </span>
        </template>
      </el-dialog>
      <el-dialog
        v-model="dialogVisible1"
        title="编辑信息"
        width="30%"
        :before-close="handleClose"
      >
        <!-- <span>This is a message</span> -->
        <el-form :model="editForm" label-width="120px">
          <el-form-item label="姓名">
            <el-input v-model="editForm.name" />
          </el-form-item>
          <el-form-item label="学号">
            <el-input v-model="editForm.id" />
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible1 = false">取消</el-button>
            <el-button type="success" @click="dialogVisible1 = false, submmitChange()">
              确认
            </el-button>
          </span>
        </template>
      </el-dialog>
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
  import FileSaver from "file-saver";
  import XLSX from "xlsx";
  import printJS from 'print-js';


  
  const body = document.getElementsByTagName("body")[0];
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
        dialogVisible: false,
        dialogVisible1: false,
        Num: 1,
        index: 0,
        length: 0,
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
      addForm: {name:"",id:""},
      editForm:'' ,
      search:''
      };
    },
    computed: {
      ...mapState(["isTransparent", "isNavFixed", "navbarFixed", "mcolor"]),
      filterstudents() {
      return this.students.filter(
        (data) => {
        // Convert each property to lowercase for case-insensitive comparison
        const lowerSearch = this.search.toLowerCase();
        const lowerName = data.name.toLowerCase();
        const lowerId = data.id.toLowerCase();

        // Check if any property contains the search value
        return (
          !lowerSearch ||
          lowerName.includes(lowerSearch) ||
          lowerId.includes(lowerSearch) 
        );
      }
      );
    },
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
            this.length += res.data.length;
            for (let i = 0; i <res.data.length; i++) {
                console.log(res.data[i].studentName);
                this.students.push({name: res.data[i].studentName, id: res.data[i].studentId});
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
      handleEdit(index, row) {
        console.log(index, row);
        this.editForm = row;
        console.log(this.editForm)
        this.index = index
      },
      submmitChange(){
        this.students[this.index] =  this.editForm;
        this.prizes[this.index].fonts = [{ text: this.editForm.name }];
      },
      handleDelete(index, row) {
        console.log(index, row);
        this.students.splice(index,1)
        this.prizes.splice(index,1)
      },
      handleAdd(){
        console.log(this.addForm);
        this.students.push(this.addForm);
        let data = {};
        if (this.length%2==0) {
          data={
            background: '#92E92D', fonts: [{ text: this.addForm.name }]
          }
        }else{
          data={
            background: '#20B237', fonts: [{ text: this.addForm.name }]
          }
        }
        this.prizes.push(data);
        this.length ++;
        this.addForm = {name:"",id:""};
      },
      exportExcel() {
        console.log(111)
        var xlsxParam = { raw: true };
        /* 从表生成工作簿对象 */
        var wb = XLSX.utils.table_to_book(
          document.querySelector("#out-table"),
          xlsxParam
        );
        /* 获取二进制字符串作为输出 */
        var wbout = XLSX.write(wb, {
          bookType: "xlsx",
          bookSST: true,
          type: "array",
        });
        try {
          FileSaver.saveAs(
            //Blob 对象表示一个不可变、原始数据的类文件对象。
            //Blob 表示的不一定是JavaScript原生格式的数据。
            //File 接口基于Blob，继承了 blob 的功能并将其扩展使其支持用户系统上的文件。
            //返回一个新创建的 Blob 对象，其内容由参数中给定的数组串联组成。
            new Blob([wbout], { type: "application/octet-stream" }),
            //设置导出文件名称
            "抽奖名单.xlsx"
          );
        } catch (e) {
          if (typeof console !== "undefined") console.log(e, wbout);
        }
        return wbout;
    },
    print() {
      const style = '@page {  } ' + '@media print {td{border:1px    solid #000;text-align:center;height:40px}th{border:1px solid #000} }';//这里修改的是el-table边框问题

      printJS({
        printable: 'out-table',	//打印区域id
        type: 'html',		//打印类型是html
        scanStyles: false,
        style: style,
        targetStyles: ['*'],
      })
    },
      //抽取随机学生
      // randomStudent() {
      //   if( this.students.length == 0){
      //       ElMessage.error('学生已全部遍历或列表为空，请获取学生数据')
      //   }else{
      //       this.selectList = []
      //       this.loading = true
      //       let arr = JSON.parse(JSON.stringify(this.students))  //深拷贝一份数组
      //       for (let i = 0; i < this.Num; i++) {
      //           let index = Math.floor(Math.random() * arr.length);
      //           this.selectList.push(arr[index]);
      //           arr.splice(index, 1);
      //       }
      //       this.students = arr;
      //       setTimeout(() => {
      //           this.loading = false
      //       }, 500)
      //   }
      // },
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
        //this.students.splice(this.index,1)
        this.selectList.push(this.students[this.index])
      },
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      }
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