<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <default-statistics-card
          title="论坛"
          :count="String(this.posts.length)"
          :percentage="{
          color: 'success',
          label: 'Total number of posts'
        }"
          menu="查看详情"
          @click="goToPostsPage"
      />
      <default-statistics-card
          title="天气"
          :count="this.weather[this.weather.length-1].temperature"
          :percentage="{
          color: 'success',
          label: this.weather[this.weather.length-1].city
        }"
          menu="查看详情"
          @click="goToWeatherPage"
      />
      <default-statistics-card
          title="悬赏"
          :count="String(this.rewards.length)"
          :percentage="{
          color: 'secondary',
          label: 'Total number of rewards'
        }"
          menu="查看详情"
          @click="goToRewardsPage"
      />
    </div>
    <div class="mt-4 row">
      <div class="col-lg-4 col-sm-6">
        <channels-chart-card />
      </div>
      <div class="mt-4 col-lg-8 col-sm-6 mt-sm-0">
        <revenue-chart-card />
      </div>
    </div>
    <div class="mt-4 row">
      <div class="col-lg-8">
        <div class="card h-100">
          <div class="p-3 pb-0 card-header">
            <div class="d-flex justify-content-between">
              <h6 class="mb-0">城市7天平均气温</h6>
            </div>
          </div>
          <div class="p-3 card-body">
            <horizontal-bar-chart
                :chart="{
                labels: ['成都', '北京', '上海', '深圳', '杭州', '重庆'],
                datasets: [
                  {
                    label: 'Average Temperature',
                    data: this.averageTemperature
                  }
                ]
              }"
            />
          </div>
        </div>
      </div>
      <div class="mt-4 col-lg-4 mt-lg-0">
        <div class="card">
          <div class="p-3 pb-0 card-header">
            <div class="d-flex justify-content-between">
              <h6 class="mb-0">即时天气</h6>
            </div>
          </div>
          <div class="p-3 card-body">
            <ul class="list-group list-group-flush list my--3">
              <li
                  v-for="(city,index) in cityWeatherNow"
                  :key="index"
                  class="px-0 border-0 list-group-item"
              >
                <div class="row align-items-center">
                  <div class="col-auto">
                    <!-- Country flag -->
                    <img :src="getWeatherImage(city.weatherCondition)" :style="{ width: '30px', height: '30px' }" alt="Country flag" />
                  </div>
                  <div class="col">
                    <p class="mb-0 text-xs font-weight-bold">城市：</p>
                    <h6 class="mb-0 text-sm">{{ city.city }}</h6>
                  </div>
                  <div class="text-center col">
                    <p class="mb-0 text-xs font-weight-bold">温度：</p>
                    <h6 class="mb-0 text-sm">{{ city.temperature }}°C</h6>
                  </div>
                  <div class="text-center col">
                    <p class="mb-0 text-xs font-weight-bold">天气：</p>
                    <h6 class="mb-0 text-sm">{{ city.weatherCondition }}</h6>
                  </div>
                  <div class="text-center col">
                    <p class="mb-0 text-xs font-weight-bold">{{ city.windDirection }}</p>
                    <h6 class="mb-0 text-sm">m/s</h6>
                  </div>
                  <div class="text-center col">
                    <p class="mb-0 text-xs font-weight-bold">相对湿度：</p>
                    <h6 class="mb-0 text-sm">{{ city.humidity }}%</h6>
                  </div>
                </div>
                <hr
                    v-if="index !== cityWeatherNow.length - 1"
                    class="mt-3 mb-1 horizontal dark"
                />
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <div class="mt-4 row">
      <div class="col-12">
        <orders-list-card
            title="悬赏列表"
            :headers="['悬赏内容', '发起人', '价值', '状态']"
            :lists="products"
        />
      </div>
    </div>
  </div>
</template>

<script>
import ChannelsChartCard from "@/AAA_Ding/Components/ChannelsChartCard.vue";
import RevenueChartCard from "@/AAA_Ding/Components/RevenueChartCard.vue";
import HorizontalBarChart from "@/AAA_Ding/Components/HorizontalBarChart.vue";
import OrdersListCard from "@/AAA_Ding/Components/OrdersListCard.vue";
// images
import axios from "axios";
import setTooltip from "@/assets/js/tooltip.js";
import DefaultStatisticsCard from "@/AAA_Ding/Components/DefaultStatisticsCard.vue";

