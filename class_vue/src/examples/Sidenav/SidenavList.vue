<template>
  <div
    class="w-auto h-auto collapse navbar-collapse max-height-vh-100 h-100"
    id="sidenav-collapse-main"
  >
    <ul class="navbar-nav">
      <li class="nav-item">
        <sidenav-collapse navText="论坛主页" :to="{ name: 'Posts', query: { userID: this.userID }  }">
          <template #icon>
            <shop />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="天气组件" :to="{ name: 'Weather', query: { userID: this.userID }  }">
          <template #icon>
            <shop />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="悬赏中心" :to="{ name: 'Rewards', query: { userID: this.userID }  }">
          <template #icon>
            <shop />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="文件管理" :to="{ name: 'Dashboard', query: { userID: this.userID } }">
          <template #icon>
            <shop />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="课程管理" :to="{ name: 'Tables', query: { userID: this.userID }  }">
          <template #icon>
            <office />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="习题管理" :to="{ name: 'Practices', query: { userID: this.userID }  }">
          <template #icon>
            <office />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="答题管理" :to="{ name: 'sp', query: { userID: this.userID }  }">
          <template #icon>
            <office />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="用户管理" :to="{ name: 'Profile', query: { userID: this.userID }  }">
          <template #icon>
            <customer-support />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="抽奖点名" :to="{ name: 'CheckRoll', query: { userID: this.userID }  }">
          <template #icon>
            <switches />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="考试管理" :to="{ name: 'Exam', query: { userID: this.userID }  }">
          <template #icon>
            <switches />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="活动管理" :to="{ name: 'Activity', query: { userID: this.userID }  }">
          <template #icon>
            <switches />
          </template>
        </sidenav-collapse>
      </li>
      <li>
        <sidenav-collapse navText="作业管理" :to="{ name: 'Virtual Reality', query: { userID: this.userID } }">
          <template #icon>
            <switches />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="待办事项" :to="{ name: 'Tasks', query: { userID: this.userID }   }">
          <template #icon>
            <office />
          </template>
        </sidenav-collapse>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="课堂管理" :to="{ name: 'ClassManage', query: { userID: this.userID }   }">
          <template #icon>
            <office />
          </template>
        </sidenav-collapse>
      </li>
      <li class="mt-3 nav-item">
        <h6
          class="text-xs ps-4 text-uppercase font-weight-bolder opacity-6"
          :class="this.$store.state.isRTL ? 'me-4' : 'ms-2'"
        >
          PAGES
        </h6>
      </li>
      <li class="nav-item">
        <sidenav-collapse navText="论坛/天气/悬赏" :to="{ name: 'NavigationBar', query: { userID: this.userID }   }">
          <template #icon>
            <office />
          </template>
        </sidenav-collapse>
      </li>

    </ul>
  </div>
</template>
<script>
import SidenavCollapse from "./SidenavCollapse.vue";
import Shop from "../../components/Icon/Shop.vue";
import Office from "../../components/Icon/Office.vue";

import CustomerSupport from "../../components/Icon/CustomerSupport.vue";
import Document from "../../components/Icon/Document.vue";
import Spaceship from "../../components/Icon/Spaceship.vue";

import Switches from "../../components/Icon/Switches.vue";

export default {
  name: "SidenavList",
  props: {
    cardBg: String,
  },
  data() {
    return {
      title: "Soft UI Dashboard PRO",
      controls: "dashboardsExamples",
      isActive: "active",
    };
  },
  components: {
    SidenavCollapse,
    Shop,
    Office,


    CustomerSupport,
    Switches,
    Document,
    Spaceship,

  },
  methods: {
    getRoute() {
      const routeArr = this.$route.path.split("/");
      return routeArr[1];
    },
  },
  computed: {
    userID() {
      console.log(this.$store.state.userID);
      return this.$store.state.userID; // 替换为你存储userID的实际state路径
    },
  },
};
</script>
