<template>
    <p>hahah</p>
    <div class="canvas-container">
        <canvas id="myChart" class="custom-canvas" ></canvas>
    </div>
</template>

<script>
    import {Chart} from "chart.js";

    export default {
        name: "TasksStatistic",
        mounted(){
            const tasks = JSON.parse(this.$route.query.tasks);
            console.log(tasks);
            const myChart = document.getElementById('myChart');
            //const doughnutCanvas = document.getElementById('doughnutChart');

            if (!myChart) {
                console.error("Canvas elements not found000");
                return;
            }else{
                this.createChart();
            }
        },
        methods:{
            processDataForChart() {
                // 对任务数据进行处理，计算每天的任务数量
                const tasks = JSON.parse(this.$route.query.tasks);
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
            createChart(){

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
    .canvas-container {
        display: flex;
        align-items: center; /* 垂直居中 */
        width: 85%;
        height: 85%;
    }


    canvas {
        margin-right: 20px; /* 为了在两个 Canvas 之间添加一些间距 */
    }
</style>