<template>
  <div class="col-12 d-flex vh-80">
    <div class="col-md-2"></div>
    <div class="card mb-4 col-md-7">
      <main class="mt-0 main-content main-content-bg">
        <div class="page-header min-vh-75">
          <div class="container">
            <div class="row">
              <div class="mx-auto d-flex flex-column">
                <div class="card card-plain">
                  <div class="pb-0 card-header text-start">
                    <p class="mb-0 justify-content-center">查看悬赏信息</p>
                  </div>
                  <div class="card-body">
                    <form role="form" class="text-start">
                      <div class="input-group">
                        <div class="col-md-2"></div>
                        <label class="col-md-2" style="position: relative; top: +16px;">发布人</label>
                        <textarea
                            class="input-element"
                            id="Title"
                            type="text"
                            :placeholder="rewards.postedByUserName"
                            name="newTitle"
                            v-model="newReward.postedByUserName"
                            :readonly="true"
                        />
                      </div>
                      <div class="input-group">
                        <div class="col-md-2"></div>
                        <label class="col-md-2" style="position: relative; top: +16px;">标题</label>
                        <textarea
                            class="input-element"
                            id="Title"
                            type="text"
                            :placeholder="rewards.title"
                            name="newTitle"
                            v-model="newReward.title"
                            :readonly="true"
                        />
                      </div>
                      <div class="input-group">
                        <div class="col-md-2"></div>
                        <label class="col-md-2" style="position: relative; top: +16px;">描述</label>
                        <textarea
                            class="input-element-pro"
                            id="Description"
                            type="text"
                            :placeholder="rewards.description"
                            name="newDescription"
                            v-model="newReward.description"
                            :readonly="true"
                        />
                      </div>
                      <div class="input-group">
                        <div class="col-md-2"></div>
                        <label class="col-md-2" style="position: relative; top: +16px;">状态</label>
                        <textarea
                            class="input-element"
                            id="Status"
                            type="text"
                            :placeholder="rewards.status"
                            name="newStatus"
                            v-model="newReward.status"
                            :readonly="true"
                        />
                      </div>
                      <div class="input-group">
                        <div class="col-md-2"></div>
                        <label class="col-md-2" style="position: relative; top: +16px;">悬赏额度</label>
                        <textarea
                            class="input-element"
                            id="RewardAmount"
                            type="text"
                            :placeholder="rewards.rewardAmount"
                            name="newRewardAmount"
                            v-model="newReward.rewardAmount"
                            :readonly="true"
                        />
                      </div>
                      <div class="input-group">
                        <div class="col-md-2"></div>
                        <label class="col-md-2" style="position: relative; top: +16px;">截止日期</label>
                        <textarea
                            class="input-element"
                            id="DeadLine"
                            type="text"
                            :placeholder="rewards.deadLine"
                            name="newDeadLine"
                            v-model="newReward.deadLine"
                            :readonly="true"
                        />
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import SoftAvatar from "@/components/SoftAvatar.vue";
import SoftBadge from "@/components/SoftBadge.vue";


import SoftInput from "@/components/SoftInput.vue";
import SoftSwitch from "@/components/SoftSwitch.vue";
import SoftButton from "@/components/SoftButton.vue";



export default {
  name: 'search',
  data() {
    return {
      rewards: [],
      newReward:{
        postedByUserName:'',
        title:'',
        description:'',
        status:'',
        rewardAmount:'',
        deadLine:'',
      }
    };
  },
  components: {
    SoftAvatar,
    SoftBadge,
    SoftInput,
    SoftSwitch,
    SoftButton,
  },
  created() {
    this.getRewardData();
  },
  methods: {
    getRewardData() {
      console.log("开始访问数据");
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/reward/${id}`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.rewards = response.data.data;

              this.newReward.title=this.rewards.title;
              this.newReward.description=this.rewards.description;
              this.newReward.status=this.rewards.status;
              this.newReward.rewardAmount=this.rewards.rewardAmount;
              this.newReward.deadLine=this.rewards.deadLine;
              this.newReward.postedByUserName=this.rewards.postedByUserName;

              console.log(this.rewards)
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    async submitChanges() {  // 添加一个新的方法来处理提交
      console.log("正在发送对象");
      const id = this.$route.params.id; // 获取页面的ID
      axios.put(`http://localhost:8080/reward/${id}`,this.newReward, {
        headers: {
          'Content-Type': 'application/json',
        }
      })
          .then(response => {
            if (response.data.code === 200) {
              console.log('Update successful');
            } else {
              console.error('Error updating reward: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
      this.$router.push('/rewards');
    },
  },
};
</script>
<style scoped>
.input-group {
  display: flex;
}
.input-element {
  font-family: "宋体", Arial, sans-serif;
  font-size: 14px;
  align-items: center;
  width: 300px; /* adjust as needed */
  border-radius: 10px;  /* 设置边框为圆角 */
  border-color: #ccc;   /* 设置边框颜色为浅灰色 */
  color: black;
  height: 40px;
  padding-top: 10px;
  padding-left: 10px;
  margin-top: 5px;    /* 设置上边距为 5px */
  margin-bottom: 5px; /* 设置下边距为 5px */
}
.input-element-pro {
  font-family: "宋体", Arial, sans-serif;
  font-size: 14px;
  align-items: center;
  width: 300px; /* adjust as needed */
  border-radius: 10px;  /* 设置边框为圆角 */
  border-color: #ccc;   /* 设置边框颜色为浅灰色 */
  color: black;
  height: 120px;
  padding-top: 10px;
  padding-left: 10px;
  margin-top: 5px;    /* 设置上边距为 5px */
  margin-bottom: 5px; /* 设置下边距为 5px */
}
.container {
  margin: auto;
  text-align: center;
}
</style>
