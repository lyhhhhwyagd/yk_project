<template>
  <div class="card mb-4 col-md-12">
    <div class="col-12 card-header pb-0 d-flex justify-content-between align-items-center">
      <h6 class="col-md-1">悬赏管理</h6>
      <div class="d-flex ms-2">
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
      <div class="d-flex">
        <div class="input-group search-box">
          <span class="input-group-text"><i class="fas fa-search"></i></span>
          <input
              type="text"
              class="form-control"
              placeholder="搜索悬赏"
              v-model="searchQuery"
              @keyup.enter="search"
          />
        </div>
        <soft-button color="dark" variant="gradient" @click="goToAddRewardPage" class="col-md-5 ms-2">
          <i class="fas fa-plus me-2"></i>
          添加悬赏
        </soft-button>
      </div>
    </div>
    <div class="card-body px-0 pt-0 pb-2">
      <div class="table-responsive p-0">
        <table class="table align-items-center mb-0">
          <thead>
            <tr>
              <th class="checkbox-align text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">
                <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" class="checkbox">
              </th>
              <th class="col-md-2 text-secondary text-xxs font-weight-bolder opacity-7">
                发布者
              </th>
              <th class="col-md-3 text-uppercase text-secondary text-xxs font-weight-bolder opacity-7 ps-2">
                悬赏内容
              </th>
              <th class="col-md-1 text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                状态
              </th>
              <th class="col-md-2 text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                发布时间
                <button class="btn-sort" @click="toggleArrow_postedTime">
                  <span class="arrow arrow-up" v-bind:class="{ 'green': arrowState_postedTime === 1 }">&#9650;</span>
                  <span class="arrow arrow-down" v-bind:class="{ 'green': arrowState_postedTime === 2 }">&#9660;</span>
                </button>
              </th>
              <th class="col-md-2 text-center text-uppercase text-secondary text-xxs font-weight-bolder opacity-7">
                截至时间
                <button class="btn-sort" @click="toggleArrow_deadLine">
                  <span class="arrow arrow-up" v-bind:class="{ 'green': arrowState_deadLine === 1 }">&#9650;</span>
                  <span class="arrow arrow-down" v-bind:class="{ 'green': arrowState_deadLine === 2 }">&#9660;</span>
                </button>
              </th>
              <th class="col-md-1 text-secondary opacity-7"></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="reward in rewards" :key="reward.rewardsID">
              <td class="checkbox-align">
                <input type="checkbox" v-model="reward.selected" class="checkbox" @change="toggleReward(reward)">
              </td>
              <td>
                <div class="d-flex px-2 py-1">
                  <div>
                    <soft-avatar
                      :img="img1"
                      size="sm"
                      border-radius="lg"
                      class="me-3"
                      alt="user1"
                    />
                  </div>
                  <div class="d-flex flex-column justify-content-center">
                    <h6 class="mb-0 text-sm">{{ reward.postedByUserName }}</h6>
                  </div>
                </div>
              </td>
              <td>
                <router-link :to="{ path: `/rewards/look/${reward.rewardsID}`, query: { userID: this.userID } }" class="text-xs font-weight-bold mb-0">
                  {{ reward.title }}
                </router-link>
                <p class="text-xs text-secondary mb-0">{{ truncateDescription(reward.description) }}</p>
              </td>
              <td class="align-middle text-center text-sm">
                <soft-badge :color="reward.status === '未领取' ? 'dark' : 'success'" variant="gradient" size="sm" @click="showPopup(reward)">{{ reward.status }}</soft-badge>
              </td>
              <td class="align-middle text-center">
                <span class="text-secondary text-xs font-weight-bold">{{ reward.postedTime }}</span>
              </td>
              <td class="align-middle text-center">
                <span class="text-secondary text-xs font-weight-bold">{{ reward.deadLine }}</span>
              </td>
              <td class="align-middle">
                <router-link
                    :to="{ path: `/rewards/modify/${reward.rewardsID}`, query: { userID: this.userID } }"
                    class="text-secondary font-weight-bold text-xs"
                    data-toggle="tooltip"
                    data-original-title="Edit user"
                >
                  修改
                </router-link>
                <a href="javascript:;" class="text-secondary font-weight-bold text-xs" data-toggle="tooltip" data-original-title="Edit user" @click="openDeleteConfirm(reward.rewardsID)">删除 </a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import * as XLSX from 'xlsx';
//npm install xlsx;
//npm install axios;
import SoftAvatar from "@/AAA_Ding/Components/SoftAvatar.vue";
import SoftBadge from "@/AAA_Ding/Components/SoftBadge.vue";
import img1 from "../../assets/img/team-2.jpg";
import SoftButton from "@/AAA_Ding/Components/SoftButton.vue";

