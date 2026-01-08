<template>
  <div class="honor-wall-container">
    <div class="aurora-bg">
      <div class="light-orb orb-1"></div>
      <div class="light-orb orb-2"></div>
      <div class="light-orb orb-3"></div>
    </div>

    <div class="hud-dashboard">
      <div class="hud-top-row">
        <div class="view-switcher glass-pill">
          <div 
            v-for="mode in availableViewModes" 
            :key="mode.key"
            class="mode-tab"
            :class="{ active: currentViewMode === mode.key }"
            @click="switchMode(mode.key)"
          >
            <el-icon><component :is="mode.icon" /></el-icon>
            <span class="tab-label">{{ mode.label }}</span>
            <div class="tab-glare"></div>
          </div>
        </div>

        <div class="hud-actions">
          <div class="toggle-switch glass-pill">
            <div class="switch-track" :class="{ 'is-mine': filterScope === 'mine' }"></div>
            <span :class="{ active: filterScope === 'all' }" @click="filterScope = 'all'">全员</span>
            <span :class="{ active: filterScope === 'mine' }" @click="filterScope = 'mine'">我的</span>
          </div>
          <div class="search-wrapper">
            <el-input
              v-model="searchQuery"
              placeholder="搜索荣誉获得者..."
              class="crystal-input"
              prefix-icon="Search"
            />
          </div>
        </div>
      </div>

      <transition name="fade-slide">
        <div v-if="showSecondaryFilter" class="secondary-filter-bar glass-panel">
          <div class="filter-icon-wrapper">
            <el-icon><Filter /></el-icon> 
          </div>
          <div class="chip-container">
            <div 
              v-for="item in activeFilterOptions" 
              :key="item"
              class="gem-chip"
              :class="{ active: activeSubFilter === item }"
              @click="activeSubFilter = item"
            >
              {{ item }}
            </div>
          </div>
        </div>
      </transition>
    </div>

    <div class="cyber-layout">
      
      <div class="view-area">
        <transition-group 
          v-if="currentViewMode !== 'timeline'" 
          name="staggered-list" 
          tag="div" 
          class="card-grid"
        >
      <div 
            v-for="item in paginatedList" 
            :key="item.id" 
            class="honor-card-3d"
            :class="item.category"
          >
            <div class="card-content-glass">
              <div class="bg-decoration-circle"></div>
              <div class="bg-decoration-icon">
                <el-icon><Trophy /></el-icon>
              </div>

              <div class="card-top">
                <div class="avatar-halo" @click.stop="handleUserClick(item.name)" role="button" aria-label="查看用户时间线">
                  <el-avatar :size="50" :src="item.avatar" class="user-avatar" />
                  <div class="halo-ring"></div>
                </div>
                <div class="user-info">
                  <div class="user-name">{{ item.name }}</div>
                  <div class="dept-badge">{{ item.department }}</div>
                </div>
                <div class="year-ribbon">
                  <span>{{ item.year }}</span>
                </div>
              </div>

              <div class="award-center">
                <h3 class="award-name" @click.stop="handleAwardClick(item.awardName)">{{ item.awardName }}</h3>
                <div class="category-tag" @click.stop="handleAwardClick(item.awardName)" role="button" aria-label="查看奖项规则">
                  <el-icon><component :is="getCategoryIcon(item.category)" /></el-icon>
                  {{ getCategoryLabel(item.category) }}
                </div>
              </div>

              <div class="card-bottom">
                <span class="date-text">获奖时间：{{ formatAwardDate(item.awardDate) }}</span>
                <div class="flower-section" @click.stop="handleGiveFlower(item)">
                  <FlowerIcon 
                    :filled="item.hasGivenFlower" 
                    :size="18" 
                    :color="item.hasGivenFlower ? '#f472b6' : '#9ca3af'"
                    :strokeColor="'#6b7280'"
                    class="flower-icon-clickable"
                  />
                  <span class="flower-count">{{ item.flowers || 0 }}</span>
                </div>
              </div>
            </div>
          </div>
        </transition-group>

        <div v-else class="timeline-container">
          <div v-if="currentTimelineUser" class="timeline-user-header glass-panel">
            <el-avatar :size="60" :src="currentTimelineUser.avatar" />
            <div class="timeline-user-info">
              <h3 class="timeline-user-name">{{ currentTimelineUser.name }}</h3>
              <div class="timeline-user-stats">
                <span class="timeline-stat-item">
                  <FlowerIcon :filled="true" :size="18" color="#f472b6" />
                  <span class="stat-value">{{ currentTimelineUser.totalFlowers }}</span>
                  <span class="stat-label">总花朵数</span>
                </span>
              </div>
            </div>
          </div>
          <div class="timeline-line"></div>
          <div v-for="block in timelineData" :key="block.year" class="timeline-group">
            <div class="year-header">
              <span class="year-text">{{ block.year }}</span>
            </div>
            <div class="timeline-items">
              <div v-for="item in block.items" :key="item.id" class="t-item">
                <div class="t-node"></div>
                <div class="t-card glass-panel" :class="item.category">
                  <div class="t-avatar" @click.stop="handleUserClick(item.name)" role="button" aria-label="查看用户时间线">
                     <el-avatar :size="40" :src="item.avatar" />
                  </div>
                  <div class="t-info">
                    <div class="t-title" @click.stop="handleAwardClick(item.awardName)" role="button" aria-label="查看奖项规则">{{ item.awardName }}</div>
                    <div class="t-meta">{{ item.name }} · {{ item.awardDate }}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="paginatedList.length === 0 && currentViewMode !== 'timeline'" class="empty-zone">
          <el-empty description="暂无荣耀记录" :image-size="160" />
        </div>

        <div 
          v-if="currentViewMode !== 'timeline' && processedList.length > 0" 
          class="pagination-bar"
        >
          <el-pagination
            background
            layout="total, sizes, prev, pager, next, jumper"
            :page-sizes="[10, 12, 20, 30, 50]"
            :page-size="pageSize"
            :current-page="currentPage"
            :total="processedList.length"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>

      <div class="ranking-sidebar">
        <div class="leaderboard-panel glass-panel">
          <div class="panel-header">
            <div class="header-icon">
              <el-icon><TrendCharts /></el-icon>
            </div>
            <div class="header-text">
              <h3>荣耀影响力</h3>
              <span>HALL OF FAME</span>
            </div>
          </div>

          <div class="ranking-list">
            <div 
              v-for="(user, index) in leaderboardData" 
              :key="user.name"
              class="rank-row"
              :class="getRankClass(index)"
            >
              <div class="rank-badge">
                <span v-if="index > 2">{{ index + 1 }}</span>
                <el-icon v-else><Medal /></el-icon>
              </div>

              <el-avatar :size="44" :src="user.avatar" class="rank-avatar" @click.stop="handleUserClick(user.name)" />

              <div class="rank-details">
                <div class="r-name">{{ user.name }}</div>
                <div class="r-dept">{{ user.department }}</div>
              </div>

              <div class="rank-stat">
                <span class="num">{{ user.count }}</span>
                <span class="unit">勋章</span>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>

