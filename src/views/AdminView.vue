<template>
  <div class="admin-view">
    <div class="admin-container">
      <div class="admin-header">
        <div class="header-content">
          <div>
            <h1>管理后台</h1>
            <p>配置首页内容、荣誉殿堂和社区头条</p>
          </div>
        </div>
      </div>

      <el-tabs v-model="activeTab" class="admin-tabs">
        <!-- 首页管理 -->
        <el-tab-pane label="首页管理" name="home">
          <div class="tab-content">
            <el-tabs v-model="homeSubTab" type="card" class="sub-tabs">
              <!-- 轮播图配置 -->
              <el-tab-pane label="轮播图配置" name="carousel">
                <div class="config-section">
                  <div class="section-header">
                    <h2>首页轮播图配置</h2>
                    <el-button type="primary" @click="handleAddCarousel">
                      <el-icon><Plus /></el-icon>
                      添加轮播图
                    </el-button>
                  </div>

                  <div class="carousel-list">
                    <div 
                      v-for="(item, index) in carouselList" 
                      :key="item.id"
                      class="carousel-item"
                    >
                      <div class="item-preview">
                        <img v-if="item.image" :src="item.image" :alt="item.title || '轮播图'" />
                        <div v-else class="no-image">暂无图片</div>
                        <div v-if="item.showContent && item.image" class="preview-content">
                          <h3>{{ item.title || '标题' }}</h3>
                          <p>{{ item.desc || '描述' }}</p>
                        </div>
                      </div>
                      <div class="item-form">
                        <el-form :model="item" label-width="120px">
                          <el-form-item label="图片">
                            <div class="image-upload-wrapper">
                              <el-radio-group v-model="item.imageType" @change="handleImageTypeChange(item, index)">
                                <el-radio label="url">URL</el-radio>
                                <el-radio label="upload">本地上传</el-radio>
                              </el-radio-group>
                              <el-input 
                                v-if="item.imageType === 'url'"
                                v-model="item.image" 
                                placeholder="请输入图片URL"
                                style="margin-top: 8px;"
                              />
                              <el-upload
                                v-else
                                class="image-uploader"
                                :auto-upload="false"
                                :show-file-list="false"
                                :on-change="(file) => handleImageFileChange(file, item, 'carousel')"
                                :before-upload="beforeImageUpload"
                                accept="image/*"
                              >
                                <el-button type="primary" style="margin-top: 8px;">选择图片</el-button>
                              </el-upload>
                            </div>
                          </el-form-item>
                          <el-form-item label="跳转链接">
                            <el-input v-model="item.link" placeholder="请输入跳转链接" />
                          </el-form-item>
                          <el-form-item label="显示标题和介绍">
                            <el-switch v-model="item.showContent" />
                          </el-form-item>
                          <template v-if="item.showContent">
                            <el-form-item label="标题">
                              <el-input v-model="item.title" placeholder="请输入标题" />
                            </el-form-item>
                            <el-form-item label="介绍">
                              <el-input 
                                v-model="item.desc" 
                                type="textarea" 
                                :rows="3"
                                placeholder="请输入介绍内容"
                              />
                            </el-form-item>
                          </template>
                          <el-form-item>
                            <el-button type="danger" @click="handleDeleteCarousel(index)">
                              删除
                            </el-button>
                            <el-button @click="handleMoveUp(index)" :disabled="index === 0">
                              上移
                            </el-button>
                            <el-button @click="handleMoveDown(index)" :disabled="index === carouselList.length - 1">
                              下移
                            </el-button>
                          </el-form-item>
                        </el-form>
                      </div>
                    </div>
                  </div>

                  <div v-if="carouselList.length === 0" class="empty-state">
                    <el-empty description="暂无轮播图，点击上方按钮添加" />
                  </div>
                </div>
              </el-tab-pane>

              <!-- 荣誉殿堂Banner配置 -->
              <el-tab-pane label="荣誉殿堂Banner" name="honorBanner">
                <div class="config-section">
                  <div class="section-header">
                    <h2>AI使用达人荣誉殿堂模块 Banner 配置</h2>
                  </div>
                  <el-form :model="honorConfig" label-width="120px">
                    <el-form-item label="Banner 图片">
                      <div class="image-upload-wrapper">
                        <el-radio-group v-model="honorConfig.bannerImageType" @change="handleHonorBannerImageTypeChange">
                          <el-radio label="url">URL</el-radio>
                          <el-radio label="upload">本地上传</el-radio>
                        </el-radio-group>
                        <el-input 
                          v-if="honorConfig.bannerImageType === 'url'"
                          v-model="honorConfig.bannerImage" 
                          placeholder="请输入Banner图片URL"
                          style="margin-top: 8px;"
                        />
                        <el-upload
                          v-else
                          class="image-uploader"
                          :auto-upload="false"
                          :show-file-list="false"
                          :on-change="(file) => handleImageFileChange(file, honorConfig, 'honorBanner')"
                          :before-upload="beforeImageUpload"
                          accept="image/*"
                        >
                          <el-button type="primary" style="margin-top: 8px;">选择图片</el-button>
                        </el-upload>
                        <div class="preview-box" v-if="honorConfig.bannerImage" style="margin-top: 12px;">
                          <img :src="honorConfig.bannerImage" alt="Banner预览" />
                        </div>
                      </div>
                    </el-form-item>
                  </el-form>
                </div>
              </el-tab-pane>

              <!-- 社区头条配置 -->
              <el-tab-pane label="社区头条" name="news">
                <div class="config-section">
                  <div class="section-header">
                    <h2>社区头条配置</h2>
                    <el-button type="primary" @click="handleAddNews">
                      <el-icon><Plus /></el-icon>
                      添加头条
                    </el-button>
                  </div>

                  <div class="news-list">
                    <div 
                      v-for="(news, index) in newsList" 
                      :key="news.id"
                      class="news-item"
                    >
                      <el-form :model="news" label-width="120px">
                        <el-form-item label="标题">
                          <el-input v-model="news.title" placeholder="请输入标题" />
                        </el-form-item>
                        <el-form-item label="图片">
                          <div class="image-upload-wrapper">
                            <el-radio-group v-model="news.imageType" @change="handleNewsImageTypeChange(news, index)">
                              <el-radio label="url">URL</el-radio>
                              <el-radio label="upload">本地上传</el-radio>
                            </el-radio-group>
                            <el-input 
                              v-if="news.imageType === 'url'"
                              v-model="news.image" 
                              placeholder="请输入图片URL"
                              style="margin-top: 8px;"
                            />
                            <el-upload
                              v-else
                              class="image-uploader"
                              :auto-upload="false"
                              :show-file-list="false"
                              :on-change="(file) => handleImageFileChange(file, news, 'news')"
                              :before-upload="beforeImageUpload"
                              accept="image/*"
                            >
                              <el-button type="primary" style="margin-top: 8px;">选择图片</el-button>
                            </el-upload>
                            <div class="preview-box" v-if="news.image" style="margin-top: 12px;">
                              <img :src="news.image" alt="图片预览" />
                            </div>
                          </div>
                        </el-form-item>
                        <el-form-item label="跳转链接">
                          <el-input v-model="news.link" placeholder="请输入跳转链接" />
                        </el-form-item>
                        <el-form-item label="日期">
                          <el-input v-model="news.date" placeholder="例如：刚刚、1小时前、昨天" />
                        </el-form-item>
                        <el-form-item>
                          <el-button type="danger" @click="handleDeleteNews(index)">
                            删除
                          </el-button>
                          <el-button @click="handleMoveNewsUp(index)" :disabled="index === 0">
                            上移
                          </el-button>
                          <el-button @click="handleMoveNewsDown(index)" :disabled="index === newsList.length - 1">
                            下移
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>

                  <div v-if="newsList.length === 0" class="empty-state">
                    <el-empty description="暂无头条，点击上方按钮添加" />
                  </div>
                </div>
              </el-tab-pane>

              <!-- AI工具配置 -->
              <el-tab-pane label="AI工具配置" name="tools">
                <div class="config-section">
                  <!-- AI工具专区Banner配置 -->
                  <div class="section-header" style="margin-top: 0;">
                    <h2>AI工具专区 Banner 配置</h2>
                    <el-button type="primary" @click="handleAddToolBanner">
                      <el-icon><Plus /></el-icon>
                      添加Banner
                    </el-button>
                  </div>
                  
                  <div class="banner-list" style="margin-bottom: 40px;">
                    <div 
                      v-for="(banner, index) in toolBannersList" 
                      :key="banner.id"
                      class="banner-item"
                    >
                      <div class="item-preview">
                        <img v-if="banner.image" :src="banner.image" :alt="banner.title || 'Banner'" />
                        <div v-else class="no-image">暂无图片</div>
                        <div v-if="banner.image" class="preview-content">
                          <h3>{{ banner.title || '标题' }}</h3>
                          <p>{{ banner.desc || '描述' }}</p>
                        </div>
                      </div>
                      <div class="item-form">
                        <el-form :model="banner" label-width="120px">
                          <el-form-item label="图片">
                            <div class="image-upload-wrapper">
                              <el-radio-group v-model="banner.imageType" @change="handleToolBannerImageTypeChange(banner, index)">
                                <el-radio label="url">URL</el-radio>
                                <el-radio label="upload">本地上传</el-radio>
                              </el-radio-group>
                              <el-input 
                                v-if="banner.imageType === 'url'"
                                v-model="banner.image" 
                                placeholder="请输入图片URL"
                                style="margin-top: 8px;"
                              />
                              <el-upload
                                v-else
                                class="image-uploader"
                                :auto-upload="false"
                                :show-file-list="false"
                                :on-change="(file) => handleImageFileChange(file, banner, 'toolBanner')"
                                :before-upload="beforeImageUpload"
                                accept="image/*"
                              >
                                <el-button type="primary" style="margin-top: 8px;">选择图片</el-button>
                              </el-upload>
                            </div>
                          </el-form-item>
                          <el-form-item label="标题">
                            <el-input v-model="banner.title" placeholder="请输入标题" />
                          </el-form-item>
                          <el-form-item label="描述">
                            <el-input 
                              v-model="banner.desc" 
                              type="textarea" 
                              :rows="3"
                              placeholder="请输入描述内容"
                            />
                          </el-form-item>
                          <el-form-item>
                            <el-button type="danger" @click="handleDeleteToolBanner(index)">
                              删除
                            </el-button>
                            <el-button @click="handleMoveToolBannerUp(index)" :disabled="index === 0">
                              上移
                            </el-button>
                            <el-button @click="handleMoveToolBannerDown(index)" :disabled="index === toolBannersList.length - 1">
                              下移
                            </el-button>
                          </el-form-item>
                        </el-form>
                      </div>
                    </div>
                  </div>

                  <div v-if="toolBannersList.length === 0" class="empty-state" style="margin-bottom: 40px;">
                    <el-empty description="暂无Banner，点击上方按钮添加" />
                  </div>

                  <div class="section-header">
                    <h2>AI工具配置</h2>
                    <el-button type="primary" @click="handleAddTool">
                      <el-icon><Plus /></el-icon>
                      添加工具
                    </el-button>
                  </div>

                  <div class="tools-list">
                    <div 
                      v-for="(tool, index) in toolsList" 
                      :key="tool.id"
                      class="tool-item"
                    >
                      <el-form :model="tool" label-width="120px">
                        <el-form-item label="工具名称">
                          <el-input v-model="tool.name" placeholder="请输入工具名称" />
                        </el-form-item>
                        <el-form-item label="工具描述">
                          <el-input v-model="tool.desc" placeholder="请输入工具描述" />
                        </el-form-item>
                        <el-form-item label="工具Logo">
                          <div class="image-upload-wrapper">
                            <el-radio-group v-model="tool.logoType" @change="handleToolLogoTypeChange(tool, index)">
                              <el-radio label="url">URL</el-radio>
                              <el-radio label="upload">本地上传</el-radio>
                            </el-radio-group>
                            <el-input 
                              v-if="tool.logoType === 'url'"
                              v-model="tool.logo" 
                              placeholder="请输入Logo URL"
                              style="margin-top: 8px;"
                            />
                            <el-upload
                              v-else
                              class="image-uploader"
                              :auto-upload="false"
                              :show-file-list="false"
                              :on-change="(file) => handleImageFileChange(file, tool, 'tool')"
                              :before-upload="beforeImageUpload"
                              accept="image/*"
                            >
                              <el-button type="primary" style="margin-top: 8px;">选择Logo</el-button>
                            </el-upload>
                            <div class="preview-box" v-if="tool.logo" style="margin-top: 12px;">
                              <img :src="tool.logo" alt="Logo预览" />
                            </div>
                          </div>
                        </el-form-item>
                        <el-form-item label="工具颜色">
                          <el-color-picker v-model="tool.color" />
                          <span style="margin-left: 12px; color: #666; font-size: 12px;">（当没有Logo时使用）</span>
                        </el-form-item>
                        <el-form-item label="跳转路由">
                          <el-input v-model="tool.link" placeholder="例如：/tools/testmate" />
                        </el-form-item>
                        <el-form-item>
                          <el-button type="danger" @click="handleDeleteTool(index)">
                            删除
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>

                  <div v-if="toolsList.length === 0" class="empty-state">
                    <el-empty description="暂无工具，点击上方按钮添加" />
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-tab-pane>

        <!-- AI优秀实践管理 -->
        <el-tab-pane label="AI优秀实践管理" name="practices">
          <div class="tab-content">
            <div class="config-section">
              <div class="section-header">
                <h2>帖子精华管理</h2>
                <el-button type="primary" @click="handleAddFeaturedPost">
                  <el-icon><Plus /></el-icon>
                  添加精华帖子
                </el-button>
              </div>

              <el-alert
                title="说明：录入帖子URL后，后端会在返回帖子数据时自动添加加精参数，前端会根据该参数显示精华标识"
                type="info"
                :closable="false"
                style="margin-bottom: 24px;"
              />

              <div class="featured-posts-list">
                <div
                  v-for="(item, index) in featuredPostsList"
                  :key="item.id"
                  class="featured-post-item"
                >
                  <el-form :model="item" label-width="120px">
                    <el-form-item label="帖子URL">
                      <el-input
                        v-model="item.url"
                        placeholder="请输入帖子URL，例如：/post/123 或 https://example.com/post/123"
                      />
                    </el-form-item>
                    <el-form-item label="备注">
                      <el-input
                        v-model="item.note"
                        placeholder="可选：添加备注信息，便于管理"
                      />
                    </el-form-item>
                    <el-form-item>
                      <el-button type="danger" @click="handleDeleteFeaturedPost(index)">
                        删除
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>

              <div v-if="featuredPostsList.length === 0" class="empty-state">
                <el-empty description="暂无精华帖子，点击上方按钮添加" />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 赋能交流管理 -->
        <el-tab-pane label="赋能交流管理" name="empowerment">
          <div class="tab-content">
            <div class="config-section">
              <div class="section-header">
                <h2>精选合集管理</h2>
                <el-button type="primary" @click="handleAddFeaturedCollection">
                  <el-icon><Plus /></el-icon>
                  添加精选合集
                </el-button>
              </div>

              <el-alert
                title="说明：录入合集URL后，后端会在返回数据时自动添加精选标识，前端会根据该参数显示精选合集"
                type="info"
                :closable="false"
                style="margin-bottom: 24px;"
              />

              <div class="featured-posts-list">
                <div
                  v-for="(item, index) in featuredCollectionsList"
                  :key="item.id"
                  class="featured-post-item"
                >
                  <el-form :model="item" label-width="120px">
                    <el-form-item label="合集URL">
                      <el-input
                        v-model="item.url"
                        placeholder="请输入合集URL，例如：/post/123 或 https://example.com/post/123"
                      />
                    </el-form-item>
                    <el-form-item label="备注">
                      <el-input
                        v-model="item.note"
                        placeholder="可选：添加备注信息，便于管理"
                      />
                    </el-form-item>
                    <el-form-item>
                      <el-button type="danger" @click="handleDeleteFeaturedCollection(index)">
                        删除
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>

              <div v-if="featuredCollectionsList.length === 0" class="empty-state">
                <el-empty description="暂无精选合集，点击上方按钮添加" />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- AI使用达人管理 -->
        <el-tab-pane label="AI使用达人管理" name="users">
          <div class="tab-content">
            <el-tabs v-model="usersSubTab" type="card" class="sub-tabs">
              <!-- 奖项管理 -->
              <el-tab-pane label="奖项管理" name="awards">
                <div class="config-section">
                  <div class="section-header">
                    <h2>奖项管理</h2>
                    <el-button type="primary" @click="handleAddAward">
                      <el-icon><Plus /></el-icon>
                      添加奖项
                    </el-button>
                  </div>

                  <div class="awards-list">
                    <div
                      v-for="(award, index) in awardsList"
                      :key="award.id"
                      class="award-item"
                    >
                      <el-form :model="award" label-width="120px">
                        <el-form-item label="奖项名称">
                          <el-input v-model="award.name" placeholder="请输入奖项名称" />
                        </el-form-item>
                        <el-form-item label="奖项分类">
                          <el-input v-model="award.category" placeholder="请输入奖项分类" />
                        </el-form-item>
                        <el-form-item label="奖项描述">
                          <el-input
                            v-model="award.description"
                            type="textarea"
                            :rows="4"
                            placeholder="请输入奖项描述（将展示在奖项详情里）"
                          />
                        </el-form-item>
                        <el-form-item>
                          <el-button type="danger" @click="handleDeleteAward(index)">
                            删除
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>

                  <div v-if="awardsList.length === 0" class="empty-state">
                    <el-empty description="暂无奖项，点击上方按钮添加" />
                  </div>
                </div>
              </el-tab-pane>

              <!-- 获奖者管理 -->
              <el-tab-pane label="获奖者管理" name="winners">
                <div class="config-section">
                  <div class="section-header">
                    <h2>获奖者管理</h2>
                    <el-button type="primary" @click="handleAddWinner">
                      <el-icon><Plus /></el-icon>
                      添加获奖者
                    </el-button>
                  </div>

                  <div class="winners-list">
                    <div
                      v-for="(winner, index) in winnersList"
                      :key="winner.id"
                      class="winner-item"
                    >
                      <el-form :model="winner" label-width="120px">
                        <el-form-item label="获奖者">
                          <el-input v-model="winner.name" placeholder="请输入获奖者姓名" />
                        </el-form-item>
                        <el-form-item label="奖项分类">
                          <el-select
                            v-model="winner.category"
                            placeholder="请先选择奖项分类"
                            style="width: 100%;"
                            @change="handleWinnerCategoryChange(winner, index)"
                          >
                            <el-option label="创新突破" value="innovation" />
                            <el-option label="效率提升" value="efficiency" />
                            <el-option label="最佳实践" value="practice" />
                            <el-option label="社区贡献" value="community" />
                          </el-select>
                        </el-form-item>
                        <el-form-item label="奖项名称">
                          <el-select
                            v-model="winner.awardName"
                            placeholder="请先选择奖项分类，然后选择奖项名称"
                            style="width: 100%;"
                            :disabled="!winner.category"
                            :loading="loadingAwards"
                          >
                            <el-option
                              v-for="award in getFilteredAwardsForWinner(winner.category)"
                              :key="award.id"
                              :label="award.name"
                              :value="award.name"
                            />
                          </el-select>
                          <div v-if="!winner.category" style="margin-top: 8px; font-size: 12px; color: #909399;">
                            提示：请先选择奖项分类，系统会从后台获取该分类下的奖项列表
                          </div>
                        </el-form-item>
                        <el-form-item label="获奖时间">
                          <el-date-picker
                            v-model="winner.awardTime"
                            type="month"
                            placeholder="选择获奖时间（年月）"
                            format="YYYY-MM"
                            value-format="YYYY-MM"
                            style="width: 100%;"
                          />
                        </el-form-item>
                        <el-form-item>
                          <el-button type="danger" @click="handleDeleteWinner(index)">
                            删除
                          </el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>

                  <div v-if="winnersList.length === 0" class="empty-state">
                    <el-empty description="暂无获奖者，点击上方按钮添加" />
                  </div>
                </div>
              </el-tab-pane>

              <!-- 获奖者推荐 -->
              <el-tab-pane label="获奖者推荐" name="recommended">
                <div class="config-section">
                  <div class="section-header">
                    <h2>获奖者推荐</h2>
                    <div style="display: flex; gap: 12px; align-items: center;">
                      <el-date-picker
                        v-model="recommendedMonth"
                        type="month"
                        placeholder="选择月份"
                        format="YYYY-MM"
                        value-format="YYYY-MM"
                        @change="loadRecommendedWinners"
                        style="width: 200px;"
                      />
                      <el-button type="primary" @click="loadRecommendedWinners">
                        <el-icon><Refresh /></el-icon>
                        刷新
                      </el-button>
                    </div>
                  </div>

                  <el-alert
                    title="说明：系统会根据本月积分自动推荐Top 3用户，管理员可以为推荐用户设置获奖记录。"
                    type="info"
                    :closable="false"
                    style="margin-bottom: 24px;"
                  />

                  <div v-if="loadingRecommended" class="loading-state">
                    <el-skeleton :rows="3" animated />
                  </div>

                  <div v-else class="recommended-winners-list">
                    <div
                      v-for="(user, index) in recommendedWinnersList"
                      :key="user.id"
                      class="recommended-winner-item"
                    >
                      <div class="winner-info">
                        <div class="winner-avatar">
                          <el-avatar :src="user.avatar" :size="60">
                            {{ user.name.charAt(0) }}
                          </el-avatar>
                          <div v-if="index < 3" class="rank-badge">
                            {{ index + 1 }}
                          </div>
                        </div>
                        <div class="winner-details">
                          <div class="winner-name-row">
                            <h3>{{ user.name }}</h3>
                            <el-tag v-if="user.hasAwarded" type="success" size="small">
                              已评奖
                            </el-tag>
                            <el-tag v-else type="info" size="small">
                              未评奖
                            </el-tag>
                          </div>
                          <div class="winner-meta">
                            <span><strong>工号：</strong>{{ user.employeeId }}</span>
                            <span><strong>部门：</strong>{{ user.department }}</span>
                          </div>
                          <div class="winner-stats">
                            <div class="stat-item">
                              <span class="stat-label">本月积分：</span>
                              <span class="stat-value highlight">{{ user.points }}</span>
                            </div>
                            <div class="stat-item">
                              <span class="stat-label">发帖数：</span>
                              <span class="stat-value">{{ user.postsCount }}</span>
                            </div>
                            <div class="stat-item">
                              <span class="stat-label">评论数：</span>
                              <span class="stat-value">{{ user.commentsCount }}</span>
                            </div>
                            <div class="stat-item">
                              <span class="stat-label">参加活动：</span>
                              <span class="stat-value">{{ user.activitiesCount }}</span>
                            </div>
                            <div class="stat-item">
                              <span class="stat-label">被点赞：</span>
                              <span class="stat-value">{{ user.likesReceived }}</span>
                            </div>
                            <div class="stat-item">
                              <span class="stat-label">被收藏：</span>
                              <span class="stat-value">{{ user.favoritesReceived }}</span>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="winner-actions">
                        <el-button
                          v-if="!user.hasAwarded"
                          type="primary"
                          @click="handleSetAward(user)"
                        >
                          设置评奖
                        </el-button>
                        <el-button
                          v-else
                          type="warning"
                          @click="handleCancelAward(user)"
                        >
                          取消评奖
                        </el-button>
                      </div>
                    </div>
                  </div>

                  <div v-if="!loadingRecommended && recommendedWinnersList.length === 0" class="empty-state">
                    <el-empty description="暂无推荐用户" />
                  </div>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-tab-pane>

        <!-- 人员管理 -->
        <el-tab-pane label="人员管理" name="userManagement">
          <div class="tab-content">
            <div class="config-section">
              <div class="section-header">
                <h2>管理员管理</h2>
                <div style="display: flex; gap: 12px; align-items: center;">
                  <el-input
                    v-model="userSearchKeyword"
                    placeholder="搜索管理员姓名或邮箱"
                    style="width: 300px;"
                    clearable
                  >
                    <template #prefix>
                      <el-icon><Search /></el-icon>
                    </template>
                  </el-input>
                  <el-button type="primary" @click="showAddAdminDialog = true">
                    <el-icon><Plus /></el-icon>
                    添加管理员
                  </el-button>
                </div>
              </div>

              <el-table :data="filteredAdminList" style="width: 100%">
                <el-table-column prop="id" label="用户ID" width="100" />
                <el-table-column prop="name" label="姓名" width="150" />
                <el-table-column prop="email" label="邮箱" width="200" />
                <el-table-column prop="department" label="部门" width="150" />
                <el-table-column prop="currentRole" label="角色" width="120">
                  <template #default="{ row }">
                    <el-tag type="danger">管理员</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="150">
                  <template #default="{ row }">
                    <el-button
                      type="danger"
                      size="small"
                      @click="handleRemoveAdmin(row)"
                    >
                      移除管理员
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <div v-if="filteredAdminList.length === 0" class="empty-state">
                <el-empty description="暂无管理员" />
              </div>
            </div>
          </div>
        </el-tab-pane>

        <!-- 扶摇Agent应用管理 -->
        <el-tab-pane label="扶摇Agent应用管理" name="agent">
          <div class="tab-content">
            <div class="config-section">
              <div class="section-header">
                <h2>置顶帖子管理</h2>
                <el-button type="primary" @click="handleAddAgentPinnedPost">
                  <el-icon><Plus /></el-icon>
                  添加置顶帖子
                </el-button>
              </div>

              <el-alert
                title="说明：录入帖子URL后，后端会在返回帖子数据时自动添加置顶参数，前端会根据该参数显示置顶标识"
                type="info"
                :closable="false"
                style="margin-bottom: 24px;"
              />

              <div class="featured-posts-list">
                <div
                  v-for="(item, index) in agentPinnedPostsList"
                  :key="item.id"
                  class="featured-post-item"
                >
                  <el-form :model="item" label-width="120px">
                    <el-form-item label="帖子URL">
                      <el-input
                        v-model="item.url"
                        placeholder="请输入帖子URL，例如：/post/123 或 https://example.com/post/123"
                      />
                    </el-form-item>
                    <el-form-item label="备注">
                      <el-input
                        v-model="item.note"
                        placeholder="可选：添加备注信息，便于管理"
                      />
                    </el-form-item>
                    <el-form-item>
                      <el-button type="danger" @click="handleDeleteAgentPinnedPost(index)">
                        删除
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>

              <div v-if="agentPinnedPostsList.length === 0" class="empty-state">
                <el-empty description="暂无置顶帖子，点击上方按钮添加" />
              </div>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>

      <!-- 保存按钮 -->
      <div class="admin-footer">
        <el-button type="primary" size="large" @click="handleSave">
          <el-icon><Check /></el-icon>
          保存所有配置
        </el-button>
        <el-button size="large" @click="handleReset">
          <el-icon><Refresh /></el-icon>
          重置
        </el-button>
      </div>
    </div>

    <!-- 添加管理员对话框 -->
    <el-dialog
      v-model="showAddAdminDialog"
      title="添加管理员"
      width="500px"
    >
      <el-form :model="addAdminForm" label-width="100px">
        <el-form-item label="用户ID" required>
          <el-input
            v-model.number="addAdminForm.userId"
            type="number"
            placeholder="请输入用户ID"
            clearable
          />
          <div style="margin-top: 8px; font-size: 12px; color: #909399;">
            提示：输入用户ID后，系统会自动查找该用户并设置为管理员
          </div>
        </el-form-item>
        <el-form-item v-if="foundUser" label="用户信息">
          <div style="padding: 12px; background: #f5f7fa; border-radius: 4px;">
            <p style="margin: 0 0 8px 0;"><strong>姓名：</strong>{{ foundUser.name }}</p>
            <p style="margin: 0 0 8px 0;"><strong>邮箱：</strong>{{ foundUser.email }}</p>
            <p style="margin: 0;"><strong>部门：</strong>{{ foundUser.department }}</p>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddAdminDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleAddAdmin"
          :disabled="!addAdminForm.userId || !foundUser"
        >
          确认添加
        </el-button>
      </template>
    </el-dialog>

    <!-- 设置评奖对话框 -->
    <el-dialog
      v-model="showSetAwardDialog"
      title="设置评奖"
      width="600px"
    >
      <el-form :model="awardForm" :rules="awardRules" ref="awardFormRef" label-width="120px">
        <el-form-item label="用户信息">
          <div style="padding: 12px; background: #f5f7fa; border-radius: 4px;">
            <p style="margin: 0 0 8px 0;"><strong>姓名：</strong>{{ currentAwardUser?.name }}</p>
            <p style="margin: 0 0 8px 0;"><strong>工号：</strong>{{ currentAwardUser?.employeeId }}</p>
            <p style="margin: 0;"><strong>部门：</strong>{{ currentAwardUser?.department }}</p>
          </div>
        </el-form-item>
        <el-form-item label="奖项分类" prop="category">
          <el-select
            v-model="awardForm.category"
            placeholder="请先选择奖项分类"
            style="width: 100%;"
            @change="handleCategoryChange"
          >
            <el-option label="创新突破" value="innovation" />
            <el-option label="效率提升" value="efficiency" />
            <el-option label="最佳实践" value="practice" />
            <el-option label="社区贡献" value="community" />
          </el-select>
        </el-form-item>
        <el-form-item label="奖项名称" prop="awardId">
          <el-select
            v-model="awardForm.awardId"
            placeholder="请先选择奖项分类，然后选择奖项名称"
            style="width: 100%;"
            :disabled="!awardForm.category"
            :loading="loadingAwards"
            @change="handleAwardChange"
          >
            <el-option
              v-for="award in filteredAwardsList"
              :key="award.id"
              :label="award.name"
              :value="award.id"
            />
          </el-select>
          <div v-if="!awardForm.category" style="margin-top: 8px; font-size: 12px; color: #909399;">
            提示：请先选择奖项分类，系统会从后台获取该分类下的奖项列表
          </div>
        </el-form-item>
        <el-form-item label="获奖时间" prop="awardDate">
          <el-date-picker
            v-model="awardForm.awardDate"
            type="month"
            placeholder="选择获奖时间（年月）"
            format="YYYY-MM"
            value-format="YYYY-MM"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showSetAwardDialog = false">取消</el-button>
        <el-button
          type="primary"
          @click="handleConfirmSetAward"
          :loading="settingAward"
        >
          确认设置
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onBeforeUnmount, shallowRef, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Check, Refresh, Search, Delete } from '@element-plus/icons-vue'
import type { UploadProps } from 'element-plus'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import type { IDomEditor, IEditorConfig, IToolbarConfig } from '@wangeditor/editor'

