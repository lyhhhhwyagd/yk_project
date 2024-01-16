<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="post-look-card">
          <p3>发布新帖</p3>
          <hr>
          <!-- 表单区域开始 -->
          <form>
            <div class="form-group">
              <label for="postTitle" class="form-label">标题</label>
              <input type="text" class="form-control" id="postTitle" placeholder="输入标题" v-model="newPost.title">
            </div>
            <div class="form-group">
              <label for="postContent" class="form-label">内容</label>
              <textarea class="form-control" id="postContent" placeholder="输入内容" v-model="newPost.content"></textarea>
            </div>
            <button type="submit" class="btn btn-primary submit-btn" @click="submitAdd()">发布</button>
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
      posts:[],
      newPost:{
        postID:'',
        title:'',
        content:'',
        authorID:'',
        authorName:'',
        postTime:'',
        updateTime:'',
        commentsCount:0,
        status:1,
      },
    };
  },
  created() {
    this.getPostedUser();
    this.getPostID();
  },
  methods:{
    getPostedUser(){
      this.newPost.authorID = this.$route.query.userID;
      axios.get(`http://localhost:8080/loginIn/${this.newPost.authorID}`)
          .then(response => {
            if (response.data.code === 200) {
              this.newPost.authorName = response.data.data.userName;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
    },

    getPostID() {
      axios.get('http://localhost:8080/post')
          .then(response => {
            if (response.data.code === 200) {
              this.posts = response.data.data;
              let today = new Date();
              let todayFormatted = today.getFullYear() + String(today.getMonth() + 1).padStart(2, '0') + String(today.getDate()).padStart(2, '0');
              let maxId = 0;
              for(let i=0; i<this.posts.length; i++){
                if(this.posts[i].postID.substring(0, 8)===todayFormatted){
                  let id = Number(this.posts[i].postID.substring(8));
                  if (id >= maxId){
                    maxId = id+1;
                  }
                }
              }
              this.newPost.postID = todayFormatted + String(maxId).padStart(4, '0');
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
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

    submitAdd() {
      this.newPost.postTime=this.getPostedTime();
      this.newPost.updateTime=this.newPost.postTime;
      if(this.newPost.title===''||this.newPost.content===''){
        window.alert("标题或内容为空");
      }else{
        const id = this.$route.params.id; // 获取页面的ID
        axios.post(`http://localhost:8080/post`,this.newPost, {
          headers: {
            'Content-Type': 'application/json',
          }
        })
            .then(response => {
              if (response.data.code === 200) {
                console.log('Post successful');
                console.log(this.newPost);
              } else {
                console.error('Error updating reward: ' + response.data.message);
              }
            })
            .catch(error => {
              console.error(error);
            });
        this.$router.push({ name: 'Posts', query: { userID: this.$route.query.userID } });
      }
    },
  }
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