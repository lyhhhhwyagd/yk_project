import jsPDF from "jspdf";
<template>
  <div class="monitor">
    <el-container>
      <el-header height="30px">
        <div class="title" style="display: flex; align-items: center; font-size: 30px; font-weight: 700">
          <span style="text-align: center;">{{ monitorPaper.title }}</span>
        </div>
      </el-header>


      <el-divider></el-divider>
      <el-main>
        <div class="analyzeTables" :model="monitorPaper">
          <div style="text-align: left">
            <el-button
             style="background-color: rgb(128, 214, 22);"
              v-if="monitorPaper.status === 'START'"
              type="primary"
              @click="endCollection()"
              >结束收集</el-button
            >
            <el-button
              v-if="monitorPaper.status === 'STOP' || monitorPaper.status === 'INIT'"
              type="warning"
              style="background-color: rgb(128, 214, 22);"
              @click="showResetModal = true"
            >重新发放</el-button>
            <el-button type="primary" @click="printBox" style="background-color: rgb(128, 214, 22);">打印</el-button>
            <el-button @click="exportPDF" type="primary" style="background-color: rgb(128, 214, 22);">导出PDF</el-button>
            <el-button @click="createChart" type="primary" style="background-color: rgb(128, 214, 22);">可视化</el-button>
            <el-dialog title="设定收集时间" v-model="showResetModal">
              <div style="height: 50px; text-align: left; line-height: 50px">
                <el-radio v-model="radio" label="1">手动结束收集</el-radio>
                <el-divider direction="vertical" />
                <el-radio v-model="radio" label="2">开始时间-结束时间</el-radio>
              </div>
              <div
                style="height: 30px; text-align: left; line-height: 30px; margin-bottom: 40px"
                v-if="datepicker"
              >
                <el-date-picker
                  v-model="date"
                  type="daterange"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  v-if="datepicker"
                >
                </el-date-picker>
              </div>
              <div class="dialog-footer">
                <el-button @click="showResetModal = false">取 消</el-button>
                <el-button type="primary" @click="resetCollection()"
                  >确 定</el-button
                >
              </div>
            </el-dialog>
          </div>
          <div style="text-align: left; margin: 5px 0">
            <span style="font-size: 20px; font-weight: 450"
            >问卷状态：{{
                { INIT: '编辑中', START: '已发放', STOP: '已回收' }[
                  monitorPaper.status
                ]
              }}</span
            >
          </div>
          <div style="text-align: left; margin: 5px 0">
            <span
                    style="font-size: 20px; font-weight: 450"
                    v-if="monitorPaper.endTime != null"
            >
            发放时段：{{ monitorPaper.startTime }} 到 {{ monitorPaper.endTime }}
            </span>
            <span style="font-size: 20px; font-weight: 450" v-else>
              发放时段：人工操作
            </span>
          </div>
          <el-dialog top="10vh" width="70%" v-model="answersVisible">
            <span style="font-size: 25px; font-weight: 450">问卷填写记录</span>
            <el-table :data="answers.rows" max-height="600px" stripe="">
              <el-table-column property="date" label="填写时间" width="200"></el-table-column>
              <el-table-column v-for="(ques, j) in monitorPaper.questionStatistics" :key="j" :property="'q'+(j+1)" :label="'第'+(j+1)+'题'+ques.title" min-width="300"></el-table-column>
            </el-table>
          </el-dialog>

          <div id="printBox">
          <div
            v-for="(question, index) in monitorPaper.questionStatistics"
            :key="question.id"
          >
            <h1 class="qusetionTitle" style="font-size: 20px; font-weight: 700">
              第{{ index + 1 }}题--{{ question.title }}
            </h1>
            <el-tag style="color:rgb(37,124,115);margin-bottom:10px;float:left"
              >填写总数：{{ question.filledInNum }}</el-tag
            >
            <el-table
              v-if="question.type === 1 || question.type == 2"
              :data="question.optionStatistics"
              border
              stripe
              style="width:100%; margin-bottom: 10px"
            >
              <el-table-column
                prop="sequence"
                label="选项"
                sortable
              ></el-table-column>
              <el-table-column prop="content" label="选项描述">
                <template v-slot="scope">
                  <span style="center">{{
                    scope.row[scope.column.property]
                  }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="selectedNum"
                label="选择人数"
                sortable
              ></el-table-column>
              <el-table-column prop="percent" label="比例"> </el-table-column>
              <div>
                <canvas id="myChart"  width="400" height="400"></canvas>
              </div>
            </el-table>

            <el-table
              v-if="question.type === 3"
              :data="question.answerList"
              border
              stripe
              style="width: 100%;margin-bottom: 40px"
            >
              <el-table-column
                label="序号"
                type="index"
                width="100"
              ></el-table-column>
              <el-table-column
                label="答案文本"
                prop="answerContent"
              ></el-table-column>
            </el-table>
          </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import { mapState, mapGetters, mapMutations, mapActions } from 'vuex';
import axios from "axios";
import print from "print-js";
import jsPDF from 'jspdf';
import html2canvas from "html2canvas";
import {Chart} from "chart.js";
export default {
  name: 'Monitor',

  data() {
    return {
      showResetModal: false,
      paperStatistic:{},
      // datepicker: false,
      answersVisible: false,
      radio: '1',
      date: ''
    }
  },
  async mounted() {
    const paperId = this.$route.params.paperId
    let res = await this.getFullPaperStatistic(paperId)
    console.log("Monitor res=", res);
    console.log("monitorPaper.questionStatistics:",this.monitorPaper.questionStatistics);
    if (!res) {
      this.$message.error('问卷统计加载失败，请询问管理员')
      return
    }
    res = await this.getAnswersRow(paperId);
    console.log(this.answers);

  },
  computed: {
    ...mapGetters(['monitorPaper', 'answers']),
    datepicker() {
      return this.radio === '2'
    },
  },
  methods: {

    ...mapActions([
      'getFullPaperStatistic',
      'terminateCollection',
      'restartCollection',
      'getAnswersRow'
    ]),
    createChart(){
      // 获取数据
      const paperId = this.$route.params.paperId
      this.$router.push({
        name: '问卷调查数据分析',
        params: { paperId } });
      console.log("createChart");
    },
    endCollection() {
      this.$confirm('确定结束此问卷收集？', '结束收集', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.terminateCollection(this.monitorPaper).then((res) => {
            this.$message({
              type: 'warning',
              message: '问卷收集结束！'
            })
          })
        })
        .catch(() => {})
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
    resetCollection() {
      //const store = useStore();
      const date = this.datepicker ? this.date : null
      console.log(`date: ${date}`);
      //const mp = state => state.paperStatistic;
      const mp = state => this.store.getters.paperStatistic;
      //console.log(mp(this.$store.state.customer));
      console.log("修改之后restartCollection mp:", mp);
      const mp1 = state => this.$store.state.customer.paperStatistic;

      console.log("修改之后restartCollection mp1:", mp1);
      this.restartCollection(date).then((res) => {
        if (res) {
          this.$message.success('重新发放成功')
          console.log(this.monitorPaper)
        } else {
          this.$message.error('发放失败')
        }
      })
      this.showResetModal = false
    }
  }
}
</script>

<style>
.questionTitle {
  margin-left: 5px;
  display: inline-block;
}
</style>
