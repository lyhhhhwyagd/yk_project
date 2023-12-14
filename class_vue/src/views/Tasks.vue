/* eslint-disable vue/no-unused-vars */
<template>
  <div>
    <el-button size="small" type="primary" @click="printBox" style="background-color: rgb(128, 214, 22);">打印</el-button>
    <el-button @click="exportPDF" size="small" type="primary" style="background-color: rgb(128, 214, 22);">导出PDF</el-button>
    <el-button @click="loadAllTasksByOrder" size="small" type="primary" style="background-color: rgb(128, 214, 22);">排序!!</el-button>
    <el-row :gutter="10">
      <el-col :xs="{span:20,offset:2}" :sm="{span:16,offset:4}" :md="{span:16,offset:4}">
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
                  <div class="task-content" ref="taskContent">
                    {{ item.taskContent }}
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="task-ddl" ref="taskDdl">
                    截止日期: {{ item.taskDdl }}
                  </div>
                </el-col>
              </el-row>
              <el-button link="true" @click="deleteTask(item.taskId)">
                删除 <i class="el-icon-delete" style="color: red"></i>
              </el-button>
              <div ref="taskInput" style="display: none" class="task-content">
                <el-input suffix-icon="el-icon-edit" ref="task_input" type="textarea"  maxlength="120"
                          show-word-limit autosize v-model="taskText" @blur="submitChange(item.taskId, id)" @keydown.enter="submitChange(item.taskId, id)"  ></el-input>
                <!--            <el-button @click="submitChange(item.taskId, id)" class="changeSubmitButton" type="primary" icon="el-icon-check" circle></el-button>-->
              </div>
              <div class="moreButton-box">
                <el-dropdown class="moreButton">
                          <span class="el-dropdown-link">
                            <i class="el-icon-more"></i>
                          </span>
                  <el-dropdown-menu>
                    <el-dropdown-item>创建 <i class="el-icon-date" style="color: #409EFF"></i>{{ item.taskCreateTime }}
                    </el-dropdown-item>
                    <el-dropdown-item @click="changeTask(item.taskId, id)" divided>
                      修改 <i class="el-icon-edit" style="color: dodgerblue"></i>
                    </el-dropdown-item>
                    <el-dropdown-item @click="deleteTask(item.taskId)" divided>
                      删除 <i class="el-icon-delete" style="color: red"></i>
                    </el-dropdown-item>
                  </el-dropdown-menu>
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
            <el-button class="finishedButton" v-if="showAddTask" size="small" @click="loadFinishedTasks"><i
                    class="el-icon-arrow-right" ref="finishedButtonIcon"></i> 已完成
              <span style="color:#ACB0AE">
                    {{ finishedTasksCount }}
                </span>
            </el-button>
            <el-button class="finishedButton" v-if="showAddTask" size="small" @click="addTaskButtonEvt"><i
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
                  <div class="task-content" ref="taskContent">
                    {{ item.taskContent }}
                  </div>
                </el-col>
                <el-col :span="6">
                  <div class="task-createtime" ref="taskCreateTime">
                    创建时间: {{ item.taskCreateTime }}
                  </div>
                </el-col>
              </el-row>
              <el-button link="true" @click="deleteTask(item.taskId)">
                删除 <i class="el-icon-delete" style="color: red"></i>
              </el-button>
              <div class="moreButton-box">
                <el-dropdown class="moreButton">
                          <span class="el-dropdown-link">
                            <i class="el-icon-more"></i>
                          </span>
                  <el-dropdown-menu>
                    <el-dropdown-item>创建 <i class="el-icon-date" style="color: #409EFF"></i>{{ item.taskCreateTime }}
                    </el-dropdown-item>
                    <el-dropdown-item>完成 <i class="el-icon-date" style="color: green"></i>{{ item.taskFinishTime }}
                    </el-dropdown-item>
                    <el-dropdown-item @click="changeTaskStatus(item.taskId, item.taskType)" divided>
                      还原为未完成 <i class="el-icon-refresh" style="color: chartreuse"></i>
                    </el-dropdown-item>
                    <el-dropdown-item @click="deleteTask(item.taskId)" divided>
                      删除 <i class="el-icon-delete" style="color: red"></i>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <el-backtop></el-backtop>
  </div>
