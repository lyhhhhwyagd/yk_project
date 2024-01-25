import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "@/views/Dashboard.vue";
import 课程管理 from "@/views/课程管理";
import Billing from "@/views/Billing.vue";
import VirtualReality from "@/views/VirtualReality.vue";
import Profile from "@/views/Profile.vue";
import 习题管理 from "../views/习题管理";
import 答题管理 from "../views/答题管理";
import Rtl from "@/views/Rtl.vue";
import SignIn from "@/AAA_Ding/Pages/AAASignIn.vue";
import SignUp from "@/views/SignUp.vue";
import CheckRoll from "@/views/CheckRoll.vue";
import 考试管理 from '@/views/考试管理';
import 论坛主页 from "@/AAA_Ding/Pages/AAAPosts.vue";
import AAAPostAdd from "@/AAA_Ding/Components/AAAPostAdd.vue";
import AAAPostLook from "@/AAA_Ding/Components/AAAPostLook.vue";
import AAAPostModify from "@/AAA_Ding/Components/AAAPostModify.vue";
import AAAPostsStatistics from "@/AAA_Ding/Components/AAAPostStatistics.vue";
import AAARewards from "@/AAA_Ding/Pages/AAARewards.vue";
import AAARewardsAdd from "@/AAA_Ding/Components/AAARewardsAdd.vue";
import AAARewardsModify from "@/AAA_Ding/Components/AAARewardsModify.vue";
import AAARewardsLook from "@/AAA_Ding/Components/AAARewardsLook.vue";
import AAARewardsStatistics from "@/AAA_Ding/Components/AAARewardsStatistics.vue";
import Tasks from "@/views/Tasks.vue";
import Classmanage from "@/views/Classmanage.vue";
import ClassmanageStatistic from "@/examples/Charts/ClassmanageStatistic";
import TasksStatistic from "@/examples/Charts/TasksStatistic";
import AAAWeather from "@/AAA_Ding/Pages/AAAWeather.vue";
import 活动管理 from '@/views/活动管理';
import AfterSubmit from "../views/questionnaire/customer/afterSubmit/AfterSubmit";
import Customer from "../views/questionnaire/customer/Customer";
import Overview from "../views/questionnaire/editor/overview/Overview";
import Create from "../views/questionnaire/editor/create/Create";
import modify from "../views/questionnaire/editor/modify/modify";
import Monitor from "../views/questionnaire/editor/monitor/Monitor";
import PaperLink from "../views/questionnaire/editor/create/PaperLink";
import questionnaireStatistic from "../examples/Charts/questionnaireStatistic";
import TaskJSC from "../examples/Charts/TaskJSC";
import NavigationBar1 from "@/AAA_Ding/Pages/AAANavigationBar.vue";
import NavigationBar2 from '@/views/驾驶舱';



const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/sign-in",
  },
  {
    path: "/navigationBar1",
    name: "驾驶舱（论坛/天气/悬赏）",
    component: NavigationBar1,
  },
  {
    path: "/navigationBar2",
    name: "驾驶舱（点名/考试/活动）",
    component: NavigationBar2,
  },
  {
    path: "/posts",
    name: "论坛主页",
    component: 论坛主页,
  },
  {
    path: "/posts/add",
    name: "新增帖子",
    component: AAAPostAdd,
  },
  {
    path: '/posts/look/:id',
    name: '查看帖子',
    component: AAAPostLook,
  },
  {
    path: '/posts/modify/:id',
    name: '修改帖子',
    component: AAAPostModify,
  },
  {
    path: '/posts/statistics',
    name: '统计帖子',
    component: AAAPostsStatistics,
  },
  {
    path: "/rewards",
    name: "悬赏中心",
    component: AAARewards,
  },
  {
    path: '/rewards/modify/:id',
    name: '修改悬赏',
    component: AAARewardsModify,
  },
  {
    path: '/rewards/add',
    name: '添加悬赏',
    component: AAARewardsAdd,
  },
  {
    path: '/rewards/look/:id',
    name: '查看悬赏',
    component: AAARewardsLook,
  },
  {
    path: '/rewards/statistics',
    name: '统计悬赏',
    component: AAARewardsStatistics,
  },
  {
    path: '/weather',
    name: '天气组件',
    component: AAAWeather,
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/课程管理",
    name: "课程管理",
    component: 课程管理,
  },
  {
    path: "/习题管理",
    name: "习题管理",
    component: 习题管理,
  },
  {
    path:"/答题管理",
    name:"答题管理",
    component:答题管理,
  },
  {
    path:"/rtl",
    name:"rtl",
    component:Rtl,
  },
  {
    path: "/billing",
    name: "Billing",
    component: Billing,
  },
  {
    path: "/virtual-reality",
    name: "Virtual Reality",
    component: VirtualReality,
  },
  {
    path: "/profile",
    name: "Profile",
    component: Profile,
  },

  {
    path: "/sign-in",
    name: "Sign In",
    component: SignIn,
  },
  {
    path: "/sign-up",
    name: "Sign Up",
    component: SignUp,
  },
  {
    path:"/tasks",
    name:"Tasks",
    component:Tasks,
  },
  {
    path:"/ClassManage",
    name:"ClassManage",
    component:Classmanage,
  },
  {
    path:"/ClassmanageStatistic",
    name:"ClassmanageStatistic",
    component:ClassmanageStatistic,
  },
  {
    path:"/TasksStatistic",
    name:"TasksStatistic",
    component:TasksStatistic,
  },

  {
    path:"/questionnaireStatistic",
    name:"questionnaireStatistic",
    component:questionnaireStatistic,
  },
  {
    path: '/customer/:paperId/complete',
    name: 'complete',
    component:AfterSubmit,
  },
  {
    path: '/customer/:paperId',
    name: 'customer',
    component:Customer,
  },
  {
    path: '/overview',
    name: 'Overview',
    component:Overview,
  },
  {
    path: '/create/:paperId',
    name: 'create',
    component:Create,
  },
  {
    path: '/modify/:paperId',
    name: 'modify',
    component:modify,
  },

  {
    path: '/monitor/:paperId',
    name: 'monitor',
    component: Monitor,
  },
  {
    path: '/paperlink/:paperId',
    name: 'paperlink',
    component: PaperLink,
  },
  {
    path: '/taskJSC',
    name: 'TaskJSC',
    component: TaskJSC,
  },
  {
    path: "/CheckRoll",
    name: "CheckRoll",
    component: CheckRoll,
  },
  {
    path: "/考试管理",
    name: "考试管理",
    component: 考试管理,
  },
  {
    path: "/活动管理",
    name: "活动管理",
    component: 活动管理,
  },
  {
    path: "/驾驶舱",
    name: "驾驶舱",
    component: NavigationBar2,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

export default router;
