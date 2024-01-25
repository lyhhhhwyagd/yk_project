<template>
  <el-container class="overview">
    <el-header height="30px">
      <span class="title" style="float: left; font-size: 30px; font-weight: 700"
        >问卷列表</span>
      <div>


        <el-button type="primary" style="float: right;background-color:rgb(128, 214, 22);" @click="createPaper()"
          >新增问卷</el-button
        >
      </div>
    </el-header>
    <el-divider></el-divider>

    <el-main class="papers">
      <h1 v-if="paperList.length === 0"> 无问卷信息，点击<span @click="createPaper()" style="cursor: pointer; color: blue;">新增问卷</span>！</h1>
      <div v-for="(paper, index) in paperList" :key="paper.id" style="display:block">
        <el-card class="paper_card" style="margin-bottom:30px">
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
          <el-button
            v-if="paper.status === 'INIT'"
            class = "button_uniform"
            type="primary"
            size="small"
            style="margin-top: 20px; background-color: rgb(128, 214, 22);float: left"
            @click="editPaper(paper.id)"
            >继续编辑</el-button
          >
          <div style="display: flex; align-items: flex-end;">
          <el-button
            v-if="paper.status === 'START' || paper.status === 'STOP'"
            type="primary"
            size="small"
            class = "button_uniform"
            style=" background-color: rgb(128, 214, 22);float: left"
            @click="checkPaperDetail(paper.id)"
            >查看问卷</el-button
          >
          <el-button
                  v-if="paper.status === 'START' || paper.status === 'STOP'"
                  type="primary"
                  size="small"
                  class = "button_uniform"
                  style="margin-top: 20px;background-color: rgb(128, 214, 22);float: left"
                  @click="modifyPaperDetail(paper.id)"
          >修改问卷</el-button
          >
          <el-button
            v-if="paper.status === 'START'"
            type="primary"
            size="small"
            class = "button_uniform"
            style="margin-top: 20px;background-color: rgb(128, 214, 22);float: left"

            @click="handleShare(paper.id)"
            >发放链接</el-button
          >
          <el-button
           type="primary"
           size="small"
            class = "button_uniform"
            style="margin-top: 20px;background-color: rgb(128, 214, 22);float: left"
            @click="showDeleteDialog(index)"
            >删除问卷</el-button
          >
          </div>
        </el-card>
      </div>
      <el-dialog title="提示" v-model="dialogVisible" width="30%">
        <span>确认删除该问卷</span>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="danger" @click="handleDelete()"
            >确 定</el-button
          >
        </span>
      </el-dialog>
    </el-main>

    <el-dialog
            title="新增问卷"
            v-model="createPaperVisible"
            :before-close="handleClose"
            @close="close()"
            width="30%"
    >
      <div class="questionnaire-name">
        问卷名称：
        <el-input v-model="title"> </el-input>
      </div>
      <div class="questonnaire-explain">
        问卷说明：
        <el-input
                type="textarea"
                :autosize="{ minRows: 2, maxRows: 4 }"
                v-model="description"
        ></el-input>
      </div>
      <span class="dialog-footer">
      <el-button type="primary" @click="reset()" style="float: left;background-color: rgb(128, 214, 22);">重 置</el-button>
      <el-button @click="set_createPaperVisible(false)">取 消</el-button>
      <el-button type="success" style="background-color: rgb(128, 214, 22);" @click="submitcreatePaper()">确 定</el-button>
    </span>
    </el-dialog>
  </el-container>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import axios from 'axios'
import Vuex from 'vuex'

