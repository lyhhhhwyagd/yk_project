import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "@/views/Dashboard.vue";
import Tables from "@/views/Tables.vue";
import Billing from "@/views/Billing.vue";
import VirtualReality from "@/views/VirtualReality.vue";
import Profile from "@/views/Profile.vue";
import Practices from "../views/Practices.vue";
import sp from "../views/sp.vue";
import Rtl from "@/views/Rtl.vue";
import SignIn from "@/AAA_Ding/Pages/AAASignIn.vue";
import SignUp from "@/views/SignUp.vue";
import CheckRoll from "@/views/CheckRoll.vue";
import AAANavigationBar from "@/AAA_Ding/Components/AAANavigationBar.vue";
import AAAPosts from "@/AAA_Ding/Pages/AAAPosts.vue";
import AAAPostList from "@/AAA_Ding/Components/AAAPostList.vue";
import AAAPostAdd from "@/AAA_Ding/Components/AAAPostAdd.vue";
import AAAPostLook from "@/AAA_Ding/Components/AAAPostLook.vue";
import AAAPostModify from "@/AAA_Ding/Components/AAAPostModify.vue";
import AAAPostsStatistics from "@/AAA_Ding/Components/AAAPostStatistics.vue";
import AAARewards from "@/AAA_Ding/Pages/AAARewards.vue";
import AAARewardsList from "@/AAA_Ding/Components/AAARewardsList.vue";
import AAARewardsAdd from "@/AAA_Ding/Components/AAARewardsAdd.vue";
import AAARewardsModify from "@/AAA_Ding/Components/AAARewardsModify.vue";
import AAARewardsLook from "@/AAA_Ding/Components/AAARewardsLook.vue";
import AAARewardsStatistics from "@/AAA_Ding/Components/AAARewardsStatistics.vue";
import Tasks from "@/views/Tasks.vue";
import AAAWeather from "@/AAA_Ding/Pages/AAAWeather.vue";
const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/sign-in",
  },
  {
    path: "/navigationBar1",
    name: "NavigationBar",
    component: AAANavigationBar,
  },
  {
    path: "/posts",
    name: "Posts",
    component: AAAPosts,
  },
  {
    path: "/posts/list",
    name: "PostsList",
    component: AAAPostList,
  },
  {
    path: "/posts/add",
    name: "PostsAdd",
    component: AAAPostAdd,
  },
  {
    path: '/posts/look/:id',
    name: 'PostsLook',
    component: AAAPostLook,
  },
  {
    path: '/posts/modify/:id',
    name: 'PostsModify',
    component: AAAPostModify,
  },
  {
    path: '/posts/statistics',
    name: 'PostsStatistics',
    component: AAAPostsStatistics,
  },
  {
    path: "/rewards",
    name: "Rewards",
    component: AAARewards,
  },
  {
    path: "/rewards/list",
    name: "RewardsList",
    component: AAARewardsList,
  },
  {
    path: '/rewards/modify/:id',
    name: 'RewardsModify',
    component: AAARewardsModify,
  },
  {
    path: '/rewards/add',
    name: 'RewardsAdd',
    component: AAARewardsAdd,
  },
  {
    path: '/rewards/look/:id',
    name: 'RewardsLook',
    component: AAARewardsLook,
  },
  {
    path: '/rewards/statistics',
    name: 'RewardsStatistics',
    component: AAARewardsStatistics,
  },
  {
    path: '/weather',
    name: 'Weather',
    component: AAAWeather,
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/tables",
    name: "Tables",
    component: Tables,
  },
  {
    path: "/practices",
    name: "Practices",
    component: Practices,
  },
  {
    path:"/sp",
    name:"sp",
    component:sp,
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
    path: "/rtl-page",
    name: "Rtl",
    component: Rtl,
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
    path: "/CheckRoll",
    name: "CheckRoll",
    component: CheckRoll,
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
  linkActiveClass: "active",
});

export default router;
