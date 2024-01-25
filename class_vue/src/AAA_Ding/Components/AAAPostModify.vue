<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="post-look-card">
          <p3>修改帖子</p3>
          <hr>
          <!-- 表单区域开始 -->
          <form @submit.prevent="submitModify">
            <div class="form-group">
              <label for="postTitle" class="form-label">标题</label>
              <input type="text" class="form-control" id="postTitle" placeholder="输入标题" v-model="newPost.title">
            </div>
            <div class="form-group">
              <label for="postContent" class="form-label">内容</label>
              <textarea class="form-control" id="postContent" placeholder="输入内容" v-model="newPost.content"></textarea>
            </div>
            <button type="submit" class="btn btn-primary submit-btn" @click.prevent="submitModify">确认修改</button>
          </form>
          <!-- 表单区域结束 -->
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

    getPostedTime() {
      let now = new Date();

      let year = now.getFullYear();
      let month = String(now.getMonth() + 1).padStart(2, '0'); // Months are 0-based
      let day = String(now.getDate()).padStart(2, '0');

      let hours = String(now.getHours()).padStart(2, '0');
      let minutes = String(now.getMinutes()).padStart(2, '0');
      let seconds = String(now.getSeconds()).padStart(2, '0');

      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },

    async submitModify() {  // 添加一个新的方法来处理提交
      event.preventDefault();
      this.newPost.updateTime=this.getPostedTime();
      console.log(this.newPost.updateTime);
      console.log(this.newPost.content);
      axios.put(`http://localhost:8080/post/${this.newPost.postID}`,this.newPost, {
        headers: {
          'Content-Type': 'application/json',
        }
      })
          .then(response => {
            if (response.data.code === 200) {
              console.log('Update successful');
              this.$router.push({ name: '论坛主页', query: { userID: this.$route.query.userID } });
            } else {
              console.error('Error updating reward: ' + response.data.message);
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

.form-label {
  font-size: 18px;
}

/* 标题输入框的高度和边框颜色 */
#postTitle {
  height: 50px;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
}

/* 内容输入框的高度和边框颜色 */
#postContent {
  height: 400px;
  border: 1px solid #ced4da;
  border-radius: 0.25rem;
}

/* 发布按钮的背景色 */
.submit-btn {
  background-color: #90ee90;
  border-color: #90ee90;
}

.submit-btn:hover {
  background-color: #76de76;
  border-color: #76de76;
}

</style>