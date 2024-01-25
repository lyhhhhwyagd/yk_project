/* eslint-disable vue/no-unused-vars */
<template>
  <div style="width: 100%">
    <el-button size="small" type="primary" @click="printBox" style="background-color: rgb(128, 214, 22);">打印</el-button>
    <el-button @click="exportPDF" size="small" type="primary" style="background-color: rgb(128, 214, 22);">导出PDF</el-button>
    <el-button @click="loadAllTasksByOrder" size="small" type="primary" style="background-color: rgb(128, 214, 22);">排序</el-button>
    <el-button @click="showChartDialog" size="small" type="primary" style="background-color: rgb(128, 214, 22);">
      统计
    </el-button>
    <el-button @click="tasksSta" size="small" type="primary" style="background-color: rgb(128, 214, 22);">
      分析
    </el-button>
    <el-date-picker v-model="taskDdl" type="date" placeholder="选择截止日期"></el-date-picker>
    <el-button @click="selectTaskByDdl" size="small" type="primary" style="background-color: rgb(128, 214, 22);">查找</el-button>
    <el-button @click="loadAllTasks" size="small" type="primary" style="background-color: rgb(128, 214, 22);">重置</el-button>
    <el-button @click="JSC" size="small" type="primary" style="background-color: rgb(128, 214, 22);">驾驶舱</el-button>

    <el-row :gutter="10" style="width: 100%">
      <el-col :xs="{span:20,offset:2}" :sm="{span:20,offset:2}" :md="{span:20,offset:2}" style="width: 100%">
        <div class="tasks-container" :key="refresh" id="printBox">
          <el-card class="box-card" v-for="(item,id) in tasks" :key="'taskId-'+item.taskId" ref="boxCards" style="margin-bottom: 10px; border-radius: 8px;">
            <div class="task-item">
              <div class="finishButton-box">
                <el-button size="small" class="finishButton" circle @mouseover="hoverButton(id)"
                           @mouseleave="leaveButton(id)" @click="changeTaskStatus(item.taskId, item.taskType)">
                  <i class="el-icon-check" ref="icons" style="color: white"></i>
                </el-button>
              </div>
              <!-- 使用el-row和el-col分为两列展示内容 -->
              <el-row>
                <el-col :span="18">
                  <div class="task-content" :style="{ display: !showTaskContent ? 'block' : 'none' }" ref="taskContent">
                    {{ item.taskContent }}
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="task-ddl" ref="taskDdl">
                    截止日期: {{ item.taskDdl }}
                  </div>
                </el-col>
              </el-row>
              <div ref="taskInput" :style="{ display: item.editMode ? 'block' : 'none' }" class="task-content">
                <el-input suffix-icon="el-icon-edit" ref="task_input" type="textarea"  maxlength="120"
                          show-word-limit autosize v-model="taskText" @blur="submitChange(item.taskId, id)" @keydown.enter="submitChange(item.taskId, id)"  ></el-input>
                <!--            <el-button @click="submitChange(item.taskId, id)" class="changeSubmitButton" type="primary" icon="el-icon-check" circle></el-button>-->
              </div>

              <div class="moreButton-box">
                <el-dropdown class="moreButton">
                  <el-icon ><MoreFilled /></el-icon>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item>
                        创建<i class="el-icon-date" style="color: #409EFF"></i>{{ formatDateTime(item.taskCreateTime)}}
                      </el-dropdown-item>
                      <el-dropdown-item @click="changeTask(item.taskId, id)" divided>
                        修改
                      </el-dropdown-item>
                      <el-dropdown-item @click="deleteTask(item.taskId)" divided>
                        删除
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>

                </el-dropdown>
              </div>
              <!--<el-link icon="el-icon-more" class="moreButton"  :underline="false"></el-link>-->
            </div>
          </el-card>
          <div class="add-task-box" v-if="!showAddTask">
            <el-input v-model="new_task" type="textarea"
                      maxlength="120"
                      show-word-limit
                      autosize
                      placeholder="请输入内容"></el-input>
            <!-- el-date-picker 组件用于选择截止日期 -->
            <el-date-picker
                    v-model="taskDdl"
                    type="date"
                    placeholder="选择截止日期">
            </el-date-picker>
            <el-button @click="addTask" size="small" style="margin-top: 5px"><i class="el-icon-s-flag"></i> 添加</el-button>
            <el-button size="small" style="margin-top: 5px" @click="addTaskButtonEvt"><i class="el-icon-close"
                                                                                         style="color: red"></i> 取消
            </el-button>
          </div>
          <div class="button-box">
            <el-button class="finishedButton" v-if="showAddTask" size="large" @click="loadFinishedTasks"><i
                    class="el-icon-arrow-right" ref="finishedButtonIcon"></i> 已完成
              <span style="color:#ACB0AE">
                    {{ finishedTasksCount }}
                </span>
            </el-button>
            <el-button class="finishedButton" v-if="showAddTask" size="large" @click="addTaskButtonEvt"><i
                    class="el-icon-s-flag"></i> 添加任务
            </el-button>
          </div>

          <el-card class="box-card" v-show="showFinished" v-for="(item) in finishedTasks" :key="item.taskId+'-only'" style="margin-bottom: 10px; border-radius: 8px;">
            <div class="task-item">
              <div class="finishButton-box">
                <el-button size="small" class="finishButton" circle @click="changeTaskStatus(item.taskId, item.taskType)">
                  <i class="el-icon-check" style="color: green"></i>
                </el-button>
              </div>
              <!-- 使用el-row和el-col分为两列展示内容 -->
              <el-row>
                <el-col :span="18">
                  <div class="task-content" :style="{ display: !showTaskContent ? 'block' : 'none' }" ref="taskContent">
                    {{ item.taskContent }}
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="task-createtime" ref="taskCreateTime">
                    创建时间: {{ formatDateTime(item.taskCreateTime) }}
                  </div>
                </el-col>
              </el-row>
              <div class="moreButton-box">
                <el-dropdown class="moreButton">
                  <el-icon ><MoreFilled /></el-icon>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item>创建 {{ formatDateTime(item.taskCreateTime) }}
                      </el-dropdown-item>
                      <el-dropdown-item>完成 {{ formatDateTime(item.taskFinishTime) }}
                      </el-dropdown-item>
                      <el-dropdown-item @click="changeTaskStatus(item.taskId, item.taskType)" divided>
                        还原为未完成 <i class="el-icon-refresh" style="color: chartreuse"></i>
                      </el-dropdown-item>
                      <el-dropdown-item @click="deleteTask(item.taskId)" divided>
                        删除
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>

              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <el-backtop></el-backtop>
    <el-dialog v-model="showChartDialog1" title="任务统计图">
      <canvas id="myChart"  width="400" height="400"></canvas>
    </el-dialog>
  </div>

