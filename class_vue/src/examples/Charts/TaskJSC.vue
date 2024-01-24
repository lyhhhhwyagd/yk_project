<template>
    <div class="background-container" :style="{ backgroundImage: 'url(' +
        require('@/assets/img/task_bg.jpg') +
        ')',backgroundSize: 'cover'}">
        <div>
        <div class="canvas-container">
            <canvas id="lineChart" class="custom-canvas" style="margin-top: 50px;background-color: rgba(255, 255, 255, 0.1); max-height: 300px"></canvas>
            <canvas id="myChart" class="custom-canvas" style="margin-top: 50px;background-color: rgba(255, 255, 255, 0.1);max-height: 300px"></canvas>
        </div>
        <div class = "container1">
            <div class="canvas-container3">
        <div class="canvas-container2">
            <canvas id="lineChart1" class="custom-canvas" style="background-color: rgba(255, 255, 255, 0.1);max-height: 250px"></canvas>
        </div>
        <div class="canvas-container1">
            <canvas id="doughnutChart" class="custom-canvas" style="background-color: rgba(255, 255, 255, 0.1);"></canvas>
            <canvas id="doughnutChart1" class="custom-canvas" style="background-color: rgba(255, 255, 255, 0.1);"></canvas>
        </div>
            </div>

        <el-main class="papers">
                <div v-for="(paper) in paperList" :key="paper.id" >
                    <el-card class="paper_card1" style="margin-bottom:15px;background-color: rgba(255, 255, 255, 0.3);">
                        <div  class="clearfix">
                            <span style="margin-left: 5px">问卷标题: {{ paper.title }}</span>
                            <label v-if="paper.status === 'INIT'" style="float: right"
                            >问卷状态：编辑中</label
                            >
                            <label v-else-if="paper.status === 'START'" style="float: right">
                                问卷状态：已发放
                            </label>
                            <label v-else-if="paper.status === 'STOP'" style="float: right">
                                问卷状态：已回收
                            </label>
                        </div>
                    </el-card>
                </div>
            </el-main>

        </div>
        </div>
    </div>