</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import { 
  Grid, Timer, Trophy, OfficeBuilding, Search, Filter, TrendCharts, Medal,
  Cpu, Lightning, Star, UserFilled
} from '@element-plus/icons-vue';
import FlowerIcon from '../components/FlowerIcon.vue';

// --- Types ---
type ViewMode = 'grid' | 'timeline' | 'category' | 'department';

interface HonorItem {
  id: number;
  name: string;
  department: string;
  avatar: string;
  awardName: string;
  awardDate: string;
  category: 'innovation' | 'efficiency' | 'practice' | 'community';
  year: string;
  isMine?: boolean;
  flowers?: number;
  hasGivenFlower?: boolean;
}

// --- Configuration ---
const router = useRouter();
const route = useRoute();

const viewModes = [
  { key: 'grid', label: '荣誉墙', icon: Grid },
  { key: 'timeline', label: '时光轴', icon: Timer },
  { key: 'category', label: '按奖项', icon: Trophy },
  { key: 'department', label: '按部门', icon: OfficeBuilding },
];
const availableViewModes = computed(() => filterScope.value === 'mine'
  ? [viewModes[0]]
  : viewModes
);

// --- Mock Data ---
const honorList = ref<HonorItem[]>([
  { id: 1, name: '林星辰', department: '架构平台部', avatar: 'https://i.pravatar.cc/150?img=11', awardName: '2026年度 AI 技术突破奖', awardDate: '2026-01-05', category: 'innovation', year: '2026', isMine: true, flowers: 12, hasGivenFlower: false },
  { id: 2, name: '林星辰', department: '架构平台部', avatar: 'https://i.pravatar.cc/150?img=11', awardName: '云原生架构奖', awardDate: '2025-06-15', category: 'innovation', year: '2025', isMine: true, flowers: 8, hasGivenFlower: true },
  { id: 3, name: 'Sarah', department: 'UED 设计中心', avatar: 'https://i.pravatar.cc/150?img=5', awardName: '最佳 AI 辅助设计实践', awardDate: '2025-12-20', category: 'practice', year: '2025', flowers: 15, hasGivenFlower: false },
  { id: 4, name: '张伟', department: '效能工程部', avatar: 'https://i.pravatar.cc/150?img=3', awardName: 'Copilot 效能提升大师', awardDate: '2025-11-15', category: 'efficiency', year: '2025', flowers: 20, hasGivenFlower: true },
  { id: 5, name: '张伟', department: '效能工程部', avatar: 'https://i.pravatar.cc/150?img=3', awardName: 'DevOps 创新奖', awardDate: '2024-09-10', category: 'efficiency', year: '2024', flowers: 10, hasGivenFlower: false },
  { id: 6, name: '张伟', department: '效能工程部', avatar: 'https://i.pravatar.cc/150?img=3', awardName: '年度代码贡献王', awardDate: '2024-02-01', category: 'efficiency', year: '2024', flowers: 25, hasGivenFlower: false },
  { id: 7, name: '王强', department: '开源办公室', avatar: 'https://i.pravatar.cc/150?img=12', awardName: '社区布道师', awardDate: '2024-05-02', category: 'community', year: '2024', flowers: 18, hasGivenFlower: true },
  { id: 8, name: 'Emily', department: '数据智能部', avatar: 'https://i.pravatar.cc/150?img=9', awardName: 'RAG 知识库构建金奖', awardDate: '2025-08-01', category: 'innovation', year: '2025', isMine: true, flowers: 14, hasGivenFlower: false },
  { id: 9, name: '赵敏', department: '移动端开发', avatar: 'https://i.pravatar.cc/150?img=24', awardName: '端侧模型落地先锋', awardDate: '2024-11-10', category: 'practice', year: '2024', flowers: 9, hasGivenFlower: false },
]);

