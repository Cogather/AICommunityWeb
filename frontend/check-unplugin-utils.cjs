#!/usr/bin/env node

/**
 * 检查 unplugin-utils 版本及其 Node.js 兼容性
 */

const fs = require('fs');
const path = require('path');

const TARGET_NODE_VERSION = '18.20.1';
const packageLockPath = path.join(__dirname, 'package-lock.json');
const packageJsonPath = path.join(__dirname, 'package.json');

console.log('\n检查 unplugin-utils 版本和兼容性...\n');
console.log('='.repeat(80));

// 读取 package.json
const packageJson = JSON.parse(fs.readFileSync(packageJsonPath, 'utf8'));

// 读取 package-lock.json
let packageLock = null;
if (fs.existsSync(packageLockPath)) {
  packageLock = JSON.parse(fs.readFileSync(packageLockPath, 'utf8'));
}

console.log(`目标 Node.js 版本: ${TARGET_NODE_VERSION}`);
console.log(`目标 npm 版本: <10.5.0\n`);

// 检查 package.json 中是否有直接依赖
const directDeps = {
  ...packageJson.dependencies,
  ...packageJson.devDependencies
};

if (directDeps['unplugin-utils']) {
  console.log(`✅ package.json 中直接依赖: unplugin-utils@${directDeps['unplugin-utils']}`);
} else {
  console.log(`ℹ️  package.json 中没有直接依赖 unplugin-utils（可能是间接依赖）`);
}

// 检查 package-lock.json 中的版本
if (packageLock && packageLock.packages) {
  const unpluginUtils = packageLock.packages['node_modules/unplugin-utils'];
  
  if (unpluginUtils) {
    console.log(`\n📦 package-lock.json 中的版本信息:`);
    console.log(`   版本: ${unpluginUtils.version}`);
    
    if (unpluginUtils.engines && unpluginUtils.engines.node) {
      const nodeRequirement = unpluginUtils.engines.node;
      console.log(`   Node.js 要求: ${nodeRequirement}`);
      
      // 简单检查
      if (nodeRequirement.includes('>=20')) {
        console.log(`   ❌ 不兼容！要求 Node.js >= 20.x，但项目使用 ${TARGET_NODE_VERSION}`);
        console.log(`   ⚠️  需要降级 unplugin-utils 或找到兼容的版本`);
      } else if (nodeRequirement.includes('>=18')) {
        console.log(`   ✅ 兼容 Node.js ${TARGET_NODE_VERSION}`);
      } else {
        console.log(`   ⚠️  需要手动检查兼容性`);
      }
    } else {
      console.log(`   ℹ️  没有 engines.node 字段`);
    }
    
    // 检查依赖来源
    console.log(`\n🔍 查找依赖来源:`);
    let foundSource = false;
    
    // 检查 vite-plugin-vue-devtools
    const devtools = packageLock.packages['node_modules/vite-plugin-vue-devtools'];
    if (devtools && devtools.dependencies && devtools.dependencies['unplugin-utils']) {
      console.log(`   📌 通过 vite-plugin-vue-devtools@${devtools.version} 引入`);
      console.log(`      要求: unplugin-utils@${devtools.dependencies['unplugin-utils']}`);
      foundSource = true;
    }
    
    // 检查 vite-plugin-inspect
    const inspect = packageLock.packages['node_modules/vite-plugin-inspect'];
    if (inspect && inspect.dependencies && inspect.dependencies['unplugin-utils']) {
      console.log(`   📌 通过 vite-plugin-inspect@${inspect.version} 引入`);
      console.log(`      要求: unplugin-utils@${inspect.dependencies['unplugin-utils']}`);
      foundSource = true;
    }
    
    if (!foundSource) {
      console.log(`   ⚠️  未找到明确的依赖来源`);
    }
  } else {
    console.log(`\nℹ️  package-lock.json 中未找到 unplugin-utils`);
  }
}

console.log('\n' + '='.repeat(80));
console.log('\n建议:');
console.log('1. unplugin-utils 0.3.1 要求 Node.js >= 20.19.0，不兼容 Node.js 18.20.1');
console.log('2. 需要锁定 unplugin-utils 到 0.3.0 或更早版本（如果兼容）');
console.log('3. 或者确保 vite-plugin-vue-devtools 使用兼容的版本');
console.log('4. 建议删除 package-lock.json 和 node_modules，重新安装依赖\n');
