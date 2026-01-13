<template>
  <svg
    :class="['flower-icon', { 'flower-filled': filled }]"
    viewBox="0 0 24 24"
    :width="size"
    :height="size"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <!-- 花茎 -->
    <path
      d="M12 14v8"
      :stroke="filled ? '#22c55e' : '#86efac'"
      stroke-width="2"
      stroke-linecap="round"
    />
    <!-- 叶子 -->
    <path
      d="M12 18c-2-1-3-2-3-3s1.5-1 3 1"
      :fill="filled ? '#22c55e' : 'none'"
      :stroke="filled ? '#22c55e' : '#86efac'"
      stroke-width="1.5"
      stroke-linecap="round"
    />
    <path
      d="M12 16c2-1 3-2 3-3s-1.5-1-3 1"
      :fill="filled ? '#22c55e' : 'none'"
      :stroke="filled ? '#22c55e' : '#86efac'"
      stroke-width="1.5"
      stroke-linecap="round"
    />
    <!-- 花瓣 - 5片花瓣呈放射状 -->
    <ellipse
      cx="12" cy="6"
      rx="2.5" ry="4"
      :fill="filled ? color : 'none'"
      :stroke="effectiveStrokeColor"
      stroke-width="1.2"
    />
    <ellipse
      cx="7.5" cy="9"
      rx="2.5" ry="4"
      transform="rotate(-72 7.5 9)"
      :fill="filled ? color : 'none'"
      :stroke="effectiveStrokeColor"
      stroke-width="1.2"
    />
    <ellipse
      cx="16.5" cy="9"
      rx="2.5" ry="4"
      transform="rotate(72 16.5 9)"
      :fill="filled ? color : 'none'"
      :stroke="effectiveStrokeColor"
      stroke-width="1.2"
    />
    <ellipse
      cx="9" cy="12.5"
      rx="2.5" ry="4"
      transform="rotate(-36 9 12.5)"
      :fill="filled ? color : 'none'"
      :stroke="effectiveStrokeColor"
      stroke-width="1.2"
    />
    <ellipse
      cx="15" cy="12.5"
      rx="2.5" ry="4"
      transform="rotate(36 15 12.5)"
      :fill="filled ? color : 'none'"
      :stroke="effectiveStrokeColor"
      stroke-width="1.2"
    />
    <!-- 花蕊 -->
    <circle
      cx="12" cy="10" r="2.5"
      :fill="filled ? '#fbbf24' : '#fef3c7'"
      :stroke="filled ? '#f59e0b' : '#fcd34d'"
      stroke-width="1"
    />
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue'

interface Props {
  filled?: boolean
  size?: number | string
  color?: string
  strokeColor?: string
}

const props = withDefaults(defineProps<Props>(), {
  filled: false,
  size: 16,
  color: '#f472b6',
  strokeColor: undefined
})

const effectiveStrokeColor = computed(() => props.strokeColor || props.color)
</script>

<style scoped lang="scss">
.flower-icon {
  display: inline-block;
  vertical-align: middle;
  transition: all 0.3s ease;

  &.flower-filled {
    animation: flowerPulse 0.4s ease;
  }
}

@keyframes flowerPulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2) rotate(10deg);
  }
  100% {
    transform: scale(1);
  }
}
</style>