const router = useRouter()

// 当前标签页
const activeTab = ref('home')
const homeSubTab = ref('carousel')
const usersSubTab = ref('awards')

// 图片上传处理（使用base64作为临时方案，实际应该上传到服务器）
const uploadAction = '/api/upload' // 这里需要替换为实际的上传接口

// 将文件转换为base64（临时方案）
const fileToBase64 = (file: File): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(reader.result as string)
    reader.onerror = reject
    reader.readAsDataURL(file)
  })
}

// 轮播图列表
interface CarouselItem {
  id: number
  image: string
  imageType: 'url' | 'upload'
  link: string
  showContent: boolean
  title: string
  desc: string
}

const carouselList = ref<CarouselItem[]>([
  {
    id: 1,
    image: 'https://picsum.photos/1200/600?random=1',
    imageType: 'url',
    link: '/practice',
    showContent: true,
    title: 'AI 优秀实践',
    desc: '探索大模型在企业级应用中的最佳落地场景，驱动业务数智化转型。'
  }
])

// 荣誉殿堂配置
interface Award {
  id: number
  name: string
  desc: string
  image: string
}

const honorConfig = ref({
  bannerImage: 'https://picsum.photos/800/300?random=30',
  bannerImageType: 'url' as 'url' | 'upload',
  awards: [
    {
      id: 1,
      name: '年度最佳贡献奖',
      desc: '2026年度',
      image: 'https://picsum.photos/200/150?random=31'
    }
  ] as Award[]
})

