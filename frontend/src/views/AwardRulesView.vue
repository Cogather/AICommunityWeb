<template>
  <div class="award-rules-view">
    <div class="header">
      <el-button text :icon="ArrowLeft" class="back-btn" @click="handleBack">返回</el-button>
      <h2>奖项规则说明</h2>
    </div>

    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><Loading /></el-icon>
      <span>加载中...</span>
    </div>

    <div v-else class="award-rules-content">
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
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, Loading } from '@element-plus/icons-vue'
import { getAwardRules, type AwardRuleDetail } from '../mock'

const router = useRouter()
const route = useRoute()

// 奖项规则数据 - 从 mock API 获取
const awardRules = ref<AwardRuleDetail[]>([])
const loading = ref(false)

// 加载奖项规则数据
const loadAwardRules = async () => {
  loading.value = true
  try {
    const result = await getAwardRules()
    awardRules.value = result.list
  } catch (error) {
    console.error('加载奖项规则失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadAwardRules()
})

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

.loading-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 40px;
  color: #64748b;

  .is-loading {
    animation: rotate 1s linear infinite;
  }
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>

