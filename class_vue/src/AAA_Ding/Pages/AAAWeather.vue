<template>
  <div style="display: flex;">
    <div class="left-container">
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
      <div class="col-lg-4 col-md-6 my-timeline">
        <timeline-list class="h-100 timeline-list-width" title="过去五天">
          <div class="timeline-item-container">
            <timeline-item
                color="success"
                icon="bell-55"
                :title="this.pastFiveWeek[4]"
                :date-time="this.pastFiveDays[4]"
            />
            <div class="additional-container">
              <div class="additional-container">
                <img :src="weatherImage(this.pastFiveDays[4])" alt="Weather Image" style="width: 30px; height: 30px;" />
              </div>
              <div class="additional-container-text">
                <p>{{CityWeatherData_pastFiveDaysTempMin[4]}} ° ~~~~ {{CityWeatherData_pastFiveDaysTempMax[4]}} °</p>
              </div>
            </div>
          </div>

          <div class="timeline-item-container">
            <TimelineItem
                color="danger"
                icon="html5"
                :title="this.pastFiveWeek[3]"
                :date-time="this.pastFiveDays[3]"
            />
            <div class="additional-container">
              <div class="additional-container">
                <img :src="weatherImage(this.pastFiveDays[3])" alt="Weather Image" style="width: 30px; height: 30px;" />
              </div>
              <div class="additional-container-text">
                <p>{{CityWeatherData_pastFiveDaysTempMin[3]}} ° ~~~~ {{CityWeatherData_pastFiveDaysTempMax[3]}} °</p>
              </div>
            </div>
          </div>

          <div class="timeline-item-container">
            <TimelineItem
                color="warning"
                icon="credit-card"
                :title="this.pastFiveWeek[2]"
                :date-time="this.pastFiveDays[2]"
            />
            <div class="additional-container">
              <div class="additional-container">
                <img :src="weatherImage(this.pastFiveDays[2])" alt="Weather Image" style="width: 30px; height: 30px;" />
              </div>
              <div class="additional-container-text">
                <p>{{CityWeatherData_pastFiveDaysTempMin[2]}} ° ~~~~ {{CityWeatherData_pastFiveDaysTempMax[2]}} °</p>
              </div>
            </div>
          </div>

          <div class="timeline-item-container">
            <TimelineItem
                color="primary"
                icon="key-25"
                :title="this.pastFiveWeek[1]"
                :date-time="this.pastFiveDays[1]"
            />
            <div class="additional-container">
              <div class="additional-container">
                <img :src="weatherImage(this.pastFiveDays[1])" alt="Weather Image" style="width: 30px; height: 30px;" />
              </div>
              <div class="additional-container-text">
                <p>{{CityWeatherData_pastFiveDaysTempMin[1]}} ° ~~~~ {{CityWeatherData_pastFiveDaysTempMax[1]}} °</p>
              </div>
            </div>
          </div>

          <div class="timeline-item-container">
            <TimelineItem
                color="info"
                icon="check-bold"
                title="今天"
                :date-time="this.pastFiveDays[0]"
            />
            <div class="additional-container">
              <div class="additional-container">
                <img :src="weatherImage(this.pastFiveDays[0])" alt="Weather Image" style="width: 30px; height: 30px;" />
              </div>
              <div class="additional-container-text">
                <p>{{CityWeatherData_pastFiveDaysTempMin[0]}} ° ~~~~ {{CityWeatherData_pastFiveDaysTempMax[0]}} °</p>
              </div>
            </div>
          </div>
        </timeline-list>
      </div>
    </div>
    <div class="right-container">
      <Amap :amapKey="this.yourAMapKey" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import ProjectsCard from "@/views/components/ProjectsCard.vue";
import TimelineItem from "@/views/components/TimelineItem.vue";
import TimelineList from "@/views/components/TimelineList.vue";
import DualRangeSlider from "@/AAA_Ding/Components/DualRangeSlider.vue";
import Amap from "@/AAA_Ding/Components/Map.vue";