// 社区头条列表
interface NewsItem {
  id: number
  title: string
  image: string
  imageType: 'url' | 'upload'
  link: string
  date: string
}

const newsList = ref<NewsItem[]>([
  {
    id: 1,
    title: '【大模型专题】多模态模型在医疗影像中的最新应用突破',
    image: 'https://picsum.photos/300/200?random=20',
    imageType: 'url',
    link: '/news',
    date: '刚刚'
  }
])

// AI工具列表
interface ToolItem {
  id: number
  name: string
  desc: string
  logo: string
  logoType: 'url' | 'upload'
  color: string
  link: string
}

const toolsList = ref<ToolItem[]>([
  {
    id: 1,
    name: 'TestMate',
    desc: '自动化测试助手',
    logo: 'https://picsum.photos/80/80?random=1',
    logoType: 'url',
    color: '#36cfc9',
    link: '/tools/testmate'
  },
  {
    id: 2,
    name: 'CodeMate',
    desc: '智能代码补全',
    logo: 'https://picsum.photos/80/80?random=2',
    logoType: 'url',
    color: '#9254de',
    link: '/tools/codemate'
  }
])

// AI工具专区Banner列表
interface ToolBannerItem {
  id: number
  image: string
  imageType: 'url' | 'upload'
  title: string
  desc: string
}

