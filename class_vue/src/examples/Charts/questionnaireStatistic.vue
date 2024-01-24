<template>
    <div>
        <div v-for="(question, index) in monitorPaper.questionStatistics" :key="index">
            <div class="canvas-container" v-if="question.type !== 3">
                <canvas :ref="'myChart' + index"></canvas>
            </div>
        </div>
    </div>
</template>

<script>
    import {Chart} from "chart.js";
    import {mapGetters} from "vuex";
    export default {
        name: "questionnaireStatistic",
        mounted() {
            console.log(this.$refs);
            this.renderCharts();
        },
        computed: {
            ...mapGetters(['monitorPaper']),
        },

        methods: {

            renderCharts() {
                // 清除之前的图表
                this.monitorPaper.questionStatistics.forEach((question, index) => {
                    // 仅处理 type 不等于 3 的情况\
                    console.log("renderCharts question:", question);
                    if (question.type !== 3) {
                        console.log("'myChart' + index",'myChart' + index)
                        const ctx = this.$refs['myChart' + index].getContext('2d');

                        // 获取 optionStatistics 数据
                        const optionStatistics = question.optionStatistics;
                        console.log("optionStatistics", optionStatistics);
                        console.log("optionStatistics.map(option => option.sequence)",optionStatistics.map(option => option.sequence));
                        console.log("optionStatistics.map(option => option.percent)", optionStatistics.map(option => option.percent));
                        // 创建新的饼图
                        const percentArray = optionStatistics.map(option => parseFloat(option.percent));
                        console.log("percentArray", percentArray);
                        const chart = new Chart(ctx, {
                            type: 'pie',
                            data: {
                                labels: optionStatistics.map(option => option.sequence),
                                datasets: [
                                    {
                                        data: percentArray,
                                        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
                                    },
                                ],
                            },
                        });

                        console.log(chart); // 检查 chart 对象是否正确
                        chart.update();
                    }
                });
            },
        },

    }
</script>

<style >
    .canvas-container {
        display: flex;
        align-items: center; /* 垂直居中 */
        width: 50%;
        height: 50%;
    }
</style>