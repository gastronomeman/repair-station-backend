import ExamLayout from '@/views/layout/ExamLayout.vue'
import AnswerNotice from '@/views/exam/AnswerNotice.vue'
import StudentInfo from '@/views/exam/StudentInfo.vue'
import AnswerMode from '@/views/exam/AnswerMode.vue'
import Settlement from '@/views/exam/Settlement.vue'

export default [
  {
    path: '/exam',
    component: ExamLayout,
    meta: { requiresAuth: true },
    redirect: '/exam/notice',
    children: [
      {
        path: 'notice',
        meta: { title: '维修站答题' },
        component: AnswerNotice
      },
      {
        path: 'stuInfo',
        meta: { title: '维修站答题' },
        component: StudentInfo
      },
      {
        path: 'answer',
        meta: { title: '维修站答题' },
        component: AnswerMode
      },
      {
        path: 'settlement',
        meta: { title: '维修站答题' },
        component: Settlement
      }
    ]
  }
]
