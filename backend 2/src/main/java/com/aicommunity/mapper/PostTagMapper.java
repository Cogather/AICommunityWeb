package com.aicommunity.mapper;

import com.aicommunity.entity.PostTag;
import com.aicommunity.entity.PostTagRelation;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子标签Mapper接口
 *
 * @author AI Community Team
 * @date 2026-01-13
 */
@Mapper
public interface PostTagMapper {

    /**
     * 根据label_id查询标签信息（保留用于兼容）
     *
     * @param labelId 标签ID
     * @return 标签信息
     */
    PostTag selectByLabelId(@Param("labelId") Integer labelId);
    
    /**
     * 查询所有标签信息
     *
     * @return 标签信息列表
     */
    List<PostTag> selectAllLabel();

    /**
     * 查询所有帖子标签关联关系
     *
     * @return 帖子标签关联关系列表
     */
    List<PostTagRelation> selectPostTagRelation();

    /**
     * 根据帖子ID查询标签列表（通过关联表）
     *
     * @param postId 帖子ID
     * @return 标签列表
     */
    List<PostTag> selectTagsByPostId(@Param("postId") String postId);

    /**
     * 插入帖子标签关联关系
     *
     * @param postId 帖子ID
     * @param tagId  标签ID
     * @return 影响行数
     */
    int insertPostTagRelation(@Param("postId") String postId, @Param("tagId") Integer tagId);

    /**
     * 删除帖子标签关联关系（逻辑删除）
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int deletePostTagRelationByPostId(@Param("postId") String postId);

    /**
     * 根据标签名称查询标签ID
     *
     * @param tagName 标签名称
     * @return 标签ID，如果不存在返回null
     */
    Integer selectTagIdByName(@Param("tagName") String tagName);
}
