package com.aicommunity.service.impl;

import com.aicommunity.common.PageQuery;
import com.aicommunity.common.PageResult;
import com.aicommunity.common.exception.BusinessException;
import com.aicommunity.controller.CommentController;
import com.aicommunity.dto.CommentDTO;
import com.aicommunity.entity.Comment;
import com.aicommunity.entity.LikeRecord;
import com.aicommunity.entity.Post;
import com.aicommunity.entity.User;
import com.aicommunity.mapper.*;
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
 * 评论服务单元测试
 *
 * @author AI Community Team
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({PageHelper.class})
public class CommentServiceImplTest {

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private LikeRecordMapper likeRecordMapper;

    @Mock
    private PointsRecordMapper pointsRecordMapper;

    @Mock
    private UserRoleMapper userRoleMapper;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Comment testComment;
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

        testPost = new Post();
        testPost.setId(1L);
        testPost.setAuthorId(2L);

        testComment = new Comment();
        testComment.setId(1L);
        testComment.setPostId(1L);
        testComment.setUserId(1L);
        testComment.setContent("测试评论");
        testComment.setLikes(5);
        testComment.setCreateTime(LocalDateTime.now());
    }

    /**
     * 测试获取评论列表
     */
    @Test
    public void testGetComments() {
        // Given
        PowerMockito.mockStatic(PageHelper.class);
        PageQuery pageQuery = new PageQuery(1, 10);

        List<Map<String, Object>> comments = new ArrayList<>();
        Map<String, Object> comment = new HashMap<>();
        comment.put("id", 1L);
        comment.put("userId", 1L);
        comment.put("userName", "测试用户");
        comment.put("content", "测试评论");
        comment.put("likes", 5);
        comments.add(comment);

        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(comments);
        pageInfo.setTotal(1);

        when(commentMapper.selectByPostId(1L)).thenReturn(comments);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(likeRecordMapper.selectByUserAndTarget(anyLong(), eq("comment"), anyLong())).thenReturn(null);
        when(userRoleMapper.selectRolesByUserId(anyLong())).thenReturn(Arrays.asList("user"));

        // When
        PageResult<CommentDTO> result = commentService.getComments(1L, pageQuery);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotal().intValue());
    }

    /**
     * 测试发表评论
     */
    @Test
    public void testCreateComment() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(postMapper.selectById(1L)).thenReturn(testPost);
        when(userMapper.selectById(1L)).thenReturn(testUser);
        when(commentMapper.insert(any(Comment.class))).thenAnswer(invocation -> {
            Comment comment = invocation.getArgument(0);
            comment.setId(1L);
            return 1;
        });
        when(postMapper.updateComments(1L, 1)).thenReturn(1);
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("user"));

        CommentController.CreateCommentRequest request = new CommentController.CreateCommentRequest();
        request.setContent("新评论");
        request.setReplyTo(null);

        // When
        CommentDTO result = commentService.createComment(1L, request);

        // Then
        assertNotNull(result);
        assertEquals("新评论", result.getContent());
        verify(commentMapper, times(1)).insert(any(Comment.class));
        verify(postMapper, times(1)).updateComments(1L, 1);
    }

    /**
     * 测试发表评论 - 帖子不存在
     */
    @Test(expected = BusinessException.class)
    public void testCreateCommentPostNotFound() {
        // Given
        when(postMapper.selectById(999L)).thenReturn(null);

        CommentController.CreateCommentRequest request = new CommentController.CreateCommentRequest();
        request.setContent("新评论");

        // When
        commentService.createComment(999L, request);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试点赞评论
     */
    @Test
    public void testLikeComment() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(commentMapper.selectById(1L)).thenReturn(testComment);
        when(likeRecordMapper.selectByUserAndTarget(2L, "comment", 1L)).thenReturn(null);
        when(likeRecordMapper.insert(any(LikeRecord.class))).thenReturn(1);
        when(commentMapper.updateLikes(1L, 1)).thenReturn(1);
        when(userRoleMapper.selectRolesByUserId(2L)).thenReturn(Arrays.asList("user"));

        CommentController.LikeCommentRequest likeRequest = new CommentController.LikeCommentRequest();
        likeRequest.setAction("like");

        // When
        CommentController.LikeCommentResponse response = commentService.likeComment(1L, likeRequest);

        // Then
        assertNotNull(response);
        assertTrue(response.getLiked());
        verify(likeRecordMapper, times(1)).insert(any(LikeRecord.class));
        verify(commentMapper, times(1)).updateLikes(1L, 1);
    }

    /**
     * 测试更新评论
     */
    @Test
    public void testUpdateComment() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(commentMapper.selectById(1L)).thenReturn(testComment);
        when(commentMapper.update(any(Comment.class))).thenReturn(1);

        CommentController.UpdateCommentRequest updateRequest = new CommentController.UpdateCommentRequest();
        updateRequest.setContent("更新后的评论");

        // When
        CommentController.UpdateCommentResponse response = commentService.updateComment(1L, updateRequest);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId().longValue());
        assertEquals("更新后的评论", response.getContent());
        verify(commentMapper, times(1)).update(any(Comment.class));
    }

    /**
     * 测试更新评论 - 无权限
     */
    @Test(expected = BusinessException.class)
    public void testUpdateCommentNoPermission() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(2L);
        when(commentMapper.selectById(1L)).thenReturn(testComment);

        CommentController.UpdateCommentRequest updateRequest = new CommentController.UpdateCommentRequest();
        updateRequest.setContent("更新后的评论");

        // When
        commentService.updateComment(1L, updateRequest);

        // Then - 应该抛出BusinessException
    }

    /**
     * 测试删除评论
     */
    @Test
    public void testDeleteComment() {
        // Given
        String token = "Bearer test-token";
        when(request.getHeader("Authorization")).thenReturn(token);
        when(jwtUtil.getUserIdFromToken("test-token")).thenReturn(1L);
        when(commentMapper.selectById(1L)).thenReturn(testComment);
        when(userRoleMapper.selectRolesByUserId(1L)).thenReturn(Arrays.asList("user"));
        when(commentMapper.deleteById(1L)).thenReturn(1);
        when(postMapper.updateComments(1L, -1)).thenReturn(1);

        // When
        commentService.deleteComment(1L);

        // Then
        verify(commentMapper, times(1)).deleteById(1L);
        verify(postMapper, times(1)).updateComments(1L, -1);
    }
}
