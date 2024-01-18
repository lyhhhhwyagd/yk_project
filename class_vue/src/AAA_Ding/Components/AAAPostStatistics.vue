<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="mb-4 col-xl-3 col-sm-6 mb-xl-0">
        <mini-statistics-card
            title="新增贴数"
            :value="this.recentNewPostNum[8]"
            :percentage="{
            value: '+55%',
            color: 'text-success'
          }"
            :icon="{
            component: 'ni ni-circle-08',
            background: 'bg-gradient-dark'
          }"
            class="ms-1"
            direction-reverse
        />
      </div>
      <div class="mb-4 col-xl-3 col-sm-6 mb-xl-0">
        <mini-statistics-card
            title="总帖子数"
            :value="this.posts.length"
            :percentage="{
            value: '+3%',
            color: 'text-success'
          }"
            :icon="{
            component: 'ni ni-world',
            background: 'bg-gradient-dark'
          }"
            class="ms-1"
            direction-reverse
        />
      </div>
      <div class="mb-4 col-xl-3 col-sm-6 mb-xl-0">
        <mini-statistics-card
            title="总评论数"
            :value="this.comments.length"
            :percentage="{
            value: '-2%',
            color: 'text-danger'
          }"
            :icon="{
            component: 'ni ni-watch-time',
            background: 'bg-gradient-dark'
          }"
            class="ms-1"
            direction-reverse
        />
      </div>
      <div class="col-xl-3 col-sm-6">
        <mini-statistics-card
            title="总点赞数"
            :value="this.likes.length"
            :percentage="{
            value: '+5%',
            color: 'text-success'
          }"
            :icon="{
            component: 'ni ni-image',
            background: 'bg-gradient-dark'
          }"
            class="ms-1"
            direction-reverse
        />
      </div>
    </div>
    <div class="row">
      <div class="col-lg-7 col-md-12">
        <div class="card">
          <div class="p-3 pb-0 card-header">
            <h6 class="mb-0">近日新增</h6>
            <div class="d-flex align-items-center">
              <span class="badge badge-md badge-dot me-4">
                <i class="bg-primary"></i>
                <span class="text-xs text-dark">新增帖子</span>
              </span>
              <span class="badge badge-md badge-dot me-4">
                <i class="bg-dark"></i>
                <span class="text-xs text-dark">新增评论</span>
              </span>
              <span class="badge badge-md badge-dot me-4">
                <i class="bg-info"></i>
                <span class="text-xs text-dark">新增点赞</span>
              </span>
            </div>
          </div>
          <div class="p-3 card-body">
            <div class="chart" v-if="!loading">
              <default-line-chart
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
                    {
                      label: '新增点赞',
                      data: recentNewPostLikesNum
                    }
                  ]
                }"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="mt-4 col-lg-5 col-md-12 mt-lg-0">
        <default-doughnut-chart
            title="Refferals"
            :chart="{
            labels: ['Adobe', 'Atlassian', 'Slack', 'Spotify', 'Jira'],
            datasets: [{ label: 'Referrals', data: [25, 3, 12, 7, 10] }]
          }"
            :actions="{
            label: 'See all referrals',
            color: 'secondary'
          }"
        />
      </div>
    </div>
  </div>
</template>

<script>
import MiniStatisticsCard from "@/examples/Cards/MiniStatisticsCard.vue";
import DefaultLineChart from "@/AAA_Ding/Components/DefaultLineChart.vue";
import DefaultDoughnutChart from "@/AAA_Ding/Components/DefaultDoughnutChart.vue";
import SocialCard from "@/AAA_Ding/Components/SocialCard.vue";
import PagesCard from "@/AAA_Ding/Components/PagesCard.vue";
import setTooltip from "@/assets/js/tooltip.js";

import logoXd from "@/assets/img/small-logos/logo-xd.svg";
import logoAtlassian from "@/assets/img/small-logos/logo-atlassian.svg";
import logoSlack from "@/assets/img/small-logos/logo-slack.svg";
import logoSpotify from "@/assets/img/small-logos/logo-spotify.svg";
import logoJira from "@/assets/img/small-logos/logo-jira.svg";
import axios from "axios";
export default {
  name: "Analytics",
  components: {
    MiniStatisticsCard,
    DefaultLineChart,
    DefaultDoughnutChart,
    SocialCard,
    PagesCard
  },
  data() {
    return {
      logoXd,
      logoAtlassian,
      logoSlack,
      logoSpotify,
      logoJira,
      recentDate:[],
      recentNewPostNum:[],
      recentNewPostCommentsNum:[],
      recentNewPostLikesNum:[],
      posts:[],
      comments:[],
      likes:[],
      loading: true,
    };
  },
  mounted() {
    setTooltip(this.$store.state.bootstrap);
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
        axios.get('http://localhost:8080/postLikes')
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
          } else if (response.config.url.endsWith('/postLikes')) {
            this.likes = response.data.data;
          }
        }
      });
    },
    getRecentData() {
      this.recentDate = [];
      this.recentNewPostNum = [];
      this.recentNewPostCommentsNum = [];
      this.recentNewPostLikesNum = [];

      const today = new Date();
      for (let i = 0; i < 9; i++) {
        // 计算过去日期
        const pastDate = new Date(today);
        pastDate.setDate(pastDate.getDate() - i);

        // 转换日期格式 (e.g., '2024-01-10')
        const dateString = pastDate.toISOString().split('T')[0];

        const newPostNum = this.getNewPostsCount(dateString);
        const newPostCommentsNum = this.getNewCommentsCount(dateString);
        const newPostLikesNum = this.getNewLikesCount(dateString);
        // 将数据保存到相应的数组中
        this.recentDate.unshift(dateString);
        this.recentNewPostNum.unshift(newPostNum);
        this.recentNewPostCommentsNum.unshift(newPostCommentsNum);
        this.recentNewPostLikesNum.unshift(newPostLikesNum);
      }
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
    getNewLikesCount(date) {
      let num=0;
      for(let i=0;i<this.likes.length;i++){
        if(this.likes[i].createTime.includes(date)){
          num+=1;
        }
      }
      return num;
    },
  }
};
</script>
