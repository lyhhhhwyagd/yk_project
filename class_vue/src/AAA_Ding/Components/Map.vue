<template>
  <div ref="amapContainer"></div>
</template>

<script>
export default {
  name: 'Amap',
  props: {
    center: {
      type: Array,
      default: () => [116.397428, 39.90923], // 默认北京天安门坐标
    },
    zoom: {
      type: Number,
      default: 10
    },
    amapKey: {
      type: String,
      required: true
    },
  },
  data() {
    return {
      map: null,
    };
  },
  mounted() {
    this.loadAmapSdk().then(() => {
      this.initMap();
    }).catch((error) => {
      console.error('AMap SDK加载失败:', error);
    });
  },
  methods: {
    loadAmapSdk() {
      return new Promise((resolve, reject) => {
        // 检查是否已经加载了SDK
        if (window.AMap && window.AMap.Map) {
          resolve();
          return;
        }
        // 动态创建script标签加载SDK
        const script = document.createElement('script');
        script.type = 'text/javascript';
        script.src = `https://webapi.amap.com/maps?v=1.4.15&callback=initAMap&key=${this.amapKey}`;
        script.onerror = reject;
        document.head.appendChild(script);
        // 等待全局回调
        window.initAMap = () => {
          resolve();
          window.initAMap = null; // 清理全局回调函数
        };
      });
    },
    initMap() {
      this.map = new window.AMap.Map(this.$refs.amapContainer, {
        center: this.center,
        zoom: this.zoom,
      });
    },
  },
  beforeUnmount() {
    if (this.map) {
      this.map.destroy();
    }
  }
};
</script>
