<template>
  <div class="honor-wall-container">
    <div class="hud-dashboard">

      <div v-show="awardType === 'individual'" class="fade-in-content">
        <div class="hud-top-row">
          <div class="view-switcher glass-pill">
            <div
              v-for="(mode, index) in availableViewModes"
              :key="mode?.key || `mode-${index}`"
              class="mode-tab"
              :class="{ active: mode && currentViewMode === mode.key }"
              @click="mode && switchMode(mode.key)"
            >
              <el-icon v-if="mode"><component :is="mode.icon" /></el-icon>
              <span v-if="mode" class="tab-label">{{ mode.label }}</span>
              <div class="tab-glare"></div>
            </div>
          </div>

          <div class="hud-actions">
            <div class="toggle-switch glass-pill">
              <div class="switch-track" :class="{ 'is-mine': filterScope === 'mine' }"></div>
              <span :class="{ active: filterScope === 'all' }" @click="filterScope = 'all'">全员</span>
              <span :class="{ active: filterScope === 'mine' }" @click="filterScope = 'mine'">我的</span>
            </div>
          </div>
        </div>

        <transition name="fade-slide">
          <div v-if="showSecondaryFilter" class="secondary-filter-bar">
            <div class="filter-type-switcher">
              <div
                class="filter-type-tab"
                :class="{ active: honorFilterType === 'award' }"
                @click="honorFilterType = 'award'; activeSubFilter = '全部'; chipSearchQuery = ''"
              >
                <el-icon><Star /></el-icon>
                <span>按奖项名称</span>
              </div>
              <div
                class="filter-type-tab"
                :class="{ active: honorFilterType === 'department' }"
                @click="honorFilterType = 'department'; activeSubFilter = '全部'; chipSearchQuery = ''"
              >
                <el-icon><OfficeBuilding /></el-icon>
                <span>按获奖者部门</span>
              </div>
              <div v-if="currentViewMode === 'grid'" class="search-wrapper">
                <el-input
                  v-model="searchQuery"
                  placeholder="搜索荣誉获得者..."
                  class="crystal-input"
                  prefix-icon="Search"
                />
              </div>
            </div>
            <div class="chip-scroll-wrapper">
              <el-input
                v-model="chipSearchQuery"
                placeholder="搜索筛选项..."
                class="chip-search-input"
                clearable
                :prefix-icon="Search"
              />
              <el-button class="scroll-btn scroll-btn-left" :disabled="!canScrollLeft" @click="scrollChips('left')" circle size="small"><el-icon><ArrowLeft /></el-icon></el-button>
              <div ref="chipContainerRef" class="chip-container" @scroll="handleChipScroll">
                <div v-for="item in activeFilterOptions" :key="item" class="gem-chip" :class="{ active: activeSubFilter === item }" @click="activeSubFilter = item">{{ item }}</div>
                <div v-if="activeFilterOptions.length === 0 && chipSearchQuery" class="chip-empty-hint">未找到匹配的筛选项</div>
              </div>
              <el-button class="scroll-btn scroll-btn-right" :disabled="!canScrollRight" @click="scrollChips('right')" circle size="small"><el-icon><ArrowRight /></el-icon></el-button>
            </div>
          </div>
        </transition>
      </div>

      <div v-show="awardType === 'team'" class="team-award-container fade-in-content">

        <div class="luminous-timeline-wrapper">
          <div class="luminous-line"></div>
          <div class="years-track">
            <div
              v-for="year in teamAwardYears"
              :key="year"
              class="luminous-node"
              :class="{ active: selectedYear === year }"
              @click="handleYearChange(year)"
            >
              <div class="node-label">{{ year }}年</div>
            </div>
          </div>
        </div>

        <div class="gold-ribbon-row">
          <div
            v-for="(award, index) in currentTeamAwards"
            :key="award.id"
            class="gold-ribbon-btn"
            :class="{ active: activeTeamAwardIndex === index }"
            @click="activeTeamAwardIndex = index"
          >
            <div class="ribbon-shape">
              <span class="ribbon-text">{{ award.title }}</span>
              <div class="gold-shine"></div>
            </div>
            <div class="ribbon-tail-left"></div>
            <div class="ribbon-tail-right"></div>
          </div>
        </div>

        <!-- 多图网格展示（一行三个） -->
        <transition name="zoom-fade" mode="out-in">
          <div v-if="currentTeamAward && currentTeamAward.images && currentTeamAward.images.length > 0" :key="currentTeamAward.id" class="team-award-images-grid">
            <div
              v-for="img in currentTeamAward.images"
              :key="img.id"
              class="team-award-image-card"
              :class="{ 'is-expanded': expandedTeamId === img.id }"
            >
              <div class="image-card-wrapper" @click="toggleTeamExpand(img.id)">
                <img :src="img.image" :alt="img.winnerName" class="award-image" />
                <!-- 展开指示器 -->
                <div class="expand-indicator">
                  <el-icon :class="{ 'is-rotated': expandedTeamId === img.id }">
                    <ArrowDown />
                  </el-icon>
                </div>
                <!-- 右下角拟态七彩玻璃信息框 -->
                <div class="team-info-glass-box">
                  <div class="glass-rainbow-border"></div>
                  <div class="glass-content">
                    <div class="team-info-section">
                      <div class="team-name-text">{{ img.winnerName }}</div>
                      <div class="team-field-text">{{ img.teamField || '技术领域' }}</div>
                    </div>
                    <button
                      class="flower-gift-btn-mini"
                      :class="{ 'is-given': img.hasGivenFlower }"
                      @click.stop="handleGiveFlowerToTeam(img)"
                    >
                      <FlowerIcon
                        :filled="img.hasGivenFlower"
                        :size="14"
                        :color="img.hasGivenFlower ? '#ec4899' : '#94a3b8'"
                        :strokeColor="img.hasGivenFlower ? '#be185d' : '#64748b'"
                      />
                      <span class="flower-count-mini">{{ img.flowers || 0 }}</span>
                    </button>
                  </div>
                </div>
              </div>
              <!-- 下拉抽屉：获奖事迹 -->
              <transition name="drawer-slide">
                <div v-if="expandedTeamId === img.id" class="team-story-drawer">
                  <div class="drawer-header">
                    <el-icon><Trophy /></el-icon>
                    <span>获奖事迹</span>
                  </div>
                  <div class="drawer-content" v-html="img.story || '暂无获奖事迹描述'"></div>
                </div>
              </transition>
            </div>
          </div>
          <div v-else-if="currentTeamAward" class="empty-images">
            <el-empty description="该奖项暂无图片" />
          </div>
        </transition>
      </div>
    </div>

    <div v-if="awardType === 'individual'" class="cyber-layout fade-in-content">

      <div class="view-area">
        <transition-group v-if="currentViewMode !== 'timeline'" name="staggered-list" tag="div" class="card-grid">
          <div v-for="item in paginatedList" :key="item.id" class="honor-card-3d" :class="item.category">
            <div class="card-content-glass">
              <div class="bg-decoration-circle"></div>
              <div class="bg-decoration-icon"><el-icon><Trophy /></el-icon></div>
              <div class="card-top">
                <div class="avatar-halo" @click.stop="handleUserClick(item.userName)"><el-avatar :size="50" :src="item.avatar" class="user-avatar" /><div class="halo-ring"></div></div>
                <div class="user-info"><div class="user-name">{{ item.userName }}</div><div class="dept-badge">{{ item.department }}</div></div>
                <div class="year-ribbon"><span>{{ item.year }}</span></div>
              </div>
              <div class="award-center"><h3 class="award-name">{{ item.awardName }}</h3><div v-if="item.achievement" class="achievement-text">{{ item.achievement }}</div></div>
              <div class="card-bottom">
                <span class="date-text">获奖时间：{{ formatAwardDate(item.awardDate) }}</span>
                <button
                  class="flower-section-btn"
                  :class="{ 'is-given': item.hasGivenFlower }"
                  @click.stop="handleGiveFlower(item)"
                >
                    <span class="flower-icon-container">
                      <FlowerIcon
                        :filled="item.hasGivenFlower"
                        :size="16"
                        :color="item.hasGivenFlower ? '#ec4899' : '#94a3b8'"
                        :strokeColor="item.hasGivenFlower ? '#be185d' : '#64748b'"
                        class="flower-icon-anim"
                      />
                      <span v-if="!item.hasGivenFlower" class="flower-particles"></span>
                    </span>
                  <span class="flower-label">{{ item.hasGivenFlower ? '已送' : '送花' }}</span>
                  <span class="flower-num">{{ item.flowers || 0 }}</span>
                </button>
              </div>
            </div>
          </div>
        </transition-group>

        <div v-else class="timeline-container">
          <div v-if="currentTimelineUser" class="timeline-user-header glass-panel">
            <el-avatar :size="60" :src="currentTimelineUser.avatar" />
            <div class="timeline-user-info">
              <h3 class="timeline-user-name">{{ currentTimelineUser.userName }}</h3>
              <div class="timeline-user-stats">
                <span class="timeline-stat-item"><FlowerIcon :filled="true" :size="18" color="#f472b6" /><span class="stat-value">{{ currentTimelineUser.totalFlowers }}</span><span class="stat-label">总花朵数</span></span>
              </div>
            </div>
            <el-button text size="small" class="back-to-all-timeline" @click="currentTimelineUserName = null; router.replace({ path: '/users', query: { view: 'timeline' } })"><el-icon><ArrowLeft /></el-icon> 返回全部时光轴</el-button>
          </div>
          <div class="timeline-line"></div>
          <div v-for="block in timelineData" :key="block.year" class="timeline-group">
            <div class="year-header"><span class="year-text">{{ block.year }}</span></div>
            <div class="timeline-items">
              <div v-for="item in block.items" :key="item.id" class="t-item">
                <div class="t-node"></div>
                <div class="t-card glass-panel" :class="item.category">
                  <div class="t-avatar" @click.stop="handleUserClick(item.userName)"><el-avatar :size="40" :src="item.avatar" /></div>
                  <div class="t-info"><div class="t-title">{{ item.awardName }}</div><div class="t-meta">{{ item.userName }} · {{ item.awardDate }}</div></div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="paginatedList.length === 0 && currentViewMode !== 'timeline'" class="empty-zone"><el-empty description="暂无荣耀记录" :image-size="160" /></div>
        <div v-if="timelineData.length === 0 && currentViewMode === 'timeline'" class="empty-zone"><el-empty description="暂无时光轴记录" :image-size="160" /></div>
        <div v-if="currentViewMode !== 'timeline' && honorListTotal > 0" class="pagination-bar"><el-pagination background layout="total, sizes, prev, pager, next, jumper" :page-sizes="[10, 12, 20, 30, 50]" :page-size="pageSize" :current-page="currentPage" :total="honorListTotal" @size-change="handleSizeChange" @current-change="handleCurrentChange" /></div>
      </div>

      <div class="ranking-sidebar">
        <div class="leaderboard-panel">
          <div class="panel-header"><div class="header-icon"><el-icon><TrendCharts /></el-icon></div><div class="header-text"><h3>荣耀影响力</h3><span>HALL OF FAME</span></div></div>
          <div class="ranking-list">
            <div v-for="(user, index) in leaderboardData" :key="user.userName" class="rank-row" :class="getRankClass(index)"><div class="rank-badge"><span v-if="index > 2">{{ index + 1 }}</span><el-icon v-else><Medal /></el-icon></div><el-avatar :size="44" :src="user.avatar" class="rank-avatar" @click.stop="handleUserClick(user.userName)" /><div class="rank-details"><div class="r-name">{{ user.userName }}</div><div class="r-dept">{{ user.department }}</div></div><div class="rank-stat"><div class="stat-row"><span class="num">{{ user.count }}</span><span class="unit">勋章</span></div><div class="stat-row"><FlowerIcon :filled="true" :size="14" color="#f472b6" /><span class="num flowers">{{ user.totalFlowers }}</span></div></div></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 保持你原有的所有 import 和逻辑
