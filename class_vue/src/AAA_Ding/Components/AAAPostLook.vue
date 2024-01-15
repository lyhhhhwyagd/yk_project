<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="post-look-card">
          <h2>{{ this.newPost.title }}</h2>
          <hr/>
          <h5>{{this.newPost.authorName}} :</h5>
          <br>
          <p class="post-content">{{ this.newPost.content }}</p>
          <br>
          <h7>编辑于：{{this.newPost.updateTime}}</h7>
        </div>
      </div>
    </div>
    <hr/>
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div id="app" class="post-look-card">
          <hr/>

          <div class="comment-section">
            <textarea class="comment-box" v-model="newComment.content" placeholder="写下你的评论..."></textarea>
            <button class="submit-button" @click="submitComment()">发布</button>
          </div>
          <hr/>

          <h5>共 {{this.validComments.length}} 条评论</h5>
          <div class="comments-list">
            <div class="comment" v-for="comment in validComments" :key="comment.id">
              <div class="comment-author">{{ comment.authorID }}</div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-date">{{ comment.updateTime }}</div>
            </div>
          </div>
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
      author:[],
      comments:[],
      validComments:[],
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
      newComment:{
        commentID:'',
        authorID:'',
        postID:'',
        postAuthorID:'',
        content:'',
        postTime:'',
        updateTime:'',
        status:'',
      }
    };
  },
  components: {

  },
  created() {
    this.getPostData();
    this.getCommentData();
  },
  methods: {
    submitComment(){

    },
    getCommentData() {
      axios.get(`http://localhost:8080/postComment`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.comments=response.data.data;
              console.log(this.comments);
              for(let i=0;i<this.comments.length;i++){
                if(this.comments[i].postID===this.$route.params.id){
                  console.log("right");
                  this.validComments.push(this.comments[i]);
                }
              }
              console.log(this.validComments);
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
.post-content {
  font-family: 'Microsoft YaHei', sans-serif;
  color: black;
  font-size: 16px;
}
.comment-box {
  border: 1px solid #ccc;
  padding: 5px 10px;
  border-radius: 10px;
  margin-left: 10px;
  margin-right: 10px;
  width: calc(100% - 20px);
  box-sizing: border-box;
  resize: vertical;
  outline: none;
  min-height: 20px;
  overflow: auto;
}
.comment-section {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.submit-button {
  font-size: 14px;
  margin-top: 10px;
  margin-right: 10px;
  height: 30px;
  width: 50px;
  border: 1px solid transparent;
  color: white;
  font-weight: bold;
  background-color: #90ee90;
  cursor: pointer;
  border-radius: 5px;
  outline: none;
  transition: background-color 0.3s ease;
}
.comment {
  margin-bottom: 15px; /* 在评论之间添加一些空间 */
}

.comment-author {
  font-weight: bold; /* 使作者名加粗 */
}

.comment-content {
  margin-left:20px;
  margin-top: 5px; /* 在作者名和评论内容之间添加一些空间 */
  margin-bottom: 5px; /* 在评论内容和日期之间添加一些空间 */
}

.comment-date {
  margin-left:20px;
  font-size: 0.9em; /* 设置日期的字体大小稍小于正文内容 */
  color: #777; /* 为日期设置一种淡色调 */
}
</style>