const toolBannersList = ref<ToolBannerItem[]>([
  {
    id: 1,
    image: 'https://picsum.photos/1200/400?random=10',
    imageType: 'url',
    title: '最新 AI 工具推荐',
    desc: '探索最新发布的 AI 工具，提升你的工作效率'
  },
  {
    id: 2,
    image: 'https://picsum.photos/1200/400?random=11',
    imageType: 'url',
    title: '热门工具排行榜',
    desc: '查看最受欢迎的 AI 工具，发现社区精选'
  }
])

// 精华帖子URL列表
interface FeaturedPostItem {
  id: number
  url: string
  note: string
}

const featuredPostsList = ref<FeaturedPostItem[]>([
  {
    id: 1,
    url: '/post/1',
    note: 'AI大会2024'
  }
])

// 奖项列表
interface AwardItem {
  id: number
  name: string
  category: string
  description: string
}

const awardsList = ref<AwardItem[]>([
  {
    id: 1,
    name: '年度最佳贡献奖',
    category: '年度奖项',
    description: '表彰在AI社区中做出卓越贡献的成员，包括技术分享、问题解答、社区建设等方面的突出表现。'
  }
])

// 获奖者列表
interface WinnerItem {
  id: number
  name: string
  awardTime: string
  awardName: string
  category?: 'innovation' | 'efficiency' | 'practice' | 'community' | ''
}

const winnersList = ref<WinnerItem[]>([
  {
    id: 1,
    name: '张工程师',
    awardTime: '2024-01',
    awardName: '年度最佳贡献奖',
    category: 'innovation'
  }
])

// 根据分类筛选奖项（用于获奖者管理）
const getFilteredAwardsForWinner = (category: string) => {
  if (!category) {
    return []
  }
  return apiAwardsList.value.filter(award => award.category === category)
}

// 获奖者管理中的分类变化处理
const handleWinnerCategoryChange = async (winner: WinnerItem, index: number) => {
  // 清空已选择的奖项名称
  winner.awardName = ''
  
  // 根据分类加载奖项列表
  if (winner.category) {
    await loadAwardsFromApi(winner.category)
  }
}

// 获奖者推荐相关
interface RecommendedWinner {
  id: number
  employeeId: string
  name: string
  avatar: string
  department: string
  points: number
  postsCount: number
  commentsCount: number
  activitiesCount: number
  likesReceived: number
  favoritesReceived: number
  hasAwarded: boolean
  honorId?: number // 如果已评奖，记录荣誉ID
}

const recommendedWinnersList = ref<RecommendedWinner[]>([])
const loadingRecommended = ref(false)
const recommendedMonth = ref<string>('')

// 设置当前月份为默认值
const getCurrentMonth = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  return `${year}-${month}`
}

recommendedMonth.value = getCurrentMonth()

// 当前设置评奖的用户
const currentAwardUser = ref<RecommendedWinner | null>(null)
const showSetAwardDialog = ref(false)
const settingAward = ref(false)
const awardFormRef = ref()

// 从接口获取的奖项列表（用于设置评奖）
interface ApiAwardItem {
  id: number
  name: string
  desc: string
  image: string
  category: 'innovation' | 'efficiency' | 'practice' | 'community'
  rules: string
}

const apiAwardsList = ref<ApiAwardItem[]>([])
const loadingAwards = ref(false)

// 根据分类筛选的奖项列表
const filteredAwardsList = computed(() => {
  if (!awardForm.value.category) {
    return []
  }
  return apiAwardsList.value.filter(award => award.category === awardForm.value.category)
})

// 评奖表单
const awardForm = ref({
  userId: null as number | null,
  awardId: null as number | null,
  awardName: '',
  awardDate: '',
  category: '' as 'innovation' | 'efficiency' | 'practice' | 'community' | ''
})

// 评奖表单验证规则
const awardRules = {
  category: [{ required: true, message: '请选择奖项分类', trigger: 'change' }],
  awardId: [{ required: true, message: '请选择奖项名称', trigger: 'change' }],
  awardDate: [{ required: true, message: '请选择获奖时间', trigger: 'change' }]
}

// 扶摇Agent应用置顶帖子列表
interface AgentPinnedPostItem {
  id: number
  url: string
  note: string
}

const agentPinnedPostsList = ref<AgentPinnedPostItem[]>([
  {
    id: 1,
    url: '/post/0',
    note: '扶摇 Agent 应用开发指南'
  }
])

// 赋能交流精选合集列表
interface FeaturedCollectionItem {
  id: number
  url: string
  note: string
}

const featuredCollectionsList = ref<FeaturedCollectionItem[]>([
  {
    id: 1,
    url: '/post/1',
    note: '顶级AI研究论文'
  }
])

// 用户列表
interface UserItem {
  id: number
  name: string
  email: string
  department: string
  currentRole: 'user' | 'admin'
}

const usersList = ref<UserItem[]>([
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@example.com',
    department: '研发部',
    currentRole: 'admin'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@example.com',
    department: '产品部',
    currentRole: 'user'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@example.com',
    department: '技术部',
    currentRole: 'user'
  },
  {
    id: 4,
    name: '赵六',
    email: 'zhaoliu@example.com',
    department: '数据部',
    currentRole: 'user'
  }
])

const userSearchKeyword = ref('')

