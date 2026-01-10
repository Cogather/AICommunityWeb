package com.aicommunity.controller;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.Result;
import com.aicommunity.dto.PostDetailDTO;
import com.aicommunity.dto.PostListDTO;
import com.aicommunity.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 帖子控制器单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private PostController.CreatePostRequest createRequest;
    private PostController.CreatePostResponse createResponse;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // 准备测试数据
        createRequest = new PostController.CreatePostRequest();
        createRequest.setTitle("测试帖子");
        createRequest.setSummary("测试摘要");
        createRequest.setContent("测试内容");
        createRequest.setZone("practices");

        createResponse = new PostController.CreatePostResponse(1L, "创建成功");
    }

    /**
     * 测试获取帖子列表
     */
    @Test
    public void testGetPosts() {
        // Given
        List<PostListDTO> posts = new ArrayList<>();
        PageResult<PostListDTO> pageResult = PageResult.of(posts, 0L, 1, 10);
        when(postService.getPosts(eq("practices"), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), any(PageQuery.class)))
                .thenReturn(pageResult);

        // When
        Result<PageResult<PostListDTO>> result = postController.getPosts("practices", null, null, null, null, null, null, 1, 10, null);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        verify(postService, times(1)).getPosts(anyString(), any(), any(), any(), any(), any(), any(), any(), any(PageQuery.class));
    }

    /**
     * 测试获取帖子详情
     */
    @Test
    public void testGetPostDetail() {
        // Given
        PostDetailDTO postDetail = new PostDetailDTO();
        postDetail.setId(1L);
        postDetail.setTitle("测试帖子");
        when(postService.getPostDetail(1L)).thenReturn(postDetail);

        // When
        Result<PostDetailDTO> result = postController.getPostDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId().longValue());
        verify(postService, times(1)).getPostDetail(1L);
    }

    /**
     * 测试创建帖子
     */
    @Test
    public void testCreatePost() {
        // Given
        when(postService.createPost(any(PostController.CreatePostRequest.class))).thenReturn(createResponse);

        // When
        Result<PostController.CreatePostResponse> result = postController.createPost(createRequest);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        assertNotNull(result.getData());
        assertEquals(1L, result.getData().getId().longValue());
        verify(postService, times(1)).createPost(any(PostController.CreatePostRequest.class));
    }

    /**
     * 测试更新帖子
     */
    @Test
    public void testUpdatePost() {
        // Given
        PostController.UpdatePostResponse updateResponse = new PostController.UpdatePostResponse(1L, "更新成功");
        when(postService.updatePost(eq(1L), any(PostController.CreatePostRequest.class))).thenReturn(updateResponse);

        // When
        Result<PostController.UpdatePostResponse> result = postController.updatePost(1L, createRequest);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        verify(postService, times(1)).updatePost(eq(1L), any(PostController.CreatePostRequest.class));
    }

    /**
     * 测试删除帖子
     */
    @Test
    public void testDeletePost() {
        // Given
        doNothing().when(postService).deletePost(1L);

        // When
        Result<?> result = postController.deletePost(1L);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        verify(postService, times(1)).deletePost(1L);
    }

    /**
     * 测试点赞帖子
     */
    @Test
    public void testLikePost() {
        // Given
        PostController.LikePostRequest likeRequest = new PostController.LikePostRequest();
        likeRequest.setAction("like");
        PostController.LikePostResponse likeResponse = new PostController.LikePostResponse(true, 11);
        when(postService.likePost(1L, likeRequest)).thenReturn(likeResponse);

        // When
        Result<PostController.LikePostResponse> result = postController.likePost(1L, likeRequest);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode().intValue());
        assertTrue(result.getData().getLiked());
        verify(postService, times(1)).likePost(1L, likeRequest);
    }
}
