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
     * 根据label_id查询标签信息
     *
     * @param labelId 标签ID
     * @return 标签信息
     */
    PostTag selectByLabelId(@Param("labelId") Integer labelId);
    
    /**
    * 根据label_id查询标签信息
    *
    * @return 标签信息
    */
   List<PostTag> selectAllLabel();

    /**
     * 根据帖子ID查询标签关联关系
     *
     * @param postId 帖子ID
     * @return 标签关联列表
     */
    List<PostTagRelation> selectByPostId(@Param("postId") String postId);

    /**
     * 根据标签ID查询标签关联关系
     *
     * @param tagId 标签ID
     * @return 标签关联列表
     */
    List<PostTagRelation> selectByTagId(@Param("tagId") Integer tagId);

    /**
     * 批量插入帖子标签关联关系
     *
     * @param relations 标签关联列表
     * @return 影响行数
     */
    int batchInsertRelations(@Param("relations") List<PostTagRelation> relations);

    /**
     * 根据帖子ID删除所有标签关联关系
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int deleteByPostId(@Param("postId") String postId);

    /**
     * 根据标签ID删除标签关联关系
     *
     * @param tagId 标签ID
     * @return 影响行数
     */
    int deleteByTagId(@Param("tagId") Integer tagId);

    /**
     * 根据帖子ID和标签ID删除关联关系
     *
     * @param postId 帖子ID
     * @param tagId 标签ID
     * @return 影响行数
     */
    int deleteRelation(@Param("postId") String postId, @Param("tagId") Integer tagId);

    /**
     * 逻辑删除帖子标签关联（设置deleted=1）
     *
     * @param postId 帖子ID
     * @return 影响行数
     */
    int softDeleteByPostId(@Param("postId") String postId);

    /**
     * 查询所有未删除的标签关联关系
     *
     * @return 标签关联列表
     */
    List<PostTagRelation> selectAllActiveRelations();
}