// 所有用户列表（用于查找用户）
const allUsersList = ref<UserItem[]>([
  {
    id: 1,
    name: '张三',
    email: 'zhangsan@example.com',
    department: '研发部',
    currentRole: 'admin'
  },
  {
    id: 2,
    name: '李四',
    email: 'lisi@example.com',
    department: '产品部',
    currentRole: 'user'
  },
  {
    id: 3,
    name: '王五',
    email: 'wangwu@example.com',
    department: '技术部',
    currentRole: 'user'
  },
  {
    id: 4,
    name: '赵六',
    email: 'zhaoliu@example.com',
    department: '数据部',
    currentRole: 'user'
  },
  {
    id: 5,
    name: '孙七',
    email: 'sunqi@example.com',
    department: '运营部',
    currentRole: 'user'
  },
  {
    id: 6,
    name: '周八',
    email: 'zhouba@example.com',
    department: '市场部',
    currentRole: 'user'
  }
])

// 从localStorage加载所有用户列表（如果有的话）
const loadAllUsers = () => {
  try {
    const saved = localStorage.getItem('all_users_list')
    if (saved) {
      const parsed = JSON.parse(saved)
      if (Array.isArray(parsed) && parsed.length > 0) {
        allUsersList.value = parsed
      }
    }
  } catch (e) {
    console.warn('加载用户列表失败:', e)
  }
}

// 保存用户列表到localStorage
const saveUsersList = () => {
  try {
    localStorage.setItem('all_users_list', JSON.stringify(allUsersList.value))
    localStorage.setItem('admin_users_list', JSON.stringify(usersList.value))
  } catch (e) {
    console.warn('保存用户列表失败:', e)
  }
}

// 过滤后的管理员列表（只显示管理员）
const filteredAdminList = computed(() => {
  let admins = usersList.value.filter(user => user.currentRole === 'admin')
  
  if (userSearchKeyword.value) {
    const keyword = userSearchKeyword.value.toLowerCase()
    admins = admins.filter(admin =>
      admin.name.toLowerCase().includes(keyword) ||
      admin.email.toLowerCase().includes(keyword) ||
      String(admin.id).includes(keyword)
    )
  }
  
  return admins
})

// 添加管理员对话框
const showAddAdminDialog = ref(false)
const addAdminForm = ref({
  userId: null as number | null
})

// 查找用户
const foundUser = computed(() => {
  if (!addAdminForm.value.userId) return null
  return allUsersList.value.find(user => user.id === addAdminForm.value.userId)
})

// 添加管理员
const handleAddAdmin = () => {
  if (!addAdminForm.value.userId) {
    ElMessage.warning('请输入用户ID')
    return
  }
  
  const user = foundUser.value
  if (!user) {
    ElMessage.error('未找到该用户ID对应的用户')
    return
  }
  
  // 检查是否已经是管理员
  const existingAdmin = usersList.value.find(u => u.id === user.id && u.currentRole === 'admin')
  if (existingAdmin) {
    ElMessage.warning('该用户已经是管理员')
    showAddAdminDialog.value = false
    addAdminForm.value.userId = null
    return
  }
  
  // 检查用户是否在列表中
  const existingUser = usersList.value.find(u => u.id === user.id)
  if (existingUser) {
    // 更新角色为管理员
    existingUser.currentRole = 'admin'
    ElMessage.success(`已将 ${user.name} 设置为管理员`)
  } else {
    // 添加新用户并设置为管理员
    usersList.value.push({
      id: user.id,
      name: user.name,
      email: user.email,
      department: user.department,
      currentRole: 'admin'
    })
    ElMessage.success(`已将 ${user.name} 添加为管理员`)
  }
  
  // 保存到localStorage
  saveUsersList()
  
  // 关闭对话框并重置表单
  showAddAdminDialog.value = false
  addAdminForm.value.userId = null
}

// 移除管理员
const handleRemoveAdmin = (user: UserItem) => {
  ElMessageBox.confirm(
    `确定要将 ${user.name} 移除管理员身份吗？`,
    '确认移除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 将角色改为普通用户
    const userIndex = usersList.value.findIndex(u => u.id === user.id)
    if (userIndex !== -1) {
      usersList.value[userIndex].currentRole = 'user'
      // 从管理员列表中移除（因为只显示管理员）
      usersList.value = usersList.value.filter(u => u.currentRole === 'admin')
    }
    
    ElMessage.success(`已将 ${user.name} 移除管理员身份`)
    
    // 保存到localStorage
    saveUsersList()
  }).catch(() => {
    // 用户取消
  })
}

// 初始化时加载用户列表
onMounted(() => {
  loadAllUsers()
  // 从localStorage加载管理员列表
  try {
    const saved = localStorage.getItem('admin_users_list')
    if (saved) {
      const parsed = JSON.parse(saved)
      if (Array.isArray(parsed) && parsed.length > 0) {
        // 只加载管理员
        const admins = parsed.filter((u: UserItem) => u.currentRole === 'admin')
        if (admins.length > 0) {
          usersList.value = admins
        }
      }
    } else {
      // 如果没有保存的数据，初始化时只保留管理员
      usersList.value = usersList.value.filter(u => u.currentRole === 'admin')
      saveUsersList()
    }
  } catch (e) {
    console.warn('加载管理员列表失败:', e)
    // 如果加载失败，初始化时只保留管理员
    usersList.value = usersList.value.filter(u => u.currentRole === 'admin')
  }
})


// 发布活动相关
const showPublishActivityDialog = ref(false)
const publishingActivity = ref(false)
const activityFormRef = ref()
const activityEditorRef = shallowRef<IDomEditor>()
const editorMode = 'default'
const editingActivityId = ref<number | null>(null)

// 所有工具列表（包括扶摇Agent应用）
const allToolsList = computed(() => {
  const tools = [...toolsList.value]
  // 添加扶摇Agent应用
  tools.push({
    id: -1,
    name: '扶摇Agent应用',
    desc: 'Agent编排引擎',
    logo: '',
    logoType: 'url' as const,
    color: '#4096ff',
    link: '/agent'
  })
  return tools
})

// 活动表单
const activityForm = ref({
  title: '',
  toolId: null as number | null,
  date: '',
  cover: '',
  content: ''
})

// 活动表单验证规则
const activityRules = {
  title: [{ required: true, message: '请输入活动标题', trigger: 'blur' }],
  toolId: [{ required: true, message: '请选择工具', trigger: 'change' }],
  date: [{ required: true, message: '请选择活动时间', trigger: 'change' }],
  cover: [{ required: true, message: '请上传封面图片', trigger: 'change' }],
  content: [{ required: true, message: '请输入活动内容', trigger: 'blur' }]
}

// 活动编辑器配置
const activityToolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: []
}

const activityEditorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入活动内容...',
  readOnly: false,
  MENU_CONF: {
    uploadImage: {
      maxFileSize: 5 * 1024 * 1024,
      maxNumberOfFiles: 10,
      allowedFileTypes: ['image/*'],
      async customUpload(file: File, insertFn: (url: string, alt?: string, href?: string) => void) {
        const isImage = file.type.startsWith('image/')
        const isLt5M = file.size / 1024 / 1024 < 5

        if (!isImage) {
          ElMessage.error('只能上传图片文件！')
          return
        }
        if (!isLt5M) {
          ElMessage.error('图片大小不能超过 5MB！')
          return
        }

        try {
          const imageUrl = URL.createObjectURL(file)
          insertFn(imageUrl, file.name)
          ElMessage.success('图片插入成功')
        } catch (error) {
          ElMessage.error('图片上传失败')
        }
      }
    }
  }
}

// 活动编辑器创建
const handleActivityEditorCreated = (editor: IDomEditor) => {
  activityEditorRef.value = editor
  
  // 如果有待加载的内容（编辑模式），设置编辑器内容
  if (pendingActivityContent.value) {
    nextTick(() => {
      editor.setHtml(pendingActivityContent.value)
      pendingActivityContent.value = ''
    })
  }
}

// 活动编辑器内容变化
const handleActivityEditorChange = (editor: IDomEditor) => {
  activityForm.value.content = editor.getHtml()
}

// 活动封面图片选择
const handleActivityCoverChange = async (file: any) => {
  if (!file.raw) return
  try {
    const base64 = await fileToBase64(file.raw)
    activityForm.value.cover = base64
    ElMessage.success('封面图片已加载')
  } catch (error) {
    console.error('图片处理失败:', error)
    ElMessage.error('图片处理失败')
  }
}

// 发布活动
const handlePublishActivity = async () => {
  if (!activityFormRef.value) return

  try {
    await activityFormRef.value.validate()
    
    if (activityEditorRef.value) {
      activityForm.value.content = activityEditorRef.value.getHtml()
    }
    
    if (!activityForm.value.content || activityForm.value.content === '<p><br></p>') {
      ElMessage.error('请输入活动内容')
      return
    }

    publishingActivity.value = true

    // 这里应该调用API发布活动
    // await publishActivity(activityForm.value)
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 保存到localStorage（实际应该保存到后端）
    const selectedTool = allToolsList.value.find(t => t.id === activityForm.value.toolId)
    
    const activities = JSON.parse(localStorage.getItem('admin_activities') || '[]')
    
    if (editingActivityId.value) {
      // 编辑模式：更新现有活动
      const index = activities.findIndex((a: any) => a.id === editingActivityId.value)
      if (index !== -1) {
        activities[index] = {
          ...activities[index],
          toolId: activityForm.value.toolId,
          toolName: selectedTool?.name || '未知工具',
          title: activityForm.value.title,
          date: activityForm.value.date,
          cover: activityForm.value.cover,
          content: activityForm.value.content
        }
      }
    } else {
      // 新建模式：添加新活动
      const activityId = Date.now()
      const activityData = {
        id: activityId,
        toolId: activityForm.value.toolId,
        toolName: selectedTool?.name || '未知工具',
        title: activityForm.value.title,
        date: activityForm.value.date,
        cover: activityForm.value.cover,
        content: activityForm.value.content
      }
      activities.push(activityData)
    }
    
    localStorage.setItem('admin_activities', JSON.stringify(activities))

    publishingActivity.value = false
    showPublishActivityDialog.value = false
    
    // 重置表单（在handleCloseActivityDialog中处理）
    handleCloseActivityDialog()
    
    ElMessage.success(editingActivityId.value ? '活动更新成功！' : '活动发布成功！')
  } catch (error) {
    console.error('发布失败:', error)
    publishingActivity.value = false
  }
}