export default {
  name: 'Overview',
  data() {
    return {
      dialogVisible: false,
      createPaperVisible:false,
      description: '',
      title: '',
      userId: -1,
      paperList:{},
      paperInfo: {},
      questionList: [],
      deleteDialogInfo: {
        index: -1
      }
    }
  },
  mounted() {
    // console.log('mounted')
    //this.getAllPapers()
    this.getAllPapers2()
  },
  computed: {
    ...mapGetters(['allPapers'])
  },
  methods: {
    ...mapActions(['editOldPaper']),
    ...mapMutations(['set_createPaperVisible', 'set_paperInfo', 'set_questionList']),
    createPaper() {
      this.createPaperVisible = true;
    },
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
          console.log("this.paperList", this.paperList);
          //this.$message.success('提交成功');
          //this.$router.push({ name: 'complete', params: { paperId: this.paperId } });
        }else{
          console.log("提交失败，请检查网络状态");
          //this.$message.error('提交失败，请检查网络状态');
        }
      })

    },
    checkPaperDetail(paperId) {
      console.log(`check paper detail with paperId: ${paperId}`)
      this.$router.push({ name: '问卷管理', params: { paperId } })
    },
    modifyPaperDetail(paperId) {
      console.log(`check paper detail with paperId: ${paperId}`)
      this.$router.push({ name: '问卷修改', params: { paperId } })
    },
    handleShare(paperId) {
      this.$router.push({ name: '问卷发放', params: { paperId } })
    },
    editPaper(paperId) {
      this.editOldPaper(paperId).then(res => {
        if(res) {
          this.$router.push({ name: '问卷创建', params: { paperId } });

        } else {
          this.$notify.success({
            title: '异常问卷，请询问管理员'
          })
        }
      })
    },
    handleDelete() {
      const index = this.deleteDialogInfo.index;
      console.log(`delete paper with index: ${index}`);
      console.log("this.paperList[index]",this.paperList[index]);

      const targetPaper = this.paperList[index];
      const paperId = targetPaper.id;
      console.log("删除的问卷ID paperId=", paperId);
      axios.get(`http://localhost:8080/api/paper/${paperId}/deletePaper`).then(res => {
        console.log("handleDelete res=",res);
        if(res.data.success){
          console.log("删除成功");
          console.log("删除之前的paperList:", this.paperList);
          this.paperList.splice(index, 1);
          console.log("删除之后的paperList:", this.paperList);

          }else{
          console.log("删除失败，请重试");
          this.$message.error('删除失败，请检查网络状态');
        }
      })
      this.dialogVisible = false
    },
    showDeleteDialog(index) {
      this.deleteDialogInfo.index = index
      this.dialogVisible = true
    },

    submitcreatePaper() {
      if(this.title === '') {
        this.$message.error('请填上标题')
        return
      }
      const paperInfo = {
        title: this.title,
        description: this.description
      }
      console.log("submitcreatePaper paperInfo:",paperInfo);
      const paperForm = {
        userId: 2,
        status: 'INIT',
        ...paperInfo
      }
      console.log("创建问卷 paperForm：", paperForm);
      axios.post(`http://localhost:8080/api/paper/addPaper`, paperForm).then(res => {
        console.log("创建问卷 res=",res);
        if(res.data.success){
          console.log("提交成功");
          const paperInfo = res.data.content;
          const paperId = paperInfo.id;
          console.log("新创建的paperId=", paperId);
          this.editOldPaper(paperId);
          //this.$router.push(`/editor/create/${paperId}`);
          this.$router.push({ name: '问卷创建', params: { paperId } })
          this.createPaperVisible = false;
          }else{
          console.log("提交失败，请检查网络状态");
          this.$message.error('创建失败，请重新整理')
        }
      })
    },

    editOldPaper1(paperId ) {
      console.log("合并测试 editOldPaper paperId:", paperId);
      axios.get(`http://localhost:8080/api/paper/${paperId}/checkPaper`).then(res => {
        console.log("editOldPaper res=", res);
        console.log("res.data.success=", res.data.success);
        if(res.data.success){
          const paperInfo = res.data.content;
          const questionList = paperInfo.question1List;

          paperInfo.questionList = undefined
          for(let question of questionList) {
            if(question.type == null) {
              question.type = 3
            }
          }
          console.log("尝试同步 paperInfo");
          this.set_paperInfo = paperInfo;
          console.log("尝试结束");
          console.log("尝试同步 questionList");
          this.set_questionList = questionList;
          console.log("尝试结束");
          this.paperInfo = paperInfo;
          this.questionList = questionList;
        }else{
          console.log("editOldPaper 提交失败，请检查网络状态");
          this.$message.error('创建失败，请重新整理')
        }
      })
      //console.log("editOldPaper res=", res)
        //commit('set_paperInfo', paperInfo)
        //commit('set_questionList', questionList)
    },

    handleClose(done) {
      this.$confirm('确认关闭?')
              .then((_) => {
                done()
              })
              .catch((_) => {})
    },

    close() {
      // console.log(close)
      this.createPaperVisible = false;
    },
    reset() {
      this.title = ''
      this.description = ''
    }

  }
}
</script>

<style>
.paper_card {
  width: 100%;
  height: 100px;
}
  .button_uniform{
    background-color: rgb(128, 214, 22);
  }
</style>