</template>

<script>
  import axios from 'axios'
  import print from 'print-js'
  import html2canvas from 'html2canvas';
  import jsPDF from 'jspdf';
  // eslint-disable-next-line no-unused-vars
  import { ref } from 'vue';

  export default {
    name: "Tasks",

    data() {
      this.taskInput = ref(null);
      this.taskContent = ref(null);
      this.task_input = ref(null);
      return {
        tasksCount: 0,
        refresh: -1,
        finishedTasksCount: 0,
        showFinished: false,
        tasks: {},
        finishedTasks: {},
        boxCardsWidth: 0,
        new_task: "",
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

    },
    created() {
      this.$store.commit("SET_TOKEN", localStorage.getItem("userToken"));
      this.$store.commit("SET_FILTER", localStorage.getItem("filterPattern"));
      //this.$store.commit("LOGIN");
      this.loadAllTasks();
    },
    updated() {
      if (this.$refs.boxCards !== undefined) {
        this.boxCardsWidth = this.$refs.boxCards['0'].$el.clientWidth;
      }
    },
    methods: {
      hoverButton(index) {
        this.$refs.icons[index].style.color = "green";
      },
      leaveButton(index) {
        this.$refs.icons[index].style.color = "white";
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
        // 格式化日期
        const formattedDate = this.formatDate(this.taskDdl);
        param.append("taskDdl", formattedDate);
        console.log(this.new_task)
        console.log(this.new_task.trim())
        console.log("taskDdl:", this.taskDdl);
        console.log("formattedDate:",formattedDate);
        // eslint-disable-next-line no-unused-vars
        axios.post("http://localhost:8080/zc/api/insert", param).then(res => {
          this.loadUnfinishedTasks();
          this.$message({
            type: 'success',
            message: '添加成功'
          });
          this.showAddTask = true
          this.new_task = ""
        })
      },
      formatDate(date) {
        const formattedDate = new Date(date).toISOString().slice(0, 19).replace("T", " ");
        return formattedDate;
      },
      loadAllTasks() {
        //let pattern = this.$store.state.filterPattern;
        let param = new FormData;
        //后续需获取用户登录传递的用户id
        //param.append("token", this.$store.state.user.token)
        param.append("token", "1")
        axios.post("http://localhost:8080/zc/api/tasks", param).then(res => {
          //this.tasks = Object.assign({},res.data.result)
          this.tasks = res.data.result
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
        axios.post("http://localhost:8080/zc/api/ordertasks", param).then(res => {
          //this.tasks = Object.assign({},res.data.result)
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
        axios.post("http://localhost:8080/zc/api/tasks", param).then(res => {
          this.tasks = res.data.result
        })
      },
      changeTask(taskId,id){
        console.log("taskId", taskId);
        console.log("id", id);
        console.log("this.$refs.taskInput", this.$refs.taskInput);
        console.log("this.$refs.taskContent", this.$refs.taskContent);
        console.log("this.$refs.task_input", this.$refs.task_input);
        console.log("第一个元素：", this.$refs.taskInput[0]);
        this.$refs.taskInput[id].style.display = "block";
        this.$refs.taskContent[id].style.display = "none";
        this.$refs.task_input[id].focus();
        this.taskText = this.$refs.taskContent[id].innerText;


      },
      submitChange(taskId,id){
        let param = new FormData;
        //**************************************************************************
        // param.append("token", this.$store.state.token);
        param.append("token", "1=1");
        param.append("taskId", taskId);
        param.append("taskContent", this.taskText);
        // console.log(param);
        axios.post("http://localhost:8080/zc/api/update", param).then(res=>{
          console.log(res);
          if(res.data.msg==="success") {
            this.$refs.taskInput[id].style.display = "none";
            this.$refs.taskContent[id].style.display = "block";
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
    max-width: 980px;
    margin: 0 auto;
    /*border: #409EFF 2px solid;*/
    padding-top: 20px;
    padding-bottom: 30px;
  }

  .box-card {
    margin: 0 auto;
    max-width: 780px;
    padding: 0 0;
  }

  .add-task-box {
    margin: 0 auto;
    max-width: 780px;
    padding: 0 0;
  }
  .button-box {
    margin: 0 auto;
    max-width: 780px;
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
