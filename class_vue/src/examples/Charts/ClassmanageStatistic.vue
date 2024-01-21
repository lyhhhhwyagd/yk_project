<template>
    <div class="canvas-container">
        <canvas id="lineChart" class="custom-canvas" ></canvas>
    </div>
    <div class="canvas-container">
        <canvas id="lineChart1" class="custom-canvas" ></canvas>
    </div>
    <div class="canvas-container1">
        <canvas id="doughnutChart" class="custom-canvas"></canvas>
        <canvas id="doughnutChart1" class="custom-canvas"></canvas>
    </div>
</template>

<script>
    import {Chart} from "chart.js";

    export default {
        name: "ClassmanageStatistic",
        mounted(){
            const classManages = JSON.parse(this.$route.query.ClassManages);
            console.log(classManages);
            const lineCanvas = document.getElementById('lineChart');
            const doughnutCanvas = document.getElementById('doughnutChart');

            if (!lineCanvas || !doughnutCanvas) {
                console.error("Canvas elements not found000");
                return;
            }else{
                this.createChart();
            }

        },
        methods:{
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
                const classManages = JSON.parse(this.$route.query.ClassManages);
                const labels = classManages.map(student => student.studentName);
                const data = classManages.map(student => student.attendanceRate);
                const attendanceRatepercentages = this.calculatePercentages(data);
                const dailyScoreData = classManages.map(student => student.dailyScore);
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
    .canvas-container1 {
        display: flex;
        width: 45%;
        height: 45%;
    }


    canvas {
        margin-right: 20px; /* 为了在两个 Canvas 之间添加一些间距 */
    }
</style>