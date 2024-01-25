<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
        <mini-statistics-card
            title="今日新帖"
            :value="this.newCommentsNum"
            :percentage="{
            value: '+505%',
            color: 'text-success',
          }"
            :icon="{
            component: 'ni ni-money-coins',
            background: iconBackground,
          }"
            direction-reverse
        />
      </div>
      <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
        <mini-statistics-card
            title="总帖子数"
            :value="this.posts.length"
            :percentage="{
            value: '+3%',
            color: 'text-success',
          }"
            :icon="{
            component: ' ni ni-world',
            background: iconBackground,
          }"
            direction-reverse
        />
      </div>
      <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
        <mini-statistics-card
            title="总评论数"
            :value="this.comments.length"
            :percentage="{
            value: '-2%',
            color: 'text-danger',
          }"
            :icon="{
            component: 'ni ni-paper-diploma',
            background: iconBackground,
          }"
            direction-reverse
        />
      </div>
      <div class="col-xl-3 col-sm-6 mb-xl-0">
        <mini-statistics-card
            title="总点赞数"
            :value="this.likes.length"
            :percentage="{
            value: '+5%',
            color: 'text-success',
          }"
            :icon="{
            component: 'ni ni-cart',
            background: iconBackground,
          }"
            direction-reverse
        />
      </div>
    </div>

    <div class="row mt-4">
      <div class='col-md-2'></div>
      <div class="col-md-6">
        <a-a-a-post-list
            title="热门帖子"
            :bills="this.bills"
        />
      </div>
      <div class="col-md-2">
        <ranking-list-card
            :horizontal-break="false"
            :card="{
              title: '我的',
              date: 'March',
              subtitle: '动态',
            }"
            :item="[
              {
                title: '写文章',
                amount: '+1',
                icon: 'fa-arrow-up',
                color: 'success',
              },
              {
                title: '写想法',
                amount: '+1',
                icon: 'fa-arrow-up',
                color: 'success',
              },
            ]"
        >
          <button @click="navigateToPostAdd" class="centered-button">开启创作之旅</button>
          <ranking-list
              title="消息"
              :item="[
              {
                title: '我的帖子',
                amount: myPostsNum,
                icon: 'fa-arrow-up',
                color: 'dark',
              },
              {
                title: '我的评论',
                amount: myCommentsNum,
                icon: 'fa-arrow-up',
                color: 'dark',
              },
              {
                title: '收到点赞',
                amount: receivedLikesNum,
                icon: 'fa-info',
                color: 'dark',
              },
              {
                title: '收到评论',
                amount: receivedCommentsNum,
                icon: 'fa-info',
                color: 'dark',
              },
            ]"
              :horizontal-break="false"
          />
        </ranking-list-card>
      </div>
    </div>
  </div>
</template>
<script>
import AAAPostList from "@/AAA_Ding/Components/AAAPostList.vue";
import MiniStatisticsCard from "@/AAA_Ding/Components/MiniStatisticsCard.vue";
import RankingListCard from "@/AAA_Ding/Components/RankingListCard.vue";
import RankingList from "@/AAA_Ding/Components/RankingList.vue";
import axios from "axios";
export default {
  data() {
    return {
      iconBackground: "bg-gradient-success",
      posts:[],
      bills:[],
      comments:[],
      likes:[],
      myPosts:[],
      newCommentsNum:'',
      myPostsNum:'',
      myCommentsNum:'',
      receivedCommentsNum:'',
      receivedLikesNum:'',
    };
  },
  created() {
    this.getPostsData().then(() => {
      this.calculateData();
    });
  },
  components: {
    RankingList, RankingListCard, AAAPostList,
    MiniStatisticsCard,
  },
  methods: {
    navigateToPostAdd() {
      this.$router.push({ name: '新增帖子', query: { userID: this.$store.state.userID } });
    },
    async getPostsData() {
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
            for(let i=0;i<this.posts.length;i++){
              this.bills.push({
                id:this.posts[i].postID,
                title:this.posts[i].title,
                content:this.posts[i].content,
                authorID:this.posts[i].authorID,
                authorName:this.posts[i].authorName,
                postTime:this.posts[i].postTime,
                updateTime: this.posts[i].updateTime,
                status: this.posts[i].status,
              })
            }
          } else if (response.config.url.endsWith('/postComment')) {
            this.comments = response.data.data;
          } else if (response.config.url.endsWith('/postLikes')) {
            this.likes = response.data.data;
          }
        }
      });
    },
    calculateData(){
      let authorID=this.$route.query.userID;
      let today = new Date();
      let todayFormatted = today.getFullYear() + String(today.getMonth() + 1).padStart(2, '0') + String(today.getDate()).padStart(2, '0');
      this.newCommentsNum=0;
      this.myPostsNum=0;
      this.myCommentsNum=0;
      this.receivedCommentsNum=0;
      this.receivedLikesNum=0;
      for(let i=0;i<this.posts.length;i++){
        if(this.posts[i].postTime.includes(todayFormatted)){
          this.newCommentsNum++;
        }
        if(this.posts[i].authorID===authorID){
          this.myPostsNum++;
          this.myPosts.push(this.posts[i]);
        }
      }
      for(let i=0;i<this.comments.length;i++){
        if(this.comments[i].authorID===authorID){
          this.myCommentsNum++;
        }
        if(this.comments[i].postAuthorID===authorID){
          this.receivedCommentsNum++;
        }
      }
      for(let i=0;i<this.likes.length;i++){
        let postID=this.likes[i].postID;
        for(let j=0;j<this.myPosts.length;j++){
          if(postID===this.myPosts[j].postID){
            this.receivedLikesNum++;
          }
        }
      }
    }
  }
};
</script>

<style>
.centered-button {
  font-size: 14px;
  color: #333;
  border: 2px solid rgb(43, 183, 53);
  border-radius: 10px;
  padding: 10px 20px;
  background-color: #fff;
  cursor: pointer;
  outline: none;
  display: block;
  transition: background-color 0.3s, border-color 0.3s;
  margin-left: 20px;
  width: calc(100% - 40px);
}

.centered-button:hover {
  background-color: #f5f5f5;
  border-color: #76b476;
}
</style>