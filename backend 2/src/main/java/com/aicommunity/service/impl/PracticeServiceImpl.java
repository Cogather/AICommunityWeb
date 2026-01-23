package com.aicommunity.service.impl;

import com.aicommunity.entity.PostTag;
import com.aicommunity.mapper.PracticeMapper;
import com.aicommunity.mapper.PostTagMapper;
import com.aicommunity.service.PracticeService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI优秀实践服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class PracticeServiceImpl implements PracticeService {

    @Autowired
    private PracticeMapper practiceMapper;

    @Autowired
    private PostTagMapper postTagMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("yyyy年MM月dd日");

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Override
    public PostListVO getPostList(Integer page, Integer pageSize, String keyword,
                                  String tag, String department, String contributor, String sortBy) {
        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }
        // 限制pageSize范围
        if (!Arrays.asList(10, 15, 20, 30, 50).contains(pageSize)) {
            pageSize = 15;
        }
        if (sortBy == null || sortBy.isEmpty()) {
            sortBy = "newest";
        }

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询帖子列表（标签筛选已在SQL中处理）
        List<PostItemVO> posts = practiceMapper.selectPracticePosts(keyword, tag, department, contributor, sortBy, offset, pageSize);
        
        // 查询总数
        Long total = practiceMapper.countPracticePosts(keyword, tag, department, contributor);

        // 处理帖子数据：添加标签、格式化时间等
        List<PostItemVO> processedPosts = processPosts(posts);

        // 分离精华帖和普通帖子
        List<PostItemVO> featuredPosts = processedPosts.stream()
                .filter(post -> post.getFeatured() != null && post.getFeatured())
                .collect(Collectors.toList());
        List<PostItemVO> normalPosts = processedPosts.stream()
                .filter(post -> post.getFeatured() == null || !post.getFeatured())
                .collect(Collectors.toList());

        // 构建返回结果
        PostListVO result = new PostListVO();
        result.setList(normalPosts);
        result.setFeaturedPosts(featuredPosts);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        result.setTotalPages((int) Math.ceil((double) total / pageSize));

        return result;
    }

    @Override
    public HotPostListVO getHotPosts(Integer limit) {
        if (limit == null || limit < 1) {
            limit = 3;
        }

        List<HotPostVO> hotPosts = practiceMapper.selectHotPosts(limit);
        
        // 设置排名
        for (int i = 0; i < hotPosts.size(); i++) {
            hotPosts.get(i).setRank(i + 1);
        }

        HotPostListVO result = new HotPostListVO();
        result.setList(hotPosts);
        return result;
    }

    @Override
    public TagListVO getTags(String department) {
        List<TagVO> tags = practiceMapper.selectTagsWithCount(department);
        
        // 如果没有"全部"标签，添加一个
        boolean hasAllTag = tags.stream().anyMatch(tag -> "全部".equals(tag.getName()));
        if (!hasAllTag && !CollectionUtils.isEmpty(tags)) {
            TagVO allTag = new TagVO();
            allTag.setName("全部");
            // 计算总数
            int totalCount = tags.stream().mapToInt(TagVO::getCount).sum();
            allTag.setCount(totalCount);
            tags.add(0, allTag);
        }

        TagListVO result = new TagListVO();
        result.setList(tags);
        return result;
    }

    @Override
    public DepartmentListVO getDepartments(String tag) {
        List<DepartmentVO> departments = practiceMapper.selectDepartmentRankings(tag);
        
        // 设置ID（如果数据库没有自动生成）
        for (int i = 0; i < departments.size(); i++) {
            if (departments.get(i).getId() == null) {
                departments.get(i).setId(i + 1);
            }
        }

        DepartmentListVO result = new DepartmentListVO();
        result.setList(departments);
        return result;
    }

    @Override
    public ContributorListVO getContributors(Integer limit) {
        if (limit == null || limit < 1) {
            limit = 5;
        }

        List<ContributorVO> contributors = practiceMapper.selectTopContributors(limit);
        
        // 处理用户ID转换（如果数据库返回的是字符串）
        for (ContributorVO contributor : contributors) {
            if (contributor.getId() == null && contributor.getName() != null) {
                // 如果ID为空，使用名称的hashCode作为临时ID
                contributor.setId(contributor.getName().hashCode());
            }
        }

        ContributorListVO result = new ContributorListVO();
        result.setList(contributors);
        return result;
    }

    /**
     * 处理帖子数据：添加标签、格式化时间等
     *
     * @param posts 原始帖子列表
     * @return 处理后的帖子列表
     */
    private List<PostItemVO> processPosts(List<PostItemVO> posts) {
        if (CollectionUtils.isEmpty(posts)) {
            return new ArrayList<>();
        }

        for (PostItemVO post : posts) {
            // 查询并设置标签列表
            List<PostTag> tags = practiceMapper.selectTagsByPostId(String.valueOf(post.getId()));
            if (!CollectionUtils.isEmpty(tags)) {
                List<String> tagNames = tags.stream()
                        .map(PostTag::getTag)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                post.setTags(tagNames);
            } else {
                post.setTags(new ArrayList<>());
            }

            // 格式化时间显示（如果createTimeDisplay为空，则根据createTime计算）
            if (post.getCreateTimeDisplay() == null || post.getCreateTimeDisplay().isEmpty()) {
                post.setCreateTimeDisplay(calculateRelativeTime(post.getCreateTime()));
            }

            // 设置默认值
            if (post.getComments() == null) {
                post.setComments(0);
            }
            if (post.getLikes() == null) {
                post.setLikes(0);
            }
            if (post.getViews() == null) {
                post.setViews(0);
            }
        }

        return posts;
    }

    /**
     * 计算相对时间显示
     *
     * @param createTime ISO格式时间字符串
     * @return 相对时间字符串
     */
    private String calculateRelativeTime(String createTime) {
        if (createTime == null || createTime.isEmpty()) {
            return "未知";
        }

        try {
            Date date = ISO_DATE_FORMAT.parse(createTime);
            long now = System.currentTimeMillis();
            long time = date.getTime();
            long diff = now - time;

            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                if (days < 30) {
                    return days + "天前";
                } else {
                    return DISPLAY_DATE_FORMAT.format(date);
                }
            } else if (hours > 0) {
                return hours + "小时前";
            } else if (minutes > 0) {
                return minutes + "分钟前";
            } else {
                return "刚刚";
            }
        } catch (Exception e) {
            log.warn("时间格式化失败: {}", createTime, e);
            return createTime;
        }
    }
}
