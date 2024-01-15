<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="post-look-card">
          <h2>{{ this.newPost.title }}</h2>
          <hr/>
          <p>{{ this.newPost.content }}</p>
        </div>
      </div>
    </div>
    <hr/>
    <div class="row justify-content-center">
      <div class="col-md-7">
        <div class="post-look-card">
          <h5>评论</h5>
          <hr/>
          <p>{{ this.newPost.content }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'search',
  data() {
    return {
      posts: [],
      newPost:{
        postID:'',
        title:'',
        content:'',
        authorID:'',
        authorName:'',
        postTime:'',
        updateTime:'',
        commentsCount:'',
        status:'',
      },
      author:[]
    };
  },
  components: {

  },
  created() {
    this.getPostData();
    this.getAuthorData();
  },
  methods: {
    getAuthorData() {
      const id = this.newPost.authorID;
      axios.get(`http://localhost:8080/loginIn/${id}`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.author=response.data.data;
              console.log(this.author);
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    getPostData() {
      console.log("开始访问数据");
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/post/${id}`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.posts = response.data.data;

              this.newPost.postID=this.posts.postID;
              this.newPost.title=this.posts.title;
              this.newPost.content=this.posts.content;
              this.newPost.authorID=this.posts.authorID;
              this.newPost.authorName=this.posts.authorName;
              this.newPost.postTime=this.posts.postTime;
              this.newPost.updateTime=this.posts.updateTime;
              this.newPost.commentsCount=this.posts.commentsCount;
              this.newPost.status=this.posts.status;

              console.log(this.posts)
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
  },
};
</script>

<style scoped>
.post-look-card {
  box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
  transition: 0.3s;
  border-radius: 15px;
  padding: 20px;
  background: white;
}

.post-look-card h2 {
  margin-top: 0;
}

.post-look-card hr {
  margin: 20px 0;
}

.post-look-card p {
  white-space: pre-line; /* 保持文本换行符 */
  margin-bottom: 0;
}
</style>