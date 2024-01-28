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
      <div class="row">
        <div class="col-12">
          <div class="card">
            <!-- Card header -->
            <div class="pb-0 card-header">
              <div class="d-lg-flex align-items-center justify-content-between">
                <div>
                  <h5 class="mb-0">城市天气记录</h5>
                  <p class="mb-0 text-sm">
                    include weather condition, temperature, wind, visibility and so on.
                  </p>
                </div>
                <div class="d-flex flex-row align-items-center mt-4">
                  <div class="input-group search-box">
                    <span class="input-group-text"><i class="fas fa-search"></i></span>
                    <input
                        type="text"
                        class="form-control"
                        placeholder="搜索城市"
                        v-model="searchQuery"
                        @keyup.enter="search"
                    />
                  </div>
                  <div>
                    <button
                        type="button"
                        class="mx-1 mb-0 btn btn-outline-success btn-sm"
                        data-bs-toggle="modal"
                        data-bs-target="#import"
                        @click="Export"
                    >
                      Export
                    </button>
                  </div>
                  <div>
                    <button
                        class="mx-1 mb-0 btn btn-outline-success btn-sm"
                        data-type="csv"
                        type="button"
                        name="button"
                        @click="Print"
                    >
                      Print
                    </button>
                  </div>
                </div>
              </div>
            </div>
            <div class="px-0 pb-0 card-body">
              <div class="table-responsive">
                <table id="products-list" class="table table-flush">
                  <thead class="thead-light">
                  <tr>
                    <th class="col-md-1 align-middle">
                      <div class="d-flex">
                        <div class="my-auto form-check my-form-check">
                          <input
                              id="customCheck23"
                              class="form-check-input"
                              type="checkbox"
                              v-model="selectAll"
                              @change="toggleSelectAll"
                          />
                        </div>
                      </div>
                    </th>
                    <th class="col-md-2">城市</th>
                    <th class="col-md-2 align-middle text-center">温度(°C)</th>
                    <th class="col-md-1 align-middle text-center">风速(m/s)</th>
                    <th class="col-md-1 align-middle text-center">风向</th>
                    <th class="col-md-1 align-middle text-center">湿度(%)</th>
                    <th class="col-md-1 align-middle text-center">能见度(km)</th>
                    <th class="col-md-2 align-middle text-center">
                      采集时间
                      <button class="btn-sort text-uppercase text-secondary text-xxs font-weight-bolder opacity-7" @click="toggleArrow_obsTime">
                        <span class="arrow arrow-up" v-bind:class="{ 'green': arrowState_obsTime === 2 }">&#9650;</span>
                        <span class="arrow arrow-down" v-bind:class="{ 'green': arrowState_obsTime === 1 }">&#9660;</span>
                      </button>
                    </th>
                    <th class="col-md-1 align-middle text-center"></th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="data in limitedWeatherData" :key="data.id">
                    <td class="col-md-1 align-middle text-center">
                      <div class="d-flex">
                        <div class="my-auto form-check my-form-check">
                          <input
                              id="customCheck23"
                              class="form-check-input"
                              type="checkbox"
                              v-model="data.selected"
                              @change="toggleReward(data)"
                          />
                        </div>
                      </div>
                    </td>
                    <td>
                      <div class="d-flex px-2 py-1">
                        <div>
                          <soft-avatar
                              :img="this.weatherImage2(data.weatherCondition)"
                              size="sm"
                              border-radius="lg"
                              class="me-3"
                              alt="user1"
                          />
                        </div>
                        <div class="d-flex flex-column justify-content-center">
                          <h6 class="mb-0 text-sm">{{ data.city }}</h6>
                        </div>
                      </div>
                    </td>
                    <td class="align-middle text-center">
                      <h6 class="mb-0 text-sm">{{ data.temperature }}</h6>
                    </td>
                    <td class="align-middle text-center">
                      <h6 class="mb-0 text-sm">{{ data.windSpeed }}</h6>
                    </td>
                    <td class="align-middle text-center">
                      <h6 class="mb-0 text-sm">{{ data.windDirection }}</h6>
                    </td>
                    <td class="align-middle text-center">
                      <h6 class="mb-0 text-sm">{{ data.humidity }}</h6>
                    </td>
                    <td class="align-middle text-center">
                      <h6 class="mb-0 text-sm">{{ data.visibility }}</h6>
                    </td>
                    <td class="align-middle text-center">
                      <span class="text-secondary text-xs font-weight-bold">{{ data.obsTime }}</span>
                    </td>
                    <td class="align-middle">
                      <a href="javascript:;" class="text-secondary font-weight-bold text-xs" data-toggle="tooltip" data-original-title="Edit user" @click="modify(data.id)">修改 </a>
                      <a href="javascript:;" class="text-secondary font-weight-bold text-xs" data-toggle="tooltip" data-original-title="Edit user" @click="deleteConfirm(data.id)">删除 </a>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </div>
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
import SoftBadge from "@/AAA_Ding/Components/SoftBadge.vue";
import SoftAvatar from "@/AAA_Ding/Components/SoftAvatar.vue";
import * as XLSX from "xlsx";


