import Vue from 'vue'
import Vuex from 'vuex'
// import { createStore } from "vuex";
import bootstrap from "bootstrap/dist/js/bootstrap.min.js";
import VuexPersistence from 'vuex-persist';
import customer from './modules/customer'
import paper from './modules/paper'


const vuexLocal = new VuexPersistence({
  storage: window.localStorage
})

const store = new Vuex.Store({
  strict: false,
  plugins: [vuexLocal.plugin],
  state: {
    userID: null,
    hideConfigButton: false,
    isPinned: true,
    showConfig: false,
    isTransparent: "",
    isRTL: false,
    color: "",
    isNavFixed: false,
    isAbsolute: false,
    showNavs: true,
    showSidenav: true,
    showNavbar: true,
    showFooter: true,
    showMain: true,
    token:"",
    navbarFixed:
      "position-sticky blur shadow-blur left-auto top-1 z-index-sticky px-0 mx-4",
    absolute: "position-absolute px-4 mx-0 w-100 z-index-2",
    bootstrap,
  },
  mutations: {
    setUserID(state, userID) {
      state.userID = userID;
    },
    toggleConfigurator(state) {
      state.showConfig = !state.showConfig;
    },
    navbarMinimize(state) {
      const sidenav_show = document.querySelector(".g-sidenav-show");
      if (sidenav_show.classList.contains("g-sidenav-hidden")) {
        sidenav_show.classList.remove("g-sidenav-hidden");
        sidenav_show.classList.add("g-sidenav-pinned");
        state.isPinned = true;
      } else {
        sidenav_show.classList.add("g-sidenav-hidden");
        sidenav_show.classList.remove("g-sidenav-pinned");
        state.isPinned = false;
      }
    },
    sidebarType(state, payload) {
      state.isTransparent = payload;
    },
    cardBackground(state, payload) {
      state.color = payload;
    },
    navbarFixed(state) {
      if (state.isNavFixed === false) {
        state.isNavFixed = true;
      } else {
        state.isNavFixed = false;
      }
    },
    toggleEveryDisplay(state) {
      state.showNavbar = !state.showNavbar;
      state.showSidenav = !state.showSidenav;
      state.showFooter = !state.showFooter;
    },
    toggleHideConfig(state) {
      state.hideConfigButton = !state.hideConfigButton;
    },
    SET_TOKEN(state, token) {
      state.token = token;
    },
    SET_FILTER(state, filter) {
      localStorage.setItem("filterPattern", filter);
      state.filterPattern = filter;
    },
    DEFAULT_FILTER(state) {
      localStorage.setItem("filterPattern", "1");
      state.filterPattern = "1";
    },
  },
  actions: {
    toggleSidebarColor({ commit }, payload) {
      commit("sidebarType", payload);
    },
    setCardBackground({ commit }, payload) {
      commit("cardBackground", payload);
    },
  },
  modules:{
    customer,
    paper,
  },
  getters: {
    userID: state => state.userID,
    customerPaper: state => state.customer.paper,
    // Create 相关
    createPaperVisible: state => state.paper.createPaperVisible,
    paperInfo: state => state.paper.currentPaper.paperInfo,
    questionList: state => state.paper.currentPaper.questionList,
    // Monitor 相关
    monitorPaper: state => state.customer.paperStatistic,
    answers: state => state.customer.answers,
    //Overview 相关
    allPapers: state => state.paper.paperList
  },
}) 

export default store;

// export default createStore({
//   state: {
//     userID: null,
//     hideConfigButton: false,
//     isPinned: true,
//     showConfig: false,
//     isTransparent: "",
//     isRTL: false,
//     color: "",
//     isNavFixed: false,
//     isAbsolute: false,
//     showNavs: true,
//     showSidenav: true,
//     showNavbar: true,
//     showFooter: true,
//     showMain: true,
//     token:"",
//     navbarFixed:
//       "position-sticky blur shadow-blur left-auto top-1 z-index-sticky px-0 mx-4",
//     absolute: "position-absolute px-4 mx-0 w-100 z-index-2",
//     bootstrap,
//   },
//   mutations: {
//     setUserID(state, userID) {
//       state.userID = userID;
//     },
//     toggleConfigurator(state) {
//       state.showConfig = !state.showConfig;
//     },
//     navbarMinimize(state) {
//       const sidenav_show = document.querySelector(".g-sidenav-show");
//       if (sidenav_show.classList.contains("g-sidenav-hidden")) {
//         sidenav_show.classList.remove("g-sidenav-hidden");
//         sidenav_show.classList.add("g-sidenav-pinned");
//         state.isPinned = true;
//       } else {
//         sidenav_show.classList.add("g-sidenav-hidden");
//         sidenav_show.classList.remove("g-sidenav-pinned");
//         state.isPinned = false;
//       }
//     },
//     sidebarType(state, payload) {
//       state.isTransparent = payload;
//     },
//     cardBackground(state, payload) {
//       state.color = payload;
//     },
//     navbarFixed(state) {
//       if (state.isNavFixed === false) {
//         state.isNavFixed = true;
//       } else {
//         state.isNavFixed = false;
//       }
//     },
//     toggleEveryDisplay(state) {
//       state.showNavbar = !state.showNavbar;
//       state.showSidenav = !state.showSidenav;
//       state.showFooter = !state.showFooter;
//     },
//     toggleHideConfig(state) {
//       state.hideConfigButton = !state.hideConfigButton;
//     },
//     SET_TOKEN(state, token) {
//       state.token = token;
//     },
//     SET_FILTER(state, filter) {
//       localStorage.setItem("filterPattern", filter);
//       state.filterPattern = filter;
//     },
//     DEFAULT_FILTER(state) {
//       localStorage.setItem("filterPattern", "1");
//       state.filterPattern = "1";
//     },
//   },
//   actions: {
//     toggleSidebarColor({ commit }, payload) {
//       commit("sidebarType", payload);
//     },
//     setCardBackground({ commit }, payload) {
//       commit("cardBackground", payload);
//     },
//   },
//   getters: {
//     userID: state => state.userID,
//   },
// });
