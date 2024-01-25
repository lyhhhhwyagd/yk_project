import jsPDF from "jspdf";
<template>
   <div  v-if="userRight">
      <div class="card" style="margin-bottom: 10px ;" >
         <div  >
            <el-input style="width: 220px" size="small" v-model="studentName"  placeholder="请输入学生姓名" :prefix-icon="Search"/>
            <el-button type="primary" size="small" style="background-color: rgb(128, 214, 22)" @click="selectBystudentName">查询</el-button>
            <el-button style="background-color: rgb(128, 214, 22)" size="small" type="primary" @click="AddStudent">增加</el-button>
            <el-button size="small" type="primary" @click="printBox" style="background-color: rgb(128, 214, 22);">打印</el-button>
            <el-button @click="exportPDF" size="small" type="primary" style="background-color: rgb(128, 214, 22);">导出PDF</el-button>
            <el-button @click="showChartDialog" size="small" type="primary" style="background-color: rgb(128, 214, 22);">
               统计
            </el-button>
            <el-button @click="classmanageSta" size="small" type="primary" style="background-color: rgb(128, 214, 22);">
               分析
            </el-button>

         </div>
      </div>

      <div class="card" style="margin-bottom: 10px">
         <div id="printBox">
            <el-table  :data="ClassManages" :default-sort="{ prop: 'classManageId' }">
               <el-table-column label="序号" sortable prop="classManageId"></el-table-column>
               <el-table-column label="学号" prop="studentId" width="180"></el-table-column>
               <el-table-column label="姓名" prop="studentName" width="180"></el-table-column>
               <el-table-column label="出勤率" prop="attendanceRate"></el-table-column>
               <el-table-column label="平时分" prop="dailyScore"></el-table-column>
               <el-table-column >
                  <template #default="scope">
                     <el-button style="background-color: rgb(128, 214, 22)" size="small" type="primary"  @click="handleEdit(scope.row)">修改</el-button>
                  </template>
               </el-table-column>
               <el-table-column >
                  <template #default="scope">
                     <el-button style="background-color: rgb(128, 214, 22)" size="small" type="primary"  @click="deleteStudent(scope.row.classManageId)">删除</el-button>
                  </template>
               </el-table-column>
            </el-table>
         </div>
      </div>


      <el-dialog v-model="showChartDialog1" title="班级情况分析">
         <canvas id="myChart"  width="400" height="400"></canvas>
         <canvas id="myChart1"  width="400" height="400"></canvas>
      </el-dialog>
      <el-dialog title="课程信息" width="40%" v-model="formVisible" >
         <el-form :model="form" label-width="100px" style="padding-right: 50px">
            <el-form-item label="学号" prop="studentId">
               <el-input v-model="form.studentId" autocomplete="off" />
            </el-form-item>
            <el-form-item label="姓名" prop="studentName">
               <el-input v-model="form.studentName" autocomplete="off" />
            </el-form-item>
            <el-form-item label="出勤率" prop="attendanceRate">
               <el-input v-model="form.attendanceRate" autocomplete="off" />
            </el-form-item>
            <el-form-item label="平时分" prop="dailyScore">
               <el-input v-model="form.dailyScore" autocomplete="off" />
            </el-form-item>
         </el-form>
         <template #footer>
      <span class="dialog-footer">
        <el-button  @click="formVisible = false">取 消</el-button>
        <el-button style="background-color: #76dc30" type="primary" @click="submitChange">保 存</el-button>
      </span>
         </template>
      </el-dialog>

      <el-dialog title="课程信息" width="40%" v-model="formaddVisible" >
         <el-form :model="form" label-width="100px" style="padding-right: 50px">
            <el-form-item label="学号" prop="studentId">
               <el-input v-model="form.studentId" autocomplete="off" />
            </el-form-item>
            <el-form-item label="姓名" prop="studentName">
               <el-input v-model="form.studentName" autocomplete="off" />
            </el-form-item>
            <el-form-item label="出勤率" prop="attendanceRate">
               <el-input v-model="form.attendanceRate" autocomplete="off" />
            </el-form-item>
            <el-form-item label="平时分" prop="dailyScore">
               <el-input v-model="form.dailyScore" autocomplete="off" />
            </el-form-item>
         </el-form>
         <template #footer>
      <span class="dialog-footer">
        <el-button  @click="formaddisible = false">取 消</el-button>
        <el-button style="background-color: #76dc30" type="primary" @click="submitAdd">保 存</el-button>
      </span>
         </template>
      </el-dialog>
   </div>
   <div  v-if="!userRight">
      <p> 您没有查看此内容的权限</p>
   </div>
   <div id="main" style="width: 60%; height: 300px">
   </div>
