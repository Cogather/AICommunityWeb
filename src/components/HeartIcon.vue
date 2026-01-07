<template>
  <svg
    :class="['heart-icon', { 'heart-filled': filled }]"
    viewBox="0 0 24 24"
    :width="size"
    :height="size"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      v-if="filled"
      d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
      :fill="color"
      stroke="none"
    />
    <path
      v-else
      d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"
      fill="none"
      :stroke="strokeColor"
      stroke-width="1.5"
      stroke-linejoin="round"
    />
  </svg>
</template>

<script setup lang="ts">
interface Props {
  filled?: boolean
  size?: number | string
  color?: string
  strokeColor?: string
}

withDefaults(defineProps<Props>(), {
  filled: false,
  size: 16,
  color: 'currentColor',
  strokeColor: 'currentColor'
})
</script>

<style scoped lang="scss">
.heart-icon {
  display: inline-block;
  vertical-align: middle;
  transition: all 0.3s ease;

  &.heart-filled {
    animation: heartBeat 0.3s ease;
  }
}

@keyframes heartBeat {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>

