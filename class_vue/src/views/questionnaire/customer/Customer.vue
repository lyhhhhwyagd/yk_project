<template>
  <div class="bg">
    <div v-if="!active" class="customer">
      <div style="margin-top: 30%" class="title">来晚一步<br/>问卷已经结束收集咯！</div>
    </div>
    <div v-else class="customer">
      <div class="container">
        <div class="title">{{ customerPaper.title }}</div>
        <div class="subtitle">问卷描述：{{ customerPaper.description }}</div>
      </div>

      <el-form
        class="questions"
        ref="customerForm"
        :model="customerPaper"
        label-position="top"
      >
        <div
          class="question-container"
          v-for="(question, index) in customerPaper.question1List"
          :key="question.id"
        >
          <el-form-item
            style="text-align: left;"
            :prop="`question1List.${index}.answer`"
            :rules="rules[question.type]"
          >
            <div class = "question-container2">
            <span class="questionTitle">
              问题 {{ index + 1 }}：{{ question.title }}
              <el-tag>{{ ['','单选题','多选题','简答题'][question.type] }}</el-tag>
            </span>
            <el-input
              v-if="question.type === 3"
              style="width: 98%; min-height: 150px;"
              type="textarea"
              size="medium"
              rows="4"
              :disabled="!active"
              v-model="question.answer"
            ></el-input>
            <el-radio-group
              v-else-if="question.type === 1"
              v-model="question.answer"
            >
              <div class="radio-options-container">
              <div
                v-for="option in question.options"
                :key="option.id"
                style="margin-bottom: 10px"
              >
                <el-radio 
                  :label="option.sequence"
                  border
                  :disabled="!active"
                >
                  {{option.sequence}}:{{option.content}}
                </el-radio>
              </div>
              </div>
            </el-radio-group>
            <el-checkbox-group
              v-if="question.type === 2"
              v-model="question.answer"
            >
              <div
                v-for="option in question.options"
                :key="option.id"
                style="margin-bottom: 10px"
              >
                <el-checkbox :label="option.sequence" 
                  :disabled="!active"
                  border
                >{{option.content}}</el-checkbox>
              </div>
            </el-checkbox-group>
            </div>
          </el-form-item>
        </div>

        <el-form-item>
          <div style="text-align: right;">
          <el-button type="primary" size="large"
                     style="background-color: rgb(128, 214, 22);"
                     @click="submitAnswer()"
            >完成填写！
          </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
// import Question from './components/CustomerQuestion'
import axios from 'axios'
export default {
  name: 'Customer',
  data() {
    const rules = {
      1: [
        {  required: true, trigger: 'change', message: '必须选择一个' }
      ],
      2: [
        { type: 'array', required: true, trigger: 'change', message: '多选至少选一个' }
      ],
      3: [
        { required: true, trigger: 'blur', message: '输入不可为空' }
      ]
    }
    return {
      active: false,
      rules,
      paperId: -1
    }
  },
  mounted() {
    this.paperId = this.$route.params.paperId
    this.getFullPaper(this.paperId).then(res => {
      console.log("res=",res);
      console.log("this.customerPaper=", this.customerPaper)
      console.log("this.active", this.active)
      if(res) {
        console.log("this.customerPaper.status === 'START'",this.customerPaper.status === 'START')
        this.active = this.customerPaper.status === 'START'
        console.log("this.active", this.active)
      } else {
        this.$message.error('网路异常，请重新整理')
      }
    })
     console.log(`paperId: ${this.paperId}`)
  },
  computed: {
    ...mapGetters([
      'customerPaper'
    ])
  },
  methods: {
    ...mapActions([
      'getFullPaper',
    ]),
    buildAnswers() {
      const buildCreateTime = () => {
        const t = new Date()
        const createTime = `${t.getFullYear()}-${t.getMonth() + 1}-${t.getDate()} ${t.getHours()}:${t.getMinutes()}:${t.getSeconds()}`
        return createTime
      }
      const answers = []
      const paperId = this.customerPaper.id
      const createTime = buildCreateTime()
      console.log("buildAnswers paperId", paperId);
      console.log("buildAnswers createTime", createTime);
      console.log("buildAnswers this.customerPaper.question1List", this.customerPaper.question1List);
      for(let question of this.customerPaper.question1List) {
        let answerContent = question.answer
        if(answerContent instanceof Array) {
          answerContent = answerContent.reduce((s, c) => `${s},${c}`)
        } else {
          answerContent = String(answerContent)
        }
        answers.push({
          paperId,
          questionId: question.id,
          questionType: question.type,
          createTime,
          answerContent,
        })
      }
      return answers
    },
    submitAnswer() {
      this.$refs.customerForm.validate((valid) => {
        if(valid) {
          console.log(`form valid and submit answer`)
          const answers = this.buildAnswers()
          console.log("submitAnswer answers=",answers);
          console.log('submit answers in actions');
          axios.post(`http://localhost:8080/api/answer/addAnswers`, answers).then(res => {
            //this.tasks = Object.assign({},res.data.result)
            console.log("submitAnswers res=",res);
            //this.tasks = res.data.result;
            if(res.data.success){
              console.log("submitAnswers res.data.success", res.data.success);
              console.log("提交成功");
              this.$message.success('提交成功');
              this.$router.push({ name: 'complete', params: { paperId: this.paperId } });
            }else{
              console.log("提交失败，请检查网络状态");
              this.$message.error('提交失败，请检查网络状态');
            }
          })
        } else {
          this.$message.error('问卷不可有空栏')
        }
      })
    }
  }
}
</script>

<style>
.bg {
  width: 100%;
  background: #efefef;
}
.radio-options-container{
  margin-top: 20px;
  min-width:1300px;
}
.question-container2{
  border: 1px solid #ccc;
  width: 100%;
  min-height: 200px;
  margin-top: 15px;
  margin-bottom: 15px;
}
.customer {
  box-sizing: border-box;
  padding-top: 40px;
  background: white;
  width: 100%;
  min-height: 100vh;
  margin: 0 auto;
  overflow: scroll;
}

.customer > .container{
  /*border: 1px solid #ccc; /* 边框样式和颜色 */
  /*padding: 10px; /* 可选：为内容区域添加一些内边距 */
}

.customer > .container >.title {
  text-align: center;
  font-size: 40px;
}
.customer > .container > .subtitle {
  margin: 0 10%;
  font-size: 20px;
  width: 85%;
  text-indent: 6px;
  text-align: left;
}

.customer > .questions {
  margin: 0 10%;
}

.questionTitle {
  font-size: 25px;
}
.el-textarea__inner {
  width: 98% !important;
  min-height: 150px !important;
}

</style>
