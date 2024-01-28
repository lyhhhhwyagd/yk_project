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
                    <p class="mb-0">修改天气</p>
                  </div>
                  <div class="card-body">
                    <form role="form" class="text-start" @submit.prevent="handleSubmit">
                      <label>城市</label>
                      <soft-input
                          id="Title"
                          type="text"
                          :placeholder=weather.city
                          name="newTitle"
                          v-model="newWeather.city"
                      />
                      <label>温度</label>
                      <soft-input
                          id="Description"
                          type="text"
                          :placeholder=weather.temperature
                          name="newDescription"
                          v-model="newWeather.temperature"
                      />
                      <label>湿度</label>
                      <soft-input
                          id="Status"
                          type="text"
                          :placeholder=weather.humidity
                          name="newStatus"
                          v-model="newWeather.humidity"
                      />
                      <label>风速</label>
                      <soft-input
                          id="RewardAmount"
                          type="text"
                          :placeholder=weather.windSpeed
                          name="newRewardAmount"
                          v-model="newWeather.windSpeed"
                      />
                      <label>风向</label>
                      <soft-input
                          id="DeadLine"
                          type="text"
                          :placeholder=weather.windDirection
                          name="newDeadLine"
                          v-model="newWeather.windDirection"
                      />
                      <label>天气状况</label>
                      <soft-input
                          id="DeadLine"
                          type="text"
                          :placeholder=weather.weatherCondition
                          name="newDeadLine"
                          v-model="newWeather.weatherCondition"
                      />
                      <label>气压</label>
                      <soft-input
                          id="DeadLine"
                          type="text"
                          :placeholder=weather.pressure
                          name="newDeadLine"
                          v-model="newWeather.pressure"
                      />
                      <label>能见度</label>
                      <soft-input
                          id="DeadLine"
                          type="text"
                          :placeholder=weather.visibility
                          name="newDeadLine"
                          v-model="newWeather.visibility"
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
      weather: [],
      newWeather:{
        city:'',
        temperature:'',
        humidity:'',
        windSpeed:'',
        windDirection:'',
        weatherCondition:'',
        pressure:'',
        visibility:'',
      }
    };
  },
  components: {

    SoftInput,

    SoftButton,
  },
  created() {
    this.getWeatherData();
  },
  methods: {
    handleSubmit(event) {
      event.preventDefault();
      // 执行提交修改的逻辑
      this.submitChanges();
    },
    getWeatherData() {
      console.log("开始访问数据");
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/weather/${id}`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.weather = response.data.data;

              this.newWeather.city=this.weather.city;
              this.newWeather.temperature=this.weather.temperature;
              this.newWeather.humidity=this.weather.humidity;
              this.newWeather.windSpeed=this.weather.windSpeed;
              this.newWeather.windDirection=this.weather.windDirection;
              this.newWeather.weatherCondition=this.weather.weatherCondition;
              this.newWeather.pressure=this.weather.pressure;
              this.newWeather.visibility=this.weather.visibility;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    async submitChanges() {  // 添加一个新的方法来处理提交
      if(this.newWeather.temperature===''||this.newWeather.humidity===''||this.newWeather.windSpeed===''||this.newWeather.windDirection===''
        ||this.newWeather.weatherCondition===''||this.newWeather.pressure===''||this.newWeather.visibility===''){
        window.alert("存在空白信息，请重新输入");
        event.preventDefault();
      }else{
        console.log("正在发送对象");
        const id = this.$route.params.id; // 获取页面的ID
        axios.put(`http://localhost:8080/weather/${id}`,this.newWeather, {
          headers: {
            'Content-Type': 'application/json',
          }
        })
            .then(response => {
              if (response.data.code === 200) {
                console.log('Update successful');
                this.$router.push({ name: '天气组件', query: { userID: this.$route.query.userID } });
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
