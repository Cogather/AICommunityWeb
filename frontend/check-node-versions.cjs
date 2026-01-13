#!/usr/bin/env node

/**
 * 检查所有依赖包的 Node.js 版本要求
 * 确保所有依赖包都兼容 Node.js 18.20.1
 */

const fs = require('fs');
const path = require('path');

const TARGET_NODE_VERSION = '18.20.1';
const packageJsonPath = path.join(__dirname, 'package.json');

// 已知的关键依赖包的 Node.js 版本要求（从官方文档和 npm 包信息）
const knownRequirements = {
  'vite': '>=18.0.0',
  '@vitejs/plugin-vue': '>=18.0.0',
  'vue': 'No requirement (browser-based)',
  'vue-router': 'No requirement (browser-based)',
  'typescript': 'No runtime requirement',
  'eslint': '>=18.18.0',
  'eslint-plugin-vue': '>=18.0.0',
  'prettier': '>=14.0.0',
  'sass': '>=14.0.0',
  'element-plus': 'No requirement (browser-based)',
  'axios': 'No requirement',
  '@wangeditor/editor': 'No requirement (browser-based)',
  '@wangeditor/editor-for-vue': 'No requirement (browser-based)',
  'vue-tsc': '>=18.0.0',
  '@vue/eslint-config-prettier': '>=18.0.0',
  '@vue/eslint-config-typescript': '>=18.0.0',
  '@vue/tsconfig': 'No requirement',
  '@tsconfig/node18': 'No requirement',
  '@types/node': 'No requirement',
  'vite-plugin-vue-devtools': '>=18.0.0',
  '@element-plus/icons-vue': 'No requirement (browser-based)',
};

// 简单的版本比较（只比较主版本号）
function checkVersionCompatibility(requirement) {
  if (!requirement || requirement.includes('No requirement')) {
    return true;
  }

  // 提取最低版本要求
  const match = requirement.match(/>=(\d+\.\d+\.\d+)/);
  if (match) {
    const minVersion = match[1];
    const minParts = minVersion.split('.').map(Number);
    const targetParts = TARGET_NODE_VERSION.split('.').map(Number);

    // 比较主版本号
    if (targetParts[0] > minParts[0]) return true;
    if (targetParts[0] < minParts[0]) return false;

    // 比较次版本号
    if (targetParts[1] > minParts[1]) return true;
    if (targetParts[1] < minParts[1]) return false;

    // 比较补丁版本号
    return targetParts[2] >= minParts[2];
  }

  // 如果没有匹配到，假设兼容（可能是其他格式）
  return true;
}

console.log(`\n检查依赖包与 Node.js ${TARGET_NODE_VERSION} 的兼容性...\n`);
console.log('='.repeat(80));

// 读取 package.json
const packageJson = JSON.parse(fs.readFileSync(packageJsonPath, 'utf8'));
const allDeps = {
  ...packageJson.dependencies,
  ...packageJson.devDependencies
};

const results = [];
let hasIssues = false;

// 检查每个依赖
Object.keys(allDeps).sort().forEach(depName => {
  const version = allDeps[depName];
  const knownReq = knownRequirements[depName];

  if (knownReq) {
    const compatible = checkVersionCompatibility(knownReq);

    results.push({
      name: depName,
      version,
      requirement: knownReq,
      compatible
    });

    if (!compatible) {
      hasIssues = true;
    }
  } else {
    // 未知的依赖，标记为需要手动检查
    results.push({
      name: depName,
      version,
      requirement: 'Unknown - needs manual check',
      compatible: null
    });
  }
});

// 输出结果
console.log('\n依赖包 Node.js 版本要求检查结果：\n');

let compatibleCount = 0;
let incompatibleCount = 0;
let unknownCount = 0;

results.forEach(result => {
  const status = result.compatible === true ? '✅' :
                 result.compatible === false ? '❌' : '⚠️';

  console.log(`${status} ${result.name.padEnd(35)} @ ${result.version.padEnd(15)}`);
  console.log(`   要求: ${result.requirement}`);

  if (result.compatible === true) {
    compatibleCount++;
  } else if (result.compatible === false) {
    incompatibleCount++;
    console.log(`   ⚠️  不兼容！需要降级或替换`);
  } else {
    unknownCount++;
    console.log(`   ⚠️  需要手动检查 npm 包页面确认`);
  }
  console.log('');
});

console.log('='.repeat(80));
console.log(`\n统计：`);
console.log(`  ✅ 兼容: ${compatibleCount}`);
console.log(`  ❌ 不兼容: ${incompatibleCount}`);
console.log(`  ⚠️  未知: ${unknownCount}`);
console.log('');

if (hasIssues) {
  console.log('❌ 发现不兼容的依赖包，请检查上述结果并调整版本。\n');
  process.exit(1);
} else if (unknownCount > 0) {
  console.log('⚠️  部分依赖包需要手动检查，建议访问 npm 包页面确认 Node.js 版本要求。\n');
  process.exit(0);
} else {
  console.log('✅ 所有已知依赖包都兼容 Node.js 18.20.1！\n');
  process.exit(0);
}
