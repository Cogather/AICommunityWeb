package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.PostController;
import com.aicommunity.dto.PostDetailDTO;
import com.aicommunity.dto.PostListDTO;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.*;
import com.aicommunity.service.PostService;
import com.aicommunity.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 帖子服务单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PageHelper.class})
public class PostServiceImplTest {

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PostTagMapper postTagMapper;

    @Mock
    private LikeRecordMapper likeRecordMapper;

    @Mock
    private FavoriteMapper favoriteMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private PointsRecordMapper pointsRecordMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private PostServiceImpl postService;

    private Post testPost;
    private User testUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        PowerMockito.mockStatic(PageHelper.class);

        // 准备测试数据
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("测试用户");
        testUser.setAvatar("http://example.com/avatar.jpg");
        testUser.setDepartment("技术部");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("测试帖子");
        testPost.setSummary("这是测试帖子的摘要");
        testPost.setContent("这是测试帖子的内容");
        testPost.setAuthorId(1L);
        testPost.setZone("practices");
        testPost.setCover("http://example.com/cover.jpg");
        testPost.setViews(100);
        testPost.setLikes(10);
        testPost.setComments(5);
        testPost.setCreateTime(LocalDateTime.now());
    }

    /**
     * 测试获取帖子列表
     */
    @Test
    public void testGetPosts() {
        // Given
        PowerMockito.mockStatic(PageHelper.class);
        PageQuery pageQuery = new PageQuery(1, 10);

        List<Map<String, Object>> posts = new ArrayList<>();
        Map<String, Object> post = new HashMap<>();
        post.put("id", 1L);
        post.put("title", "测试帖子");
        post.put("authorName", "测试用户");
        posts.add(post);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(posts);
        pageInfo.setTotal(1);

        when(postMapper.selectByCondition(eq("practices"), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(posts);

        // When
        PageResult<PostListDTO> result = postService.getPosts("practices", null, null, null, null, null, null, null, pageQuery);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal().intValue());
    }

    /**
     * 测试获取帖子详情
     */
    @Test
    public void testGetPostDetail() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(postTagMapper.selectTagsByPostId(1L)).thenReturn(Arrays.asList("标签1", "标签2"));
        when(likeRecordMapper.selectByUserAndTarget(2L, "post", 1L)).thenReturn(null);
        when(favoriteMapper.selectByUserAndTarget(2L, "post", 1L)).thenReturn(null);
        when(userRoleMapper.selectRolesByUserId(2L)).thenReturn(Arrays.asList("user"));

        // When
        PostDetailDTO result = postService.getPostDetail(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals("测试帖子", result.getTitle());
        assertEquals("测试用户", result.getAuthorName());
        assertFalse(result.getIsLiked());
        assertFalse(result.getIsCollected());
    }

    /**
     * 测试获取帖子详情 - 帖子不存在
     */
    @Test(expected = BusinessException.class)
    public void testGetPostDetailNotFound() {
        // Given
        when(postMapper.selectById(999L)).thenReturn(null);

        // When
        postService.getPostDetail(999L);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试创建帖子
     */
    @Test
    public void testCreatePost() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(postMapper.insert(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            post.setId(1L);
            return 1;
        });
        when(postTagMapper.insertBatch(anyList())).thenReturn(1);
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("user"));

        PostController.CreatePostRequest request = new PostController.CreatePostRequest();
        request.setTitle("新帖子");
        request.setSummary("新帖子摘要");
        request.setContent("新帖子内容");
        request.setTags(Arrays.asList("标签1", "标签2"));
        request.setCover("http://example.com/cover.jpg");
        request.setZone("practices");

        // When
        PostController.CreatePostResponse response = postService.createPost(request);

        // Then
        assertNotNull(response);
        assertNotNull(response.getId());
        verify(postMapper, times(1)).insert(any(Post.class));
        verify(postTagMapper, times(1)).insertBatch(anyList());
    }

    /**
     * 测试点赞帖子
     */
    @Test
    public void testLikePost() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(likeRecordMapper.selectByUserAndTarget(2L, "post", 1L)).thenReturn(null);
        when(likeRecordMapper.insert(any())).thenReturn(1);
        when(postMapper.updateLikes(1L, 1)).thenReturn(1);
        when(userRoleMapper.selectRolesByUserId(2L)).thenReturn(Arrays.asList("user"));

        PostController.LikePostRequest likeRequest = new PostController.LikePostRequest();
        likeRequest.setAction("like");

        // When
        PostController.LikePostResponse response = postService.likePost(1L, likeRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getLiked());
        verify(likeRecordMapper, times(1)).insert(any());
        verify(postMapper, times(1)).updateLikes(1L, 1);
    }

    /**
     * 测试取消点赞帖子
     */
    @Test
    public void testUnlikePost() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(likeRecordMapper.selectByUserAndTarget(2L, "post", 1L)).thenReturn(new com.aicommunity.entity.LikeRecord());
        when(likeRecordMapper.delete(2L, "post", 1L)).thenReturn(1);
        when(postMapper.updateLikes(1L, -1)).thenReturn(1);

        PostController.LikePostRequest likeRequest = new PostController.LikePostRequest();
        likeRequest.setAction("unlike");

        // When
        PostController.LikePostResponse response = postService.likePost(1L, likeRequest);

        // Then
        assertNotNull(response);
        assertFalse(response.getLiked());
        verify(likeRecordMapper, times(1)).delete(2L, "post", 1L);
        verify(postMapper, times(1)).updateLikes(1L, -1);
    }

    /**
     * 测试删除帖子
     */
    @Test
    public void testDeletePost() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("user"));
        when(postMapper.deleteById(1L)).thenReturn(1);
        when(postTagMapper.deleteByPostId(1L)).thenReturn(1);

        // When
        postService.deletePost(1L);

        // Then
        verify(postMapper, times(1)).deleteById(1L);
        verify(postTagMapper, times(1)).deleteByPostId(1L);
    }

    /**
     * 测试删除帖子 - 无权限
     */
    @Test(expected = BusinessException.class)
    public void testDeletePostNoPermission() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(userRoleMapper.selectRolesByUserId(2L)).thenReturn(Arrays.asList("user"));

        // When
        postService.deletePost(1L);

        // Then - 应该抛出BusinessException
    }
}