import { ref, computed, watch, onMounted, onBeforeUnmount, nextTick } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import {
  Grid, Timer, Trophy, OfficeBuilding, TrendCharts, Medal,
  Star, ArrowLeft, ArrowRight, ArrowDown, Search
} from '@element-plus/icons-vue';
import FlowerIcon from '../components/FlowerIcon.vue';
// API 层 - 支持 Mock/Real API 自动切换
import { getTeamAwards, giveFlower, getHonorList, getLeaderboard, getAwardNames, getDepartments } from '../api/honor'
import type { HonorRecord, LeaderboardUser } from '../api/types'

// --- 类型定义 ---
type ViewMode = 'grid' | 'timeline';
type HonorFilterType = 'award' | 'department';

// 使用 API 层定义的 HonorRecord 类型
type HonorItem = HonorRecord

// --- 配置 ---
const router = useRouter();
const route = useRoute();

const viewModes = [
  { key: 'grid', label: '荣誉墙', icon: Grid },
  { key: 'timeline', label: '时光轴', icon: Timer },
];
const availableViewModes = computed(() => filterScope.value === 'mine' ? [viewModes[0]] : viewModes);

// --- 荣誉数据（通过 api/honor.ts 获取；内部支持 mock/real 切换）---
const honorList = ref<HonorItem[]>([])
const honorListLoading = ref(false)
const honorListTotal = ref(0) // 🔑 接口返回的总条数
const honorListTotalPages = ref(0) // 🔑 接口返回的总页数

// 下拉筛选项（优先走后端接口；无数据时降级为从 honorList 推导）
const awardNamesFromApi = ref<string[]>([])
const departmentNamesFromApi = ref<string[]>([])

// 荣誉影响力榜（优先走后端接口；无数据时降级为从 honorList 推导）
const leaderboardFromApi = ref<LeaderboardUser[]>([])

// 加载荣誉列表数据
const loadHonorList = async () => {
  honorListLoading.value = true
  try {
    const params = {
      page: currentPage.value,
      pageSize: pageSize.value,
      scope: filterScope.value as 'all' | 'mine',
      filterType: honorFilterType.value as 'award' | 'department',
      filterValue: activeSubFilter.value !== '全部' ? activeSubFilter.value : undefined,
      keyword: searchQuery.value || undefined,
      view: currentViewMode.value as 'grid' | 'timeline',
      userName: currentTimelineUserName.value || undefined
    }
    const response = await getHonorList(params)
    honorList.value = response.data.list
    // 🔑 保存接口返回的分页信息
    honorListTotal.value = response.data.total || 0
    honorListTotalPages.value = response.data.totalPages || 0
  } catch (error) {
    console.error('加载荣誉列表失败:', error)
    ElMessage.error('加载荣誉列表失败')
  } finally {
    honorListLoading.value = false
  }
}

// 加载奖项/部门筛选项
const loadHonorFilterOptions = async () => {
  try {
    const [awardsRes, deptsRes] = await Promise.all([getAwardNames(), getDepartments()])
    awardNamesFromApi.value = awardsRes.data.list || []
    departmentNamesFromApi.value = deptsRes.data.list || []
  } catch (e) {
    console.error('加载荣誉筛选项失败:', e)
  }
}

// 加载荣耀影响力榜
const loadLeaderboard = async () => {
  try {
    const params = {
      limit: 10,
      scope: filterScope.value as 'all' | 'mine',
      filterType: honorFilterType.value as 'award' | 'department',
      filterValue: activeSubFilter.value !== '全部' ? activeSubFilter.value : undefined,
    }
    const res = await getLeaderboard(params)
    leaderboardFromApi.value = res.data.list || []
  } catch (e) {
    console.error('加载荣耀影响力榜失败:', e)
  }
}

// --- 状态 ---
const awardType = ref<'individual' | 'team'>('individual'); // 默认展示个人风采
const currentViewMode = ref<ViewMode>('grid');
const filterScope = ref<'all' | 'mine'>('all');
const searchQuery = ref('');
const honorFilterType = ref<HonorFilterType>('award');
const activeSubFilter = ref<string>('全部');
const currentPage = ref(1);
const pageSize = ref(12);
const currentTimelineUserName = ref<string | null>(null);
const chipContainerRef = ref<HTMLElement | null>(null);
const canScrollLeft = ref(false);
const canScrollRight = ref(false);
const chipSearchQuery = ref(''); // 🔑 筛选项搜索关键词

// 团队奖状态
const selectedYear = ref<string>('2026'); // 默认选中最新
const activeTeamAwardIndex = ref<number>(0);
const expandedTeamId = ref<number | null>(null); // 当前展开的团队ID

// 切换团队获奖事迹展开/收起
const toggleTeamExpand = (teamId: number) => {
  if (expandedTeamId.value === teamId) {
    expandedTeamId.value = null;
  } else {
    expandedTeamId.value = teamId;
  }
};

const handleYearChange = (year: string) => {
  selectedYear.value = year;
  activeTeamAwardIndex.value = 0;
  notifyNavbarUpdate();
};

// 处理团队荣誉送花
const handleGiveFlowerToTeam = async (img: TeamAwardImage) => {
  if (img.hasGivenFlower) {
    ElMessage.warning('已送过花')
    return
  }
  try {
    const response = await giveFlower(img.id, 'team')
    img.flowers = response.data.flowers
    img.hasGivenFlower = response.data.hasGivenFlower
    ElMessage.success('送花成功！')
  } catch (error: unknown) {
    console.error('送花失败:', error)
    ElMessage.error((error as Error).message || '送花失败')
  }
};

// 团队奖数据（通过 api/honor.ts 获取；内部支持 mock/real 切换）
interface TeamAwardImage {
  id: number;
  image: string;
  imageType: 'url' | 'upload';
  winnerName: string; // 团队名称
  teamField?: string; // 团队领域
  story?: string; // 获奖事迹（HTML富文本）
  flowers?: number; // 花朵数
  hasGivenFlower?: boolean; // 是否已送花
}

interface TeamAward {
  id: number;
  title: string;
  year?: number | string;
  images: TeamAwardImage[];
}

// 加载团队奖项数据
const loadTeamAwards = async (): Promise<TeamAward[]> => {
  try {
    // 优先从API获取
    const response = await getTeamAwards()
    if (response && response.data && response.data.list && response.data.list.length > 0) {
      return response.data.list.map((item: TeamAward) => ({
        id: item.id,
        title: item.title,
        year: String(item.year || new Date().getFullYear()),
        images: item.images.map((img: TeamAwardImage) => ({
          id: img.id,
          image: img.image,
          imageType: 'url' as const,
          winnerName: img.winnerName,
          teamField: img.teamField || '',
          flowers: img.flowers || 0,
          hasGivenFlower: img.hasGivenFlower || false
        }))
      }));
    }
  } catch (e) {
    console.error('加载团队奖项失败:', e);
  }

  // 不在页面里写死 mock：mock/real 由 api/honor.ts 内部统一切换
  return [];
};

