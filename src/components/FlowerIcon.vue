<template>
  <svg
    :class="['flower-icon', { 'flower-filled': filled }]"
    viewBox="0 0 24 24"
    :width="size"
    :height="size"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      d="M12 2C8.13 2 5 5.13 5 9c0 2.38 1.19 4.47 3 5.74V17c0 .55.45 1 1 1h6c.55 0 1-.45 1-1v-2.26c1.81-1.27 3-3.36 3-5.74 0-3.87-3.13-7-7-7zm0 2c2.76 0 5 2.24 5 5 0 1.88-1.12 3.53-2.82 4.24L14 14h-4l-.18-.76C8.12 12.53 7 10.88 7 9c0-2.76 2.24-5 5-5z"
      :fill="filled ? color : 'none'"
      :stroke="filled ? 'none' : effectiveStrokeColor"
      stroke-width="1.5"
      stroke-linejoin="round"
    />
    <circle cx="9" cy="9" r="1.5" :fill="filled ? '#fff' : color" />
    <circle cx="15" cy="9" r="1.5" :fill="filled ? '#fff' : color" />
    <circle cx="12" cy="6" r="1.5" :fill="filled ? '#fff' : color" />
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