export default {
  name: "authors-table",
  data() {
    return {
      img1,
      userID:'',
      userName:'',
      rewards: [],
      rewardsAll:[],
      rewardsSelected:[],
      rewardsSearched:[],
      searchQuery: '',
      selectAll:false,
      arrowState_postedTime: 0,
      arrowState_deadLine: 0,
    };
  },
  components: {
    SoftAvatar,
    SoftBadge,
    SoftButton,
  },
  created() {
    this.getUserData();
    this.getRewardData();
  },
  methods: {
    toggleSelectAll() {
      this.rewardsSelected=[];
      this.rewards.forEach(reward => {
        reward.selected = this.selectAll;
        this.toggleReward(reward);
      });
    },
    toggleReward(reward){
      if (reward.selected) {
        this.rewardsSelected.push(reward);
      } else {
        const index = this.rewardsSelected.findIndex(item => item.name === reward.name);
        if (index !== -1) {
          this.rewardsSelected.splice(index, 1);
        }
      }
      console.log(this.rewardsSelected);
    },
    toggleArrow_postedTime() {
      this.arrowState_postedTime = (this.arrowState_postedTime + 1) % 3;
      if(this.arrowState_postedTime!==0){
        this.arrowState_deadLine=0;
      }
      this.sortByPostTime();
    },
    toggleArrow_deadLine(){
      this.arrowState_deadLine = (this.arrowState_deadLine + 1) % 3;
      if(this.arrowState_deadLine!==0){
        this.arrowState_postedTime=0;
      }
      this.sortByDeadLine();
    },
    search(){
      console.log(this.searchQuery);
      if(this.searchQuery===""){
        this.rewards=this.rewardsAll;
      }
      else{
        this.rewardsSearched=[];
        let flag=0;
        for(let i=0;i<this.rewardsAll.length;i++){
          if(this.rewardsAll[i].title.indexOf(this.searchQuery)!==-1||
              this.rewardsAll[i].postedByUserName.indexOf(this.searchQuery)!==-1||
              this.rewardsAll[i].description.indexOf(this.searchQuery)!==-1||
              this.rewardsAll[i].status.indexOf(this.searchQuery)!==-1
          ){
            this.rewardsSearched.push(this.rewardsAll[i]);
          }
        }
        this.rewards=this.rewardsSearched;
      }
    },
    Export() {
      if (this.rewardsSelected.length===0){
        window.confirm("未选择任何信息！");
      }else{
        if (window.confirm("是否导出？")) {
          this.ExportToExcel();
        }
      }
    },
    ExportToExcel() {
      // 创建工作表数据数组
      const ws_data = this.rewardsSelected.map(reward => [
        reward.rewardsID,
        reward.postedByUserID,
        reward.postedByUserName,
        reward.postedTime,
        reward.title,
        reward.description,
        reward.status,
        reward.receiverID,
        reward.receiverName,
        reward.receivedTime,
        reward.deadLine
      ]);

      // 添加标题行
      ws_data.unshift([
        "Rewards ID",
        "Posted By User ID",
        "Posted By User Name",
        "Create Time",
        "Title",
        "Description",
        "Status",
        "Receiver ID",
        "Receiver Name",
        "Deadline"
      ]);

      // 使用数据构建工作表
      const ws = XLSX.utils.aoa_to_sheet(ws_data);

      // 构建工作簿并添加工作表
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "RewardsData");

      // 生成工作簿的二进制字符串
      const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
      // 将二进制字符串转换为Blob对象
      function s2ab(s) {
        const buf = new ArrayBuffer(s.length);
        const view = new Uint8Array(buf);
        for (let i = 0; i < s.length; i++) {
          view[i] = s.charCodeAt(i) & 0xFF;
        }
        return buf;
      }
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
      a.download = 'rewards_data_'+formattedTime+'.xlsx';
      document.body.appendChild(a);
      a.click();
      setTimeout(function() {
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      }, 0);
    },
    Print() {
      if (this.rewardsSelected.length===0){
        window.confirm("未选择任何信息！");
      }else{
        if (window.confirm("是否进行打印预览？")) {
          this.PrintExcel();
        }
      }
    },
    PrintExcel() {
      // 构建HTML表格的数据字符串
      let html = "<table><thead><tr>";
      // 添加标题行
      const headers = [
        "Rewards ID",
        "Posted By User ID",
        "Posted By User Name",
        "Create Time",
        "Title",
        "Description",
        "Status",
        "Receiver ID",
        "Receiver Name",
        "Deadline"
      ];
      headers.forEach(header => {
        html += `<th>${header}</th>`;
      });
      html += "</tr></thead><tbody>";

      // 添加数据行
      this.rewardsSelected.forEach(reward => {
        html += "<tr>";
        html += `<td>${reward.rewardsID}</td>`;
        html += `<td>${reward.postedByUserID}</td>`;
        html += `<td>${reward.postedByUserName}</td>`;
        html += `<td>${reward.postedTime}</td>`;
        html += `<td>${reward.title}</td>`;
        html += `<td>${reward.description}</td>`;
        html += `<td>${reward.status}</td>`;
        html += `<td>${reward.receiverID}</td>`;
        html += `<td>${reward.receiverName}</td>`;
        html += `<td>${reward.deadLine}</td>`;
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
    `);

      // 添加一个打印按钮
      printWindow.document.write(`
      <button onclick="window.print();">Print</button>
      <button onclick="window.close();">Close</button>
    `);

      // 关闭文档写入，以便可以打开打印对话框
      printWindow.document.close();
    },
    Statistics(){
      this.$router.push({ name: 'RewardsStatistics', query: { userID: this.userID } });
    },
    sortByPostTime(){
      if(this.arrowState_postedTime===0){
        this.rewards=this.rewardsSearched;
      }
      if(this.arrowState_postedTime===1){
        this.rewards = this.rewardsSearched.sort((a, b) => {
          // convert postedTime to Date object
          const dateA = new Date(a.postedTime);
          const dateB = new Date(b.postedTime);

          // compare dates
          return dateA - dateB;
        });
      }
      if(this.arrowState_postedTime===2){
        this.rewards = this.rewardsSearched.sort((a, b) => {
          // convert postedTime to Date object
          const dateA = new Date(a.postedTime);
          const dateB = new Date(b.postedTime);

          // compare dates
          return dateB - dateA;
        });
      }
    },
    sortByDeadLine(){
      if(this.arrowState_deadLine===0){
        this.rewards=this.rewardsSearched;
      }
      if(this.arrowState_deadLine===1){
        this.rewards = this.rewardsSearched.sort((a, b) => {
          // convert postedTime to Date object
          const dateA = new Date(a.postedTime);
          const dateB = new Date(b.postedTime);

          // compare dates
          return dateA - dateB;
        });
      }
      if(this.arrowState_deadLine===2){
        this.rewards = this.rewardsSearched.sort((a, b) => {
          // convert postedTime to Date object
          const dateA = new Date(a.postedTime);
          const dateB = new Date(b.postedTime);

          // compare dates
          return dateB - dateA;
        });
      }
    },
    getUserData(){
      this.userID = this.$route.query.userID;
      axios.get(`http://localhost:8080/loginIn/${this.userID}`)
          .then(response => {
            if (response.data.code === 200) {
              console.log('successful');
              console.log(response.data.data);
              this.userName = response.data.data.userName;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
    },
    getRewardData() {
      console.log("开始访问数据");
      axios.get('http://localhost:8080/reward')
        .then(response => {
          if (response.data.code === 200) {
            this.rewardsAll = response.data.data;
            console.log(this.rewardsAll);
            this.rewardsSearched=this.rewardsAll;
            this.rewards=this.rewardsAll;
          } else {
            console.error('Error fetching rewards: ' + response.data.message);
          }
        })
        .catch(error => {
          console.error(error);
        });
    },
    showPopup(reward) {
      if (reward.status === '未领取') {
        if (window.confirm("是否领取？")) {
          this.askRewards(reward);
        }
      }
    },
    getReceivedTime() {
      let now = new Date();

      let year = now.getFullYear();
      let month = String(now.getMonth() + 1).padStart(2, '0'); // Months are 0-based
      let day = String(now.getDate()).padStart(2, '0');

      let hours = String(now.getHours()).padStart(2, '0');
      let minutes = String(now.getMinutes()).padStart(2, '0');
      let seconds = String(now.getSeconds()).padStart(2, '0');

      let timeNow = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
      return timeNow;
    },
    askRewards(reward){
      this.getUserData();
      console.log("正在发送对象");
      const id = reward.rewardsID; // 获取页面的ID
      reward.receiverID=this.userID;
      reward.receiverName=this.userName;
      reward.receivedTime=this.getReceivedTime();
      reward.status="已领取";
      axios.put(`http://localhost:8080/reward/${id}`,reward)
      .then(response => {
        if (response.data.code === 200) {
          console.log('领取成功');
        } else {
          console.error('Error updating reward: ' + response.data.message);
        }
      })
      .catch(error => {
        console.error(error);
      });
    },
    goToAddRewardPage() {
      this.$router.push({ name: 'RewardsAdd', query: { userID: this.userID } });
    },
    openDeleteConfirm(rewardsID) {
      if (window.confirm("是否删除？")) {
        this.deleteItem(rewardsID);
      }
    },
    async deleteItem(rewardsID) {
      console.log("开始删除");
      const id = rewardsID;
      try {
        const response = await axios.delete(`http://localhost:8080/reward/${id}`);
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
    truncateDescription(description) {
      return description.length > 30
          ? description.substring(0, 20) + '...'
          : description;
    },
  },
};
</script>
<style scoped>
.small-button {
  font-size: 12px;
  padding: 2px 8px; /* You may need to adjust this value based on your design */
}
.checkbox-align {
  text-align: left;
  vertical-align: middle;
  padding-right: 5px; /* or use margin-right: 15px; */
}
.checkbox {
  width: 12px;
  height: 12px;
  margin-left: 15px;
}
.btn-sort {
  background: none;
  border: none;
  cursor: pointer;
}
.green {
  color: limegreen;
}
</style>