</template>

<script>
  import axios from 'axios'
  import print from 'print-js'
  import html2canvas from 'html2canvas';
  import jsPDF from 'jspdf';
  import { MoreFilled, Delete, Download } from "@element-plus/icons-vue";
  //import Chart from 'chart.js/auto';
  // eslint-disable-next-line no-unused-vars
  import { ref } from 'vue';
  import { Chart, registerables } from 'chart.js';
  import 'chartjs-adapter-moment';
  import moment from 'moment';




  export default {
    name: "Tasks",
    components: {
      MoreFilled,
    },
    data() {
      this.taskInput = ref(null);
      this.taskContent = ref(null);
      this.task_input = ref(null);
      return {
        tasksCount: 0,
        refresh: -1,
        finishedTasksCount: 0,
        showFinished: false,
        showTaskInput: false,
        showTaskContent:false,
        showChart:false,
        showChartDialog1: false,
        chart: null,
        tasks: {},
        finishedTasks: {},
        boxCardsWidth: 0,
        new_task: "",
        UserID:0,
        taskDdl: null, // 截止日期
        taskText:"nihao",
        showAddTask: true,
        expressions: [
          '🎉', '🍜', '✨', '😀', '🍕'
        ]
      }
    },
    mounted() {
      window.addEventListener('scroll', this.handleScroll, true);
      //this.UserID = this.$route.query.userID;
      //console.log("Tasks UserID=", this.UserID);

    },
    created() {
      this.$store.commit("SET_TOKEN", localStorage.getItem("userToken"));
      this.$store.commit("SET_FILTER", localStorage.getItem("filterPattern"));
      //this.$store.commit("LOGIN");
      this.UserID = this.$route.query.userID;
      console.log("Tasks UserID=", this.UserID);
      this.loadAllTasks();
    },
    /*
    updated() {
      console.log("updated this.$refs.boxCards=", this.$refs.boxCards);
      if (this.$refs.boxCards !== undefined) {
        this.boxCardsWidth = this.$refs.boxCards['0'].$el.clientWidth;
      }
    },

     */
    updated() {
      this.$nextTick(() => {
        if (this.$refs.boxCards !== undefined && this.$refs.boxCards[0] !== undefined) {
          this.boxCardsWidth = this.$refs.boxCards[0].$el.clientWidth;
        }
      });
    },

    methods: {
      hoverButton(index) {
        this.$refs.icons[index].style.color = "green";
      },
      leaveButton(index) {
        this.$refs.icons[index].style.color = "white";
      },
      formatDateTime(value) {
        // 创建 Date 对象
        var originalDate = new Date(value);
        var year = originalDate.getFullYear();
        var month = ('0' + (originalDate.getMonth() + 1)).slice(-2);
        var day = ('0' + originalDate.getDate()).slice(-2);
        var hours = ('0' + originalDate.getHours()).slice(-2);
        var minutes = ('0' + originalDate.getMinutes()).slice(-2);
        var seconds = ('0' + originalDate.getSeconds()).slice(-2);

        // 拼接为指定格式
        var formattedDateTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}.0`;
        //console.log("规格化时间：",formattedDateTime);
        return formattedDateTime;
      },
      processDataForChart() {
        // 对任务数据进行处理，计算每天的任务数量
        const taskCounts = {};

        this.tasks.forEach(task => {
          const date = task.taskDdl.split(' ')[0]; // 提取日期部分
          taskCounts[date] = (taskCounts[date] || 0) + 1;
        });
        return {
          labels: Object.keys(taskCounts),
          data: Object.values(taskCounts),
        };
      },
      tasksSta(){
        this.$router.push({
          name: '待办事项数据分析',
          query: { tasks: JSON.stringify(this.tasks) } });
      },
      createChart(){
        this.showChart = true;
        const chartdata = this.tasks;
        const { labels, data } = this.processDataForChart();
        console.log("labels:", labels);
        console.log("data:", data);
        // 获取 canvas 元素
        const canvas = document.getElementById('myChart');
        // 检查 canvas 是否存在
        if (!canvas) {
          console.error("Canvas element not found");
          return;
        }
        const ctx = canvas.getContext('2d');
        this.chart = new Chart(ctx, {
          type: 'bar',
          data: {
            labels: labels,
            datasets: [{
              label: '每天的任务数量',
              data: data,
              backgroundColor: 'rgba(75, 192, 192, 0.2)',
              borderColor: 'rgba(75, 192, 192, 1)',
              borderWidth: 1,
            }],
          },
          options: {
            scales: {
              x: {
                type: 'time',
                time: {
                  unit: 'day',
                  displayFormats:{
                    day : 'YYYY-MM-DD HH:mm:ss'
                  }
                },
                title: {
                  display: true,
                  text: '日期',
                },
              },
              y: {
                beginAtZero: true,
                title: {
                  display: true,
                  text: '任务数量',
                },
              },
            },
          },
        });

      },
      showChartDialog() {
        this.showChartDialog1 = true;
        console.log("showChartDialog1=", this.showChartDialog1);
        //this.createChart();

        // 使用 $nextTick 确保图表在弹窗打开后才创建

        this.$nextTick(() => {
          const canvas = document.getElementById('myChart');
          if (canvas) {
            this.createChart();
          } else {
            console.error("Canvas element not found");
          }
        });
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
      changeTaskStatus(taskId, taskType) { //status=0，代表对已完成任务操作，1为对未完成任务操作
        console.log('item:', this.item);
        let msg = '是否确认完成该任务?';
        if (taskType === 1) { // 让用户再确认一下
          msg = '是否确认还原为未完成?';
        }
        this.$confirm(msg, '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'success'
        }).then(() => {
          let param = new FormData;
          param.append("taskId", taskId);
          param.append("taskType", taskType);
          //param.append("token", this.$store.state.user.token);
          param.append("token", "1=1");
          // eslint-disable-next-line no-unused-vars
          axios.post("http://localhost:8080/zc/api/change", param).then(res => {
            this.loadAllTasks();
            let msg = '完成任务  ' + this.expressions[Math.floor((Math.random() * 5))] + '  累计完成任务 ' + (this.finishedTasksCount + 1);
            if (taskType === 1) {
              msg = "还原成功";
            }
            this.$message({
              type: 'success',
              message: msg
            });
          })
        }).catch(() => {

        });
      },
      addTask() {
        let param = new FormData;
        //*********************************************************
        //param.append("token", this.$store.state.token);
        param.append("token", "1=1");
        param.append("taskContent", this.new_task);
        param.append("UserID",this.UserID)
        // 格式化日期
        const formattedDate = this.formatDate(this.taskDdl);
        param.append("taskDdl", formattedDate);
        console.log(this.new_task)
        console.log(this.new_task.trim())
        console.log("taskDdl:", this.taskDdl);
        console.log("formattedDate:",formattedDate);
        // eslint-disable-next-line no-unused-vars
        axios.post("http://localhost:8080/zc/api/insert", param).then(res => {
          console.log("addTask res=", res);
          if(res.data.msg == "success"){
            this.loadUnfinishedTasks();
            this.$message({
              type: 'success',
              message: '添加成功'
            });
            this.showAddTask = true
            this.new_task = ""}
        })
      },
      selectTaskByDdl(){
        let param = new FormData;
        param.append("token", "1=1");
        param.append("UserID",this.UserID)
        //param.append("taskContent", this.new_task);
        // 格式化日期
        console.log("taskDdl=",this.taskDdl)
        const formattedDate = this.formatDate(this.taskDdl);
        console.log("selectTaskByDdl formattedDate=",formattedDate);
        param.append("taskDdl", formattedDate);
        axios.post("http://localhost:8080/zc/api/selectDdl", param).then(res => {
          console.log("selectTaskByDdl res=", res);
          if(res.data.msg == "success"){
            this.tasks = res.data.result;
            /*
            this.$message({
              type: 'success',
              message: '添加成功'
            });
            this.showAddTask = true
            this.new_task = ""*/
          }
        })

      },
      formatDate(date) {
        console.log("formatDate date=", date);

        // 使用 toLocaleDateString 获取本地日期字符串
        const localDateString = date.toLocaleDateString();

        console.log("localDateString=", localDateString);

        return localDateString;
      },
      JSC(){
        this.$router.push({
          name: 'TaskJSC',
          query: { tasks: JSON.stringify(this.tasks) } });
      },


      loadAllTasks() {
        //let pattern = this.$store.state.filterPattern;
        let param = new FormData;
        //后续需获取用户登录传递的用户id
        //param.append("token", this.$store.state.user.token)
        param.append("token", "1")
        param.append("UserID",this.UserID)
        console.log("loadAllTasks UserID",this.UserID);
        axios.post("http://localhost:8080/zc/api/tasks", param).then(res => {
          //this.tasks = Object.assign({},res.data.result)
          console.log("loadAllTasks res.data.result=",res.data.result);
          //this.tasks = res.data.result;
          this.tasks = res.data.result.map(task => {
            return { ...task, editMode: false };
          });
          console.log("loadAllTasks tasks=", this.tasks);
          this.refresh = Math.random(); //强制触发vue的diff重新渲染
        })
        axios.post("http://localhost:8080/zc/api/finished", param).then(res => {
          this.finishedTasks = res.data.result
          this.finishedTasksCount = this.finishedTasks.length
        })
      },
      loadAllTasksByOrder() {
        // eslint-disable-next-line no-unused-vars
        let pattern = this.$store.state.filterPattern;
        let param = new FormData;
        //后续需获取用户登录传递的用户id
        //param.append("token", this.$store.state.user.token)
        param.append("token", "1")
        param.append("UserID",this.UserID)
        axios.post("http://localhost:8080/zc/api/ordertasks", param).then(res => {
          //this.tasks = Object.assign({},res.data.result)
          console.log("loadAllTasksByOrder res.data.result",res.data.result);
          this.tasks = res.data.result
          this.refresh = Math.random(); //强制触发vue的diff重新渲染
        })
        axios.post("http://localhost:8080/zc/api/orderfinished", param).then(res => {
          this.finishedTasks = res.data.result
          this.finishedTasksCount = this.finishedTasks.length
        })
      },
      loadUnfinishedTasks() {
        let param = new FormData;
        //后续需获取用户登录传递的用户id
        //param.append("token", this.$store.state.user.token)
        param.append("token", "1")
        param.append("UserID",this.UserID)
        axios.post("http://localhost:8080/zc/api/tasks", param).then(res => {
          this.tasks = res.data.result
        })
      },
      changeTask(taskId,id){
        this.taskText = this.tasks[id].taskContent;
        console.log("changeTask id", id)
        console.log("changeTask this.tasks[id].taskContent",this.tasks[id].taskContent)
        console.log("taskId", taskId);
        console.log("id", id);
        console.log("this.$refs.taskInput", this.$refs.taskInput);
        console.log("this.$refs.taskContent", this.$refs.taskContent);
        console.log("this.$refs.task_input", this.$refs.task_input);
        console.log("第一个元素：", this.$refs.taskInput[0]);
        const targetTask = this.tasks.find(task => task.taskId === taskId);
        if (targetTask) {
          targetTask.editMode = !targetTask.editMode;
        }
        //this.$refs.taskInput[id].style.display = "block";
        //this.$refs.taskContent[id].style.display = "none";
        //this.showTaskInput = true;
        //this.showTaskContent = true;
        console.log("this.$refs.taskInput",this.$refs.taskInput);
        console.log("this.$refs.taskContent",this.$refs.taskContent);
        this.$refs.task_input[id].focus();
        this.taskText = this.$refs.taskContent[id].innerText;
        console.log("this.taskText = this.$refs.taskContent[id].innerText=", this.taskText = this.$refs.taskContent[id].innerText);
        console.log(taskId);
        console.log(id);
        console.log(this.$refs.taskInput[id]);
        console.log(this.$refs.taskContent[id]);
        console.log(this.$refs.task_input[id]);
      },
      submitChange(taskId,id){
        let param = new FormData;
        //**************************************************************************
        // param.append("token", this.$store.state.token);
        param.append("token", "1=1");
        param.append("taskId", taskId);
        param.append("taskContent", this.taskText);
        param.append("UserID",this.UserID)
        // console.log(param);
        axios.post("http://localhost:8080/zc/api/update", param).then(res=>{
          console.log(res);
          if(res.data.msg==="success") {
            const targetTask = this.tasks.find(task => task.taskId === taskId);
            if (targetTask) {
              targetTask.editMode = !targetTask.editMode;
            }
            const taskType = targetTask.taskType;
            if(taskType == 0){
              this.loadAllTasks();
            }else if(taskType == 1){
              this.loadFinishedTasks();
            }
            //this.$refs.taskInput[id].style.display = "none";
            //this.$refs.taskContent[id].style.display = "block";
            this.$refs.taskContent[id].innerText = this.taskText;
          } else {
            console.log(res.data.msg);
          }
        }).catch(err=>{
          console.log(err);})
      },
      deleteTask(taskId) {
        this.$confirm('是否确认删除该任务?', '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let param = new FormData;
          //********************************************************************************
          //param.append("token", this.$store.state.token);
          param.append("token", "1=1");
          param.append("taskId", taskId);
          param.append("UserID",this.UserID)
          // eslint-disable-next-line no-unused-vars
          axios.post("http://localhost:8080/zc/api/delete", param).then(res => {
            this.loadAllTasks();//删除了再加载
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
      handleScroll() {
        let scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
        if (this.$route.path !== '/tasks' || this.boxCardsWidth === 0 || this.$refs.boxCards === undefined || this.$refs.boxCards.length === 0) return;
        let length = this.tasks.length
        if (scrollTop <= 10) {
          for (let i = 0; i < length; i++) {
            this.$refs.boxCards[i].$el.style.width = this.boxCardsWidth + "px"
            this.$refs.boxCards[i].$el.style.opacity = "100%"
          }
          return
        }
        let sum = 0
        let index = 1;
        let opacity = 0;
        for (let i = 0; i < length; i++) {
          let tempHeight = this.$refs.boxCards[i].$el.offsetHeight
          sum += tempHeight
          if (scrollTop <= sum && scrollTop >= sum - tempHeight) {
            opacity = (sum - scrollTop) + 10 > 100 ? 100 : (sum - scrollTop) + 10
            index = i
            break
          }
        }
        /* console.log("opacity:"+opacity)
         console.log("index:"+index)*/
        let currentWidth = this.$refs.boxCards[index].$el.clientWidth;
        /*console.log(currentWidth + " <--> "+ this.boxCardsWidth)*/
        this.$refs.boxCards[index].$el.style.opacity = opacity + "%"
        if (currentWidth === this.boxCardsWidth) {
          let out = currentWidth - 15
          this.$refs.boxCards[index].$el.style.width = out + "px"
          if (index >= 1) { //宽度调正回来
            for (let i = 0; i < index; i++) {
              this.$refs.boxCards[i].$el.style.width = this.boxCardsWidth + "px"
              this.$refs.boxCards[i].$el.style.opacity = "100%"
            }
          }
          for (let i = index + 1; i < length; i++) {
            this.$refs.boxCards[i].$el.style.width = this.boxCardsWidth + "px"
            this.$refs.boxCards[i].$el.style.opacity = "100%"
          }
        }

        /*console.log(this.$refs.boxCards['0'].$el.offsetHeight)
        console.log(this.$refs.boxCards['1'].$el.offsetHeight)
        console.log(this.$refs.boxCards['2'].$el.offsetHeight)*/
      },
      loadFinishedTasks(evt) { // 改变已完成那个button的箭头方向
        /*console.log(this.$refs.finishedButtonIcon.removeAttribute('class'))
        this.$refs.finishedButtonIcon.class = "el-icon-arrow-down";*/
        if (this.$refs.finishedButtonIcon.className === "el-icon-arrow-right") { //显示已完成的任务
          this.$refs.finishedButtonIcon.setAttribute('class', "el-icon-arrow-down")
          this.showFinished = true
        } else { //不显示已完成的任务
          this.$refs.finishedButtonIcon.setAttribute('class', "el-icon-arrow-right")
          this.showFinished = false
        }

        let target = evt.target;
        if (target.nodeName == "SPAN") {
          target = evt.target.parentNode;
        }
        target.blur();
      },
      addTaskButtonEvt() {
        this.showAddTask = !this.showAddTask;
      }
    }
  }
</script>

<style>
  .myClass input.el-input__inner {
    border-radius:15px;
  }
</style>
<style scoped>
  .tasks-container {
    /*max-width: 980px;*/
    width: 100%;
    margin: 0 auto;
    /*border: #409EFF 2px solid;*/
    padding-top: 20px;
    padding-bottom: 30px;
  }

  .box-card {
    margin: 0 auto;
    /*max-width: 780px;*/
    padding: 0 0;
    min-height: 100px;
  }

  .add-task-box {
    margin: 0 auto;
    /*max-width: 780px;*/
    width: 100%;
    padding: 0 0;
  }
  .button-box {
    margin: 0 auto;
    padding: 0 0;
  }

  .el-icon-check {
    font-weight: bolder;
  }

  .task-item {
    /*border: yellow 2px solid;*/
    /*font-size: 14px;
    overflow: hidden;*/
    display: grid;
    grid-template: auto 1fr auto / auto 1fr auto
  }

  .task-input>>>.el-input__inner{
    border-radius: 30px;    /*输入框圆角值*/
  }

  .changeSubmitButton{

  }

  .finishButton-box {
    /* border: red solid 2px;*/
    grid-column: 1 / 2;
  }

  .finishButton {
    /*float: left;*/
    /*margin-right: 20px;*/
  }

  .task-content { /*不应该为float*/
    /*float: left;*/
    /*max-width: 600px;*/
    /*width: 60%;*/
    margin-top: 3px;
    /*border: black 2px solid;*/
    grid-column: 2 / 3;
    padding-left: 5px;
    padding-right: 5px;
    word-break:break-all;
  }
  .task-input {
    float: left;
    width: 85%;
  }

  .moreButton-box {
    /*float: right;*/
    /*  margin-right: 10px;*/
    grid-column: 3 / 4;
    /*border: burlywood solid 2px;*/
  }

  .finishedButton {
    margin-top: 20px;
    margin-bottom: 10px;
  }
</style>