</template>
<script>
    import {Chart} from "chart.js";
    import axios from "axios";

    export default {
        name: "TaskJSC",
        data(){
          return{
              tasks: {},
              UserID:0,
              paperList:{},
              ClassManages:{}
          }
        },
         mounted(){

             const tasks = JSON.parse(this.$route.query.tasks);
             console.log(tasks);
             this.loadClassManage();
             this.getAllPapers2();

             this.createChart1();
        },

        methods:{
            getAllPapers2(){
                console.log("getAllPapers2 test");
                const userId = 2;
                console.log(`getAllPapers2 get paperList with userId: ${userId}`);
                axios.get(`http://localhost:8080/api/paper/${userId}/getUserPapers`).then(res => {
                    //this.tasks = Object.assign({},res.data.result)
                    console.log("submitAnswers res=",res);
                    //this.tasks = res.data.result;
                    if(res.data.success){
                        //console.log("submitAnswers res.data.success", res.data.success);
                        console.log("提交成功");
                        this.paperList = res.data.content;
                        this.paperList = this.paperList.slice(0, 6);
                        console.log("this.paperList", this.paperList);
                        //this.$message.success('提交成功');
                        //this.$router.push({ name: 'complete', params: { paperId: this.paperId } });
                    }else{
                        console.log("提交失败，请检查网络状态");
                        //this.$message.error('提交失败，请检查网络状态');
                    }
                })

            },
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
                    console.log("loadClassManage this.ClassManages",this.ClassManages)
                    this.createChart();
                })
            },
            calculatePercentages(data){
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
            calculatePercentagesdailyScore(data) {
                // 根据需要实现计算百分比的逻辑
                // 这里只是一个示例，具体实现取决于你的需求
                const total = data.length;
                const counts = [0, 0, 0, 0, 0]; // 分组计数器

                data.forEach(score => {
                    if (score < 60) counts[0]++;
                    else if (score < 70) counts[1]++;
                    else if (score < 80) counts[2]++;
                    else if (score < 90) counts[3]++;
                    else counts[4]++;
                });

                // 计算百分比
                return counts.map(count => (count / total) * 100);
            },
            createChart(){
                console.log("this.ClassManages",this.ClassManages)
                const labels = this.ClassManages.map(student => student.studentName);
                const data = this.ClassManages.map(student => student.attendanceRate);
                const attendanceRatepercentages = this.calculatePercentages(data);
                const dailyScoreData = this.ClassManages.map(student => student.dailyScore);
                const dailyScoreRatepercentages = this.calculatePercentagesdailyScore(dailyScoreData);
                console.log("labels:", labels);
                console.log("data:", data);
                console.log("dailyScore:", dailyScoreData)
                console.log("attendanceRatepercentages:", attendanceRatepercentages);
                console.log("dailyScoreRatepercentages:", dailyScoreRatepercentages);
                // 获取 canvas 元素
                const canvas = document.getElementById('lineChart');
                // 检查 canvas 是否存在
                if (!canvas) {
                    console.error("Canvas0 element not found");
                    return;
                }
                const ctx = canvas.getContext('2d');
                // 获取 canvas 元素
                const canvas2 = document.getElementById('lineChart1');
                // 检查 canvas 是否存在
                if (!canvas2) {
                    console.error("Canvas2 element not found");
                    return;
                }
                const ctx2 = canvas2.getContext('2d');
                const canvas1 = document.getElementById('doughnutChart');
                // 检查 canvas 是否存在
                if (!canvas1) {
                    console.error("Canvas1 element not found");
                    return;
                }
                const ctx1 = canvas1.getContext('2d');

                const canvas3 = document.getElementById('doughnutChart1');
                // 检查 canvas 是否存在
                if (!canvas3) {
                    console.error("Canvas3 element not found");
                    return;
                }
                const ctx3 = canvas3.getContext('2d');
                const lineChart = new Chart(ctx, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '出勤率',
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
                                    text: '姓名',
                                },
                            },
                            y: {
                                title: {
                                    display: true,
                                    text: '出勤率',
                                },
                                beginAtZero: true,
                            },
                        },
                        plugins: {
                            title: {
                                display: true,
                                text: '出勤率统计',
                                font: {
                                    size: 16,
                                },
                            },
                        },
                    },
                });
                const lineChart2 = new Chart(ctx2, {
                    type: 'line',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '平时分',
                            data: dailyScoreData,
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 1,
                            fill: false,
                        }],
                    },
                    options: {
                        scales: {
                            x: {
                                title: {
                                    display: true,
                                    text: '姓名',
                                },
                            },
                            y: {
                                title: {
                                    display: true,
                                    text: '平时分',
                                },
                                beginAtZero: true,
                            },
                        },
                        plugins: {
                            title: {
                                display: true,
                                text: '平时分统计',
                                font: {
                                    size: 16,
                                },
                            },
                        },
                    },
                });
                // 创建扇形统计图
                const doughnutChart = new Chart(ctx1, {
                    title:{
                        text:"出勤率统计"
                    },
                    type: 'doughnut',
                    data: {
                        labels: ['< 0.6', '0.6 - 0.7', '0.7 - 0.8', '0.8 - 0.9', '0.9 - 1'],
                        datasets: [{
                            data: attendanceRatepercentages,
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
                        plugins: {
                            title: {
                                display: true,
                                text: '出勤率统计',
                                font: {
                                    size: 16,
                                },
                            },
                        },
                    },
                });
                const doughnutChart2 = new Chart(ctx3, {
                    type: 'doughnut',
                    data: {
                        labels: ['< 60', '60 - 70', '70 - 80', '80 - 90', '90 - 100'],
                        datasets: [{
                            data: dailyScoreRatepercentages,
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
                        plugins: {
                            title: {
                                display: true,
                                text: '平时分统计',
                                font: {
                                    size: 16,
                                },
                            },
                        },
                    },
                });

                lineChart.update();
                lineChart2.update();
                doughnutChart.update();
                doughnutChart2.update();


            },
            processDataForChart() {
                // 对任务数据进行处理，计算每天的任务数量
                const tasks = JSON.parse(this.$route.query.tasks);

                console.log("tasks", tasks)
                const taskCounts = {};
                tasks.forEach(task => {
                    const date = task.taskDdl.split(' ')[0]; // 提取日期部分
                    taskCounts[date] = (taskCounts[date] || 0) + 1;
                });
                return {
                    labels: Object.keys(taskCounts),
                    data: Object.values(taskCounts),
                };
            },
            createChart1(){

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
        },
    }
</script>

<style>
    .background-container{
    background-repeat: no-repeat;
    background-size: 100% 100%;
    position: fixed;
    height: 100%;
    width: 100%;
    }
    .paper_card1{
        height: 90px;
        width: 100%;
        max-height: 70px;
    }
    .canvas-container {

        display: flex;
        margin-left: 25px;
        align-items: center; /* 垂直居中 */
        width: 50%;
        height: 60%;

    }
    .papers{
        margin-bottom: -50px;
        flex: 1;
    }
    .canvas-container2{
        width: 94%;
        margin-left: 25px;
    }
    .canvas-container1 {
        width: 40%;
        margin-left: 25px;
    }
    .container1{
        display: flex;
    }
    .canvas-container3 {
        display: flex;
        flex: 2;
        flex-direction: column; /* 将左侧容器设置为竖直排列 */
        margin-right: 20px; /* 右边距，根据实际需要调整 */
    }



    canvas {
        margin-right: 50px; /* 为了在两个 Canvas 之间添加一些间距 */
    }
</style>