// --- State ---
const currentViewMode = ref<ViewMode>('grid');
const filterScope = ref<'all' | 'mine'>('all'); 
const searchQuery = ref('');
const activeSubFilter = ref<string>('全部'); 
const currentPage = ref(1);
const pageSize = ref(16);

// --- Computed Logic ---
const processedList = computed(() => {
  let result = honorList.value;
  if (filterScope.value === 'mine') {
    result = result.filter(item => item.isMine);
  }
  if (searchQuery.value) {
    result = result.filter(item => item.name.includes(searchQuery.value));
  }
  if (activeSubFilter.value !== '全部') {
    if (currentViewMode.value === 'category') {
      const map: Record<string, string> = { '技术创新': 'innovation', '效能提升': 'efficiency', '最佳实践': 'practice', '社区贡献': 'community' };
      const key = map[activeSubFilter.value];
      result = result.filter(item => item.category === key);
    } else if (currentViewMode.value === 'department') {
      result = result.filter(item => item.department === activeSubFilter.value);
    }
  }
  return result;
});

const paginatedList = computed(() => {
  if (currentViewMode.value === 'timeline') return processedList.value;
  const start = (currentPage.value - 1) * pageSize.value;
  return processedList.value.slice(start, start + pageSize.value);
});

const leaderboardData = computed(() => {
  const map = new Map<string, { name: string, department: string, avatar: string, count: number }>();
  processedList.value.forEach(item => {
    if (!map.has(item.name)) {
      map.set(item.name, { name: item.name, department: item.department, avatar: item.avatar, count: 0 });
    }
    const user = map.get(item.name)!;
    user.count++;
  });
  return Array.from(map.values()).sort((a, b) => b.count - a.count);
});

const allDepartments = computed(() => ['全部', ...Array.from(new Set(honorList.value.map(i => i.department)))]);
const allCategories = computed(() => ['全部', '技术创新', '效能提升', '最佳实践', '社区贡献']);
const showSecondaryFilter = computed(() => filterScope.value === 'all' && (currentViewMode.value === 'category' || currentViewMode.value === 'department'));
const activeFilterOptions = computed(() => {
  if (currentViewMode.value === 'category') return allCategories.value;
  if (currentViewMode.value === 'department') return allDepartments.value;
  return [];
});