// 图片上传前的验证
const beforeImageUpload = (file: File) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

// 图片文件选择处理（使用base64作为临时方案）
const handleImageFileChange = async (file: any, target: any, type: string) => {
  if (!file.raw) return
  
  try {
    // 转换为base64（临时方案，实际应该上传到服务器）
    const base64 = await fileToBase64(file.raw)
    
    if (type === 'carousel') {
      target.image = base64
    } else if (type === 'honorBanner') {
      target.bannerImage = base64
    } else if (type === 'news') {
      target.image = base64
    } else if (type === 'tool') {
      target.logo = base64
    } else if (type === 'activity') {
      target.bannerImage = base64
    } else if (type === 'toolBanner') {
      target.image = base64
    }
    
    ElMessage.success('图片已加载')
    
    // 实际应该调用上传API
    // const formData = new FormData()
    // formData.append('file', file.raw)
    // const response = await uploadImage(formData)
    // target.image = response.url
  } catch (error) {
    console.error('图片处理失败:', error)
    ElMessage.error('图片处理失败')
  }
}

// 图片类型切换处理
const handleImageTypeChange = (item: CarouselItem, index: number) => {
  if (item.imageType === 'url') {
    // 切换到URL模式，清空上传的图片
    // 这里可以根据需要决定是否清空
  }
}

const handleHonorBannerImageTypeChange = () => {
  // 处理荣誉殿堂banner图片类型切换
}

const handleNewsImageTypeChange = (news: NewsItem, index: number) => {
  // 处理头条图片类型切换
}

const handleToolLogoTypeChange = (tool: ToolItem, index: number) => {
  // 处理工具Logo类型切换
}

// 添加工具Banner
const handleAddToolBanner = () => {
  toolBannersList.value.push({
    id: Date.now(),
    image: '',
    imageType: 'url',
    title: '',
    desc: ''
  })
}