const teamAwards = ref<TeamAward[]>([]);

// 监听配置更新
const handleConfigUpdate = async () => {
  teamAwards.value = await loadTeamAwards();
  // 如果当前选中的年份没有奖项了，切换到最新年份
  const firstYear = teamAwardYears.value[0];
  if (currentTeamAwards.value.length === 0 && teamAwardYears.value.length > 0 && firstYear) {
    selectedYear.value = firstYear;
  }
  activeTeamAwardIndex.value = 0;
  // 通知导航栏更新
  notifyNavbarUpdate();
};

// 通知导航栏更新团队荣誉数据
const notifyNavbarUpdate = () => {
  window.dispatchEvent(new CustomEvent('teamAwardsUpdate', {
    detail: {
      awardType: awardType.value,
      years: teamAwardYears.value,
      selectedYear: selectedYear.value,
      currentTeamAwards: currentTeamAwards.value,
      activeTeamAwardIndex: activeTeamAwardIndex.value
    }
  }));
};

// 事件处理函数
const handleAwardTypeChange = ((e: CustomEvent) => {
  if (e.detail?.type) {
    const newType = e.detail.type as 'individual' | 'team'
    awardType.value = newType
    notifyNavbarUpdate()
  }
}) as EventListener;

const handleTeamAwardYearChange = ((e: CustomEvent) => {
  if (e.detail?.year) {
    selectedYear.value = e.detail.year;
    activeTeamAwardIndex.value = 0;
  }
}) as EventListener;

const handleTeamAwardIndexChange = ((e: CustomEvent) => {
  if (e.detail?.index !== undefined) {
    activeTeamAwardIndex.value = e.detail.index;
  }
}) as EventListener;

const handleStorageChange = (e: StorageEvent) => {
  if (e.key === 'admin_team_awards_config') {
    handleConfigUpdate();
  }
};

// 窗口大小变化时的处理（保留以防后续需要）
const handleResize = () => {
  // CSS flex 布局已自动处理滚动条
};

// 监听筛选条件变化，重新加载荣誉列表
watch(
  [filterScope, honorFilterType, activeSubFilter, searchQuery, currentViewMode, currentTimelineUserName, currentPage],
  () => {
    loadHonorList()
    // 榜单也跟随筛选条件更新（后端支持则用后端数据；否则降级本地计算）
    loadLeaderboard()
  }
)

// 🔑 单独监听pageSize变化，重置到第一页并重新加载
watch(pageSize, () => {
  if (currentPage.value !== 1) {
    currentPage.value = 1 // 这会触发上面的watch
  } else {
    loadHonorList() // currentPage已经是1，手动调用
  }
})

onMounted(async () => {
  // 先设置事件监听器（确保能接收到导航栏发送的事件）
  window.addEventListener('awardTypeChange', handleAwardTypeChange);
  window.addEventListener('teamAwardYearChange', handleTeamAwardYearChange);
  window.addEventListener('teamAwardIndexChange', handleTeamAwardIndexChange);
  window.addEventListener('adminConfigUpdated', handleConfigUpdate);
  // 初始化时也监听storage事件（跨标签页同步）
  window.addEventListener('storage', handleStorageChange);
  // 监听窗口大小变化
  window.addEventListener('resize', handleResize);

  // 初始化加载团队奖项
  teamAwards.value = await loadTeamAwards();

  // 默认选中最新的年份
  if (teamAwardYears.value.length > 0) {
    selectedYear.value = teamAwardYears.value[0]!;
  }

  // 初始化加载荣誉列表
  await loadHonorList();

  // 初始化加载筛选项/榜单
  await loadHonorFilterOptions();
  await loadLeaderboard();

  // 通知导航栏当前状态
  notifyNavbarUpdate();

  // 检查URL参数中是否有指定跳转到团队荣誉的参数
  const urlType = route.query.type as string
  const urlYear = route.query.year as string
  const urlAward = route.query.award as string

  if (urlType === 'team') {
    // 切换到团队荣誉视图
    awardType.value = 'team'

    // 如果指定了年份，切换到对应年份
    if (urlYear && teamAwardYears.value.includes(urlYear)) {
      selectedYear.value = urlYear
    }

    // 如果指定了奖项名称，查找并选中对应奖项
    if (urlAward) {
      const awardIndex = currentTeamAwards.value.findIndex(
        a => a.title === urlAward || a.title.includes(urlAward)
      )
      if (awardIndex !== -1) {
        activeTeamAwardIndex.value = awardIndex
      }
    }

    notifyNavbarUpdate()
  }
  // 默认使用初始值 'individual'
});

onBeforeUnmount(() => {
  window.removeEventListener('awardTypeChange', handleAwardTypeChange);
  window.removeEventListener('teamAwardYearChange', handleTeamAwardYearChange);
  window.removeEventListener('teamAwardIndexChange', handleTeamAwardIndexChange);
  window.removeEventListener('adminConfigUpdated', handleConfigUpdate);
  window.removeEventListener('storage', handleStorageChange);
  window.removeEventListener('resize', handleResize);
});

const teamAwardYears = computed(() => {
  // 保持由近到远 (倒序)
  return Array.from(new Set(teamAwards.value.map(a => String(a.year || new Date().getFullYear())))).sort((a, b) => Number(b) - Number(a));
});

const currentTeamAwards = computed(() => {
  return teamAwards.value.filter(a => String(a.year || '') === selectedYear.value);
});

const currentTeamAward = computed(() => {
  if (currentTeamAwards.value.length > 0 && activeTeamAwardIndex.value < currentTeamAwards.value.length) {
    return currentTeamAwards.value[activeTeamAwardIndex.value];
  }
  return null;
});

// 监听状态变化，通知导航栏
watch([awardType, selectedYear, activeTeamAwardIndex, currentTeamAwards], () => {
  notifyNavbarUpdate();
}, { deep: true });

// --- Computed Logic (保持原有逻辑) ---
const processedList = computed(() => {
  let result = honorList.value;
  if (currentViewMode.value === 'timeline' && currentTimelineUserName.value) {
    result = result.filter(item => item.userName === currentTimelineUserName.value);
  } else {
    if (filterScope.value === 'mine') result = result.filter(item => item.isMine);
    if (searchQuery.value) result = result.filter(item => item.userName.includes(searchQuery.value));
    if (currentViewMode.value === 'grid' && activeSubFilter.value !== '全部') {
      if (honorFilterType.value === 'award') result = result.filter(item => item.awardName === activeSubFilter.value);
      else if (honorFilterType.value === 'department') result = result.filter(item => item.department === activeSubFilter.value);
    }
  }
  return result;
});

const paginatedList = computed(() => {
  // 🔑 后端已经做了分页，前端直接使用返回的列表
  if (currentViewMode.value === 'timeline') return processedList.value;
  return honorList.value;
});

const leaderboardFallback = computed(() => {
  const map = new Map<string, LeaderboardUser>();
  processedList.value.forEach(item => {
    if (!map.has(item.userName)) {
      map.set(item.userName, {
        userName: item.userName,
        department: item.department,
        avatar: item.avatar,
        count: 0,
        totalFlowers: 0
      });
    }
    const user = map.get(item.userName);
    if (user) {
      user.count++;
      user.totalFlowers += item.flowers || 0;
    }
  });
  return Array.from(map.values()).sort((a, b) => b.count - a.count || b.totalFlowers - a.totalFlowers).slice(0, 10);
});

const leaderboardData = computed(() => (leaderboardFromApi.value.length > 0 ? leaderboardFromApi.value : leaderboardFallback.value));

const allDepartments = computed(() => {
  const list = departmentNamesFromApi.value.length > 0
    ? departmentNamesFromApi.value
    : Array.from(new Set(honorList.value.map(i => i.department))).filter(Boolean) as string[]
  return ['全部', ...list.filter(i => i !== '全部')]
});

const allAwards = computed(() => {
  const list = awardNamesFromApi.value.length > 0
    ? awardNamesFromApi.value
    : Array.from(new Set(honorList.value.map(i => i.awardName))).filter(Boolean) as string[]
  return ['全部', ...list.filter(i => i !== '全部')]
});
const showSecondaryFilter = computed(() => filterScope.value === 'all' && currentViewMode.value === 'grid');
const activeFilterOptions = computed(() => {
  if (currentViewMode.value === 'grid') {
    const options = honorFilterType.value === 'award' ? allAwards.value : allDepartments.value;
    // 🔑 根据搜索关键词过滤
    if (chipSearchQuery.value) {
      const keyword = chipSearchQuery.value.toLowerCase();
      return options.filter(item => item.toLowerCase().includes(keyword));
    }
    return options;
  }
  return [];
});

const timelineData = computed(() => {
  const groups: Record<string, HonorItem[]> = {};
  const sorted = [...processedList.value].sort((a, b) => new Date(b.awardDate).getTime() - new Date(a.awardDate).getTime());
  sorted.forEach(item => {
    const year = item.year;
    if (!groups[year]) {
      groups[year] = [];
    }
    groups[year].push(item);
  });
  return Object.keys(groups).sort((a, b) => Number(b) - Number(a)).map(year => {
    const yearGroup = groups[year];
    return { year, items: yearGroup || [] };
  });
});