const timelineData = computed(() => {
  const groups: Record<string, HonorItem[]> = {};
  const sorted = [...processedList.value].sort((a, b) => new Date(b.awardDate).getTime() - new Date(a.awardDate).getTime());
  sorted.forEach(item => {
    if (!groups[item.year]) groups[item.year] = [];
    groups[item.year].push(item);
  });
  return Object.keys(groups)
    .sort((a, b) => Number(b) - Number(a)) // 年份倒序
    .map(year => ({
      year,
      items: groups[year].sort((a, b) => new Date(b.awardDate).getTime() - new Date(a.awardDate).getTime())
    }));
});

const currentTimelineUser = computed(() => {
  if (currentViewMode.value !== 'timeline' || !searchQuery.value) return null;
  const userItems = processedList.value.filter(item => item.name === searchQuery.value);
  if (userItems.length === 0) return null;
  const firstItem = userItems[0];
  const totalFlowers = userItems.reduce((sum, item) => sum + (item.flowers || 0), 0);
  return {
    name: firstItem.name,
    avatar: firstItem.avatar,
    department: firstItem.department,
    totalFlowers
  };
});

watch(processedList, () => {
  currentPage.value = 1;
});

// --- UI Helpers ---
const getCategoryLabel = (cat: string) => {
  const labels = { innovation: '技术创新', efficiency: '效能提升', practice: '最佳实践', community: '社区贡献' };
  return labels[cat as keyof typeof labels];
};
const getCategoryIcon = (cat: string) => {
  const icons = { innovation: Cpu, efficiency: Lightning, practice: Star, community: UserFilled };
  return icons[cat as keyof typeof icons] || Trophy;
};
const getRankClass = (index: number) => {
  if (index === 0) return 'rank-1';
  if (index === 1) return 'rank-2';
  if (index === 2) return 'rank-3';
  return 'rank-normal';
};
const switchMode = (mode: string) => {
  currentViewMode.value = mode as ViewMode;
  activeSubFilter.value = '全部'; 
  if (mode !== 'timeline') {
    searchQuery.value = '';
    router.replace({ path: '/users' });
  }
};

const handleUserClick = (userName: string) => {
  router.push({
    path: '/users',
    query: {
      user: userName,
      view: 'timeline'
    }
  });
};

const handleAwardClick = (awardName: string) => {
  router.push({
    path: '/award-rules',
    query: { award: awardName }
  });
};

