package com.aicommunity.service.impl;

import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.common.ErrorCodeEnum;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.PostTag;
import com.aicommunity.mapper.EmpowermentMapper;
import com.aicommunity.mapper.PracticeMapper;
import com.aicommunity.service.EmpowermentService;
import com.aicommunity.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 赋能交流服务实现类
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Slf4j
@Service
public class EmpowermentServiceImpl implements EmpowermentService {

    @Autowired
    private EmpowermentMapper empowermentMapper;

    @Autowired
    private PracticeMapper practiceMapper;

    private static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    static {
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    /**
     * 格式化日期为ISO 8601格式
     *
     * @param date 日期
     * @return ISO 8601格式字符串
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return ISO_DATE_FORMAT.format(date);
    }

    @Override
    public FeaturedPostListVO getFeaturedPosts() {
        log.info("获取赋能交流精华帖子列表");
        List<FeaturedPostVO> featuredPosts = empowermentMapper.selectFeaturedPosts();

        // 为每个精华帖子添加标签信息
        for (FeaturedPostVO post : featuredPosts) {
            List<String> tags = getPostTags(String.valueOf(post.getId()));
            post.setTags(tags);
        }

        FeaturedPostListVO vo = new FeaturedPostListVO();
        vo.setList(featuredPosts);
        return vo;
    }

    @Override
    public EmpowermentPostListVO getPosts(String keyword, String tag, String sortBy, Integer page, Integer pageSize) {
        log.info("获取赋能交流帖子列表, keyword={}, tag={}, sortBy={}, page={}, pageSize={}", 
                keyword, tag, sortBy, page, pageSize);

        // 参数校验和默认值设置
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 15;
        }
        if (sortBy == null) {
            sortBy = "newest";
        }

        // 计算偏移量
        Integer offset = (page - 1) * pageSize;

        // 查询帖子列表
        List<EmpowermentPostItemVO> posts = empowermentMapper.selectEmpowermentPosts(
                keyword, tag, sortBy, offset, pageSize);

        // 为每个帖子添加标签信息
        for (EmpowermentPostItemVO post : posts) {
            List<String> tags = getPostTags(String.valueOf(post.getId()));
            post.setTags(tags);
        }

        // 统计总数
        Long total = empowermentMapper.countEmpowermentPosts(keyword, tag);

        // 构建返回对象
        EmpowermentPostListVO vo = new EmpowermentPostListVO();
        vo.setList(posts);
        vo.setTotal(total != null ? total : 0L);
        vo.setPage(page);
        vo.setPageSize(pageSize);

        return vo;
    }

    @Override
    public TagStatListVO getTags() {
        log.info("获取赋能交流标签统计");
        List<TagStatVO> tagStats = empowermentMapper.selectTagStats();

        TagStatListVO vo = new TagStatListVO();
        vo.setList(tagStats);
        return vo;
    }

    @Override
    public CollectionListVO getCollections(Integer limit) {
        log.info("获取精选合集, limit={}", limit);

        // 参数校验和默认值设置
        if (limit == null || limit < 1) {
            limit = 5;
        }

        // TODO: 精选合集功能需要根据实际业务需求实现
        // 目前返回空列表，后续可以根据需要添加精选合集的表结构或配置
        // 如果精选合集是基于帖子数据的，可以从数据库中查询
        // 如果精选合集是配置数据，可以从配置表或配置文件中读取

        List<CollectionVO> collections = new ArrayList<>();
        // 示例：可以从数据库中查询特定条件的帖子作为精选合集
        // 这里暂时返回空列表，实际使用时需要根据业务需求实现

        CollectionListVO vo = new CollectionListVO();
        vo.setList(collections);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FeaturedPostSetResponseVO setFeaturedPost(Integer postId, Boolean featured) {
        log.info("设置精华帖子, postId={}, featured={}", postId, featured);

        // 参数校验
        if (postId == null) {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), "帖子ID不能为空");
        }
        if (featured == null) {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), "featured字段不能为空");
        }

        // 将Integer类型的postId转换为String类型（数据库中的post_id是varchar类型）
        String postIdStr = String.valueOf(postId);

        // 查询帖子是否存在
        Post post = empowermentMapper.selectByPostId(postIdStr);
        if (post == null) {
            throw new BusinessException(ErrorCodeEnum.NOT_FOUND.getCode(), "帖子不存在");
        }

        // 校验是否为赋能交流帖子
        if (post.getZoneId() == null || post.getZoneId() != 4) {
            throw new BusinessException(ErrorCodeEnum.BAD_REQUEST.getCode(), "该帖子不属于赋能交流专区");
        }

        // 设置或取消精华状态
        int result;
        if (featured) {
            // 设置为精华帖前，先取消其他精华帖（如果需要只保留一个精华帖）
            // 如果允许多个精华帖，则不需要取消其他精华帖
            result = empowermentMapper.setFeaturedPost(postIdStr);
        } else {
            result = empowermentMapper.cancelFeaturedPost(postIdStr);
        }

        if (result <= 0) {
            throw new BusinessException(ErrorCodeEnum.DATABASE_ERROR.getCode(), "设置精华帖子失败");
        }

        // 构建返回对象
        FeaturedPostSetResponseVO vo = new FeaturedPostSetResponseVO();
        vo.setPostId(postId);
        vo.setFeatured(featured);
        vo.setSetTime(formatDate(new Date()));

        return vo;
    }

    /**
     * 获取帖子的标签列表
     *
     * @param postId 帖子ID
     * @return 标签名称列表
     */
    private List<String> getPostTags(String postId) {
        List<PostTag> tags = practiceMapper.selectTagsByPostId(postId);
        if (CollectionUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }
        return tags.stream()
                .map(PostTag::getTag)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