const currentTimelineUser = computed(() => {
  if (currentViewMode.value !== 'timeline' || !currentTimelineUserName.value) return null;
  const userItems = honorList.value.filter(item => item.userName === currentTimelineUserName.value);
  const firstItem = userItems[0];
  if (!firstItem) return null;
  return {
    userName: firstItem.userName,
    avatar: firstItem.avatar,
    department: firstItem.department,
    totalFlowers: userItems.reduce((s, i) => s + (i.flowers || 0), 0)
  };
});

// --- Actions (保持原有逻辑) ---
const updateScrollButtons = () => { if (chipContainerRef.value) { canScrollLeft.value = chipContainerRef.value.scrollLeft > 0; canScrollRight.value = chipContainerRef.value.scrollLeft < chipContainerRef.value.scrollWidth - chipContainerRef.value.clientWidth - 1; } };
const scrollChips = (dir: 'left' | 'right') => { if (chipContainerRef.value) chipContainerRef.value.scrollTo({ left: chipContainerRef.value.scrollLeft + (dir === 'left' ? -200 : 200), behavior: 'smooth' }); };
const handleChipScroll = () => updateScrollButtons();
watch([activeFilterOptions, honorFilterType], () => nextTick(() => setTimeout(updateScrollButtons, 100)));
// 🔑 搜索关键词变化时，重置滚动位置到最左边
watch(chipSearchQuery, () => {
  nextTick(() => {
    if (chipContainerRef.value) {
      chipContainerRef.value.scrollLeft = 0;
    }
    setTimeout(updateScrollButtons, 100);
  });
});
onMounted(() => nextTick(() => setTimeout(updateScrollButtons, 200)));

const getRankClass = (idx: number) => ['rank-1', 'rank-2', 'rank-3'][idx] || 'rank-normal';
const switchMode = (mode: string) => { currentViewMode.value = mode as ViewMode; activeSubFilter.value = '全部'; currentTimelineUserName.value = null; searchQuery.value = ''; chipSearchQuery.value = ''; router.replace({ path: '/users' }); };
const handleUserClick = (userName: string) => { if (currentViewMode.value === 'grid') router.push({ path: '/users', query: { view: 'timeline', user: userName } }); else { currentTimelineUserName.value = userName; router.replace({ path: '/users', query: { view: 'timeline', user: userName } }); } };
const formatAwardDate = (d: string) => { const dt = new Date(d); return `${dt.getFullYear()}年${String(dt.getMonth()+1).padStart(2,'0')}月`; };
const handleGiveFlower = async (item: HonorItem) => {
  if (item.hasGivenFlower) {
    ElMessage.warning('已送过花')
    return
  }
  try {
    const response = await giveFlower(item.id)
    item.flowers = response.data.flowers
    item.hasGivenFlower = response.data.hasGivenFlower
    ElMessage.success('送花成功！')
  } catch (error: unknown) {
    console.error('送花失败:', error)
    ElMessage.error((error as Error).message || '送花失败')
  }
}
const handleSizeChange = (val: number) => {
  pageSize.value = val;
  // 🔑 pageSize 变化会触发 watch，自动重置页码并加载数据
};
const handleCurrentChange = (val: number) => {
  currentPage.value = val;
  window.scrollTo({ top: 0, behavior: 'smooth' });
  // 🔑 currentPage 变化会触发 watch，自动加载数据
};

watch(filterScope, (v) => { if (v === 'mine') { currentViewMode.value = 'grid'; activeSubFilter.value = '全部'; currentTimelineUserName.value = null; chipSearchQuery.value = ''; } });
watch(() => route.query.view, (v) => { if (v === 'timeline') { currentViewMode.value = 'timeline'; filterScope.value = 'all'; currentTimelineUserName.value = (route.query.user as string) || null; } else { currentViewMode.value = 'grid'; currentTimelineUserName.value = null; } }, { immediate: true });
watch(() => route.query.user, (v) => { if (currentViewMode.value === 'timeline') currentTimelineUserName.value = (v as string) || null; });

// 监听从首页跳转过来的团队荣誉参数
watch(() => [route.query.type, route.query.year, route.query.award], ([newType, newYear, newAward]) => {
  if (newType === 'team') {
    awardType.value = 'team'

    const urlYear = newYear as string
    const urlAward = newAward as string

    // 切换年份
    if (urlYear && teamAwardYears.value.includes(urlYear)) {
      selectedYear.value = urlYear
    }

    // 查找并选中对应奖项
    nextTick(() => {
      if (urlAward) {
        const awardIndex = currentTeamAwards.value.findIndex(
          a => a.title === urlAward || a.title.includes(urlAward)
        )
        if (awardIndex !== -1) {
          activeTeamAwardIndex.value = awardIndex
        }
      }
      notifyNavbarUpdate()
    })
  }
});
</script>

<style lang="scss">
/* 全局样式 */
</style>

<style scoped lang="scss">
@import url('https://fonts.googleapis.com/css2?family=Outfit:wght@400;600;800&display=swap');

/* 全局容器 */
.honor-wall-container {
  width: 100%;
  min-height: 100vh;
  padding: 24px;
  max-width: 1600px;
  margin: 0 auto;
  font-family: 'Outfit', sans-serif;
  color: #1e293b;
  position: relative;
  box-sizing: border-box; /* 🔑 确保padding不增加宽度 */
  overflow-x: hidden; /* 🔑 防止横向滚动 */
}

/* ================== 1. 团队奖：流光时光轴 ================== */
.luminous-timeline-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 0 30px 0;
  margin-bottom: 16px;
  overflow: visible; /* 允许光效溢出 */
}

/* 连接线 - 炫彩明显 */
.luminous-line {
  position: absolute;
  top: 50%;
  left: 8%;
  right: 8%;
  height: 4px;
  background: linear-gradient(
      90deg,
      rgba(99, 102, 241, 0.8) 0%,
      rgba(34, 211, 238, 0.9) 25%,
      rgba(244, 114, 182, 0.9) 50%,
      rgba(168, 85, 247, 0.9) 75%,
      rgba(99, 102, 241, 0.8) 100%
  );
  z-index: 0;
  border-radius: 999px;
  box-shadow:
    0 0 20px rgba(99, 102, 241, 0.6),
    0 0 40px rgba(34, 211, 238, 0.4),
    0 0 60px rgba(244, 114, 182, 0.3);
}

.luminous-line::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  width: 100%;
  height: 200%;
  transform: translateY(-50%);
  background: linear-gradient(
      90deg,
      transparent,
      rgba(99, 102, 241, 0.4),
      rgba(34, 211, 238, 0.5),
      rgba(244, 114, 182, 0.5),
      rgba(168, 85, 247, 0.4),
      transparent
  );
  border-radius: 999px;
  filter: blur(8px);
  z-index: -1;
}

.luminous-line::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.8), transparent);
  animation: lightFlow 3s infinite linear;
  border-radius: 999px;
}

@keyframes lightFlow {
  0% { transform: translateX(-100%); opacity: 0; }
  50% { opacity: 1; }
  100% { transform: translateX(100%); opacity: 0; }
}

.years-track {
  display: flex;
  justify-content: center;
  gap: 120px;
  position: relative;
  z-index: 2;
}

.luminous-node {
  position: relative;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.4s ease;
}

.node-label {
  font-size: 24px;
  font-weight: 900;
  color: #475569;
  transition: all 0.4s ease;
  text-shadow:
    0 0 10px rgba(255, 255, 255, 0.9),
    0 0 20px rgba(255, 255, 255, 0.7),
    0 0 30px rgba(255, 255, 255, 0.5),
    0 2px 8px rgba(0, 0, 0, 0.15);
  letter-spacing: 1px;
  position: relative;
}

/* 激活状态 */
.luminous-node.active .node-label {
  color: #f59e0b;
  font-size: 28px;
  font-weight: 900;
  text-shadow:
    0 0 12px rgba(255, 255, 255, 1),
    0 0 24px rgba(255, 255, 255, 0.8),
    0 0 36px rgba(255, 255, 255, 0.6),
    0 0 20px rgba(245, 158, 11, 0.8),
    0 0 40px rgba(245, 158, 11, 0.4),
    0 4px 12px rgba(0, 0, 0, 0.3);
  transform: scale(1.1);
}

/* ================== 2. 团队奖：黄金绶带按钮（与首页同步） ================== */
.gold-ribbon-row {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  gap: 24px;
  margin-bottom: 40px;
  padding: 0 20px;
}