export default {
  components: {Amap, DualRangeSlider, TimelineList, TimelineItem, ProjectsCard},
  data() {
    return {
      map: null,
      selectedCity: '成都',
      dropdownOpen: false,
      cities: ['北京', '上海', '成都', '三亚', '深圳', '杭州'],
      pastFiveDays:[],
      pastFiveWeek:[],
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
      CityWeatherData_pastFiveDaysTempMax:[],
      CityWeatherData_pastFiveDaysTempMin:[],
      yourAMapKey:'be791b6a6dada3c5f7c7c88608826963',
    };
  },

  created(){
    this.getCityWeatherData();
    this.getDateAndWeek();
  },
  methods: {
    toggleDropdown() {
      this.dropdownOpen = !this.dropdownOpen;
    },
    selectCity(city) {
      this.selectedCity = city;
      this.dropdownOpen = false;
      this.getCityWeatherData();
      this.getPastFiveDaysCityWeatherData_MaxAndMin();
    },
    getDateAndWeek() {
      let pastFiveDays = [];
      let pastFiveWeek = [];
      let today = new Date();
      let options = {year: 'numeric', month: 'long', day: 'numeric'};
      pastFiveDays.push(today.toLocaleDateString('zh-CN', options));
      pastFiveWeek.push(today.toLocaleDateString('zh-CN', {weekday: 'long'}));
      for (let i = 1; i <= 4; i++) {
        let pastDay = new Date(today);
        pastDay.setDate(today.getDate() - i); // 计算过去的日期
        pastFiveDays.push(pastDay.toLocaleDateString('zh-CN', options));
        pastFiveWeek.push(pastDay.toLocaleDateString('zh-CN', {weekday: 'long'}));
      }
      this.pastFiveDays=pastFiveDays;
      this.pastFiveWeek=pastFiveWeek;
    },

    getAllWeatherData() {
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/weather`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.allCityWeatherData=response.data.data;
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    getCityWeatherData() {
      const id = this.$route.params.id; // 获取页面的ID
      axios.get(`http://localhost:8080/weather_city/${this.selectedCity}`) // 将ID添加到请求的URL中
          .then(response => {
            if (response.data.code === 200) {
              this.CityWeatherData=response.data.data;
              this.lastestCityWeatherData=this.CityWeatherData[this.CityWeatherData.length-1];
              this.lastestObsTime=this.lastestCityWeatherData.obsTime.substring(0, 16);


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

              this.getPastFiveDaysCityWeatherData_MaxAndMin();
            } else {
              console.error('Error fetching rewards: ' + response.data.message);
            }
          })
          .catch(error => {
            console.error(error);
          });
    },
    getPastFiveDaysCityWeatherData_MaxAndMin() {
      // 假设obsTime是一个可以转换为Date对象的字符串
      // 并且假设温度数据是一个包含温度的数字字段

      // 初始化最大最小温度数组
      this.CityWeatherData_pastFiveDaysTempMax = new Array(5).fill(-Infinity);
      this.CityWeatherData_pastFiveDaysTempMin = new Array(5).fill(Infinity);

      // 获取今天的日期并清除时间部分
      const today = new Date();
      today.setHours(0, 0, 0, 0);

      // 遍历CityWeatherData数组
      this.CityWeatherData.forEach(data => {
        const obsTime = new Date(data.obsTime);
        const temp = data.temperature; // 假设字段名为temperature
        const dayDifference = (today - obsTime) / (1000 * 3600 * 24);

        // 如果数据是在过去五天内
        if (dayDifference >= 0 && dayDifference < 5) {
          const index = Math.floor(dayDifference); // 计算索引

          // 更新最大温度
          if (Number(temp) > Number(this.CityWeatherData_pastFiveDaysTempMax[index])) {
            this.CityWeatherData_pastFiveDaysTempMax[index] = temp;
          }

          // 更新最小温度
          if (Number(temp) < Number(this.CityWeatherData_pastFiveDaysTempMin[index])) {
            this.CityWeatherData_pastFiveDaysTempMin[index] = temp;
          }
        }
      });

      // 处理-Infinity和Infinity的情况
      for (let i = 0; i < 5; i++) {
        if (this.CityWeatherData_pastFiveDaysTempMax[i] === -Infinity) {
          this.CityWeatherData_pastFiveDaysTempMax[i] = null;
        }
        if (this.CityWeatherData_pastFiveDaysTempMin[i] === Infinity) {
          this.CityWeatherData_pastFiveDaysTempMin[i] = null;
        }
      }
    },
    weatherImage(date) {
      const matches = date.match(/(\d{4})年(\d{1,2})月(\d{1,2})日/);
      const year = matches[1];
      const month = matches[2].padStart(2, '0'); // 确保月份有两位数字
      const day = matches[3].padStart(2, '0');   // 确保天数有两位数字
      let dateStr=year+'-'+month+'-'+day;

      // 找到与dateStr匹配的第一个obsTime的索引
      const startIndex = this.CityWeatherData.findIndex(data =>
          data.obsTime.startsWith(dateStr)
      );
      // 检查是否有足够的数据，并获取第24项的WeatherCondition
      const targetIndex = startIndex + 23; // 从0开始计数，所以加23
      let weatherCondition = 'default'; // 默认天气状况
      if (startIndex !== -1 && targetIndex < this.CityWeatherData.length) {
        weatherCondition = this.CityWeatherData[targetIndex].weatherCondition;
      }

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
  },
};
</script>

<style>
.left-container{
  width:380px;
}
.right-container{
  margin-left:20px;
  width: calc(100% - 420px);
  background-color: white;
  border-radius: 15px;
}
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
.my-timeline{
  margin-top: 20px;
  width: 380px;
}
.timeline-list-width {
  width: 380px;
}
.timeline-item-container {
  display: flex;
  align-items: center;
}
.additional-container {
  display: flex;
  align-items: center; /* 这将垂直居中所有子元素 */
  justify-content: center; /* 如果您也想水平居中图片，可以添加这个属性 */
  margin-left: 20px;
  margin-top:-15px;
}
.additional-container-text{
  margin-left: 40px;
}
</style>