</template>

<script>

   //import request from "@/utils/request";
   import {reactive, ref} from "vue";
   import {ElMessage} from "element-plus";
   import {ElMessageBox} from "element-plus";
   import * as echarts from 'echarts';
   import {onMounted} from "@vue/runtime-core";
   import axios from "axios";
   import html2canvas from 'html2canvas';
   import jsPDF from 'jspdf';
   import print from "print-js";
   import {Chart} from "chart.js";
   // request.get('/').then(res => {
   //     console.log(res)
   // })
   export default{
      name: "Classmanage",
      data() {
         this.taskInput = ref(null);
         this.taskContent = ref(null);
         this.task_input = ref(null);
         return {
            courseName:"",
            studentName:"",
            dep:"",
            ClassManages:{},
            total:0,
            pageSize:4,//当前最大个数
            pageNum:1,
            formVisible:false,
            showChartDialog1: false,
            formaddVisible:false,
            userRight:false,
            UserID:0,
            form:{}
         }
      },
      created() {
         this.$store.commit("SET_TOKEN", localStorage.getItem("userToken"));
         this.$store.commit("SET_FILTER", localStorage.getItem("filterPattern"));
         //this.$store.commit("LOGIN");
         this.UserID = this.$route.query.userID;
         console.log("How to get userID",this.$route.query.userID);
         const userID = parseInt(this.$route.query.userID);
         console.log("useID 类型",typeof userID);
         if(userID < 30000){
            console.log("有权限");
            this.userRight = true;
            if(this.userRight == true){
               this.loadClassManage();
            }
         }else{
            console.log("无权查看");
         }
      },
      methods:{
         loadClassManage() {
            //let pattern = this.$store.state.filterPattern;
            let param = new FormData;
            //后续需获取用户登录传递的用户id
            //param.append("token", this.$store.state.user.token)
            param.append("token", "1")
            axios.post("http://localhost:8080/Classmanage/getclassmanage", param).then(res => {
               //this.tasks = Object.assign({},res.data.result)
               //console.log("loadClassManage res=", res);
               console.log("loadClassManage res.data.result=",res.data.result)
               //console.log("loadClassManage res.data.result=",res.data.result[0].studentName)
               this.ClassManages = res.data.result;
               this.refresh = Math.random(); //强制触发vue的diff重新渲染
            })
         },
         classmanageSta(){
            this.$router.push({
               name: '课堂数据分析',
               query: { ClassManages: JSON.stringify(this.ClassManages) } });
         },
         handleEdit(row) {
            this.form = JSON.parse(JSON.stringify(row));
            this.formVisible = true;
         },
         selectBystudentName(){
            let param = new FormData;
            //后续需获取用户登录传递的用户id
            //param.append("token", this.$store.state.user.token)
            console.log("selectBystudentName studentName",this.studentName);
            param.append("studentName", this.studentName)
            axios.post("http://localhost:8080/Classmanage/getclassmanageBystudentName", param).then(res => {
               //this.tasks = Object.assign({},res.data.result)
               //console.log("loadClassManage res=", res);
               console.log("selectBystudentName res.data.result=",res.data.result)
               //console.log("loadClassManage res.data.result=",res.data.result[0].studentName)
               this.ClassManages = res.data.result;
               this.refresh = Math.random(); //强制触发vue的diff重新渲染
            })

         },
         showChartDialog() {
            this.showChartDialog1 = true;
            console.log("this.ClassManages",this.ClassManages);
            console.log("this.ClassManages",this.ClassManages[0].studentName);
            console.log("showChartDialog1=", this.showChartDialog1);
            //this.createChart();

            this.$nextTick(() => {
               const canvas = document.getElementById('myChart');
               if (canvas) {
                  this.createChart();
               } else {
                  console.error("Canvas element not found");
               }
            });
         },
         calculatePercentages(data) {
            const counts = [0, 0, 0, 0, 0];

            data.forEach(rate => {
               if (rate < 0.6) counts[0]++;
               else if (rate < 0.7) counts[1]++;
               else if (rate < 0.8) counts[2]++;
               else if (rate < 0.9) counts[3]++;
               else counts[4]++;
            });

            const total = data.length;
            return counts.map(count => (count / total) * 100);
         },
         createChart(){
            const labels = this.ClassManages.map(student => student.studentName);
            const data = this.ClassManages.map(student => student.attendanceRate);
            const percentages = this.calculatePercentages(data);
            console.log("labels:", labels);
            console.log("data:", data);
            console.log("percentages:", percentages);
            // 获取 canvas 元素
            const canvas = document.getElementById('myChart');
            // 检查 canvas 是否存在
            if (!canvas) {
               console.error("Canvas element not found");
               return;
            }
            const ctx = canvas.getContext('2d');
            const canvas1 = document.getElementById('myChart1');
            // 检查 canvas 是否存在
            if (!canvas1) {
               console.error("Canvas element not found");
               return;
            }
            const ctx1 = canvas1.getContext('2d');
            const lineChart = new Chart(ctx, {
               type: 'line',
               data: {
                  labels: labels,
                  datasets: [{
                     label: 'Attendance Rate',
                     data: data,
                     borderColor: 'rgba(75, 192, 192, 1)',
                     borderWidth: 1,
                     fill: false,
                  }],
               },
               options: {
                  scales: {
                     x: {
                        title: {
                           display: true,
                           text: 'Student Name',
                        },
                     },
                     y: {
                        title: {
                           display: true,
                           text: 'Attendance Rate',
                        },
                        beginAtZero: true,
                     },
                  },
               },
            });
            // 创建扇形统计图
            const doughnutChart = new Chart(ctx1, {
               type: 'doughnut',
               data: {
                  labels: ['< 0.6', '0.6 - 0.7', '0.7 - 0.8', '0.8 - 0.9', '0.9 - 1'],
                  datasets: [{
                     data: percentages,
                     backgroundColor: [
                        'rgba(255, 99, 132, 0.6)',
                        'rgba(255, 159, 64, 0.6)',
                        'rgba(255, 205, 86, 0.6)',
                        'rgba(75, 192, 192, 0.6)',
                        'rgba(54, 162, 235, 0.6)',
                     ],
                  }],
               },
               options: {
                  aspectRatio: 1,
               },
            });
            lineChart.update();
            doughnutChart.update();


         },

         submitChange(){
            //console.log(data.form.id);
            console.log("正在修改的11id", this.form.classManageId);
            let param = new FormData;
            //后续需获取用户登录传递的用户id
            //param.append("token", this.$store.state.user.token)
            param.append("token", "1")
            param.append("classManageId",this.form.classManageId);
            param.append("studentId",this.form.studentId);
            param.append("studentName",this.form.studentName);
            param.append("attendanceRate",this.form.attendanceRate);
            param.append("dailyScore",this.form.dailyScore);
            axios.post("http://localhost:8080/Classmanage/updateclassmanage", param).then(res => {
               //this.tasks = Object.assign({},res.data.result)
               console.log("submitChange res=", res);
               console.log("submitChange res.data.result=",res.data.result)
               console.log("res.data.msg=", res.data.msg);
               if(res.data.msg == "success")
               {
                  this.formVisible=false;
                  if(this.formVisible == false)
                     this.loadClassManage();
               }
            })
         },
         submitAdd(){
            //console.log(data.form.id);
            //console.log("正在修改的11id", this.form.id);
            let param = new FormData;
            //后续需获取用户登录传递的用户id
            //param.append("token", this.$store.state.user.token)
            param.append("token", "1")
            // param.append("id",this.form.id);
            param.append("studentId",this.form.studentId);
            param.append("studentName",this.form.studentName);
            param.append("attendanceRate",this.form.attendanceRate);
            param.append("dailyScore",this.form.dailyScore);
            param.append("userId",1);
            console.log("param=", param);
            axios.post("http://localhost:8080/Classmanage/addclassmanage", param).then(res => {
               //this.tasks = Object.assign({},res.data.result)
               console.log("submitAdd res=", res);
               console.log("submitAdd res.data.result=",res.data.result)
               if(res.data.result == "添加学生成功"){
                  this.formaddVisible=false;
                  if(this.formaddVisible == false)
                     this.loadClassManage();
               }
            })
         },
         deleteStudent(classManageId) {
            this.$confirm('是否确认删除该学生?', '提示', {
               confirmButtonText: '确认',
               cancelButtonText: '取消',
               type: 'warning'
            }).then(() => {
               let param = new FormData;
               //********************************************************************************
               //param.append("token", this.$store.state.token);
               console.log("classManageId=", classManageId);
               param.append("token", "1=1");
               param.append("classManageId", classManageId);
               // eslint-disable-next-line no-unused-vars
               axios.post("http://localhost:8080/Classmanage/deleteStudent", param).then(res => {
                  this.loadClassManage();//删除了再加载
                  this.$message({
                     type: 'success',
                     message: '删除成功'
                  });
               });
            }).catch(() => {
               this.$message({
                  type: 'success',
                  message: '已取消删除'
               });
            });
         },
         AddStudent(){
            console.log("214414");
            this.form={};
            this.formaddVisible=true;
         },
         printBox() {
            setTimeout(function () {
               print({
                  printable: 'printBox',
                  type: 'html',
                  scanStyles: false,
                  targetStyles: ['*']
               })
            }, 500)
         },
         exportPDF() {
            const ele = document.getElementById('printBox');

            html2canvas(ele, {
               dpi: 96,
               scale: 2,
               useCORS: true,
               bgcolor: '#ffffff',
               logging: false,
            }).then((canvas) => {
               const contentWidth = canvas.width;
               const contentHeight = canvas.height;
               const pageHeight = (contentWidth / 592.28) * 841.89;
               let leftHeight = contentHeight;
               let position = 0;
               const imgWidth = 595.28;
               const imgHeight = (595.28 / contentWidth) * contentHeight;
               const ctx = canvas.getContext('2d');

               ctx.textAlign = 'center';
               ctx.textBaseline = 'middle';
               ctx.rotate((25 * Math.PI) / 180);
               ctx.font = '20px Microsoft Yahei';
               ctx.fillStyle = 'rgba(184, 184, 184, 0.8)';

               for (let i = contentWidth * -1; i < contentWidth; i += 240) {
                  for (let j = contentHeight * -1; j < contentHeight; j += 100) {
                     ctx.fillText('YKXM04', i, j);
                  }
               }

               const pageData = canvas.toDataURL('image/jpeg', 1.0);
               const pdf = new jsPDF('', 'pt', 'a4');

               if (leftHeight < pageHeight) {
                  pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
               } else {
                  while (leftHeight > 0) {
                     pdf.addImage(pageData, 'JPEG', 0, position, imgWidth, imgHeight);
                     leftHeight -= pageHeight;
                     position -= 841.89;
                     if (leftHeight > 0) {
                        pdf.addPage();
                     }
                  }
               }

               pdf.save(`文件名.pdf`);
            });
         },
      },
   }

   /*
   const load=() =>{
       request.get("/course/selectpage", {
           params:
               {
                   pageNum: data.pageNum,
                   pageSize: data.pageSize,
                   courseName:data.courseName,
                   dep:data.dep
               }
       }).then(res =>{
           //console.log(res)
           data.tableData=res.data.list
           data.total=res.data.total || 0
       })
   }
    */


   const  handleCurrentChange =()=>
   {
      //翻页时重新加载
      // load();
   }
   const reset =()=>
   {
      //data.courseName="";
      //data.dep="",
      //  load();
   }
   const handleAdd = () =>
   {

      //data.form={};
      //data.formVisible=true;
   }
   const save = () =>
   {
      //console.log(data.form.id);
      /*
      request.request({
          url: data.form.id ? '/course/update' :'/course/add',
          method: data.form.id? 'PUT' :'POST',
          data: data.form
      }).then(res=>{
          if(res.code=="200")
          {
              ElMessage.success("操作成功");
              data.formVisible=false;
              load();
          }
          else
          {
              ElMessage.error(res.msg);
          }
      })

       */


   }

   const del = (id) => {
      /*
      ElMessageBox.confirm("删除后无法恢复，您确认删除吗",'删除确认',{ type: 'warning'}).then(res=>{
              request.delete("/course/delete/"+id).then(res=>
              {
                  if(res.code=="200")
                  {
                      ElMessage.success("操作成功");
                      load();
                  }
                  else
                  {
                      ElMessage.error(res.msg);
                  }

              })
          }
      )

       */

   }

   onMounted(async () => {
      setTimeout(() => {aa()}, 1000)
   })


   const aa =()=> {

      var option;
      option = {
         xAxis: {
            type: 'category',
            data: ["计算机学院","软件学院","数学学院","文学与新闻学院"]
         },
         yAxis: {
            type: 'value'
         },
         series: [
            {
               data: [],
               type: 'line'
            },
            {
               data: [],
               type: 'bar'
            },

         ]
      };
      var chartDom = document.getElementById('main');
      var myChart = echarts.init(chartDom);
      /*
      request.get("/course/map").then(res =>{
          if(res.data) {
              //option.xAxis.data = res.data.x;
              option.series[0].data = res.data;
              option.series[1].data = res.data;
              myChart.setOption(option);
          }
      })

       */

   }
   const printAll = (index, row) => {
      // 正常跳转
      // router.push({ name: 'record', params: { id: row.id } })
      // 为了打开新窗口用下面方式
      // const url = router.resolve({
      //     name: 'record',
      //     params: { id: row.id }
      // })
      window.open("Dashborad")
   }
</script>
