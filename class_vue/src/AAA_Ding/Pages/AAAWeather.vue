<template>
  <div class="rounded-rectangle">
    <div class="city-container">
      <div class="city-display">{{ selectedCity }}</div>
      <button @click="toggleDropdown" class="dropbtn">切换</button>
      <div class="obs-time">{{ this.lastestObsTime }}</div>
    </div>
    <div v-if="dropdownOpen" class="dropdown-content">
      <a href="#" v-for="city in cities" :key="city" @click="selectCity(city)">{{ city }}</a>
    </div>

    <div class="weather-info-container">
      <div class="weather-condition-image">
        <img v-if="WeatherData_Now.WeatherCondition === '晴'" src="../images/晴.png" alt="晴">
        <img v-else-if="WeatherData_Now.WeatherCondition === '多云'" src="../images/多云.png" alt="多云">
        <img v-else-if="WeatherData_Now.WeatherCondition.includes('雨')" src="../images/小雨.png" alt="小雨">
        <img v-else-if="WeatherData_Now.WeatherCondition.includes('雪')" src="../images/小雪.png" alt="小雪">
        <img v-else src="../images/阴.png" alt="阴">
      </div>
      <div class="weather-condition-text">
        <p>{{ WeatherData_Now.Temperature }}°</p>
        <p>{{ WeatherData_Now.WeatherCondition }}</p>
      </div>
    </div>


    <div class="gray-rectangle">
      <div class="current-basic d-flex justify-content-between align-items-center">
        <a href="#" class="current-basic___item">
          <p>{{this.WeatherData_Now.WindSpeed}}米/秒</p>
          <p>{{this.WeatherData_Now.WindDirection}}</p>
        </a>
        <a href="#" class="current-basic___item">
          <p>{{this.WeatherData_Now.Humidity}}%</p>
          <p>相对湿度</p>
        </a>
        <a href="#" class="current-basic___item">
          <p>{{this.WeatherData_Now.Visibility}}千米</p>
          <p>能见度</p>
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      selectedCity: '北京',
      dropdownOpen: false,
      cities: ['北京', '上海', '成都', '三亚', '深圳', '杭州'],
      allCityWeatherData:[],
      CityWeatherData:[],
      lastestCityWeatherData:[],
      WeatherData_Now: {
        id:'',
        City:'',
        obsTime:'',
        Temperature:'',
        Humidity:'',
        WindSpeed:'',
        WindDirection:'',
        WeatherCondition:'',
        Pressure:'',
        Visibility:'',
      },
      lastestObsTime:'',
    };
  },
  created(){
    this.getCityWeatherData();
  },
  methods: {
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    selectCity(city) {
      this.selectedCity = city;
      this.dropdownOpen = false;
    },


    getAllWeatherData() {
      console.log("开始访问数据");
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/weather`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.allCityWeatherData=response.data.data;
              console.log(this.allCityWeatherData);
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    getCityWeatherData() {
      console.log("开始访问数据");
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/weather_city/${this.selectedCity}`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.CityWeatherData=response.data.data;
              this.lastestCityWeatherData=this.CityWeatherData[this.CityWeatherData.length-1];
              this.lastestObsTime=this.lastestCityWeatherData.obsTime.substring(0, 16);
              console.log(this.lastestCityWeatherData);

              this.WeatherData_Now.id=this.lastestCityWeatherData.id;
              this.WeatherData_Now.City=this.lastestCityWeatherData.city;
              this.WeatherData_Now.obsTime=this.lastestCityWeatherData.obsTime;
              this.WeatherData_Now.Temperature=this.lastestCityWeatherData.temperature;
              this.WeatherData_Now.Humidity=this.lastestCityWeatherData.humidity;
              this.WeatherData_Now.WindSpeed=this.lastestCityWeatherData.windSpeed;
              this.WeatherData_Now.WindDirection=this.lastestCityWeatherData.windDirection;
              this.WeatherData_Now.WeatherCondition=this.lastestCityWeatherData.weatherCondition;
              this.WeatherData_Now.Pressure=this.lastestCityWeatherData.pressure;
              this.WeatherData_Now.Visibility=this.lastestCityWeatherData.visibility;

              console.log(this.WeatherData_Now);
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
  },
};
</script>

<style>
.rounded-rectangle {
  background-image: url('../images/weather_background.png');
  background-size: cover; /* 覆盖整个容器 */
  background-repeat: no-repeat; /* 避免背景重复 */
  background-position: center; /* 使背景图片居中 */
  width: 380px;
  height: 386px;
  background-color: white;
  border-radius: 15px;
  position: relative;
  padding: 10px;
  box-sizing: border-box;
}

.city-container {
  margin-top: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.city-display {
  font-size: 13.5pt;
  color: black;
  font-family: 'Segoe UI', sans-serif;
  /* 根据需要调整右边距，以便为按钮留出空间 */
  margin-left: 10px;
}

.dropbtn {
  background: none;
  border: none;
  color: rgb(128, 128, 128);
  text-decoration: underline; /* 添加下划线 */
  cursor: pointer;
  padding: 0;
  font-family: 'Segoe UI', sans-serif;
  font-size: 10pt;
  margin-left: 5px;
}
.obs-time{
  margin-left: auto;
  font-size: 10pt;
  color: rgb(128, 128, 128);
}

.weather-condition-image img {
  display: block;
  margin-left: 50px; /* 水平居中 */
  margin-top: 50px;
  width: 160px;  /* 设置图片宽度为 20px */
  height: 160px;
  /* 更多样式，根据需要进行设置 */
}
.weather-info-container {
  display: flex;
  align-items: center; /* 垂直居中 */
}
.weather-condition-text {
  /* 如果需要，可以添加您的样式，例如设置左边距 */
  margin-left: 10px; /* 或者您想要的任何间距 */
  color:white;
}
.weather-condition-text p:first-child {
  margin-top: 50px;
  font-size: 40pt; /* 设置第一个 <p> (即温度) 的字体大小为 100pt */
  font-weight: bold; /* 加粗字体 */
}

.weather-condition-text p:last-child {
  font-size: 15pt; /* 设置第二个 <p> (即天气条件) 的字体大小为 80pt */
  font-weight: normal; /* 默认字体权重，可以不写这行 */
  margin-top: -30px;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
  top: 100%; /* 定位在切换按钮的正下方 */
  right: 10px;
}

.dropdown-content a {
  color: black;
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

.dropdown-content a:hover {
  background-color: #f1f1f1;
}

.dropdown-content {
  display: block;
}

.gray-rectangle {
  background-color: rgb(183, 194, 206);
  border-radius: 15px;
  position: absolute;
  bottom: 20px;
  left: 20px;
  right: 20px;
  height: 70px;
}
.current-basic___item p {
  margin-right: 25px;
  margin-left: 25px;
}

.current-basic___item p:first-child {
  color: black;
  margin-top: 10px;
  font-family: 'Microsoft YaHei', '微软雅黑', sans-serif;
}
.current-basic___item p:last-child {
  color: rgb(87, 92, 100);
  margin-top: -20px;
}

</style>