<template>
  <div class="card h-100">
    <div class="p-3 pb-0 card-header">
      <div class="d-flex justify-content-between">
        <h6 class="mb-0">发帖人身份占比</h6>
        <button
          type="button"
          class="mb-0 btn btn-icon-only btn-rounded btn-outline-secondary ms-2 btn-sm d-flex align-items-center justify-content-center"
          data-bs-toggle="tooltip"
          data-bs-placement="bottom"
          title=""
          data-bs-original-title="See traffic channels"
        >
          <i class="fas fa-info" aria-hidden="true"></i>
        </button>
      </div>
    </div>
    <div class="p-3 pb-0 mt-4 card-body">
      <div class="row">
        <div class="col-7 text-start">
          <pie-chart
            :key="chartKey"
            :chart="{
              labels: ['管理员', '教师', '学生'],
              datasets: {
                label: 'Projects',
                data: [this.adminNum, this.teacherNum, this.studentNum],
              },
            }"
          />
        </div>
        <div class="my-auto col-5">
          <span class="badge badge-md badge-dot me-4 d-block text-start">
            <i class="bg-info"></i>
            <span class="text-xs text-dark">管理员</span>
          </span>
          <span class="badge badge-md badge-dot me-4 d-block text-start">
            <i class="bg-primary"></i>
            <span class="text-xs text-dark">教师</span>
          </span>
          <span class="badge badge-md badge-dot me-4 d-block text-start">
            <i class="bg-dark"></i>
            <span class="text-xs text-dark">学生</span>
          </span>
        </div>
      </div>
    </div>
    <div class="p-3 pt-0 pb-0 card-footer d-flex align-items-center">
      <div class="w-60">
        <p class="text-sm">
          More than <b>{{ this.adminNum+this.teacherNum+this.studentNum }}</b> users are willing to share something about study,
          and <b>{{ this.teacherNum }}</b> are from teacher.
        </p>
      </div>
      <div class="w-40 text-end">
        <a class="mb-0 btn bg-light text-end" href="javascript:;" @click="goToPostPage">Read more</a>
      </div>
    </div>
  </div>
</template>

<script>
import PieChart from "@/AAA_Ding/Components/PieChart.vue";
import axios from "axios";
export default {
  name: "ChannelsChartCard",
  components: {
    PieChart,
  },
  data(){
    return{
      chartKey: 0,
      posts:[],
      adminNum:'',
      teacherNum:'',
      studentNum:'',
    }
  },
  created(){
    this.getAllPostData().then(() => {
      this.calculatePercentage();
    });
  },
  methods:{
    async getAllPostData(){
      console.log("开始访问数据");

      // 创建一个包含所有axios请求的Promise数组
      const requests = [
        axios.get('http://localhost:8080/post'),
      ];
      // 等待所有请求完成
      const responses = await Promise.all(requests);

      // 根据响应来更新数据
      responses.forEach(response => {
        if (response.data.code === 200) {
          if (response.config.url.endsWith('/post')) {
            this.posts = response.data.data;
          }
        }
      });
    },
    calculatePercentage(){
      this.adminNum=0;this.teacherNum=0;this.studentNum=0;
      for(let i=0;i<this.posts.length;i++){
        if(this.posts[i].authorID[0]==='1'){
          this.adminNum++;
        }else if(this.posts[i].authorID[0]==='2'){
          this.teacherNum++;
        }else if(this.posts[i].authorID[0]==='3'){
          this.studentNum++;
        }
      }
      this.chartKey++;
    },
    goToPostPage(){
      this.$router.push({ name: 'Posts', query: { userID: this.$route.query.userID } });
    }
  }
};
</script>
