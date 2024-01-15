<template>
  <div class="col-12 d-flex justify-content-center vh-80">
    <div class="col-md-2"></div>
    <div class="card mb-4 col-md-7">
      <main class="mt-0 main-content main-content-bg">
        <div class="page-header min-vh-75">
          <div class="container">
            <div class="row">
              <div class="mx-auto col-lg-5 d-flex flex-column">
                <div class="card card-plain">
                  <div class="pb-0 card-header text-start">
                    <p class="mb-0">添加新的悬赏</p>
                  </div>
                  <div class="card-body">
                    <form role="form" class="text-start">
                      <label>标题</label>
                      <soft-input
                          id="Title"
                          type="text"

                          name="newTitle"
                          v-model="newReward.title"
                      />
                      <label>描述</label>
                      <soft-input
                          id="Description"
                          type="text"

                          name="newDescription"
                          v-model="newReward.description"
                      />

                      <label>悬赏额度</label>
                      <soft-input
                          id="RewardAmount"
                          type="text"
                          name="newRewardAmount"
                          v-model="newReward.rewardAmount"
                          style="width: 100%;height:40px;"
                      />
                      <div>
                        <label for="DeadLine">截止日期</label>
                        <br>
                        <div class="input-wrapper">
                          <input
                              id="DeadLine"
                              type="datetime-local"
                              name="newDeadLine"
                              v-model="newReward.deadLine"
                              style="width: 100%;height:40px;"
                          />
                        </div>
                      </div>

                      <div class="text-center">
                        <soft-button
                            class="my-4 mb-2"
                            variant="gradient"
                            color="success"
                            full-width
                            @click="submitAdd"
                        >添加新的悬赏
                        </soft-button>
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
    <div class="col-md-3"></div>
  </div>

</template>

<script>
import axios from 'axios';

import SoftInput from "@/AAA_Ding/Components/SoftInput.vue";
import SoftButton from "@/AAA_Ding/Components/SoftButton.vue";

export default {
  name: 'search',
  data() {
    return {
      rewards: [],
      newReward:{
        title: '',
        description: '',
        status: '未领取',
        rewardAmount: '',
        deadLine: '',
        rewardsID: '',
        postedByUserID: '',
        postedByUserName: '',
        postedTime: '',
        receiverID:'',
        receiverName:'',
      }
    };
  },
  components: {
    
    SoftInput,
   
    SoftButton,
  },
  created() {
    this.getRewardData();
    this.getRewardsID();
    this.getPostedTime();
    this.getPostedUser();
  },
  methods: {
    getPostedUser(){
      this.newReward.postedByUserID = this.$route.query.userID;
      axios.get(`http://localhost:8080/loginIn/${this.newReward.postedByUserID}`)
          .then(response => {
            if (response.data.code === 200) {
              console.log('successful');
              console.log(response.data.data);
              this.newReward.postedByUserName = response.data.data.userName;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
    },
    getRewardData() {

    },
    getRewardsID() {
      axios.get('http://localhost:8080/reward')
          .then(response => {
            if (response.data.code === 200) {
              console.log('successful');
              this.rewards = response.data.data;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
      let today = new Date();
      let todayFormatted = today.getFullYear() + String(today.getMonth() + 1).padStart(2, '0') + String(today.getDate()).padStart(2, '0');

      let maxId = 0;
      for(let i=0; i<this.rewards.length; i++){
        if(this.rewards[i].rewardsID.substring(0, 8)===todayFormatted){
          let id = Number(this.rewards[i].rewardsID.substring(8));
          if (id > maxId){
            maxId = id;
          }
        }
      }
      let newRewardsID = todayFormatted + String(maxId + 1).padStart(4, '0');
      return newRewardsID;
    },

    getPostedTime() {
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

    async showDetails(){
      this.newReward.rewardsID=this.getRewardsID();
      this.newReward.postedTime=this.getPostedTime();
      console.log(this.newReward.rewardsID);
      console.log(this.newReward.postedTime);
    },

    submitAdd() {
      this.newReward.rewardsID=this.getRewardsID();
      this.newReward.postedTime=this.getPostedTime();
      console.log("正在发送对象");
      const id = this.$route.params.id; // 获取页面的ID
      axios.post(`http://localhost:8080/reward`,this.newReward, {
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
      this.$router.push({ name: 'Rewards', query: { userID: this.newReward.postedByUserID } });
    },
  },
};
</script>
<style scoped>
.input-wrapper {
  border-radius: 8px;
  border: 1px solid rgb(210, 214, 218);
  outline: none;
}
.input-wrapper input {
  padding: 10px;
  border: none;
  border-radius: 8px;
  outline: none;
}

.input-wrapper:focus {
  border: 3px solid rgb(233, 174, 222);
}
</style>
