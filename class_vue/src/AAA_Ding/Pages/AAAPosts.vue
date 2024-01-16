<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-xl-3 col-sm-6 mb-xl-0 mb-4">
        <mini-statistics-card
            title="今日新帖"
            value="+3,000"
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
            title="新增用户"
            value="+2,300"
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
            title="今日访问"
            value="+3,462"
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
            title="总用户量"
            value="103,430"
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
                amount: '2',
                icon: 'fa-arrow-up',
                color: 'dark',
              },
              {
                title: '我的评论',
                amount: '5',
                icon: 'fa-arrow-up',
                color: 'dark',
              },
              {
                title: '收到点赞',
                amount: '765',
                icon: 'fa-info',
                color: 'dark',
              },
              {
                title: '收到评论',
                amount: '231',
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
    };
  },
  created() {
    this.getPostsData();
  },
  components: {
    RankingList, RankingListCard, AAAPostList,
    MiniStatisticsCard,
  },
  methods: {
    navigateToPostAdd() {
      this.$router.push({ name: 'PostsAdd', query: { userID: this.$store.state.userID } });
    },
    getPostsData() {
      console.log("开始访问数据");
      axios.get('http://localhost:8080/post')
          .then(response => {
            if (response.data.code === 200) {
              this.posts = response.data.data;
              for(let i=0;i<this.posts.length;i++){
                this.bills.push({
                  title:this.posts[i].title,
                  content:this.posts[i].content,
                  createTime:this.posts[i].updateTime,
                  id:this.posts[i].postID,
                  author:this.posts[i].authorName,
                })
              }
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
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
  margin: 0 auto;
  transition: background-color 0.3s, border-color 0.3s;
  width: 200px;
}

.centered-button:hover {
  background-color: #f5f5f5;
  border-color: #76b476;
}
</style>