.gold-ribbon-btn {
  cursor: pointer;
  position: relative;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  display: flex;
  justify-content: center;
  padding-bottom: 12px;
  margin: 0 10px; /* 左右增加间隙 */

  &:hover, &.active {
    transform: translateY(-4px) scale(1.03);

    .ribbon-shape {
      background: linear-gradient(
          180deg,
          rgba(255, 215, 0, 0.9) 0%,
          rgba(255, 179, 71, 0.9) 50%,
          rgba(255, 140, 0, 0.9) 100%
      );
      box-shadow:
        0 6px 20px rgba(255, 165, 0, 0.4),
        inset 0 1px 0 rgba(255, 255, 255, 0.4);
    }

    .ribbon-tail-left,
    .ribbon-tail-right {
      background: linear-gradient(180deg, #ff8c00 0%, #cc7000 100%);
    }
  }
}

.ribbon-shape {
  position: relative;
  /* 金色渐变 - 带透明度 */
  background: linear-gradient(
      180deg,
      rgba(255, 224, 102, 0.85) 0%,
      rgba(255, 201, 64, 0.85) 30%,
      rgba(255, 176, 32, 0.85) 70%,
      rgba(255, 149, 0, 0.85) 100%
  );
  border: none;
  padding: 12px 24px;
  min-width: 140px;
  text-align: center;
  color: #78350f;
  font-weight: 800;
  font-size: 14px;
  letter-spacing: 0.5px;
  box-shadow:
    0 2px 4px rgba(0, 0, 0, 0.05),
    0 8px 16px rgba(251, 191, 36, 0.15);
  overflow: hidden;
  border-radius: 2px;
  z-index: 2;
}

/* 绶带左尾巴 - 折叠效果 */
.ribbon-tail-left {
  position: absolute;
  top: 10px;
  left: -12px;
  width: 24px;
  height: 36px;
  background: linear-gradient(180deg, #d97706 0%, #b45309 100%);
  clip-path: polygon(100% 0, 100% 100%, 0 50%, 0 0);
  z-index: -1;
}

/* 绶带右尾巴 - 折叠效果 */
.ribbon-tail-right {
  position: absolute;
  top: 10px;
  right: -12px;
  width: 24px;
  height: 36px;
  background: linear-gradient(180deg, #d97706 0%, #b45309 100%);
  clip-path: polygon(0 0, 0 100%, 100% 50%, 100% 0);
  z-index: -1;
}

.ribbon-text {
  position: relative;
  z-index: 1;
  display: block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-shadow: 0 1px 0 rgba(255, 255, 255, 0.3);
}

.gold-shine {
  position: absolute;
  top: 0;
  left: -100%;
  width: 50%;
  height: 100%;
  background: linear-gradient(
      to right,
      transparent,
      rgba(255, 255, 255, 0.7),
      transparent
  );
  transform: skewX(-20deg);
  animation: shine 3s infinite;
}

@keyframes shine {
  0% { left: -100%; }
  50%, 100% { left: 150%; }
}

/* ================== 3. 团队奖：预览卡片 ================== */
// 团队奖项多图网格（一行三个）
.team-award-images-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 一行3个 */
  gap: 28px;
  max-width: 1400px;
  margin: 0 auto;
  padding: 20px 24px;
  align-items: start; /* 让每个卡片独立高度，不拉伸 */
}

.team-award-image-card {
  position: relative;
  border-radius: 12px;
  overflow: visible; /* 允许子元素溢出 */
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease, margin-bottom 0.3s ease;
  margin-bottom: 20px;
  cursor: pointer;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
  }

  &.is-expanded {
    margin-bottom: 200px; /* 展开时为抽屉留出空间 */
    box-shadow: 0 12px 32px rgba(99, 102, 241, 0.2);
    border-color: rgba(99, 102, 241, 0.3);
  }
}

// 展开指示器
.expand-indicator {
  position: absolute;
  top: 12px;
  right: 12px;
  width: 28px;
  height: 28px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6366f1;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 5;
  transition: all 0.3s ease;

  .el-icon {
    transition: transform 0.3s ease;

    &.is-rotated {
      transform: rotate(180deg);
    }
  }
}

// 下拉抽屉样式
.team-story-drawer {
  background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
  border-radius: 0 0 12px 12px;
  border-top: 1px solid rgba(99, 102, 241, 0.2);
  padding: 16px 20px;
  margin-top: -12px;

  .drawer-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 14px;
    font-weight: 600;
    color: #6366f1;
    margin-bottom: 12px;

    .el-icon {
      color: #f59e0b;
      font-size: 18px;
    }
  }

  .drawer-content {
    font-size: 14px;
    line-height: 1.7;
    color: #4b5563;

    p {
      margin: 8px 0;
    }

    ul, ol {
      margin: 8px 0;
      padding-left: 20px;
    }

    li {
      margin: 4px 0;
    }
  }
}

// 抽屉展开/收起动画
.drawer-slide-enter-active,
.drawer-slide-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}

.drawer-slide-enter-from,
.drawer-slide-leave-to {
  opacity: 0;
  max-height: 0;
  padding-top: 0;
  padding-bottom: 0;
}

.drawer-slide-enter-to,
.drawer-slide-leave-from {
  opacity: 1;
  max-height: 300px;
}

.image-card-wrapper {
  position: relative;
  width: 100%;
  padding-bottom: 56%; /* 16:9 宽屏比例 */
  overflow: visible; /* 允许玻璃框溢出 */
  border-radius: 12px;
}

.award-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 12px;
}

/* 右下角拟态七彩玻璃信息框 - 右下角，部分在图片外 */
.team-info-glass-box {
  position: absolute;
  bottom: -30px; /* 底部在图片外 */
  right: -20px; /* 右边也在图片外 */
  min-width: 200px;
  max-width: 280px;
  border-radius: 12px;
  overflow: visible;
  z-index: 10;

  /* 七彩玻璃背景 - 更加绚丽 */
  background: linear-gradient(
      135deg,
      rgba(255, 255, 255, 0.92) 0%,
      rgba(255, 235, 245, 0.88) 20%,
      rgba(235, 245, 255, 0.88) 40%,
      rgba(240, 255, 245, 0.88) 60%,
      rgba(255, 250, 235, 0.88) 80%,
      rgba(255, 255, 255, 0.92) 100%
  );
  backdrop-filter: blur(24px) saturate(200%);
  -webkit-backdrop-filter: blur(24px) saturate(200%);

  /* 更强的阴影效果 */
  box-shadow:
    0 10px 40px rgba(0, 0, 0, 0.15),
    0 4px 12px rgba(0, 0, 0, 0.1),
    inset 0 2px 2px rgba(255, 255, 255, 0.9),
    inset 0 -1px 1px rgba(0, 0, 0, 0.05);

  /* 细微边框增强玻璃感 */
  border: 1px solid rgba(255, 255, 255, 0.6);

  transition: all 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);

  &:hover {
    transform: translateY(-4px);
    box-shadow:
      0 16px 50px rgba(0, 0, 0, 0.18),
      0 6px 16px rgba(0, 0, 0, 0.12),
      inset 0 2px 2px rgba(255, 255, 255, 1);
  }
}

/* 七彩边框效果 */
.glass-rainbow-border {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
  background: linear-gradient(
      90deg,
      #f472b6 0%,
      #c084fc 20%,
      #60a5fa 40%,
      #34d399 60%,
      #fbbf24 80%,
      #f472b6 100%
  );
  background-size: 200% 100%;
  animation: rainbow-flow 3s linear infinite;
}

@keyframes rainbow-flow {
  0% { background-position: 0% 50%; }
  100% { background-position: 200% 50%; }
}

.glass-content {
  padding: 10px 12px;
  display: flex;
  flex-direction: row; /* 水平排列 */
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.team-info-section {
  display: flex;
  flex-direction: column;
  gap: 2px;
  flex: 1;
}

.team-name-text {
  color: #1e293b;
  font-size: 13px;
  font-weight: 700;
  line-height: 1.3;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.8);
  white-space: nowrap;
}

