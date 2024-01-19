<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <default-statistics-card
          title="论坛"
          :count="String(this.posts.length)"
          :percentage="{
          color: 'success',
          value: '+55%',
          label: 'since last month'
        }"
          menu="查看详情"
          @click="goToPostsPage"
      />
      <default-statistics-card
          title="天气"
          count="3.200"
          :percentage="{
          color: 'success',
          value: '+12%',
          label: 'since last month'
        }"
          menu="查看详情"
          @click="goToWeatherPage"
      />
      <default-statistics-card
          title="悬赏"
          :count="String(this.rewards.length)"
          :percentage="{
          color: 'secondary',
          value: '+$213',
          label: 'since last month'
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
              <h6 class="mb-0">Sales by Age</h6>
            </div>
          </div>
          <div class="p-3 card-body">
            <horizontal-bar-chart
                :chart="{
                labels: ['16-20', '21-25', '26-30', '31-36', '36-42', '42+'],
                datasets: [
                  {
                    label: 'Sales by age',
                    data: [15, 20, 12, 60, 20, 15]
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
              <h6 class="mb-0">Sales by Country</h6>
            </div>
          </div>
          <div class="p-3 card-body">
            <ul class="list-group list-group-flush list my--3">
              <li
                  v-for="(sale, index) in sales"
                  :key="index"
                  class="px-0 border-0 list-group-item"
              >
                <div class="row align-items-center">
                  <div class="col-auto">
                    <!-- Country flag -->
                    <img :src="sale.flag" alt="Country flag" />
                  </div>
                  <div class="col">
                    <p class="mb-0 text-xs font-weight-bold">Country:</p>
                    <h6 class="mb-0 text-sm">{{ sale.country }}</h6>
                  </div>
                  <div class="text-center col">
                    <p class="mb-0 text-xs font-weight-bold">Sales:</p>
                    <h6 class="mb-0 text-sm">{{ sale.sales }}</h6>
                  </div>
                  <div class="text-center col">
                    <p class="mb-0 text-xs font-weight-bold">Bounce:</p>
                    <h6 class="mb-0 text-sm">{{ sale.bounce }}</h6>
                  </div>
                </div>
                <hr
                    v-if="index !== sales.length - 1"
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
            title="Top Selling Products"
            :headers="['Product', 'Value', 'Ads Spent', 'Refunds']"
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
import US from "@/assets/img/icons/flags/US.png";
import DE from "@/assets/img/icons/flags/DE.png";
import GB from "@/assets/img/icons/flags/GB.png";
import BR from "@/assets/img/icons/flags/BR.png";
import AU from "@/assets/img/icons/flags/AU.png";
import setTooltip from "@/assets/js/tooltip.js";
import DefaultStatisticsCard from "@/AAA_Ding/Components/DefaultStatisticsCard.vue";
import axios from "axios";

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
      products: [
        {
          title: "Nike v22 Running",
          order: "12.821",
          values: ["$130.992", "$9.500", "13"],
          info: "Refund rate is lower with 97% than other products",
          img:
              "https://raw.githubusercontent.com/creativetimofficial/public-assets/master/soft-ui-design-system/assets/img/ecommerce/blue-shoe.jpg",
          icon: "bold-down text-success"
        },
        {
          title: "Business Kit (Mug + Notebook)",
          order: "8.232",
          values: ["$80.250", "$4.200", "40"],
          img:
              "https://raw.githubusercontent.com/creativetimofficial/public-assets/master/soft-ui-design-system/assets/img/ecommerce/black-mug.jpg",
          icon: "bold-down text-success"
        },
        {
          title: "Black Chair",
          order: "2.421",
          values: ["$40.600", "$9.430", "54"],
          img:
              "https://raw.githubusercontent.com/creativetimofficial/public-assets/master/soft-ui-design-system/assets/img/ecommerce/black-chair.jpg",
          icon: "bold-up text-danger"
        },
        {
          title: "Wireless Charger",
          order: "5.921",
          values: ["$91.300", "$7.364", "5"],
          img:
              "https://raw.githubusercontent.com/creativetimofficial/public-assets/master/soft-ui-design-system/assets/img/ecommerce/bang-sound.jpg",
          icon: "bold-down text-success"
        },
        {
          title: "Mountain Trip Kit (Camera + Backpack)",
          order: "921",
          values: ["$140.925", "$20.531", "121"],
          info: "Refund rate is higher with 70% than other products",
          img:
              "https://raw.githubusercontent.com/creativetimofficial/public-assets/master/soft-ui-design-system/assets/img/ecommerce/photo-tools.jpg",
          icon: "bold-up text-danger"
        }
      ],
      sales: [
        {
          country: "United States",
          sales: 2500,
          bounce: "29.9%",
          flag: US
        },
        {
          country: "Germany",
          sales: "3.900",
          bounce: "40.22%",
          flag: DE
        },
        {
          country: "Great Britain",
          sales: "1.400",
          bounce: "23.44%",
          flag: GB
        },
        {
          country: "Brasil",
          sales: "562",
          bounce: "32.14%",
          flag: BR
        },
        {
          country: "Australia",
          sales: "400",
          bounce: "56.83%",
          flag: AU
        }
      ]
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
    async getAllData() {
      console.log("开始访问数据");

      // 创建一个包含所有axios请求的Promise数组
      const requests = [
        axios.get('http://localhost:8080/post'),
        axios.get('http://localhost:8080/weather'),
        axios.get('http://localhost:8080/reward')
      ];

      // 等待所有请求完成
      const responses = await Promise.all(requests);

      // 根据响应来更新数据
      responses.forEach(response => {
        if (response.data.code === 200) {
          if (response.config.url.endsWith('/post')) {
            this.posts = response.data.data;
          } else if (response.config.url.endsWith('/weather')) {
            this.weather = response.data.data;
          } else if (response.config.url.endsWith('/reward')) {
            this.rewards = response.data.data;
          }
        }
      });
    },
  },
};
</script>
