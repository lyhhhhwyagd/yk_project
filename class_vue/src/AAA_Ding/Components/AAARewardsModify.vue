<template>
  <div class="col-12 d-flex vh-80">
    <div class="col-md-2"></div>
    <div class="card mb-4 col-md-7">
    <main class="mt-0 main-content main-content-bg">
      <div class="page-header min-vh-75">
        <div class="container">
          <div class="row">
            <div class="mx-auto col-lg-5 d-flex flex-column">
              <div class="card card-plain">
                <div class="pb-0 card-header text-start">  
                  <p class="mb-0">修改你的悬赏信息</p>
                </div>
                <div class="card-body">
                  <form role="form" class="text-start" @submit="handleSubmit">
                    <label>标题</label>
                    <soft-input
                      id="Title"
                      type="text"
                      :placeholder=rewards.title
                      name="newTitle"
                      v-model="newReward.title"
                    />
                    <label>描述</label>
                    <soft-input
                      id="Description"
                      type="text"
                      :placeholder=rewards.description
                      name="newDescription"
                      v-model="newReward.description"
                    />
                    <label>状态</label>
                    <soft-input
                      id="Status"
                      type="text"
                      :placeholder=rewards.status
                      name="newStatus"
                      v-model="newReward.status"
                    />
                    <label>悬赏额度</label>
                    <soft-input
                      id="RewardAmount"
                      type="text"
                      :placeholder=rewards.rewardAmount
                      name="newRewardAmount"
                      v-model="newReward.rewardAmount" 
                    />
                    <label>截止日期</label>
                    <soft-input
                      id="DeadLine"
                      type="text"
                      :placeholder=rewards.deadLine
                      name="newDeadLine"
                      v-model="newReward.deadLine"
                    />

                    <div class="text-center">
                      <soft-button
                        class="my-4 mb-2"
                        variant="gradient"
                        color="success"
                        full-width
                        type="submit"
                      >提交修改
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
        title:'',
        description:'',
        status:'',
        rewardAmount:'',
        deadLine:'',
        receiverID:'',
        receiverName:'',
        receivedTime:'',
      }
    };
  },
  components: {
   
    SoftInput,
    
    SoftButton,
  },
  created() {
    this.getRewardData();
  },
  methods: {
    handleSubmit(event) {
      event.preventDefault();
      // 执行提交修改的逻辑
      this.submitChanges();
    },
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
        this.newReward.receiverID=this.rewards.receiverID;
        this.newReward.receiverName=this.rewards.receiverName;
        this.newReward.receivedTime=this.rewards.receivedTime;

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
      if(this.newReward.title===''||this.newReward.description===''||this.newReward.rewardAmount===''||this.newReward.deadLine===''||this.newReward.status===''){
        window.alert("存在空白信息，请重新输入");
        event.preventDefault();
      }else{
        if(this.newReward.status==='未领取'){
          this.newReward.receiverID='';
          this.newReward.receiverName='';
        }
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
                this.$router.push({ name: '悬赏中心', query: { userID: this.$route.query.userID } });
              } else {
                console.error('Error updating reward: ' + response.data.message);
              }
            })
            .catch(error => {
              console.error(error);
            });
      }
    },
  },
};
</script>
