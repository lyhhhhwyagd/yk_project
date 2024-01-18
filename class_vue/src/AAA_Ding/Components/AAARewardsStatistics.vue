<template>
  <div class="col-md-12">
    <div class="col-md-1"></div>
    <!-- line chart -->
    <div class="card z-index-2 col-md-10 position-relative" v-if="!loading">
      <gradient-line-chart
          id="chart-line"
          title="Gradient Line Chart"
          description="<i class='fa fa-arrow-up text-success'></i>
      <span class='font-weight-bold'>4% more</span> in 2021"
          :chart="{
              labels: date,
              datasets: [
                {
                  label: '发布的悬赏数',
                  data: RewardsPostedNumber,
                },
                {
                  label: '接受的悬赏数',
                  data: RewardsReceivedNumber,
                },
              ],
            }"
          :key="chartKey"
      />
      <!-- dropdown selection -->
      <div class="form-group position-absolute top-5 end-5">
        <select class="form-control custom-select-size" v-model="selectedDate">
          <option v-for="date in dateSelected" :key="date">{{date}}</option>
        </select>
      </div>
    </div>
  </div>
</template>

<script>
import GradientLineChart from "@/AAA_Ding/Components/GradientLineChart.vue";
import axios from "axios";
import moment from "moment";
export default {
  data(){
    return{
      chartKey:'',
      loading: true,
      rewards:[],
      selectedDate: '7天',
      dateSelected:['7天','15天','30天'],
      date:[],
      RewardsPostedNumber:[],
      RewardsReceivedNumber:[],
    }
  },
  components:{
    GradientLineChart
  },

  created() {
    this.getRewardData().then(() => {
      this.computeRewards();
    });
  },
  watch: {
    selectedDate: {
      handler() {
        this.computeRewards();
      },
      immediate: true, // 确保在组件创建时立即调用处理函数
    },
  },
  methods:{
    async getRewardData() {
      console.log("开始访问数据");

      // 创建一个包含所有axios请求的Promise数组
      const requests = [
        axios.get('http://localhost:8080/reward')
      ];
      // 等待所有请求完成
      const responses = await Promise.all(requests);

      // 根据响应来更新数据
      responses.forEach(response => {
        if (response.data.code === 200) {
          if (response.config.url.endsWith('/reward')) {
            this.rewards = response.data.data;
            console.log(this.rewards)
          }
        }
      });
    },
    computeRewards() {
      this.loading=true;
      let days = parseInt(this.selectedDate);
      this.date = [];
      this.RewardsPostedNumber = [];
      this.RewardsReceivedNumber = [];

      const today = new Date();
      for (let i = 0; i < days; i++) {
        const pastDate = new Date(today);
        pastDate.setDate(pastDate.getDate() - i);

        // 转换日期格式 (e.g., '2024-01-10')
        const dateString = pastDate.toISOString().split('T')[0];
        const newRewardsPostedNumber = this.getNewRewardsPostedNumber(dateString);
        const newRewardsReceivedNumber = this.getNewRewardsReceivedNumber(dateString);
        // 将数据保存到相应的数组中
        this.date.unshift(dateString);
        this.RewardsPostedNumber.unshift(newRewardsPostedNumber);
        this.RewardsReceivedNumber.unshift(newRewardsReceivedNumber);
      }
      this.chartKey = Date.now();
      this.loading=false;
    },
    getNewRewardsPostedNumber(date){
      let num=0;
      for(let i=0;i<this.rewards.length;i++){
        if(this.rewards[i].postedTime!==null){
          if(this.rewards[i].postedTime.includes(date)){
            num+=1;
          }
        }
      }
      return num;
    },
    getNewRewardsReceivedNumber(date){
      let num=0;
      for(let i=0;i<this.rewards.length;i++){
        if(this.rewards[i].receivedTime!==null){
          if(this.rewards[i].receivedTime.includes(date)){
            num+=1;
          }
        }
      }
      return num;
    }
  }
}
</script>

<style scoped>
.position-relative {
  position: relative;
}

.position-absolute {
  position: absolute;
}
  .top-5 {
    top: 5px;
  }

  .end-5 {
    right: 5px;
  }

  .custom-select-size {
    width: 80px;
    height: 40px;
  }
</style>