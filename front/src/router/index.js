import HomeComponent from "@/components/mainmenu/HomeComponent.vue";
import MainComponent from "@/components/member/main.vue";
import LoginComponent from "@/components/member/login.vue";
import JoinComponent from "@/components/member/join.vue";
import FindComponent from "@/components/member/find.vue";

import MainmenuComponent from "@/components/maingame/Mainmenu.vue";
import IntroStart1 from "@/components/mainmenu/intro/introstart/IntroStart1.vue";
import IntroStart2 from "@/components/mainmenu/intro/introstart/IntroStart2.vue";
import IntroStart3 from "@/components/mainmenu/intro/introstart/IntroStart3.vue";
import IntroStart4 from "@/components/mainmenu/intro/introstart/IntroStart4.vue";
import TutorialStart from "@/components/mainmenu/tutorial/tutorialStart.vue";
import TutorialMain from "@/components/mainmenu/tutorial/tutorialMain.vue";
import BankTuto from "@/components/mainmenu/intro/introMenu/bankTuto.vue";
import OrderingTuto from "@/components/mainmenu/intro/introMenu/OrderingTuto.vue";
import StorageTuto from "@/components/mainmenu/intro/introMenu/StorageTuto.vue";
import GameTuto from "@/components/mainmenu/intro/introMenu/gameTuto.vue";

import gameendsummary from "@/components/mainmenu/ending/gameendsummary.vue";

import PoorEnding1 from "@/components/mainmenu/ending/poorending1.vue";
import PoorEnding2 from "@/components/mainmenu/ending/poorending2.vue";
import PoorEnding3 from "@/components/mainmenu/ending/poorending3.vue";
import NomalEnding1 from "@/components/mainmenu/ending/nomalending1.vue";
import NomalEnding2 from "@/components/mainmenu/ending/nomalending2.vue";
import NomalEnding3 from "@/components/mainmenu/ending/nomalending3.vue";
import RichEnding1 from "@/components/mainmenu/ending/richending1.vue";
import RichEnding2 from "@/components/mainmenu/ending/richending2.vue";
import RichEnding3 from "@/components/mainmenu/ending/richending3.vue";
import SuperRichEnding1 from "@/components/mainmenu/ending/superrichending1.vue";
import SuperRichEnding2 from "@/components/mainmenu/ending/superrichending2.vue";
import SuperRichEnding3 from "@/components/mainmenu/ending/superrichending3.vue";

import RealNews1 from "@/components/mainmenu/realnews/realnews1.vue";

import news from "@/components/maingame/news/news.vue";
import maingameComponent from "@/components/maingame/maingame.vue";
import QuizChoice from "@/components/quiz/quizChoice.vue";
import QuizMain from "@/components/quiz/quizMain.vue";
import Counter from "@/components/store/counter.vue";
import Customer from "@/components/store/customer.vue";
import Calculation from "@/components/maingame/calculation.vue";

import OrderingMain from "@/components/ordering/OrderingMain.vue";
import StorageMain from "@/components/storage/StorageMain.vue";

import bankComponent from "@/components/bank/bankMain.vue";
import quizComponent from "@/components/quiz/quizMain.vue";

import Settings from "@/components/common/volumesettings.vue"; // 개발용으로 추가가

import { createRouter, createWebHistory } from "vue-router";
import CalculationTuto from "@/components/mainmenu/intro/introMenu/calculationTuto.vue";
import Finalcalculation from "@/components/mainmenu/ending/finalcalculation.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "Main",
      component: MainComponent,
    },
    {
      path: "/login",
      name: "login",
      component: LoginComponent,
    },
    {
      path: "/join",
      name: "join",
      component: JoinComponent,
    },
    {
      path: "/find",
      name: "find",
      component: FindComponent,
    },
    {
      path: "/quiz",
      name: "quiz",
      component: quizComponent,
    },

    {
      path: "/bank",
      name: "bank",
      component: bankComponent,
    },
    {
      path: "/news",
      name: "news",
      component: news,
    },
    {
      path: "/realnews1",
      name: "realnews1",
      component: RealNews1,
    },
    {
      path: "/maingame",
      name: "maingame",
      component: maingameComponent,
      children: [
        {
          path: ":customerCount",
          components: {
            customer: Customer,
            counter: Counter,
          },
        },
        {
          path: "quiz",
          components: {
            customer: QuizMain,
            counter: QuizChoice,
          },
        },
      ],
    },
    {
      path: "/calculation",
      name: "calculation",
      component: Calculation,
    },
    {
      path: "/homeMenu",
      name: "menu",
      component: HomeComponent,
    },
    {
      path: "/mainmenu",
      name: "mainmenu",
      component: MainmenuComponent,
    },

    {
      path: "/linkOrderingTutorial",
      name: "linkOrderingTutorial",
      component: OrderingTuto,
    },
    {
      path: "/linkStorageTutorial",
      name: "linkStorageTutorial",
      component: StorageTuto,
    },
    {
      path: "/linkBankTutorial",
      name: "linkBankTutorial",
      component: BankTuto,
    },
    {
      path: "/linkGameTutorial",
      name: "linkGameTutorial",
      component: GameTuto,
    },
    {
      path: "/linkCalculationTutorial",
      name: "linkCalculationTutorial",
      component: CalculationTuto,
    },
    {
      path: "/introStart",
      name: "introStart",
      component: IntroStart1,
    },
    {
      path: "/intro2",
      name: "introStart2",
      component: IntroStart2,
    },
    {
      path: "/intro3",
      name: "introStart3",
      component: IntroStart3,
    },
    {
      path: "/intro4",
      name: "introStart4",
      component: IntroStart4,
    },
    {
      path: "/endsummary",
      name: "endsummary",
      component: gameendsummary,
    },
    {
      path: "/finalcalculation",
      name: "finalcalculation",
      component: Finalcalculation,
    },
    {
      path: "/poorending1",
      name: "poorending1",
      component: PoorEnding1,
    },
    {
      path: "/poorending2",
      name: "poorending2",
      component: PoorEnding2,
    },
    {
      path: "/poorending3",
      name: "poorending3",
      component: PoorEnding3,
    },
    {
      path: "/nomalending1",
      name: "nomalending1",
      component: NomalEnding1,
    },
    {
      path: "/nomalending2",
      name: "nomalending2",
      component: NomalEnding2,
    },
    {
      path: "/nomalending3",
      name: "nomalending3",
      component: NomalEnding3,
    },
    {
      path: "/richending1",
      name: "richending1",
      component: RichEnding1,
    },
    {
      path: "/richending2",
      name: "richending2",
      component: RichEnding2,
    },
    {
      path: "/richending3",
      name: "richending3",
      component: RichEnding3,
    },
    {
      path: "/superrichending1",
      name: "superrichending1",
      component: SuperRichEnding1,
    },
    {
      path: "/superrichending2",
      name: "superrichending2",
      component: SuperRichEnding2,
    },
    {
      path: "/superrichending3",
      name: "superrichending3",
      component: SuperRichEnding3,
    },
    {
      path: "/tutorialStart",
      name: "tutorialStart",
      component: TutorialStart,
    },
    {
      path: "/tutorialMain",
      name: "tutorialMain",
      component: TutorialMain,
    },
    {
      path: "/orderingMain",
      name: "orderingMain",
      component: OrderingMain,
    },
    {
      path: "/storageMain",
      name: "storageMain",
      component: StorageMain,
    },
    {
      path: "/settings",
      name: "settings",
      component: Settings,
    },
  ],
});

export default router;