.team-field-text {
  color: #64748b;
  font-size: 11px;
  font-weight: 500;
  line-height: 1.3;
  display: flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;

  &::before {
    content: '';
    width: 4px;
    height: 4px;
    border-radius: 50%;
    background: linear-gradient(135deg, #f472b6, #c084fc, #60a5fa);
    flex-shrink: 0;
  }
}

/* 迷你送花按钮 */
.flower-gift-btn-mini {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  background: linear-gradient(135deg, #fce7f3 0%, #fbcfe8 100%);
  box-shadow: 0 2px 6px rgba(236, 72, 153, 0.15);
  transition: all 0.2s ease;
  flex-shrink: 0;

  &:hover {
    transform: scale(1.05);
    box-shadow: 0 3px 10px rgba(236, 72, 153, 0.25);
  }

  &.is-given {
    background: linear-gradient(135deg, #ec4899 0%, #db2777 100%);

    .flower-count-mini {
      color: #fff;
    }
  }
}

.flower-count-mini {
  font-size: 11px;
  font-weight: 600;
  color: #ec4899;
}

/* 拟物化送花按钮 */
.flower-gift-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-family: inherit;
  font-size: 11px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;

  /* 未送花状态 - 粉色渐变 */
  background: linear-gradient(135deg, #fce7f3 0%, #fbcfe8 50%, #f9a8d4 100%);
  color: #be185d;
  box-shadow:
    0 4px 12px rgba(236, 72, 153, 0.25),
    0 2px 4px rgba(236, 72, 153, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.6),
    inset 0 -1px 0 rgba(190, 24, 93, 0.1);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(
        90deg,
        transparent,
        rgba(255, 255, 255, 0.4),
        transparent
    );
    transition: left 0.5s ease;
  }

  &:hover {
    transform: translateY(-2px) scale(1.05);
    box-shadow:
      0 6px 20px rgba(236, 72, 153, 0.35),
      0 4px 8px rgba(236, 72, 153, 0.2),
      inset 0 1px 0 rgba(255, 255, 255, 0.8);

    &::before {
      left: 100%;
    }

    .flower-icon-animated {
      animation: flower-bounce 0.6s ease;
    }

    .flower-sparkle {
      opacity: 1;
    }
  }

  &:active {
    transform: translateY(0) scale(0.98);
  }

  /* 已送花状态 */
  &.is-given {
    background: linear-gradient(135deg, #fdf2f8 0%, #fce7f3 100%);
    color: #9ca3af;
    box-shadow:
      0 2px 8px rgba(0, 0, 0, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.8);
    cursor: default;

    &:hover {
      transform: none;
      box-shadow:
        0 2px 8px rgba(0, 0, 0, 0.08),
        inset 0 1px 0 rgba(255, 255, 255, 0.8);

      .flower-icon-animated {
        animation: none;
      }
    }
  }
}

.flower-icon-wrap {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.flower-icon-animated {
  transition: all 0.3s ease;
}

@keyframes flower-bounce {
  0%, 100% { transform: scale(1) rotate(0deg); }
  25% { transform: scale(1.2) rotate(-10deg); }
  50% { transform: scale(1.3) rotate(10deg); }
  75% { transform: scale(1.1) rotate(-5deg); }
}

/* 闪烁粒子效果 */
.flower-sparkle {
  position: absolute;
  width: 24px;
  height: 24px;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;

  &::before,
  &::after {
    content: '✨';
    position: absolute;
    font-size: 8px;
    animation: sparkle-float 1s ease infinite;
  }

  &::before {
    top: -6px;
    left: 50%;
    animation-delay: 0s;
  }

  &::after {
    top: 0;
    right: -4px;
    animation-delay: 0.3s;
  }
}

@keyframes sparkle-float {
  0%, 100% {
    transform: translateY(0) scale(1);
    opacity: 0.8;
  }
  50% {
    transform: translateY(-4px) scale(1.2);
    opacity: 1;
  }
}

.flower-text {
  white-space: nowrap;
}

.flower-count-badge {
  background: rgba(255, 255, 255, 0.9);
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 700;
  color: #ec4899;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.empty-images {
  max-width: 1200px;
  margin: 40px auto;
  padding: 40px;
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(20px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.preview-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 10px 0;
}
.preview-info h3 { font-size: 28px; font-weight: 900; margin: 0 0 5px 0; color: #1e293b; }
.team-name { font-size: 16px; color: #64748b; font-weight: 600; margin: 0; }
.divider { width: 40px; height: 4px; background: #f59e0b; margin: 20px 0; border-radius: 2px; }
.award-desc { font-size: 16px; line-height: 1.7; color: #475569; margin-bottom: auto; }
.award-date {
  align-self: flex-start;
  padding: 6px 16px;
  background: rgba(245, 158, 11, 0.1);
  color: #b45309;
  border-radius: 8px;
  font-weight: 700;
  font-size: 14px;
}


/* ================== 通用动画 ================== */
.fade-in-content {
  animation: contentFadeIn 0.5s ease-out;
  width: 100%; /* 🔑 占满父容器宽度 */
  max-width: 100%; /* 🔑 不超出父容器 */
  overflow: hidden; /* 🔑 隐藏超出部分 */
  box-sizing: border-box;
}
@keyframes contentFadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.zoom-fade-enter-active, .zoom-fade-leave-active { transition: all 0.3s ease; }
.zoom-fade-enter-from, .zoom-fade-leave-to { opacity: 0; transform: scale(0.98); }

/* --- 个人奖核心样式（完整版） --- */
.card-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 固定一行三个 */
  gap: 20px; /* 卡片间距 */
  width: 100%;
  box-sizing: border-box;
  padding: 10px; /* 🔑 添加padding容纳hover放大效果 */
  margin: -10px; /* 🔑 负margin抵消padding，保持原有布局 */
}

.honor-card-3d {
  position: relative;
  height: 420px; /* 加大卡片高度 */
  perspective: 1000px;
  min-width: 0; /* 防止 grid 项目溢出 */
  max-width: 100%;
  box-sizing: border-box;
  z-index: 1; /* 🔑 默认层级 */
  transition: z-index 0s 0.3s; /* 🔑 延迟重置z-index */

  &.innovation {
    --theme-color: #0891b2;
    --bg-grad: linear-gradient(135deg, #7dd3fc 0%, #38bdf8 50%, #0ea5e9 100%);
    --trophy-color: linear-gradient(135deg, #0891b2 0%, #0e7490 50%, #155e75 100%);
  }
  &.efficiency {
    --theme-color: #7c3aed;
    --bg-grad: linear-gradient(135deg, #d8b4fe 0%, #c084fc 50%, #a855f7 100%);
    --trophy-color: linear-gradient(135deg, #7c3aed 0%, #6d28d9 50%, #5b21b6 100%);
  }
  &.practice {
    --theme-color: #7c3aed;
    --bg-grad: linear-gradient(135deg, #d8b4fe 0%, #c084fc 50%, #a855f7 100%);
    --trophy-color: linear-gradient(135deg, #7c3aed 0%, #6d28d9 50%, #5b21b6 100%);
  }
  &.community {
    --theme-color: #059669;
    --bg-grad: linear-gradient(135deg, #6ee7b7 0%, #34d399 50%, #10b981 100%);
    --trophy-color: linear-gradient(135deg, #059669 0%, #047857 50%, #065f46 100%);
  }

  &:hover {
    z-index: 10; /* 🔑 hover时提升层级 */
    transition: z-index 0s 0s; /* 🔑 立即提升z-index */
  }
  &:hover .card-content-glass {
    transform: translateY(-8px) scale(1.02);
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.08), 0 0 0 1px var(--theme-color);
  }
  &:hover .bg-decoration-icon {
    transform: rotate(-10deg) scale(1.25);
    opacity: 0.25;
    filter: drop-shadow(0 6px 12px rgba(99, 102, 241, 0.3));
  }
  &:hover .halo-ring {
    transform: rotate(180deg) scale(1.1);
    border-color: var(--theme-color);
  }
}

.card-content-glass {
  height: 100%;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(14px) saturate(120%);
  border-radius: 18px;
  border: 1px solid rgba(255, 255, 255, 0.9);
  padding: 24px 16px 16px 16px; /* 🔑 上边距从16px增加到24px */
  display: flex;
  flex-direction: column;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  box-shadow: 0 12px 28px rgba(0,0,0,0.06), inset 0 1px 0 rgba(255,255,255,0.7);
  position: relative;
  overflow: hidden;
  box-sizing: border-box;
}

.card-content-glass::before {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 18px;
  border: 1px solid rgba(255, 255, 255, 0.4);
  box-shadow: inset 0 0 22px rgba(255, 255, 255, 0.2), inset 0 0 8px rgba(255, 255, 255, 0.4);
  pointer-events: none;
}

.card-content-glass::after {
  content: '';
  position: absolute;
  inset: -20%;
  background:
    radial-gradient(ellipse at 20% 20%, rgba(255, 255, 255, 0.3), transparent 50%),
    linear-gradient(120deg, rgba(255,255,255,0.08) 0%, rgba(255,255,255,0.4) 14%, rgba(255,255,255,0.05) 30%);
  opacity: 0.3;
  transform: translateX(-40%);
  animation: shimmerSweep 3s linear infinite;
  pointer-events: none;
}

@keyframes shimmerSweep {
  0% { transform: translateX(-50%); opacity: 0.25; }
  50% { transform: translateX(10%); opacity: 0.45; }
  100% { transform: translateX(60%); opacity: 0.25; }
}

.bg-decoration-circle {
  position: absolute;
  top: -50px;
  right: -50px;
  width: 200px;
  height: 200px;
  background: var(--bg-grad);
  border-radius: 50%;
  filter: blur(40px);
  opacity: 0.4;
  z-index: 0;
  box-shadow: 0 0 60px rgba(99, 102, 241, 0.2);
}

.bg-decoration-icon {
  position: absolute;
  bottom: -15px;
  right: -15px;
  font-size: 150px;
  opacity: 0.2;
  z-index: 0;
  transition: all 0.5s;
  transform-origin: center;
  color: var(--theme-color);

  :deep(svg) {
    width: 100%;
    height: 100%;
    filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
  }
}

/* 所有卡片都使用奖杯图标，统一颜色 */
.honor-card-3d.innovation .bg-decoration-icon { color: #0891b2; }
.honor-card-3d.efficiency .bg-decoration-icon { color: #7c3aed; }
.honor-card-3d.practice .bg-decoration-icon { color: #7c3aed; }
.honor-card-3d.community .bg-decoration-icon { color: #059669; }

.card-top {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 14px; /* 🔑 从6px增加到14px */
  z-index: 1;
  flex-shrink: 0;
}

.avatar-halo {
  position: relative;
  cursor: pointer;
}

.halo-ring {
  position: absolute;
  inset: -4px;
  border-radius: 50%;
  border: 2px dashed #cbd5e1;
  transition: all 0.8s ease;
}

.user-avatar {
  border: 2px solid #fff;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  cursor: pointer;
}

.user-info {
  flex: 1;
}

.user-name {
  font-size: 16px;
  font-weight: 900;
  color: #0f172a;
  margin-bottom: 2px;
}

.dept-badge {
  display: inline-block;
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 6px;
  background: linear-gradient(135deg, #e2e8f0, #cbd5e1);
  color: #334155;
  font-weight: 700;
}

.year-ribbon {
  background: var(--theme-color);
  color: #fff;
  padding: 4px 8px;
  border-radius: 8px 0 8px 0;
  font-weight: 800;
  font-size: 11px;
  box-shadow: 2px 2px 8px rgba(0,0,0,0.15);
}

/* 🔑 方案C核心样式：award-center 使用 flex 布局 */
.award-center {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  text-align: left;
  z-index: 1;
  padding-top: 2px;
  overflow: hidden;
  min-height: 0; /* 关键：允许 flex 子项收缩 */
}

.award-name {
  font-size: 15px;
  line-height: 1.3;
  color: #020617;
  margin: 0 0 4px 0;
  font-weight: 900;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  cursor: pointer;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  flex-shrink: 0; /* 不压缩 */
}

/* 🔑 achievement-text 改为滚动条显示 */
.achievement-text {
  font-size: 13px;
  line-height: 1.55;
  color: #475569;
  margin-top: 2px;
  flex: 1;
  min-height: 0;
  overflow-y: auto; /* 🔑 垂直方向超出显示滚动条 */
  overflow-x: hidden; /* 🔑 禁止横向滚动 */
  cursor: default;
  font-weight: 500;
  width: 100%;
  padding-right: 4px; /* 给滚动条留出空间 */

  /* 🔑 强制长文本/链接折行 */
  word-wrap: break-word;
  word-break: break-all;
  overflow-wrap: break-word;
  white-space: pre-wrap;

  /* 🔑 美化滚动条 */
  &::-webkit-scrollbar {
    width: 4px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
    border-radius: 4px;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(148, 163, 184, 0.4);
    border-radius: 4px;

    &:hover {
      background: rgba(148, 163, 184, 0.6);
    }
  }
}

.card-bottom {
  border-top: 1px solid rgba(0,0,0,0.08);
  padding-top: 8px;
  margin-top: 6px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  z-index: 1;
  flex-shrink: 0;
}

.date-text {
  color: #0f172a;
  font-weight: 700;
  background: rgba(255, 255, 255, 0.9);
  padding: 4px 10px;
  border-radius: 8px;
  backdrop-filter: blur(4px);
}

/* 个人荣誉送花按钮 - 拟物化设计 */
.flower-section-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 12px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-family: inherit;
  font-size: 12px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.34, 1.56, 0.64, 1);
  position: relative;
  overflow: hidden;

  /* 默认状态 - 柔和粉色 */
  background: linear-gradient(135deg, #fff1f5 0%, #fce7f3 50%, #fbcfe8 100%);
  color: #be185d;
  box-shadow:
    0 2px 8px rgba(236, 72, 153, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.8),
    inset 0 -1px 0 rgba(190, 24, 93, 0.1);

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.5), transparent);
    transition: left 0.4s ease;
  }

  &:hover {
    transform: translateY(-2px) scale(1.08);
    box-shadow:
      0 4px 16px rgba(236, 72, 153, 0.3),
      inset 0 1px 0 rgba(255, 255, 255, 0.9);

    &::before {
      left: 100%;
    }

    .flower-icon-anim {
      animation: flower-wiggle 0.5s ease;
    }

    .flower-particles {
      opacity: 1;
    }
  }

  &:active {
    transform: translateY(0) scale(0.95);
  }

  /* 已送花状态 */
  &.is-given {
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    color: #94a3b8;
    box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
    cursor: default;

    &:hover {
      transform: none;
      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);

      .flower-icon-anim {
        animation: none;
      }
    }
  }
}

.flower-icon-container {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}

.flower-icon-anim {
  transition: all 0.3s ease;
}

@keyframes flower-wiggle {
  0%, 100% { transform: rotate(0deg) scale(1); }
  20% { transform: rotate(-15deg) scale(1.1); }
  40% { transform: rotate(15deg) scale(1.2); }
  60% { transform: rotate(-10deg) scale(1.15); }
  80% { transform: rotate(10deg) scale(1.1); }
}

/* 飘散粒子效果 */
.flower-particles {
  position: absolute;
  width: 20px;
  height: 20px;
  opacity: 0;
  transition: opacity 0.3s ease;
  pointer-events: none;

  &::before,
  &::after {
    content: '🌸';
    position: absolute;
    font-size: 6px;
    animation: particle-drift 0.8s ease infinite;
  }

  &::before {
    top: -4px;
    left: 2px;
  }

  &::after {
    top: -2px;
    right: -2px;
    animation-delay: 0.2s;
  }
}

@keyframes particle-drift {
  0% {
    transform: translateY(0) rotate(0deg);
    opacity: 0;
  }
  50% {
    opacity: 1;
  }
  100% {
    transform: translateY(-8px) rotate(45deg);
    opacity: 0;
  }
}

.flower-label {
  white-space: nowrap;
}

.flower-num {
  background: rgba(255, 255, 255, 0.95);
  padding: 2px 6px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 700;
  color: #ec4899;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  min-width: 18px;
  text-align: center;
}

/* HUD & Layout 基础 */
.hud-dashboard {
  margin-bottom: 24px;
  position: relative;
  z-index: 5;
  width: 100%; /* 🔑 占满父容器宽度 */
  max-width: 100%; /* 🔑 不超出父容器 */
  overflow: hidden; /* 🔑 隐藏超出部分 */
  box-sizing: border-box;
}

.hud-top-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 20px;
  padding-left: 0;
}

.glass-pill {
  background: rgba(255,255,255,0.85);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255,255,255,0.9);
  border-radius: 99px;
  padding: 5px;
  display: flex;
}

.mode-tab {
  padding: 8px 18px;
  border-radius: 99px;
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  color: #64748b;
  font-weight: 600;
  transition: all 0.3s;

  &.active {
    color: #fff;
    background: linear-gradient(135deg, #6366f1, #3b82f6);
    box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
  }
}

.toggle-switch {
  width: 130px;
  cursor: pointer;
  position: relative;
  display: flex;
  align-items: center;

  span {
    flex: 1;
    text-align: center;
    z-index: 2;
    font-size: 13px;
    font-weight: 600;
    color: #64748b;
    transition: color 0.3s;
    position: relative;
    padding: 8px 0;

    &.active {
      color: #fff;
    }
  }

  .switch-track {
    position: absolute;
    width: 50%;
    height: calc(100% - 10px);
    background: #0f172a;
    border-radius: 99px;
    left: 5px;
    top: 5px;
    transition: left 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    z-index: 1;

    &.is-mine {
      left: calc(50% + 5px);
      background: #4f46e5;
    }
  }
}

.secondary-filter-bar {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 16px 0;
  /* 去掉背景 */
  align-items: flex-start;
  width: 100%; /* 🔑 占满父容器宽度 */
  max-width: 100%; /* 🔑 不超出父容器 */
  overflow: hidden; /* 🔑 隐藏超出部分 */
  box-sizing: border-box;
}

.filter-type-switcher {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px;
  background: rgba(255,255,255,0.8);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.85);
  width: fit-content;
}

.filter-type-tab {
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  display: flex;
  gap: 6px;
  align-items: center;
  transition: all 0.3s;
  color: #64748b;

  &:hover {
    background: rgba(255, 255, 255, 0.8);
    color: #4f46e5;
  }

  &.active {
    background: linear-gradient(135deg, #6366f1, #8b5cf6);
    color: #fff;
    box-shadow: 0 2px 8px rgba(99, 102, 241, 0.3);
  }
}

.chip-scroll-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  position: relative;
  width: 100%; /* 🔑 占满父容器宽度 */
  max-width: 100%; /* 🔑 不超出父容器 */
  overflow: hidden; /* 🔑 隐藏超出部分 */
}

/* 🔑 筛选项搜索框样式 */
.chip-search-input {
  width: 160px;
  flex-shrink: 0;

  :deep(.el-input__wrapper) {
    border-radius: 10px;
    background: rgba(255, 255, 255, 0.9) !important;
    backdrop-filter: blur(10px);
    box-shadow: none !important;
    border: 1px solid rgba(203, 213, 225, 0.6);
    transition: all 0.3s;
    height: 32px;

    &:hover {
      background: rgba(255, 255, 255, 0.95) !important;
      border-color: rgba(99, 102, 241, 0.3);
    }

    &.is-focus {
      background: rgba(255, 255, 255, 0.98) !important;
      border-color: #6366f1;
      box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.1) !important;
    }
  }

  :deep(.el-input__inner) {
    color: #1e293b;
    font-weight: 500;
    font-size: 12px;

    &::placeholder {
      color: #94a3b8;
    }
  }

  :deep(.el-input__prefix) {
    .el-icon {
      font-size: 14px;
      color: #94a3b8;
    }
  }

  :deep(.el-input__suffix) {
    .el-icon {
      font-size: 14px;
      color: #94a3b8;
      cursor: pointer;

      &:hover {
        color: #6366f1;
      }
    }
  }
}

.scroll-btn {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  background: rgba(255, 255, 255, 0.85) !important;
  border: 1px solid rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(10px);
  color: #475569 !important;
  transition: all 0.3s;

  &:hover:not(:disabled) {
    background: rgba(255, 255, 255, 0.95) !important;
    color: #4f46e5 !important;
    transform: scale(1.05);
  }

  &:disabled {
    opacity: 0.4;
    cursor: not-allowed;
  }

  .el-icon {
    font-size: 16px;
  }
}

.chip-container {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  overflow-y: hidden;
  padding-bottom: 4px;
  flex: 1;
  min-width: 0; /* 🔑 关键：允许flex子项收缩 */
  scrollbar-width: none;
  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }
}

/* 🔑 筛选项空状态提示 */
.chip-empty-hint {
  white-space: nowrap;
  font-size: 13px;
  color: #94a3b8;
  padding: 6px 16px;
}

.search-wrapper {
  margin-left: 8px;
}

.crystal-input {
  width: 200px;

  :deep(.el-input__wrapper) {
    border-radius: 99px;
    background: rgba(255, 255, 255, 0.85) !important;
    backdrop-filter: blur(10px);
    box-shadow: none !important;
    border: 1px solid rgba(255,255,255,0.85);
    transition: all 0.3s;
    height: 32px;

    &:hover {
      background: rgba(255, 255, 255, 0.95) !important;
      border-color: rgba(99, 102, 241, 0.3);
    }

    &.is-focus {
      background: rgba(255, 255, 255, 0.95) !important;
      border-color: #6366f1;
      box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.1) !important;
    }
  }

  :deep(.el-input__inner) {
    color: #1e293b;
    font-weight: 500;
    font-size: 13px;

    &::placeholder {
      color: #94a3b8;
    }
  }

  :deep(.el-input__prefix) {
    .el-icon {
      font-size: 14px;
      color: #94a3b8;
    }
  }
}

.gem-chip {
  padding: 6px 16px;
  border-radius: 12px;
  background: rgba(255,255,255,0.8);
  cursor: pointer;
  white-space: nowrap;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
  transition: all 0.3s;

  &:hover {
    background: rgba(255,255,255,0.9);
    transform: translateY(-2px);
  }

  &.active {
    background: rgba(255,255,255,0.95);
    color: #4f46e5;
    border: 1px solid #c7d2fe;
    font-weight: 700;
    box-shadow: 0 4px 12px rgba(79, 70, 229, 0.15);
  }
}

.cyber-layout {
  display: flex;
  gap: 30px;
  align-items: flex-start;
  z-index: 2;
  position: relative;
  overflow: visible; /* 🔑 允许子元素溢出 */
}

.view-area {
  flex: 1;
  min-width: 0; /* 关键：允许 flex 项目收缩到小于内容宽度 */
  overflow: visible; /* 🔑 改为 visible，允许 hover 效果溢出 */
  box-sizing: border-box;
}

.ranking-sidebar {
  width: 340px;
  flex-shrink: 0;
  position: sticky;
  top: 20px;
}

/* 排行榜完整样式 */
.leaderboard-panel {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(25px) saturate(200%);
  -webkit-backdrop-filter: blur(25px) saturate(200%);
  border-radius: 24px;
  border: 1px solid rgba(255, 255, 255, 0.9);
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.panel-header {
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 12px;
  background: linear-gradient(to bottom, rgba(255,255,255,0.5), rgba(255,255,255,0));
}

.header-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #4f46e5, #7c3aed);
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 12px;
  font-size: 20px;
  box-shadow: 0 4px 12px rgba(79, 70, 229, 0.5);
}

.header-text h3 {
  margin: 0;
  font-size: 17px;
  font-weight: 900;
  color: #0f172a;
}

.header-text span {
  font-size: 11px;
  letter-spacing: 1px;
  color: #475569;
  font-weight: 800;
}

.ranking-list {
  padding: 0 16px 24px 16px;
}

.rank-row {
  display: flex;
  align-items: center;
  padding: 12px;
  margin-bottom: 8px;
  border-radius: 16px;
  transition: all 0.3s;
  background: transparent;

  &:hover {
    background: rgba(255,255,255,0.85);
    transform: translateX(4px);
  }

  &.rank-1 {
    background: linear-gradient(90deg, rgba(254, 243, 199, 0.8), transparent);
    border: 1px solid rgba(251, 191, 36, 0.4);
    .rank-badge { color: #b45309; text-shadow: 0 2px 4px rgba(180, 83, 9, 0.5); }
    .num { color: #b45309; font-weight: 900; }
    .r-name { color: #0f172a; font-weight: 900; }
  }
  &.rank-2 {
    background: linear-gradient(90deg, rgba(241, 245, 249, 0.9), transparent);
    border: 1px solid rgba(148, 163, 184, 0.6);
    .rank-badge { color: #475569; }
    .num { color: #475569; font-weight: 900; }
    .r-name { color: #1e293b; font-weight: 900; }
  }
  &.rank-3 {
    background: linear-gradient(90deg, rgba(255, 237, 213, 0.8), transparent);
    border: 1px solid rgba(253, 186, 116, 0.5);
    .rank-badge { color: #c2410c; }
    .num { color: #c2410c; font-weight: 900; }
    .r-name { color: #1e293b; font-weight: 900; }
  }
}

.rank-badge {
  width: 24px;
  font-weight: 900;
  font-style: italic;
  text-align: center;
  font-size: 17px;
  color: #64748b;
}

.rank-avatar {
  margin: 0 12px;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
  cursor: pointer;
}

.rank-details {
  flex: 1;
}

.r-name {
  font-weight: 800;
  color: #0f172a;
  font-size: 15px;
}

.r-dept {
  font-size: 12px;
  color: #475569;
  font-weight: 600;
}

.rank-stat {
  text-align: right;
  display: flex;
  flex-direction: column;
  gap: 4px;
  align-items: flex-end;
}

.stat-row {
  display: flex;
  align-items: center;
  gap: 4px;
}

.num {
  display: block;
  font-weight: 900;
  font-size: 17px;
  line-height: 1;
}

.num.flowers {
  font-size: 15px;
  color: #db2777;
  font-weight: 900;
}

.unit {
  font-size: 11px;
  color: #475569;
  font-weight: 700;
}

/* Timeline 完整样式 */
.timeline-container {
  position: relative;
  padding: 0 20px 20px 20px;
}

.timeline-user-header {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 24px;
  margin-bottom: 30px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(25px);
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.95);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.06);
  position: relative;
  z-index: 10;
}

.timeline-user-info {
  flex: 1;
}

.timeline-user-name {
  margin: 0 0 12px 0;
  font-size: 24px;
  font-weight: 800;
  color: #0f172a;
}

.timeline-user-stats {
  display: flex;
  gap: 20px;
}

.timeline-stat-item {
  display: flex;
  align-items: center;
  gap: 8px;

  .stat-value {
    font-size: 18px;
    font-weight: 800;
    color: #0f172a;
  }

  .stat-label {
    font-size: 13px;
    color: #64748b;
    font-weight: 600;
  }
}

.back-to-all-timeline {
  color: #64748b;
  font-weight: 600;

  &:hover {
    color: #4f46e5;
  }
}

.timeline-line {
  position: absolute;
  left: 60px;
  top: 20px;
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

.year-header {
  position: relative;
  margin-bottom: 20px;
  padding-left: 90px;
}

.year-text {
  font-size: 32px;
  font-weight: 900;
  padding: 8px 14px;
  border-radius: 14px;
  background: linear-gradient(135deg, #6366f1 0%, #22d3ee 35%, #f472b6 70%, #a855f7 100%);
  background-clip: padding-box;
  -webkit-background-clip: padding-box;
  color: #fff;
  box-shadow: 0 6px 18px rgba(99, 102, 241, 0.35);
  border: 1px solid rgba(255, 255, 255, 0.4);
  display: inline-block;
}

.timeline-items {
  margin-bottom: 30px;
}

.t-item {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
  position: relative;
}

.t-node {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #3b82f6;
  border: 3px solid #fff;
  box-shadow: 0 0 0 2px #dbeafe;
  position: absolute;
  left: 55px;
  z-index: 2;
}

.t-card {
  margin-left: 90px;
  flex: 1;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: rgba(255,255,255,0.9);
  border-radius: 16px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.03);
  transition: transform 0.3s;

  &:hover {
    transform: translateX(10px);
    background: rgba(255,255,255,0.95);
  }

  &.innovation { border-left: 4px solid #06b6d4; }
  &.efficiency { border-left: 4px solid #8b5cf6; }
  &.practice { border-left: 4px solid #8b5cf6; }
  &.community { border-left: 4px solid #10b981; }
}

.t-avatar {
  cursor: pointer;
}

.t-info {
  flex: 1;
}

.t-title {
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 4px;
  cursor: pointer;

  &:hover {
    color: #4f46e5;
  }
}

.t-meta {
  font-size: 12px;
  color: #64748b;
}

/* 分页样式 */
.pagination-bar {
  display: flex;
  justify-content: center;
  margin-top: 30px;
  padding: 20px 0;
}

:deep(.el-pagination.is-background .el-pager li:not(.is-disabled).is-active) {
  background-color: #4f46e5;
}

.empty-zone {
  padding: 60px 20px;
  text-align: center;
}

/* 响应式 */
@media (max-width: 1024px) {
  .cyber-layout {
    flex-direction: column;
  }

  .ranking-sidebar {
    width: 100%;
    position: static;
  }

  .preview-container {
    flex-direction: column;
  }

  .hud-top-row {
    flex-direction: column;
    gap: 16px;
  }

  .card-grid {
    grid-template-columns: repeat(2, 1fr); /* 小屏幕一行两个 */
    padding: 10px;
    margin: -10px;
  }
}

@media (max-width: 600px) {
  .card-grid {
    grid-template-columns: 1fr; /* 手机端一行一个 */
    padding: 10px;
    margin: -10px;
  }
}
</style>