// 删除工具Banner
const handleDeleteToolBanner = (index: number) => {
  ElMessageBox.confirm('确定要删除这个Banner吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    toolBannersList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 上移工具Banner
const handleMoveToolBannerUp = (index: number) => {
  if (index > 0) {
    const temp = toolBannersList.value[index]
    toolBannersList.value[index] = toolBannersList.value[index - 1]
    toolBannersList.value[index - 1] = temp
  }
}

// 下移工具Banner
const handleMoveToolBannerDown = (index: number) => {
  if (index < toolBannersList.value.length - 1) {
    const temp = toolBannersList.value[index]
    toolBannersList.value[index] = toolBannersList.value[index + 1]
    toolBannersList.value[index + 1] = temp
  }
}

// 处理工具Banner图片类型切换
const handleToolBannerImageTypeChange = (banner: ToolBannerItem, index: number) => {
  // 处理工具Banner图片类型切换
}

// 添加精华帖子
const handleAddFeaturedPost = () => {
  featuredPostsList.value.push({
    id: Date.now(),
    url: '',
    note: ''
  })
}

// 删除精华帖子
const handleDeleteFeaturedPost = (index: number) => {
  ElMessageBox.confirm('确定要删除这个精华帖子吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    featuredPostsList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 添加奖项
const handleAddAward = () => {
  awardsList.value.push({
    id: Date.now(),
    name: '',
    category: '',
    description: ''
  })
}

// 删除奖项
const handleDeleteAward = (index: number) => {
  ElMessageBox.confirm('确定要删除这个奖项吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    awardsList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 添加获奖者
const handleAddWinner = async () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  
  winnersList.value.push({
    id: Date.now(),
    name: '',
    awardTime: `${year}-${month}`,
    awardName: '',
    category: ''
  })
  
  // 加载所有奖项列表（不筛选分类）
  await loadAwardsFromApi()
}

// 删除获奖者
const handleDeleteWinner = (index: number) => {
  ElMessageBox.confirm('确定要删除这个获奖者吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    winnersList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 加载获奖者推荐列表
const loadRecommendedWinners = async () => {
  if (!recommendedMonth.value) {
    ElMessage.warning('请选择月份')
    return
  }

  loadingRecommended.value = true
  try {
    // 模拟API调用：GET /api/admin/honors/recommended-winners?month=YYYY-MM&limit=3
    // 实际应该调用真实API
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据：从localStorage或生成mock数据
    const savedData = localStorage.getItem(`recommended_winners_${recommendedMonth.value}`)
    if (savedData) {
      recommendedWinnersList.value = JSON.parse(savedData)
    } else {
      // 生成模拟数据（Top 3用户）
      const mockUsers: RecommendedWinner[] = [
        {
          id: 2,
          employeeId: 'E001',
          name: '李四',
          avatar: 'https://picsum.photos/80/80?random=2',
          department: '产品部',
          points: 156,
          postsCount: 8,
          commentsCount: 25,
          activitiesCount: 3,
          likesReceived: 45,
          favoritesReceived: 12,
          hasAwarded: false
        },
        {
          id: 3,
          employeeId: 'E002',
          name: '王五',
          avatar: 'https://picsum.photos/80/80?random=3',
          department: '技术部',
          points: 142,
          postsCount: 6,
          commentsCount: 30,
          activitiesCount: 2,
          likesReceived: 38,
          favoritesReceived: 10,
          hasAwarded: false
        },
        {
          id: 4,
          employeeId: 'E003',
          name: '赵六',
          avatar: 'https://picsum.photos/80/80?random=4',
          department: '数据部',
          points: 128,
          postsCount: 5,
          commentsCount: 20,
          activitiesCount: 4,
          likesReceived: 32,
          favoritesReceived: 8,
          hasAwarded: false
        }
      ]
      
      recommendedWinnersList.value = mockUsers
      localStorage.setItem(`recommended_winners_${recommendedMonth.value}`, JSON.stringify(mockUsers))
    }
  } catch (error) {
    console.error('加载推荐用户失败:', error)
    ElMessage.error('加载推荐用户失败')
  } finally {
    loadingRecommended.value = false
  }
}

// 从接口获取奖项列表
const loadAwardsFromApi = async (category?: string) => {
  loadingAwards.value = true
  try {
    // 模拟API调用：GET /api/awards?category=xxx
    // 实际应该调用真实API
    await new Promise(resolve => setTimeout(resolve, 300))
    
    // 从localStorage获取已配置的奖项（实际应该从接口获取）
    const savedAwards = localStorage.getItem('admin_awards_list')
    let awards: AwardItem[] = []
    if (savedAwards) {
      awards = JSON.parse(savedAwards)
    } else {
      awards = awardsList.value
    }
    
    // 转换为API格式的奖项列表
    // 注意：这里需要根据实际的API响应格式来调整
    // 假设API返回的category字段对应关系：
    // '年度奖项' -> 'innovation' | 'efficiency' | 'practice' | 'community'
    // 这里需要根据实际配置的奖项分类来映射
    apiAwardsList.value = awards.map(award => ({
      id: award.id,
      name: award.name,
      desc: award.description || '',
      image: '',
      category: mapCategoryToApiCategory(award.category),
      rules: award.description || ''
    }))
    
    // 如果有指定分类，只返回该分类的奖项
    if (category) {
      apiAwardsList.value = apiAwardsList.value.filter(a => a.category === category)
    }
  } catch (error) {
    console.error('获取奖项列表失败:', error)
    ElMessage.error('获取奖项列表失败')
  } finally {
    loadingAwards.value = false
  }
}

// 将配置的奖项分类映射到API的category
const mapCategoryToApiCategory = (category: string): 'innovation' | 'efficiency' | 'practice' | 'community' => {
  // 根据实际配置的奖项分类来映射
  // 这里可以根据需要调整映射逻辑
  const categoryMap: Record<string, 'innovation' | 'efficiency' | 'practice' | 'community'> = {
    '创新突破': 'innovation',
    '效率提升': 'efficiency',
    '最佳实践': 'practice',
    '社区贡献': 'community',
    '年度奖项': 'innovation', // 默认映射
    'innovation': 'innovation',
    'efficiency': 'efficiency',
    'practice': 'practice',
    'community': 'community'
  }
  return categoryMap[category] || 'innovation'
}

// 设置评奖
const handleSetAward = async (user: RecommendedWinner) => {
  currentAwardUser.value = user
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  
  awardForm.value = {
    userId: user.id,
    awardId: null,
    awardName: '',
    awardDate: `${year}-${month}`,
    category: ''
  }
  
  // 加载所有奖项列表（不筛选分类）
  await loadAwardsFromApi()
  
  showSetAwardDialog.value = true
}

// 奖项分类变化
const handleCategoryChange = async (category: string) => {
  // 清空已选择的奖项
  awardForm.value.awardId = null
  awardForm.value.awardName = ''
  
  // 根据分类加载奖项列表
  if (category) {
    await loadAwardsFromApi(category)
  }
}

// 奖项选择变化
const handleAwardChange = (awardId: number) => {
  const award = apiAwardsList.value.find(a => a.id === awardId)
  if (award) {
    awardForm.value.awardName = award.name
  }
}

// 确认设置评奖
const handleConfirmSetAward = async () => {
  if (!awardFormRef.value) return

  try {
    await awardFormRef.value.validate()
    
    if (!currentAwardUser.value) {
      ElMessage.error('用户信息丢失')
      return
    }

    if (!awardForm.value.awardId) {
      ElMessage.error('请选择奖项名称')
      return
    }

    const selectedAward = apiAwardsList.value.find(a => a.id === awardForm.value.awardId)
    if (!selectedAward) {
      ElMessage.error('奖项信息不存在')
      return
    }

    settingAward.value = true

    // 模拟API调用：POST /api/admin/honors
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 创建荣誉记录
    const honorId = Date.now()
    // 从获奖时间（YYYY-MM）中提取年份
    const year = awardForm.value.awardDate.split('-')[0]
    
    const honorRecord = {
      id: honorId,
      userId: currentAwardUser.value.id,
      awardId: awardForm.value.awardId,
      awardName: selectedAward.name,
      awardDate: awardForm.value.awardDate,
      category: awardForm.value.category,
      year: year
    }

    // 保存到localStorage（实际应该保存到后端）
    const honors = JSON.parse(localStorage.getItem('admin_honors') || '[]')
    honors.push(honorRecord)
    localStorage.setItem('admin_honors', JSON.stringify(honors))

    // 更新推荐用户列表中的状态
    const userIndex = recommendedWinnersList.value.findIndex(u => u.id === currentAwardUser.value!.id)
    if (userIndex !== -1) {
      recommendedWinnersList.value[userIndex].hasAwarded = true
      recommendedWinnersList.value[userIndex].honorId = honorId
      
      // 更新localStorage中的推荐用户数据
      localStorage.setItem(
        `recommended_winners_${recommendedMonth.value}`,
        JSON.stringify(recommendedWinnersList.value)
      )
    }

    settingAward.value = false
    showSetAwardDialog.value = false
    ElMessage.success('评奖设置成功')
  } catch (error) {
    console.error('设置评奖失败:', error)
    settingAward.value = false
  }
}

// 取消评奖
const handleCancelAward = (user: RecommendedWinner) => {
  if (!user.honorId) {
    ElMessage.warning('该用户没有获奖记录')
    return
  }

  ElMessageBox.confirm(
    `确定要取消 ${user.name} 的获奖记录吗？`,
    '确认取消',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      // 模拟API调用：DELETE /api/admin/honors/:id
      await new Promise(resolve => setTimeout(resolve, 500))
      
      // 从localStorage删除荣誉记录
      const honors = JSON.parse(localStorage.getItem('admin_honors') || '[]')
      const updatedHonors = honors.filter((h: any) => h.id !== user.honorId)
      localStorage.setItem('admin_honors', JSON.stringify(updatedHonors))

      // 更新推荐用户列表中的状态
      const userIndex = recommendedWinnersList.value.findIndex(u => u.id === user.id)
      if (userIndex !== -1) {
        recommendedWinnersList.value[userIndex].hasAwarded = false
        recommendedWinnersList.value[userIndex].honorId = undefined
        
        // 更新localStorage中的推荐用户数据
        localStorage.setItem(
          `recommended_winners_${recommendedMonth.value}`,
          JSON.stringify(recommendedWinnersList.value)
        )
      }

      ElMessage.success('已取消评奖')
    } catch (error) {
      console.error('取消评奖失败:', error)
      ElMessage.error('取消评奖失败')
    }
  }).catch(() => {
    // 用户取消
  })
}

// 添加扶摇Agent应用置顶帖子
const handleAddAgentPinnedPost = () => {
  agentPinnedPostsList.value.push({
    id: Date.now(),
    url: '',
    note: ''
  })
}

// 删除扶摇Agent应用置顶帖子
const handleDeleteAgentPinnedPost = (index: number) => {
  ElMessageBox.confirm('确定要删除这个置顶帖子吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    agentPinnedPostsList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 添加赋能交流精选合集
const handleAddFeaturedCollection = () => {
  featuredCollectionsList.value.push({
    id: Date.now(),
    url: '',
    note: ''
  })
}

// 删除赋能交流精选合集
const handleDeleteFeaturedCollection = (index: number) => {
  ElMessageBox.confirm('确定要删除这个精选合集吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    featuredCollectionsList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 添加轮播图
const handleAddCarousel = () => {
  carouselList.value.push({
    id: Date.now(),
    image: '',
    imageType: 'url',
    link: '',
    showContent: false,
    title: '',
    desc: ''
  })
}

// 删除轮播图
const handleDeleteCarousel = (index: number) => {
  ElMessageBox.confirm('确定要删除这个轮播图吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    carouselList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 上移轮播图
const handleMoveUp = (index: number) => {
  if (index > 0) {
    const temp = carouselList.value[index]
    carouselList.value[index] = carouselList.value[index - 1]
    carouselList.value[index - 1] = temp
  }
}

// 下移轮播图
const handleMoveDown = (index: number) => {
  if (index < carouselList.value.length - 1) {
    const temp = carouselList.value[index]
    carouselList.value[index] = carouselList.value[index + 1]
    carouselList.value[index + 1] = temp
  }
}

// 添加荣誉殿堂奖项（旧版，保留用于兼容）
const handleAddHonorAward = () => {
  honorConfig.value.awards.push({
    id: Date.now(),
    name: '',
    desc: '',
    image: ''
  })
}

// 删除荣誉殿堂奖项（旧版，保留用于兼容）
const handleDeleteHonorAward = (index: number) => {
  ElMessageBox.confirm('确定要删除这个奖项吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    honorConfig.value.awards.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 添加头条
const handleAddNews = () => {
  newsList.value.push({
    id: Date.now(),
    title: '',
    image: '',
    link: '',
    date: ''
  })
}

// 删除头条
const handleDeleteNews = (index: number) => {
  ElMessageBox.confirm('确定要删除这条头条吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    newsList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 上移头条
const handleMoveNewsUp = (index: number) => {
  if (index > 0) {
    const temp = newsList.value[index]
    newsList.value[index] = newsList.value[index - 1]
    newsList.value[index - 1] = temp
  }
}

// 下移头条
const handleMoveNewsDown = (index: number) => {
  if (index < newsList.value.length - 1) {
    const temp = newsList.value[index]
    newsList.value[index] = newsList.value[index + 1]
    newsList.value[index + 1] = temp
  }
}

// 添加工具
const handleAddTool = () => {
  toolsList.value.push({
    id: Date.now(),
    name: '',
    desc: '',
    logo: '',
    logoType: 'url',
    color: '#409eff',
    link: ''
  })
}

// 删除工具
const handleDeleteTool = (index: number) => {
  ElMessageBox.confirm('确定要删除这个工具吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    toolsList.value.splice(index, 1)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

// 保存配置
const handleSave = async () => {
  try {
    // 这里应该调用API保存配置
    // await saveAdminConfig({
    //   carousel: carouselList.value,
    //   honor: honorConfig.value,
    //   news: newsList.value,
    //   tools: toolsList.value,
    //   practices: practicesPosts.value,
    //   awards: awardsList.value,
    //   winners: winnersList.value,
    //   toolActivities: toolActivitiesList.value
    // })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('配置保存成功！')
    
    // 实际应该更新全局状态或重新加载数据
    // 这里可以触发事件或调用store更新
    // 更新HomeView、HeroCarousel、PostCreateView等组件的数据
    updateComponentsData()
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

// 更新组件数据（使用localStorage存储，实际应该通过store或API）
const updateComponentsData = () => {
  // 保存到localStorage（实际应该保存到后端）
  try {
    localStorage.setItem('admin_carousel_config', JSON.stringify(carouselList.value))
    localStorage.setItem('admin_honor_config', JSON.stringify(honorConfig.value))
    localStorage.setItem('admin_news_config', JSON.stringify(newsList.value))
    localStorage.setItem('admin_tools_config', JSON.stringify(toolsList.value))
    localStorage.setItem('admin_tool_banners_config', JSON.stringify(toolBannersList.value))
    localStorage.setItem('admin_featured_posts', JSON.stringify(featuredPostsList.value))
    localStorage.setItem('admin_awards_list', JSON.stringify(awardsList.value))
    localStorage.setItem('admin_winners_list', JSON.stringify(winnersList.value))
    localStorage.setItem('admin_agent_pinned_posts', JSON.stringify(agentPinnedPostsList.value))
    localStorage.setItem('admin_featured_collections', JSON.stringify(featuredCollectionsList.value))
    localStorage.setItem('admin_users_list', JSON.stringify(usersList.value))
    
    // 触发storage事件，通知其他页面更新
    window.dispatchEvent(new Event('adminConfigUpdated'))
  } catch (e) {
    console.error('保存配置到localStorage失败:', e)
  }
}

// 重置配置
const handleReset = () => {
  ElMessageBox.confirm('确定要重置所有配置吗？这将恢复默认值。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    // 这里应该从API重新加载配置
    // loadConfig()
    ElMessage.success('已重置')
  }).catch(() => {})
}

// 加载配置
const loadConfig = async () => {
  try {
    // 从localStorage加载配置（实际应该从API加载）
    const carouselConfig = localStorage.getItem('admin_carousel_config')
    if (carouselConfig) {
      const config = JSON.parse(carouselConfig)
      carouselList.value = config.map((item: any) => ({
        ...item,
        imageType: item.imageType || (item.image ? 'url' : 'url')
      }))
    }
    
    const honorConfigData = localStorage.getItem('admin_honor_config')
    if (honorConfigData) {
      const config = JSON.parse(honorConfigData)
      honorConfig.value = {
        ...config,
        bannerImageType: config.bannerImageType || (config.bannerImage ? 'url' : 'url')
      }
    }
    
    const newsConfig = localStorage.getItem('admin_news_config')
    if (newsConfig) {
      const config = JSON.parse(newsConfig)
      newsList.value = config.map((item: any) => ({
        ...item,
        imageType: item.imageType || (item.image ? 'url' : 'url')
      }))
    }
    
    const toolsConfig = localStorage.getItem('admin_tools_config')
    if (toolsConfig) {
      const config = JSON.parse(toolsConfig)
      toolsList.value = config.map((item: any) => ({
        ...item,
        logoType: item.logoType || (item.logo ? 'url' : 'url')
      }))
    }
    
    const toolBannersConfig = localStorage.getItem('admin_tool_banners_config')
    if (toolBannersConfig) {
      const config = JSON.parse(toolBannersConfig)
      toolBannersList.value = config.map((item: any) => ({
        ...item,
        imageType: item.imageType || (item.image ? 'url' : 'url')
      }))
    }
    
    // 初始化图片类型（如果没有配置）
    carouselList.value.forEach(item => {
      if (!item.imageType) {
        item.imageType = item.image ? 'url' : 'url'
      }
    })
    newsList.value.forEach(item => {
      if (!item.imageType) {
        item.imageType = item.image ? 'url' : 'url'
      }
    })
    toolsList.value.forEach(item => {
      if (!item.logoType) {
        item.logoType = item.logo ? 'url' : 'url'
      }
    })
    toolBannersList.value.forEach(item => {
      if (!item.imageType) {
        item.imageType = item.image ? 'url' : 'url'
      }
    })
    if (!honorConfig.value.bannerImageType) {
      honorConfig.value.bannerImageType = honorConfig.value.bannerImage ? 'url' : 'url'
    }
  } catch (error) {
    console.error('加载配置失败:', error)
  }
}

// 监听编辑活动事件
const handleEditActivityEvent = (event: CustomEvent) => {
  const activityId = event.detail.activityId
  if (activityId) {
    // 先打开对话框
    showPublishActivityDialog.value = true
    // 等待对话框打开后再加载数据
    nextTick(() => {
      setTimeout(() => {
        loadActivityForEdit(activityId)
      }, 100)
    })
  }
}

onMounted(() => {
  loadConfig()
  // 监听编辑活动事件
  window.addEventListener('editActivity', handleEditActivityEvent as EventListener)
  // 加载获奖者推荐列表
  loadRecommendedWinners()
})

// 待加载的活动内容（用于编辑器创建后加载）
const pendingActivityContent = ref<string>('')

// 加载活动数据用于编辑
const loadActivityForEdit = (activityId: number) => {
  const activities = JSON.parse(localStorage.getItem('admin_activities') || '[]')
  const activity = activities.find((a: any) => a.id === activityId)
  
  if (activity) {
    editingActivityId.value = activityId
    activityForm.value = {
      title: activity.title || '',
      toolId: activity.toolId,
      date: activity.date || '',
      cover: activity.cover || '',
      content: activity.content || ''
    }
    
    // 保存待加载的内容
    pendingActivityContent.value = activity.content || ''
    
    // 如果编辑器已经创建，直接设置内容
    if (activityEditorRef.value) {
      activityEditorRef.value.setHtml(pendingActivityContent.value)
      pendingActivityContent.value = ''
    }
  } else {
    ElMessage.error('活动不存在')
  }
}

// 关闭活动对话框
const handleCloseActivityDialog = () => {
  // 清理编辑器
  if (activityEditorRef.value) {
    activityEditorRef.value.destroy()
    activityEditorRef.value = undefined as any
  }
  // 重置表单
  editingActivityId.value = null
  activityForm.value = {
    title: '',
    toolId: null,
    date: '',
    cover: '',
    content: ''
  }
  if (activityFormRef.value) {
    activityFormRef.value.clearValidate()
  }
}

onBeforeUnmount(() => {
  // 清理事件监听
  window.removeEventListener('editActivity', handleEditActivityEvent as EventListener)
  // 清理编辑器
  if (activityEditorRef.value) {
    activityEditorRef.value.destroy()
  }
})
</script>

<style scoped lang="scss">
.admin-view {
  min-height: 100vh;
  padding: 24px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
}

.admin-container {
  max-width: 1400px;
  margin: 0 auto;
}

.admin-header {
  margin-bottom: 32px;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-wrap: wrap;
    gap: 20px;

    > div:first-child {
      flex: 1;
      text-align: center;

      h1 {
        font-size: 32px;
        font-weight: 700;
        color: #333;
        margin: 0 0 8px 0;
      }

      p {
        font-size: 14px;
        color: #666;
        margin: 0;
      }
    }

  }
}

.admin-tabs {
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);

  :deep(.el-tabs__header) {
    margin-bottom: 24px;
  }

  :deep(.el-tabs__item) {
    font-size: 16px;
    font-weight: 600;
  }
}

.sub-tabs {
  :deep(.el-tabs__header) {
    margin-bottom: 20px;
  }

  :deep(.el-tabs__item) {
    font-size: 14px;
  }
}

.image-upload-wrapper {
  width: 100%;

  .image-uploader {
    width: 100%;
  }
}

.no-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  color: #999;
  font-size: 14px;
}

.tools-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 24px;

  .tool-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
}

.tab-content {
  min-height: 400px;
}

.config-section {
  margin-bottom: 40px;

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 2px solid rgba(0, 0, 0, 0.1);

    h2 {
      font-size: 20px;
      font-weight: 700;
      color: #333;
      margin: 0;
    }
  }
}

// 轮播图列表
.carousel-list,
.banner-list {
  display: flex;
  flex-direction: column;
  gap: 24px;

  .carousel-item,
  .banner-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    display: flex;
    gap: 24px;

    .item-preview {
      flex-shrink: 0;
      width: 300px;
      height: 200px;
      border-radius: 8px;
      overflow: hidden;
      position: relative;
      background: #f5f5f5;

      img {
        width: 100%;
        height: 100%;
        object-fit: cover;
      }

      .preview-content {
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        padding: 16px;
        background: linear-gradient(to top, rgba(0, 0, 0, 0.7), transparent);
        color: #fff;

        h3 {
          margin: 0 0 8px 0;
          font-size: 18px;
          font-weight: 600;
        }

        p {
          margin: 0;
          font-size: 14px;
          opacity: 0.9;
        }
      }
    }

    .item-form {
      flex: 1;
    }
  }
}

// 奖项列表
.awards-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(400px, 1fr));
  gap: 24px;

  .award-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
}

// 头条列表
.news-list {
  display: flex;
  flex-direction: column;
  gap: 24px;

  .news-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
}

.preview-box {
  margin-top: 12px;
  width: 200px;
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  background: #f5f5f5;
  border: 1px solid rgba(0, 0, 0, 0.1);

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.admin-footer {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 2px solid rgba(0, 0, 0, 0.1);
  display: flex;
  justify-content: center;
  gap: 16px;
}

@media (max-width: 768px) {
  .carousel-item {
    flex-direction: column;

    .item-preview {
      width: 100%;
    }
  }

  .awards-list {
    grid-template-columns: 1fr;
  }
}

// 精华帖子URL列表
.featured-posts-list {
  display: flex;
  flex-direction: column;
  gap: 24px;

  .featured-post-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    transition: all 0.3s;

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }
  }
}

// 获奖者列表
.winners-list {
  display: flex;
  flex-direction: column;
  gap: 24px;

  .winner-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
}

// 获奖者推荐列表
.recommended-winners-list {
  display: flex;
  flex-direction: column;
  gap: 24px;

  .recommended-winner-item {
    background: rgba(255, 255, 255, 0.8);
    border-radius: 12px;
    padding: 24px;
    border: 1px solid rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;
    transition: all 0.3s;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

    &:hover {
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transform: translateY(-2px);
    }

    .winner-info {
      display: flex;
      gap: 20px;
      flex: 1;

      .winner-avatar {
        position: relative;
        flex-shrink: 0;

        .rank-badge {
          position: absolute;
          top: -8px;
          right: -8px;
          width: 24px;
          height: 24px;
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 12px;
          font-weight: 700;
          box-shadow: 0 2px 8px rgba(102, 126, 234, 0.4);
        }
      }

      .winner-details {
        flex: 1;

        .winner-name-row {
          display: flex;
          align-items: center;
          gap: 12px;
          margin-bottom: 12px;

          h3 {
            margin: 0;
            font-size: 18px;
            font-weight: 700;
            color: #333;
          }
        }

        .winner-meta {
          display: flex;
          gap: 24px;
          margin-bottom: 16px;
          font-size: 14px;
          color: #666;

          span {
            strong {
              color: #333;
            }
          }
        }

        .winner-stats {
          display: grid;
          grid-template-columns: repeat(3, 1fr);
          gap: 16px;

          .stat-item {
            display: flex;
            flex-direction: column;
            gap: 4px;

            .stat-label {
              font-size: 12px;
              color: #999;
            }

            .stat-value {
              font-size: 16px;
              font-weight: 600;
              color: #333;

              &.highlight {
                color: #409eff;
                font-size: 20px;
              }
            }
          }
        }
      }
    }

    .winner-actions {
      flex-shrink: 0;
    }
  }
}

.loading-state {
  padding: 40px 0;
}

// 活动列表
.activities-list {
  display: flex;
  flex-direction: column;
  gap: 24px;

  .activity-item {
    background: rgba(255, 255, 255, 0.6);
    border-radius: 12px;
    padding: 20px;
    border: 1px solid rgba(0, 0, 0, 0.1);
  }
}

// 编辑器样式
.editor-wrapper {
  width: 100%;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: #fff;

  .editor-toolbar {
    border-bottom: 1px solid #e4e7ed;
  }

  .editor-content {
    height: 400px !important;
    overflow-y: auto;

    :deep(.w-e-text-container) {
      height: 400px !important;
    }
  }
}

// 封面预览
.cover-uploader {
  .cover-preview {
    position: relative;
    width: 200px;
    height: 120px;
    border-radius: 8px;
    overflow: hidden;
    border: 1px solid #dcdfe6;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .cover-overlay {
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: rgba(0, 0, 0, 0.5);
      display: flex;
      align-items: center;
      justify-content: center;
      opacity: 0;
      transition: opacity 0.3s;

      &:hover {
        opacity: 1;
      }
    }
  }
}
</style>