// 格式化日期为年月格式
const formatAwardDate = (dateStr: string) => {
  if (!dateStr) return '';
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}年${month}月`;
};

const handleGiveFlower = (item: HonorItem) => {
  if (item.hasGivenFlower) {
    ElMessage.warning('您已经送过花了');
    return;
  }
  item.flowers = (item.flowers || 0) + 1;
  item.hasGivenFlower = true;
  ElMessage.success('送花成功！');
};

// 计算用户收到的总花朵数
const getUserFlowersCount = (userName: string) => {
  return honorList.value
    .filter(item => item.name === userName)
    .reduce((sum, item) => sum + (item.flowers || 0), 0);
};

watch(filterScope, (val) => {
  if (val === 'mine') {
    currentViewMode.value = 'grid';
    activeSubFilter.value = '全部';
  }
});

watch(() => route.query.user, (val) => {
  if (typeof val === 'string' && val) {
    searchQuery.value = val;
    filterScope.value = 'all';
    currentViewMode.value = 'timeline';
  }
}, { immediate: true });

watch(() => route.query.view, (val) => {
  if (val === 'timeline' && filterScope.value === 'all') {
    currentViewMode.value = 'timeline';
  }
}, { immediate: true });

// 处理分页大小变化
const handleSizeChange = (val: number) => {
  pageSize.value = val
  currentPage.value = 1 // 重置到第一页
}

// 处理当前页变化
const handleCurrentChange = (val: number) => {
  currentPage.value = val
  // 滚动到顶部
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Outfit:wght@400;600;800&display=swap');

/* --- 0. 全局容器与背景 --- */
.honor-wall-container {
  width: 100%;
  min-height: 100vh;
  padding: 24px;
  max-width: 1600px;
  margin: 0 auto;
  background:
    radial-gradient(120% 140% at 18% 95%, #d1ebff 0%, #c1e5ff 28%, rgba(193, 229, 255, 0) 70%),
    radial-gradient(110% 130% at 85% 10%, #c2c7ef 0%, #c5cbf0 35%, rgba(194, 199, 239, 0) 72%),
    radial-gradient(120% 150% at 92% 85%, #bed9fd 0%, #b8d5fc 42%, rgba(184, 213, 252, 0) 78%),
    linear-gradient(135deg, #c1e5ff 0%, #c5cbf0 38%, #bed9fd 100%);
  font-family: 'Outfit', 'Helvetica Neue', sans-serif;
  color: #1e293b;
  position: relative;
  overflow: hidden;
  box-sizing: border-box;
}

/* 动态极光背景 */
.aurora-bg {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.light-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.6;
  animation: floatOrb 20s infinite ease-in-out;
}

.orb-1 {
  width: 50vw; height: 50vw;
  background: radial-gradient(circle, rgba(167, 243, 208, 0.4), rgba(59, 130, 246, 0.2));
  top: -10%; left: -10%;
  animation-delay: 0s;
}

.orb-2 {
  width: 40vw; height: 40vw;
  background: radial-gradient(circle, rgba(251, 207, 232, 0.5), rgba(236, 72, 153, 0.15));
  bottom: -10%; right: -5%;
  animation-delay: -5s;
}

.orb-3 {
  width: 30vw; height: 30vw;
  background: radial-gradient(circle, rgba(199, 210, 254, 0.5), rgba(99, 102, 241, 0.2));
  top: 40%; left: 30%;
  animation-delay: -10s;
}

@keyframes floatOrb {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -50px) scale(1.1); }
  66% { transform: translate(-20px, 20px) scale(0.9); }
}

/* --- 1. HUD & 顶部栏 (玻璃拟态) --- */
.hud-dashboard {
  position: relative;
  z-index: 10;
  margin-bottom: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.glass-pill {
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(20px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
  border-radius: 99px;
  padding: 5px;
  display: flex;
}

.hud-top-row { display: flex; justify-content: space-between; align-items: center; gap: 20px; }

/* 视图切换 */
.mode-tab {
  padding: 8px 18px;
  border-radius: 99px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #64748b;
  position: relative;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  font-weight: 600;
  font-size: 14px;
  
  &:hover { color: #3b82f6; background: rgba(255,255,255,0.5); }
  
  &.active {
    color: #fff;
    background: linear-gradient(135deg, #6366f1, #3b82f6);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
    .tab-glare { opacity: 0.3; transform: translateX(100%); }
  }
}
.tab-glare {
  position: absolute; top: 0; left: 0; width: 100%; height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.8), transparent);
  opacity: 0;
  transform: translateX(-100%);
  transition: transform 0.6s;
}

/* 开关与搜索 */
.hud-actions { display: flex; gap: 16px; align-items: center; }

.toggle-switch {
  position: relative;
  width: 130px;
  cursor: pointer;
  justify-content: space-between;
  padding: 0;
  overflow: hidden;
  
  span {
    flex: 1; z-index: 2; text-align: center; line-height: 36px; font-size: 13px; font-weight: 600; color: #64748b; transition: color 0.3s;
    &.active { color: #fff; }
  }
  
  .switch-track {
    position: absolute;
    width: 50%; height: 100%;
    background: #0f172a;
    border-radius: 99px;
    left: 0;
    transition: left 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
    &.is-mine { left: 50%; background: #4f46e5; }
  }
}

.crystal-input {
  width: 240px;
  :deep(.el-input__wrapper) {
    border-radius: 99px;
    background: rgba(255, 255, 255, 0.65) !important;
    backdrop-filter: blur(10px);
    box-shadow: none !important;
    border: 1px solid rgba(255,255,255,0.5);
    padding-left: 15px;
    transition: all 0.3s;
    &:hover, &.is-focus { 
      background: #fff !important; 
      box-shadow: 0 4px 15px rgba(0,0,0,0.05) !important; 
      width: 280px; 
    }
  }
}

/* 二级筛选 */
.secondary-filter-bar {
  display: flex; align-items: center; gap: 12px; padding: 12px 20px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  overflow: hidden;
}
.filter-icon-wrapper {
  width: 32px; height: 32px; border-radius: 50%; background: #e0e7ff; color: #4f46e5; display: flex; align-items: center; justify-content: center;
}
.chip-container {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: nowrap;
  overflow-x: auto;
  padding-bottom: 4px;
  width: 100%;
}
.gem-chip {
  padding: 6px 16px; border-radius: 12px; font-size: 13px; cursor: pointer;
  background: rgba(255,255,255,0.5); color: #475569; transition: all 0.3s;
  border: 1px solid transparent;
  white-space: nowrap;
  &:hover { background: #fff; transform: translateY(-2px); }
  &.active {
    background: #fff;
    color: #4f46e5;
    font-weight: 700;
    border-color: #c7d2fe;
    box-shadow: 0 4px 12px rgba(79, 70, 229, 0.15);
  }
}

/* --- 2. 布局 --- */
.cyber-layout { display: flex; gap: 30px; align-items: flex-start; z-index: 2; position: relative; }
.view-area { flex: 1; min-width: 0; }
.ranking-sidebar { width: 340px; flex-shrink: 0; position: sticky; top: 20px; }

/* --- 3. 核心卡片 (Honor Card 3D) - 重头戏 --- */
.card-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }

.honor-card-3d {
  position: relative;
  height: 280px;
  perspective: 1000px;
  
  /* 分类颜色变量 - 更鲜艳的背景 */
  &.innovation { 
    --theme-color: #06b6d4; 
    --bg-grad: linear-gradient(135deg, #bae6fd 0%, #7dd3fc 50%, #38bdf8 100%);
    --trophy-color: linear-gradient(135deg, #06b6d4 0%, #0891b2 50%, #0e7490 100%);
  }
  &.efficiency { 
    --theme-color: #8b5cf6; 
    --bg-grad: linear-gradient(135deg, #e9d5ff 0%, #d8b4fe 50%, #c084fc 100%);
    --trophy-color: linear-gradient(135deg, #8b5cf6 0%, #7c3aed 50%, #6d28d9 100%);
  }
  &.practice { 
    --theme-color: #f59e0b; 
    --bg-grad: linear-gradient(135deg, #fde68a 0%, #fcd34d 50%, #fbbf24 100%);
    --trophy-color: linear-gradient(135deg, #f59e0b 0%, #d97706 50%, #b45309 100%);
  }
  &.community { 
    --theme-color: #10b981; 
    --bg-grad: linear-gradient(135deg, #a7f3d0 0%, #6ee7b7 50%, #34d399 100%);
    --trophy-color: linear-gradient(135deg, #10b981 0%, #059669 50%, #047857 100%);
  }

  &:hover .card-content-glass {
    transform: translateY(-8px) scale(1.02);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08), 0 0 0 1px var(--theme-color);
  }
  &:hover .bg-decoration-icon { 
    transform: rotate(-10deg) scale(1.25); 
    opacity: 0.25; 
    filter: drop-shadow(0 6px 12px rgba(255, 193, 7, 0.5));
  }
  &:hover .halo-ring { transform: rotate(180deg) scale(1.1); border-color: var(--theme-color); }
}

.card-content-glass {
  height: 100%;
  background: linear-gradient(135deg, 
    rgba(255, 240, 200, 0.85) 0%, 
    rgba(255, 230, 180, 0.82) 25%,
    rgba(255, 220, 160, 0.78) 50%,
    rgba(200, 220, 255, 0.8) 75%,
    rgba(180, 210, 255, 0.85) 100%);
  backdrop-filter: blur(14px) saturate(120%);
  border-radius: 18px;
  border: 1px solid rgba(255, 210, 120, 0.4);
  padding: 16px;
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 12px 28px rgba(0,0,0,0.06), inset 0 1px 0 rgba(255,255,255,0.7);
  position: relative;
  overflow: hidden;
}

.card-content-glass::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 18px;
  border: 1px solid rgba(255, 190, 92, 0.6);
  box-shadow: inset 0 0 22px rgba(255, 190, 92, 0.3), inset 0 0 8px rgba(255, 255, 255, 0.4);
  pointer-events: none;
}

.card-content-glass::after {
  content: '';
  position: absolute;
  inset: -20%;
  background:
    radial-gradient(ellipse at 20% 20%, rgba(255, 255, 255, 0.3), transparent 50%),
    linear-gradient(120deg, rgba(255,255,255,0.08) 0%, rgba(255,255,255,0.4) 14%, rgba(255,255,255,0.05) 30%);
  opacity: 0.4;
  transform: translateX(-40%);
  animation: shimmerSweep 3s linear infinite;
  pointer-events: none;
}

/* 背景装饰 */
.bg-decoration-circle {
  position: absolute; top: -50px; right: -50px; width: 200px; height: 200px;
  background: var(--bg-grad);
  border-radius: 50%; filter: blur(40px); opacity: 0.75; z-index: 0;
  box-shadow: 0 0 60px rgba(255, 193, 7, 0.3);
}
.bg-decoration-icon {
  position: absolute; bottom: -15px; right: -15px; font-size: 150px; 
  opacity: 0.2; z-index: 0; transition: all 0.5s;
  transform-origin: center;
  color: var(--theme-color);
  
  :deep(svg) {
    width: 100%;
    height: 100%;
    filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
  }
}

.honor-card-3d.innovation .bg-decoration-icon {
  color: #06b6d4;
}

.honor-card-3d.efficiency .bg-decoration-icon {
  color: #8b5cf6;
}

.honor-card-3d.practice .bg-decoration-icon {
  color: #f59e0b;
}

.honor-card-3d.community .bg-decoration-icon {
  color: #10b981;
}

/* 卡片上部 */
.card-top { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; z-index: 1; }

.avatar-halo { position: relative; cursor: pointer; }
.halo-ring {
  position: absolute; inset: -4px; border-radius: 50%;
  border: 2px dashed #cbd5e1;
  transition: all 0.8s ease;
}
.user-avatar { border: 2px solid #fff; box-shadow: 0 4px 10px rgba(0,0,0,0.1); cursor: pointer; }

.user-info { flex: 1; }
.user-name { font-size: 15px; font-weight: 800; color: #1e293b; margin-bottom: 2px; }
.dept-badge {
  display: inline-block; font-size: 11px; padding: 2px 8px; border-radius: 6px;
  background: #f1f5f9; color: #64748b; font-weight: 600;
}

.year-ribbon {
  background: var(--theme-color); color: #fff;
  padding: 4px 8px; border-radius: 8px 0 8px 0;
  font-weight: 800; font-size: 11px;
  box-shadow: 2px 2px 8px rgba(0,0,0,0.15);
}

/* 卡片中部（奖项） */
.award-center {
  flex: 1; display: flex; flex-direction: column; justify-content: center; align-items: flex-start;
  text-align: left; z-index: 1;
}
.award-name {
  font-size: 16px; line-height: 1.3; color: #0f172a; margin: 0 0 10px 0;
  font-weight: 900;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  cursor: pointer;
}

@keyframes shimmerSweep {
  0% { transform: translateX(-50%); opacity: 0.25; }
  50% { transform: translateX(10%); opacity: 0.45; }
  100% { transform: translateX(60%); opacity: 0.25; }
}
.category-tag {
  display: inline-flex; align-items: center; gap: 6px;
  font-size: 12px; font-weight: 600; color: var(--theme-color);
  background: rgba(255,255,255,0.9); border: 1px solid var(--theme-color);
  padding: 4px 10px; border-radius: 16px;
  cursor: pointer;
}

/* 卡片底部 */
.card-bottom {
  border-top: 1px solid rgba(0,0,0,0.08);
  padding-top: 12px; margin-top: 8px;
  display: flex; justify-content: space-between; align-items: center;
  font-size: 12px; z-index: 1;
}

.date-text {
  color: #1e293b;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.7);
  padding: 4px 10px;
  border-radius: 8px;
  backdrop-filter: blur(4px);
}

.flower-section {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 12px;
  transition: all 0.3s ease;
  
  &:hover {
    background: rgba(244, 114, 182, 0.1);
    transform: scale(1.05);
  }
  
  .flower-icon-clickable {
    transition: all 0.3s ease;
  }
  
  .flower-count {
    font-size: 13px;
    font-weight: 700;
    color: #ec4899;
    min-width: 20px;
    text-align: center;
    background: rgba(255, 255, 255, 0.8);
    padding: 2px 6px;
    border-radius: 6px;
    backdrop-filter: blur(4px);
  }
}


/* --- 4. 排行榜 (Glory Sidebar) --- */
.leaderboard-panel {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(25px);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.panel-header {
  padding: 24px;
  display: flex; align-items: center; gap: 12px;
  background: linear-gradient(to bottom, rgba(255,255,255,0.5), rgba(255,255,255,0));
}
.header-icon {
  width: 40px; height: 40px; background: linear-gradient(135deg, #6366f1, #8b5cf6);
  border-radius: 12px; color: #fff; display: flex; align-items: center; justify-content: center;
  font-size: 20px; box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
}
.header-text h3 { margin: 0; font-size: 16px; font-weight: 800; color: #1e293b; }
.header-text span { font-size: 10px; letter-spacing: 1px; color: #94a3b8; font-weight: 700; }

.ranking-list { padding: 0 16px 24px 16px; }

.rank-row {
  display: flex; align-items: center; padding: 12px; margin-bottom: 8px;
  border-radius: 16px; transition: all 0.3s;
  background: transparent;
  
  &:hover { background: rgba(255,255,255,0.6); transform: translateX(4px); }
  
  /* 前三名样式 */
  &.rank-1 {
    background: linear-gradient(90deg, rgba(254, 243, 199, 0.6), transparent);
    border: 1px solid rgba(251, 191, 36, 0.2);
    .rank-badge { color: #d97706; text-shadow: 0 2px 4px rgba(217, 119, 6, 0.3); }
    .num { color: #d97706; }
  }
  &.rank-2 {
    background: linear-gradient(90deg, rgba(241, 245, 249, 0.8), transparent);
    border: 1px solid rgba(203, 213, 225, 0.5);
    .rank-badge { color: #64748b; }
    .num { color: #64748b; }
  }
  &.rank-3 {
    background: linear-gradient(90deg, rgba(255, 237, 213, 0.6), transparent);
    border: 1px solid rgba(253, 186, 116, 0.3);
    .rank-badge { color: #ea580c; }
    .num { color: #ea580c; }
  }
}

.rank-badge { width: 24px; font-weight: 800; font-style: italic; text-align: center; font-size: 16px; color: #cbd5e1; }
.rank-avatar { margin: 0 12px; border: 2px solid #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.08); cursor: pointer; }
.rank-details { flex: 1; }
.r-name { font-weight: 700; color: #334155; font-size: 14px; }
.r-dept { font-size: 11px; color: #94a3b8; }
.rank-stat { text-align: right; }
.num { display: block; font-weight: 800; font-size: 16px; line-height: 1; }
.unit { font-size: 10px; color: #94a3b8; }

/* --- 5. Timeline (Glass) --- */
.timeline-container { position: relative; padding: 20px; }

.timeline-user-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(25px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 10;
  
  .timeline-user-info {
    flex: 1;
    
    .timeline-user-name {
      margin: 0 0 12px 0;
      font-size: 24px;
      font-weight: 800;
      color: #1e293b;
    }
    
    .timeline-user-stats {
      display: flex;
      gap: 24px;
      align-items: center;
      
      .timeline-stat-item {
        display: flex;
        align-items: center;
        gap: 8px;
        
        .stat-value {
          font-size: 20px;
          font-weight: 800;
          color: #ec4899;
        }
        
        .stat-label {
          font-size: 14px;
          color: #64748b;
          font-weight: 600;
        }
      }
    }
  }
}

.timeline-line {
  position: absolute;
  left: 60px;
  top: 140px;
  bottom: 0;
  width: 4px;
  background: linear-gradient(
    to bottom,
    rgba(99, 102, 241, 0) 0%,
    rgba(99, 102, 241, 0.9) 25%,
    rgba(34, 211, 238, 0.9) 50%,
    rgba(244, 114, 182, 0.9) 75%,
    rgba(99, 102, 241, 0) 100%
  );
  box-shadow: 0 0 16px rgba(99, 102, 241, 0.4), 0 0 10px rgba(34, 211, 238, 0.35);
  border-radius: 999px;
}
.year-header { position: relative; margin-bottom: 20px; padding-left: 90px; }
.year-text {
  font-size: 32px;
  font-weight: 900;
  padding: 8px 14px;
  border-radius: 14px;
  background: linear-gradient(135deg, #6366f1 0%, #22d3ee 35%, #f472b6 70%, #a855f7 100%);
  color: #fff;
  box-shadow: 0 6px 18px rgba(99, 102, 241, 0.35);
  border: 1px solid rgba(255, 255, 255, 0.4);
}

.t-item { display: flex; align-items: center; margin-bottom: 24px; position: relative; }
.t-node { width: 12px; height: 12px; border-radius: 50%; background: #3b82f6; border: 3px solid #fff; box-shadow: 0 0 0 2px #dbeafe; position: absolute; left: 55px; z-index: 2; }
.t-card {
  margin-left: 90px; flex: 1; display: flex; align-items: center; gap: 16px; padding: 16px;
  background: rgba(255,255,255,0.8); border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.03); transition: transform 0.3s;
  
  &:hover { transform: translateX(10px); background: #fff; }
  &.innovation { border-left: 4px solid #06b6d4; }
  &.efficiency { border-left: 4px solid #8b5cf6; }
  &.practice { border-left: 4px solid #f59e0b; }
  &.community { border-left: 4px solid #10b981; }
}
.t-avatar { cursor: pointer; }
.t-info { .t-title { font-weight: 700; color: #1e293b; margin-bottom: 4px; } .t-meta { font-size: 12px; color: #64748b; } }

/* --- Pagination --- */
.pagination-bar { 
  display: flex; 
  justify-content: center; 
  margin-top: 30px; 
  padding: 20px 0;
}
:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #4f46e5;
}

</style>