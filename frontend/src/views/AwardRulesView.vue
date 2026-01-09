<template>
  <div class="award-rules-view">
    <div class="header">
      <el-button text :icon="ArrowLeft" class="back-btn" @click="handleBack">返回</el-button>
      <h2>奖项规则说明</h2>
    </div>

    <div class="award-rules-content">
      <div
        v-for="rule in awardRules"
        :key="rule.name"
        class="award-rule-card"
        :class="{ active: rule.name === activeAward }"
      >
        <div class="card-header">
          <div class="title-line">
            <h3 class="rule-name">{{ rule.name }}</h3>
            <el-tag type="info" size="small">{{ rule.cycle }}</el-tag>
          </div>
          <p class="rule-description">{{ rule.description }}</p>
        </div>
        <div class="criteria">
          <div class="criteria-title">评选标准</div>
          <ul class="criteria-list">
            <li v-for="(criterion, idx) in rule.criteria" :key="idx">{{ criterion }}</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()

const awardRules = [
  { name: '技术创新奖', description: '表彰在AI技术方案上有重大突破的个人或团队', criteria: ['提交创新方案不少于2篇', '落地至少1个生产项目', '产出技术分享或专利'], cycle: '年度' },
  { name: '效能提升奖', description: '在工程效能、自动化与质量提升方面贡献突出', criteria: ['引入自动化工具并落地', '显著降低缺陷率或提升交付速度'], cycle: '季度' },
  { name: '最佳实践奖', description: '在业务场景中形成可复制的AI最佳实践并推广', criteria: ['形成完整案例文档', '内部分享不少于2场', '被至少一个团队复用'], cycle: '季度' },
  { name: '社区贡献奖', description: '对社区布道、开源贡献或知识传播有突出表现', criteria: ['发布高质量文章/视频', '组织或参与社区活动', '持续开源贡献'], cycle: '年度' }
]

const activeAward = computed(() => {
  const target = route.query.award
  return typeof target === 'string' ? target : ''
})

const handleBack = () => {
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/users')
  }
}
</script>

<style scoped lang="scss">
.award-rules-view {
  max-width: 960px;
  margin: 0 auto;
  padding: 24px;
}

.header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;

  h2 {
    margin: 0;
    font-size: 24px;
    font-weight: 800;
    color: #1e293b;
  }

  .back-btn {
    color: #606266;
    padding: 0;

    &:hover {
      color: #409eff;
    }
  }
}

.award-rules-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.award-rule-card {
  background: rgba(255, 255, 255, 0.85);
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 14px;
  padding: 18px;
  transition: all 0.3s ease;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.05);

  &.active {
    border-color: #409eff;
    box-shadow: 0 10px 24px rgba(64, 158, 255, 0.15);
  }
}

.title-line {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.rule-name {
  margin: 0;
  font-size: 18px;
  font-weight: 800;
  color: #1f2937;
}

.rule-description {
  margin: 0;
  color: #475569;
  line-height: 1.6;
}

.criteria {
  margin-top: 12px;
}

.criteria-title {
  font-size: 13px;
  font-weight: 700;
  color: #64748b;
  margin-bottom: 6px;
}

.criteria-list {
  margin: 0;
  padding-left: 18px;
  color: #475569;
  line-height: 1.8;
}
</style>

