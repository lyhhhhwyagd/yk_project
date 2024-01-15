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
          <p>编辑于：{{this.newPost.updateTime}}</p>
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
            <button
                class="submit-button"
                :class="{ 'button-active': newComment.content, 'button-inactive': !newComment.content }"
                @click="submitComment"
                :disabled="!newComment.content">
              发布
            </button>
          </div>
          <hr/>

          <h5>共 {{this.validComments.length}} 条评论</h5>
          <div class="comments-list">
            <div class="comment" v-for="comment in validComments" :key="comment.id">
              <div class="comment-author">{{ comment.authorName }}</div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-footer">
                <span class="comment-date">{{ comment.updateTime }}</span>
                <div class="comment-actions">
                  <a href="javascript:;" class="comment-delete" @click="confirmDelete(comment.commentID)">删除</a>
                </div>
              </div>
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
        authorName:'',
        postID:'',
        postAuthorID:'',
        content:'',
        postTime:'',
        updateTime:'',
        status:1,
      }
    };
  },
  components: {

  },
  created() {
    this.getPostData();
    this.getCommentData();
    this.getAuthorData();
  },
  methods: {
    getCommentID(){
      let today = new Date();
      let todayFormatted = today.getFullYear() + String(today.getMonth() + 1).padStart(2, '0') + String(today.getDate()).padStart(2, '0');
      let maxId = 1;
      for(let i=0; i<this.comments.length; i++){
        if(this.comments[i].commentID.substring(0, 8)===todayFormatted){
          let id = Number(this.comments[i].commentID.substring(8));
          if (id >= maxId){
            maxId = id+1;
          }
        }
      }
      this.newComment.commentID = todayFormatted + String(maxId).padStart(4, '0');
    },
    getAuthorData(){
      this.newComment.authorID = this.$route.query.userID;
      axios.get(`http://localhost:8080/loginIn/${this.newComment.authorID}`)
          .then(response => {
            if (response.data.code === 200) {
              this.newComment.authorName = response.data.data.userName;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
    },
    getCurrentTime() {
      let now = new Date();

      let year = now.getFullYear();
      let month = String(now.getMonth() + 1).padStart(2, '0'); // Months are 0-based
      let day = String(now.getDate()).padStart(2, '0');

      let hours = String(now.getHours()).padStart(2, '0');
      let minutes = String(now.getMinutes()).padStart(2, '0');
      let seconds = String(now.getSeconds()).padStart(2, '0');

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },

    submitComment(){
      this.getAuthorData();
      this.getCommentID();
      this.newComment.postID=this.posts.postID;
      this.newComment.postAuthorID=this.posts.authorID;
      this.newComment.postTime=this.getCurrentTime();
      this.newComment.updateTime=this.getCurrentTime();

      console.log(this.newComment);
      axios.post(`http://localhost:8080/postComment`,this.newComment, {
        headers: {
          'Content-Type': 'application/json',
        }
      })
          .then(response => {
            if (response.data.code === 200) {
              console.log('Post successful');
            } else {
              console.error('Error updating reward: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
      window.location.reload();
    },
    getCommentData() {
      axios.get(`http://localhost:8080/postComment`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.comments=response.data.data;
              console.log(this.comments);
              for(let i=0;i<this.comments.length;i++){
                if(this.comments[i].postID===this.$route.params.id){
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
    confirmDelete(id) {
      console.log("确认删除？");
      if (window.confirm('确认删除？')) {
        this.deleteItem(id);
      }
    },
    async deleteItem(id) {
      console.log("开始删除");
      try {
        const response = await axios.delete(`http://localhost:8080/postComment/${id}`);
        if (response.data.code === 200) {
          console.log('删除成功');
          window.location.reload();
        } else {
          console.error('Error updating reward: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
      }
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
  white-space: pre-line;
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
  cursor: pointer;
  border-radius: 5px;
  outline: none;
  transition: background-color 0.3s ease;
}
.button-active {
  background-color: #28a745; /* 绿色 */
  color: white;
}

.button-inactive {
  background-color: #90ee90;
  color: white;
  cursor: not-allowed;
}


.comment {
  margin-bottom: 15px;
}

.comment-author {
  font-weight: bold;
}

.comment-content {
  margin-left:20px;
  margin-top: 5px;
  margin-bottom: 5px;
}

.comment-date {
  margin-left:20px;
  font-size: 0.9em;
  color: #777;
}
.comment-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.comment-actions a {
  margin-right: 20px;
  text-decoration: none;
  font-size: 0.9em;
}

.comment-actions a.comment-edit {
  color: #007bff;
}

.comment-actions a.comment-delete {
  color: red;
}
</style>