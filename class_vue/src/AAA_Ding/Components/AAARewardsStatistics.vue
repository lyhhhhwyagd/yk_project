<template>
  <div class="col-md-12">
    <div class="col-md-1"></div>
    <!-- line chart -->
    <div class="card z-index-2 col-md-10 position-relative">
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
      rewards:[],
      selectedDate: '7天',
      dateSelected:['7天','15天','30天'],
      date:{},
      RewardsPostedNumber:{},
      RewardsReceivedNumber:{},
    }
  },
  components:{
    GradientLineChart
  },
  watch: {
    selectedDate: {
      handler() {
        this.computeRewards();
      },
      immediate: true,  // 确保在组件创建时立即调用处理函数
    },
  },
  created() {
    this.getRewardData();
  },
  methods:{
    getRewardData() {
      console.log("开始访问数据");
      axios.get(`http://localhost:8080/reward`)
          .then(response => {
            if (response.data.code === 200) {
              this.rewards = response.data.data;

              this.computeRewards();
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    computeRewards() {
      let days = parseInt(this.selectedDate);
      this.date = [];
      this.RewardsPostedNumber = [];
      this.RewardsReceivedNumber = [];
      for (let i = 0; i < days; i++) {
        let day = moment().subtract(i, 'days');
        this.date.unshift(day.format('MMDD'));
        this.RewardsPostedNumber.unshift(this.rewards.filter(reward => moment(reward.postedTime).isSame(day, 'day')).length);
        this.RewardsReceivedNumber.unshift(this.rewards.filter(reward => moment(reward.receiveTime).isSame(day, 'day')).length);
      }
      console.log(this.RewardsReceivedNumber);
      console.log(this.RewardsPostedNumber);
    },
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