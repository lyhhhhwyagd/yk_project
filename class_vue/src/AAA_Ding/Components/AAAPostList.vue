<template>
  <div class="card" style="width: 100%;">
    <div class="card-header pb-0 px-3 d-flex justify-content-between align-items-center">
      <h6 class="mb-0">{{ title }}</h6>
      <div>
        <!-- 按钮被添加到这个 div 中 -->
        <soft-button @click="Export" class="small-button me-2">
          <i class="fas fa-file-export"></i>
        </soft-button>
        <soft-button @click="Print" class="small-button me-2">
          <i class="fas fa-print"></i>
        </soft-button>
        <soft-button @click="Statistics" class="small-button me-2">
          <i class="fas fa-chart-bar"></i>
        </soft-button>
      </div>
      <div class="search-sort-wrapper">
        <div class="input-group search-box">
          <span class="input-group-text"><i class="fas fa-search"></i></span>
          <input
              type="text"
              class="form-control"
              placeholder="搜索帖子"
              v-model="searchQuery"
              @keyup.enter="search"
          />
        </div>
        <div>
          <button class="my-btn-sort" @click="toggleArrow_updateTime">
            时间
            <span class="arrow arrow-up" v-bind:class="{ 'green': arrowState_updateTime === 1 }">&#9650;</span>
            <span class="arrow arrow-down" v-bind:class="{ 'green': arrowState_updateTime === 2 }">&#9660;</span>
          </button>
        </div>
      </div>
    </div>
    <div class="card-body pt-4 p-3">
      <ul class="list-group">
        <li v-for="({ title, content }, index) of BillsToShow" :key="index" class="list-group-item border-0 d-flex p-4 mb-2 bg-gray-100 border-radius-lg">
          <div class="d-flex flex-column justify-content-between" style="min-height: 15vh; overflow: hidden;" id="container">
              <div>
                <h3 class="mb-3 textL1">{{ title }}</h3>
                <span class="mb-2 text-xs">
                  <span class="text-dark textL3 font-weight-bold preserve-format" id="content">{{ content }}</span>
                </span>
              </div>
              <div class="d-flex justify-content-between">
                <div>
                  <a
                      class="btn btn-link px-3 mb-0"
                      :class="{ 'text-danger': judgeIsLiked(BillsToShow[index].id), 'text-secondary': !judgeIsLiked(BillsToShow[index].id) }"
                      @click="toggleLike(BillsToShow[index].id)"
                  >
                    <i
                        class="fas fa-thumbs-up me-2"
                        :class="{ 'text-danger': judgeIsLiked(BillsToShow[index].id), 'text-secondary': !judgeIsLiked(BillsToShow[index].id) }"
                        aria-hidden="true"
                    ></i>点赞
                  </a>
                  <a class="btn btn-link text-dark px-3 mb-0" @click="postLook(BillsToShow[index].id)">
                    <i class="fas fa-pencil-alt text-dark me-2" aria-hidden="true"></i>评论
                  </a>
                  <a class="btn btn-link px-3 mb-0" id="expand-button" @click="postLook(BillsToShow[index].id)">
                    <i class="fas fa-arrow-down"></i>阅读全文
                  </a>
                  <a class="btn btn-link text-dark px-3 mb-0" @click="postModify(BillsToShow[index].id)">
                    <i class="fas fa-edit text-dark me-2" aria-hidden="true"></i>修改
                  </a>
                  <a href="javascript:;" class="btn btn-link text-danger text-gradient px-3 mb-0" @click="confirmDelete(BillsToShow[index].id)">
                    <i class="fas fa-trash text-danger me-2" aria-hidden="true"></i>删除
                  </a>
                </div>
              </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>
  
  <script>
  import axios from "axios";
  import SoftButton from "@/AAA_Ding/Components/SoftButton.vue";
  import * as XLSX from "xlsx";

  export default {
    name: "BillingCard",
    components: {SoftButton},
    props: {
      title: {
        type: String,
        default: "",
      },
      bills: {
        type: Array,
        id: String,
        title: String,
        content: String,
        authorID: String,
        authorName: String,
        postTime: String,
        updateTime: String,
        status: String,
        default: () => [],
      },
    },
    data() {
      return {
        BillsToShow:[],
        postLikes:[],
        newLikes:{
          id:'',
          authorID:'',
          postID:'',
          createTime:'',
        },
        searchQuery:'',
        arrowState_updateTime: 0,
      };
    },
    created(){
      this.getPostLikes();
      this.BillsToShow=this.bills;
    },
    methods: {
      toggleArrow_updateTime() {
        this.arrowState_updateTime = (this.arrowState_updateTime + 1) % 3;
        if(this.arrowState_updateTime===1){
          this.BillsToShow = this.BillsToShow.sort((a, b) => {
            // convert postedTime to Date object
            const dateA = new Date(a.postTime);
            const dateB = new Date(b.postTime);
            // compare dates
            return dateA - dateB;
          });
        }
        if(this.arrowState_updateTime===2){
          this.BillsToShow = this.BillsToShow.sort((a, b) => {
            // convert postedTime to Date object
            const dateA = new Date(a.postTime);
            const dateB = new Date(b.postTime);
            // compare dates
            return dateB - dateA;
          });
        }
      },
      getPostLikes(){
        axios.get(`http://localhost:8080/postLikes`) // 将ID添加到请求的URL中
            .then(response => {
              if (response.data.code === 200) {
                this.postLikes=response.data.data;
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
          const response = await axios.delete(`http://localhost:8080/post/${id}`);
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
      postLook(id){
        this.$router.push({ name: 'PostsLook', params: { id: id }, query: { userID: this.$route.query.userID } });
      },
      postModify(id){
        this.$router.push({ name: 'PostsModify', params: { id: id }, query: { userID: this.$route.query.userID } });
      },
      judgeIsLiked(id){
        let authorID=this.$route.query.userID;
        for(let i=0;i<this.postLikes.length;i++){
          if(this.postLikes[i].authorID===authorID&&this.postLikes[i].postID===id){
            return true;
          }
        }
        return false;
      },
      toggleLike(id){
        let authorID=this.$route.query.userID;
        let flag=0;
        for(let i=0;i<this.postLikes.length;i++){
          if(this.postLikes[i].authorID===authorID&&this.postLikes[i].postID===id){
            flag=1;
            break;
          }
        }
        if(flag===0){
          this.newLikes.id=this.getPostLikesID();
          this.newLikes.authorID=authorID;
          this.newLikes.postID=id;
          this.newLikes.createTime=this.getTime();
          axios.post(`http://localhost:8080/postLikes`,this.newLikes);
        }
        if(flag===1){
          axios.delete(`http://localhost:8080/postLikes/${authorID}/${id}`);
        }
        window.location.reload();
      },
      getPostLikesID() {
        let today = new Date();
        let todayFormatted = today.getFullYear() + String(today.getMonth() + 1).padStart(2, '0') + String(today.getDate()).padStart(2, '0');
        let maxId = 1;
        for(let i=0; i<this.postLikes.length; i++){
          if(this.postLikes[i].id.substring(0, 8)===todayFormatted){
            let id = Number(this.postLikes[i].id.substring(8));
            if (id >= maxId){
              maxId = id+1;
            }
          }
        }
        return todayFormatted + String(maxId).padStart(4, '0');
      },
      getTime() {
        let now = new Date();

        let year = now.getFullYear();
        let month = String(now.getMonth() + 1).padStart(2, '0'); // Months are 0-based
        let day = String(now.getDate()).padStart(2, '0');

        let hours = String(now.getHours()).padStart(2, '0');
        let minutes = String(now.getMinutes()).padStart(2, '0');
        let seconds = String(now.getSeconds()).padStart(2, '0');

        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      },
      Export(){
        function s2ab(s) {
          const buf = new ArrayBuffer(s.length);
          const view = new Uint8Array(buf);
          for (let i = 0; i < s.length; i++) {
            view[i] = s.charCodeAt(i) & 0xFF;
          }
          return buf;
        }
        if (window.confirm("是否导出全部帖子？")) {
          const ws_data = this.bills.map(bills => [
            bills.id,
            bills.title,
            bills.content,
            bills.authorID,
            bills.authorName,
            bills.postTime,
            bills.updateTime,
            bills.status
          ]);

          // 添加标题行
          ws_data.unshift([
            "Post ID",
            "Title",
            "Content",
            "AuthorID",
            "AuthorName",
            "PostedTime",
            "UpdateTime",
            "Status"
          ]);

          // 使用数据构建工作表
          const ws = XLSX.utils.aoa_to_sheet(ws_data);

          // 构建工作簿并添加工作表
          const wb = XLSX.utils.book_new();
          XLSX.utils.book_append_sheet(wb, ws, "PostsData");

          // 生成工作簿的二进制字符串
          const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
          // 将二进制字符串转换为Blob对象

          // 保存生成的Blob对象
          const blob = new Blob([s2ab(wbout)], { type: 'application/octet-stream' });
          // 创建下载链接
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');

          const now = new Date();
          const year = now.getFullYear();
          const month = ('0' + (now.getMonth() + 1)).slice(-2);
          const day = ('0' + now.getDate()).slice(-2);
          const hours = ('0' + now.getHours()).slice(-2);
          const minutes = ('0' + now.getMinutes()).slice(-2);
          const seconds = ('0' + now.getSeconds()).slice(-2);
          const formattedTime = year + month + day + hours + minutes + seconds;

          a.href = url;
          a.download = 'posts_data_'+formattedTime+'.xlsx';
          document.body.appendChild(a);
          a.click();
          setTimeout(function() {
            document.body.removeChild(a);
            window.URL.revokeObjectURL(url);
          }, 0);
        }
      },
      Print(){
        if (window.confirm("是否进行打印预览？")) {
          let html = "<table><thead><tr>";
          // 添加标题行
          const headers = [
            "Post ID",
            "Title",
            "Content",
            "AuthorID",
            "AuthorName",
            "PostedTime",
            "UpdateTime",
            "Status"
          ];
          headers.forEach(header => {
            html += `<th>${header}</th>`;
          });
          html += "</tr></thead><tbody>";

          // 添加数据行
          this.bills.forEach(bills => {
            html += "<tr>";
            html += `<td>${bills.id}</td>`;
            html += `<td>${bills.title}</td>`;
            html += `<td>${bills.content}</td>`;
            html += `<td>${bills.authorID}</td>`;
            html += `<td>${bills.authorName}</td>`;
            html += `<td>${bills.postTime}</td>`;
            html += `<td>${bills.updateTime}</td>`;
            html += `<td>${bills.status}</td>`;
            html += "</tr>";
          });
          html += "</tbody></table>";

          // 新建一个窗口并写入HTML表格
          const printWindow = window.open('', '_blank');
          printWindow.document.write(html);

          // 添加样式以确保打印效果
          printWindow.document.write(`
            <style>
              table {
                width: 100%;
                border-collapse: collapse;
              }
              th, td {
                border: 1px solid black;
                padding: 5px;
                text-align: left;
              }
            </style>
    `     );

          // 添加一个打印按钮
          printWindow.document.write(`
            <button onclick="window.print();">Print</button>
            <button onclick="window.close();">Close</button>
          `);
          // 关闭文档写入，以便可以打开打印对话框
          printWindow.document.close();
        }
      },
      Statistics(){

      },
      search() {
        this.BillsToShow = [];
        for (let i = 0; i < this.bills.length; i++) {
          if (this.bills[i].title.includes(this.searchQuery) ||
              this.bills[i].content.includes(this.searchQuery) ||
              this.bills[i].authorName.includes(this.searchQuery)) {
            this.BillsToShow.push(this.bills[i]);
          }
        }
        if (this.searchQuery === '') {
          this.BillsToShow = this.bills;
        }
      },
    },
  };
  </script>
<style scoped>
.preserve-format {
  white-space: pre-wrap;
}
.textL1 {
  font-size: 20px; /* Adjust to your desired size */
}

.textL3 {
  font-family: "Times New Roman", "Microsoft YaHei", "微软雅黑", sans-serif;
  font-size: 16px;
  line-height: 1.8; /* Single line spacing can vary, but 1.2 is a common value */
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
.text-secondary {
  color: #6c757d; /* Bootstrap的深灰色 */
}
.small-button {
  padding: 0.25rem 0.5rem; /* 小的内边距 */
  font-size: 0.875rem; /* 较小的字体大小 */
  line-height: 1.25; /* 行高 */
  border-radius: 10px; /* 较小的边框圆角 */
}
.input-group{
  width: 120px;
}
.search-sort-wrapper {
  display: flex;
  align-items: center; /* This will vertically center align the items in the flex container */
}
.my-btn-sort {
  background: none;
  border: none;
  cursor: pointer;
  margin-left:5px;
  width: 80px;
  font-size: 12px;
}
.green {
  color: limegreen;
}
</style>
