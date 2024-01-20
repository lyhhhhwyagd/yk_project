<template>
  <div class="card">
    <div class="p-3 pb-0 card-header">
      <div class="d-flex justify-content-between">
        <h6 class="mb-0">近日新增帖子</h6>
        <button
          type="button"
          class="mb-0 btn btn-icon-only btn-rounded btn-outline-secondary ms-2 btn-sm d-flex align-items-center justify-content-center"
          data-bs-toggle="tooltip"
          data-bs-placement="bottom"
          title=""
          data-bs-original-title="See which ads perform better"
        >
          <i class="fas fa-info" aria-hidden="true"></i>
        </button>
      </div>
      <div class="d-flex align-items-center">
        <span class="badge badge-md badge-dot me-4">
          <i class="bg-success"></i>
          <span class="text-xs text-dark">New Post Num</span>
        </span>
        <span class="badge badge-md badge-dot me-4">
          <i class="bg-dark"></i>
          <span class="text-xs text-dark">New Comment Num</span>
        </span>
      </div>
    </div>
    <div class="p-3 card-body" v-if="!loading">
      <default-line-chart
        id="chart-line3"
        title="Traffic channels"
        :chart="{
          labels: recentDate,
          datasets: [
            {
              label: '新增帖子',
              data: recentNewPostNum
            },
            {
              label: '新增评论',
              data: recentNewPostCommentsNum
            },
          ],
        }"
      />
    </div>
  </div>
</template>

<script>
import DefaultLineChart from "@/AAA_Ding/Components/DefaultLineChart.vue";
import axios from "axios";
export default {
  name: "RevenueChartCard",
  components: {
    DefaultLineChart,
  },
  data(){
    return{
      recentDate:[],
      recentNewPostNum:[],
      recentNewPostCommentsNum:[],
      posts:[],
      comments:[],
      loading: true,
    }
  },
  created() {
    this.getPostData().then(() => {
      this.getRecentData();
    });
  },
  methods:{
    async getPostData() {
      console.log("开始访问数据");

      // 创建一个包含所有axios请求的Promise数组
      const requests = [
        axios.get('http://localhost:8080/post'),
        axios.get('http://localhost:8080/postComment'),
      ];

      // 等待所有请求完成
      const responses = await Promise.all(requests);

      // 根据响应来更新数据
      responses.forEach(response => {
        if (response.data.code === 200) {
          if (response.config.url.endsWith('/post')) {
            this.posts = response.data.data;
          } else if (response.config.url.endsWith('/postComment')) {
            this.comments = response.data.data;
          }
        }
      });
    },
    getRecentData() {
      this.recentDate = [];
      this.recentNewPostNum = [];
      this.recentNewPostCommentsNum = [];

      const today = new Date();
      for (let i = 0; i < 9; i++) {
        // 计算过去日期
        const pastDate = new Date(today);
        pastDate.setDate(pastDate.getDate() - i);

        // 转换日期格式 (e.g., '2024-01-10')
        const dateString = pastDate.toISOString().split('T')[0];

        const newPostNum = this.getNewPostsCount(dateString);
        const newPostCommentsNum = this.getNewCommentsCount(dateString);
        // 将数据保存到相应的数组中
        this.recentDate.unshift(dateString);
        this.recentNewPostNum.unshift(newPostNum);
        this.recentNewPostCommentsNum.unshift(newPostCommentsNum);
      }
      this.chartKey = Date.now();
      this.loading=false;
    },
    getNewPostsCount(date) {
      let num=0;
      for(let i=0;i<this.posts.length;i++){
        if(this.posts[i].postTime.includes(date)){
          num+=1;
        }
      }
      return num;
    },
    getNewCommentsCount(date) {
      let num=0;
      for(let i=0;i<this.comments.length;i++){
        if(this.comments[i].postTime.includes(date)){
          num+=1;
        }
      }
      return num;
    },
  }
};
</script>