export default {
  name: "Overview",
  components: {
    ChannelsChartCard,
    RevenueChartCard,
    HorizontalBarChart,
    OrdersListCard,
    DefaultStatisticsCard
  },
  data() {
    return {
      userID:'',
      posts:[],
      weather:[],
      rewards:[],
      averageTemperature:[],
      products: [],
      cityWeatherNow:[],
    };
  },
  mounted() {
    setTooltip(this.$store.state.bootstrap);
  },
  created(){
    this.getAllData();
    this.userID=this.$route.query.userID;
  },
  methods: {
    goToRewardsPage(){
      this.$router.push({ name: 'Rewards', query: { userID: this.userID } });
    },
    goToWeatherPage(){
      this.$router.push({ name: 'Weather', query: { userID: this.userID } });
    },
    goToPostsPage(){
      this.$router.push({ name: 'Posts', query: { userID: this.userID } });
    },
    getAllData() {
      console.log("开始访问数据");
      axios.get(`http://localhost:8080/post`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.posts=response.data.data;
            }
          });
      axios.get('http://localhost:8080/weather')
          .then(response => {
            if (response.data.code === 200) {
              this.weather=response.data.data;
              this.calculateAverageTemperature();
              this.getCityWeatherNow();
            }
          });
      axios.get('http://localhost:8080/reward')
          .then(response => {
            if (response.data.code === 200) {
              this.rewards=response.data.data;
              this.getRewardToProduct();
            }
          });
    },
    calculateAverageTemperature(){
      let length=[];
      for(let i=0;i<6;i++) length[i]=0;
      for(let i=0;i<6;i++) this.averageTemperature[i]=0;
      for(let i=this.weather.length-1;i>=this.weather.length-2500&&i>0;i--){
        if(this.weather[i].city==="成都"){
          length[0]++;
          this.averageTemperature[0]+=Number(this.weather[i].temperature);
        }else if(this.weather[i].city==="北京"){
          length[1]++;
          this.averageTemperature[1]+=Number(this.weather[i].temperature);
        }else if(this.weather[i].city==="上海"){
          length[2]++;
          this.averageTemperature[2]+=Number(this.weather[i].temperature);
        }else if(this.weather[i].city==="深圳"){
          length[3]++;
          this.averageTemperature[3]+=Number(this.weather[i].temperature);
        }else if(this.weather[i].city==="杭州"){
          length[4]++;
          this.averageTemperature[4]+=Number(this.weather[i].temperature);
        }else if(this.weather[i].city==="重庆"){
          length[5]++;
          this.averageTemperature[5]+=Number(this.weather[i].temperature);
        }
      }
      for(let i=0;i<6;i++) this.averageTemperature[i]=this.averageTemperature[i]/length[i];
    },
    getCityWeatherNow(){
      let num=0;
      for(let i=this.weather.length-1;i>=this.weather.length-7;i--){
        if(this.weather[i].city!=="三亚"){
          this.cityWeatherNow.push(this.weather[i]);
          num++;
          if(num===5){
            break;
          }
        }
      }
    },
    getWeatherImage(weatherCondition){
      if(weatherCondition.includes('雨')){
        return require("@/AAA_Ding/images/小雨.png");
      }
      else if(weatherCondition.includes('雪')){
        return require("@/AAA_Ding/images/小雪.png");
      }
      else if(weatherCondition.includes('晴')){
        return require("@/AAA_Ding/images/晴.png");
      }
      else if(weatherCondition.includes('多云')){
        return require("@/AAA_Ding/images/多云.png");
      }
      else{
        return require("@/AAA_Ding/images/阴.png");
      }
    },
    getRewardToProduct(){
      for(let i=0;i<this.rewards.length&&i<5;i++){
        let NewProduction={
          title: '',
          order: '',
          values: [],
          info: '',
          img:'',
          icon:'',
        };
        NewProduction.title=this.rewards[i].title;
        NewProduction.order = this.rewards[i].description.length > 30
            ? this.rewards[i].description.slice(0, 30) + "..."
            : this.rewards[i].description;
        NewProduction.values.push(this.rewards[i].postedByUserName);
        NewProduction.values.push(this.rewards[i].rewardAmount);
        NewProduction.values.push(this.rewards[i].status);
        NewProduction.img=require("../../assets/img/team-2.jpg");
        if(this.rewards[i].status==="已领取"){
          NewProduction.icon="bold-up text-success";
        }else{
          NewProduction.icon="bold-down text-danger";
        }
        this.products.push(NewProduction);
      }
    }
  },
};
</script>
<style>

</style>