export default {
  components: {SoftAvatar, SoftBadge, Amap, DualRangeSlider, TimelineList, TimelineItem, ProjectsCard},
  data() {
    return {
      arrowState_obsTime: 0,
      selectAll:false,
      WeatherDataSelected:[],
      searchQuery: '',
      map: null,
      selectedCity: '成都',
      dropdownOpen: false,
      cities: ['北京', '上海', '成都', '三亚', '深圳', '杭州'],
      pastFiveDays:[],
      pastFiveWeek:[],
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
      allCityWeatherData:[],
      selectedWeatherData:[],
    };
  },

  created(){
    this.getAllWeatherData();
    this.getCityWeatherData();
    this.getDateAndWeek();
  },
  computed: {
    limitedWeatherData() {
      const dataLength = this.selectedWeatherData.length;
      const startIndex = Math.max(dataLength - 100, 0);
      return this.selectedWeatherData.slice(startIndex, dataLength).reverse();
    }
  },
  methods: {
    toggleArrow_obsTime() {
      this.arrowState_obsTime = (this.arrowState_obsTime + 1) % 3;
      if(this.arrowState_obsTime===0){
        this.selectedWeatherData=this.allCityWeatherData;
      }
      if(this.arrowState_obsTime===1){
        this.selectedWeatherData = this.allCityWeatherData.sort((a, b) => {
          // convert postedTime to Date object
          const dateA = new Date(a.obsTime);
          const dateB = new Date(b.obsTime);

          // compare dates
          return dateA - dateB;
        });
      }
      if(this.arrowState_obsTime===2){
        this.selectedWeatherData = this.allCityWeatherData.sort((a, b) => {
          // convert postedTime to Date object
          const dateA = new Date(a.obsTime);
          const dateB = new Date(b.obsTime);

          // compare dates
          return dateB - dateA;
        });
      }
    },
    toggleSelectAll() {
      this.WeatherDataSelected=[];
      this.limitedWeatherData.forEach(data => {
        data.selected = this.selectAll;
        this.toggleReward(data);
      });
    },
    toggleReward(data){
      if (data.selected) {
        this.WeatherDataSelected.push(data);
      } else {
        const index = this.WeatherDataSelected.findIndex(item => item.id === data.id);
        if (index !== -1) {
          this.WeatherDataSelected.splice(index, 1);
        }
      }
    },
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
              this.selectedWeatherData=this.allCityWeatherData;
            }
          })
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
    weatherImage2(weatherCondition){
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
    modify(id){
      this.$router.push({ name: '修改天气', params: { id: id }, query: { userID: this.$route.query.userID } });
    },
    deleteConfirm(id) {
      if (window.confirm("是否删除？")) {
        this.deleteItem(id);
      }
    },
    async deleteItem(id) {
      console.log("开始删除");
      try {
        const response = await axios.delete(`http://localhost:8080/weather/${id}`);
        if (response.data.code === 200) {
          console.log('删除成功');
          window.location.reload();
        } else {
          console.error('Error updating reward: ' + response.data.message);
        }
      } catch (error) {
        console.error(error);
      }
    },
    search(){
      this.selectedWeatherData = [];
      for (let i = 0; i < this.allCityWeatherData.length; i++) {
        if (this.allCityWeatherData[i].city.includes(this.searchQuery)) {
          this.selectedWeatherData.push(this.allCityWeatherData[i]);
        }
      }
      if (this.searchQuery === '') {
        this.selectedWeatherData = this.allCityWeatherData;
      }
    },
    Export() {
      if (this.WeatherDataSelected.length===0){
        window.confirm("未选择任何信息！");
      }else{
        if (window.confirm("是否导出？")) {
          this.ExportToExcel();
        }
      }
    },
    ExportToExcel() {
      // 创建工作表数据数组
      const ws_data = this.WeatherDataSelected.map(data => [
        data.id,
        data.city,
        data.obsTime,
        data.temperature,
        data.humidity,
        data.windSpeed,
        data.windDirection,
        data.weatherCondition,
        data.pressure,
        data.visibility
      ]);

      // 添加标题行
      ws_data.unshift([
        "id",
        "city",
        "obsTime",
        "temperature(°C)",
        "humidity(%)",
        "windSpeed(m/s)",
        "windDirection",
        "weatherCondition",
        "pressure(kPa)",
        "visibility(km)"
      ]);

      // 使用数据构建工作表
      const ws = XLSX.utils.aoa_to_sheet(ws_data);

      // 构建工作簿并添加工作表
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "WeatherData");

      // 生成工作簿的二进制字符串
      const wbout = XLSX.write(wb, { bookType: 'xlsx', type: 'binary' });
      // 将二进制字符串转换为Blob对象
      function s2ab(s) {
        const buf = new ArrayBuffer(s.length);
        const view = new Uint8Array(buf);
        for (let i = 0; i < s.length; i++) {
          view[i] = s.charCodeAt(i) & 0xFF;
        }
        return buf;
      }
      // 保存生成的Blob对象
      const blob = new Blob([s2ab(wbout)], { type: 'application/octet-stream' });
      // 创建下载链接
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');

      const now = new Date();
      const year = now.getFullYear();
      const month = ('0' + (now.getMonth() + 1)).slice(-2);
      const day = ('0' + now.getDate()).slice(-2);
      const hours = ('0' + now.getHours()).slice(-2);
      const minutes = ('0' + now.getMinutes()).slice(-2);
      const seconds = ('0' + now.getSeconds()).slice(-2);
      const formattedTime = year + month + day + hours + minutes + seconds;

      a.href = url;
      a.download = 'weather_data_'+formattedTime+'.xlsx';
      document.body.appendChild(a);
      a.click();
      setTimeout(function() {
        document.body.removeChild(a);
        window.URL.revokeObjectURL(url);
      }, 0);
    },
    Print() {
      if (this.WeatherDataSelected.length===0){
        window.confirm("未选择任何信息！");
      }else{
        if (window.confirm("是否进行打印预览？")) {
          this.PrintExcel();
        }
      }
    },
    PrintExcel() {
      // 构建HTML表格的数据字符串
      let html = "<table><thead><tr>";
      // 添加标题行
      const headers = [
        "id",
        "city",
        "obsTime",
        "temperature(°C)",
        "humidity(%)",
        "windSpeed(m/s)",
        "windDirection",
        "weatherCondition",
        "pressure(kPa)",
        "visibility(km)"
      ];
      headers.forEach(header => {
        html += `<th>${header}</th>`;
      });
      html += "</tr></thead><tbody>";

      // 添加数据行
      this.WeatherDataSelected.forEach(data => {
        html += "<tr>";
        html += `<td>${data.id}</td>`;
        html += `<td>${data.city}</td>`;
        html += `<td>${data.obsTime}</td>`;
        html += `<td>${data.temperature}</td>`;
        html += `<td>${data.humidity}</td>`;
        html += `<td>${data.windSpeed}</td>`;
        html += `<td>${data.windDirection}</td>`;
        html += `<td>${data.weatherCondition}</td>`;
        html += `<td>${data.pressure}</td>`;
        html += `<td>${data.visibility}</td>`;
        html += "</tr>";
      });
      html += "</tbody></table>";

      // 新建一个窗口并写入HTML表格
      const printWindow = window.open('', '_blank');
      printWindow.document.write(html);

      // 添加样式以确保打印效果
      printWindow.document.write(`
      <style>
      table {
        width: 100%;
        border-collapse: collapse;
      }
      th, td {
        border: 1px solid black;
        padding: 5px;
        text-align: left;
      }
      </style>
    `);
      printWindow.document.write(`
      <button onclick="window.print();">Print</button>
      <button onclick="window.close();">Close</button>
    `);
      printWindow.document.close();
    },
  },
};
</script>

<style>
.my-form-check{
  margin-left: 10px;
}
.left-container{
  width:380px;
}
.right-container{
  margin-left:20px;
  width: calc(100% - 420px);
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
.btn-sort {
  background: none;
  border: none;
  cursor: pointer;
}
.green {
  color: limegreen;
}
</style>