
$(function () {
echarts_1();
echarts_2();

echarts_5();

function echarts_1() {
        // 基于准备好的dom，初始化echarts实例
// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('echart1'));

// 替换URL为您的后端接口地址
const apiUrl = 'http://localhost:8080/user/countUserType';

// 使用fetch发送GET请求
fetch(apiUrl)
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(data => {
    // 在这里处理从后端获取的数据
    console.log(data);

    // 提取用户类型和人数
    var userTypes = Object.keys(data.userTypeCount);
    var userCounts = Object.values(data.userTypeCount);

    // 指定图表的配置项和数据
    var option = {
        title: {
            text: '用户类型统计'
        },
        tooltip: {},
        legend: {
            data: ['人数']
        },
        xAxis: {
            data: userTypes
        },
        yAxis: {},
        series: [{
            name: '人数',
            type: 'bar',
            data: userCounts
        }]
    };

    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
  })
  .catch(error => {
    console.error('There was a problem with the fetch operation:', error);
  });

    }
function echarts_2() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart2'));
        const apiUrl = 'http://localhost:8080/file/countByCourseId';
        var barColor = ['#ffffff'];
        // 使用fetch发送GET请求
        fetch(apiUrl)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {
            // 在这里处理从后端获取的数据
            console.log(data);
        
            // 提取用户类型和人数
            var userTypes = Object.keys(data.courseFileCount);
            var userCounts = Object.values(data.courseFileCount);
        
            // 指定图表的配置项和数据
            var option = {
                title: {
                    text: '每个的课序号文件数统计'
                },
                tooltip: {},
                legend: {
                    data: ['文件数']
                },
                xAxis: {
                    data: userTypes
                },
                yAxis: {},
                series: [{
                    name: '文件数',
                    type: 'bar',
                    data: userCounts,
                    itemStyle: {
                        // 设置柱子颜色
                        color: barColor
                    }
                }]
            };
        
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
          })
          .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
          });
    }
function echarts_5() {
        // 使用 ECharts 初始化图表
        var myChart = echarts.init(document.getElementById('echart5'));
    
        // 调用后端接口获取数据
        fetch('http://localhost:8080/homework/submitAndNotSubmitCount')
            .then(response => response.json())
            .then(data => {
                // 获取数据
                const submitCount = data.submitCount;
                const notSubmitCount = data.notSubmitCount;
    
                // 绘制饼状图
                myChart.setOption({
                    title: {
                        text: 'Homework Submission Status',
                        subtext: 'Submitted vs Not Submitted',
                        left: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b} : {c} ({d}%)'
                    },
                    series: [
                        {
                            name: 'Submission Status',
                            type: 'pie',
                            radius: '55%',
                            center: ['50%', '60%'],
                            data: [
                                { value: submitCount, name: 'Submitted' },
                                { value: notSubmitCount, name: 'Not Submitted' }
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            })
            .catch(error => console.error('Error fetching data:', error));
    }
    

				
	
})



		
		
		


		









