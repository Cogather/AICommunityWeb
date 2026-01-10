package com.aicommunity.mapper;

import com.aicommunity.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签Mapper接口
 *
 * @author AI Community Team
 */
@Mapper
public interface TagMapper {
    /**
     * 根据名称和专区查询标签
     */
    Tag selectByNameAndZone(@Param("name") String name, @Param("zone") String zone);

    /**
     * 根据帖子ID查询标签名称列表
     */
    List<String> selectNamesByPostId(@Param("postId") Long postId);

    /**
     * 根据专区和工具ID查询标签列表
     */
    List<Tag> selectByZoneAndToolId(@Param("zone") String zone, @Param("toolId") Long toolId);

    /**
     * 插入标签
     */
    void insert(Tag tag);
}
