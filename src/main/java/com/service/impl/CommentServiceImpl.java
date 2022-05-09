package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapper.CommentBriefMapper;
import com.mapper.CommentMapper;
import com.mapper.PostMapper;
import com.mapper.UserMapper;
import com.pojo.Comment;
import com.pojo.Post;
import com.pojo.User;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.PostPage;
import com.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;
    private final CommentBriefMapper commentBriefMapper;
    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper, CommentBriefMapper commentBriefMapper, PostMapper postMapper, UserMapper userMapper) {
        this.commentMapper = commentMapper;
        this.commentBriefMapper = commentBriefMapper;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    @Override
    public List<CommentBrief> getCommentBriefsByPage(PostPage postPage) {
        Integer size = postPage.getCommentSum();
        Integer start = (postPage.getPageNum() - 1) * size;
        List<CommentBrief> commentBriefs = commentBriefMapper.selectCommentBriefByPage(postPage.getPostId(), start, size);
        for (CommentBrief commentBrief : commentBriefs) {
            commentBrief.setCommentChildrenNum(commentBrief.getCommentBriefs().size());
            if (commentBrief.getCommentBriefs() != null && commentBrief.getCommentBriefs().size() > 2) {
                commentBrief.setCommentBriefs(commentBrief.getCommentBriefs().subList(0, 2));
            }
            for (CommentBrief commentBrief1 : commentBrief.getCommentBriefs()) {
                if (commentBrief1.getCommentParentId() != null && !commentBrief1.getCommentParentId().equals(commentBrief1.getCommentRootParentId())) {
                    Comment comment = commentMapper.selectById(commentBrief1.getCommentParentId());
                    User user = userMapper.selectById(comment.getUserId());
                    commentBrief1.setCommentParentUserId(user.getUserId());
                    commentBrief1.setCommentParentName(user.getUserName());
                }
            }
        }
        return commentBriefs;
    }

    @Override
    public Boolean commentPost(Comment comment) {
        comment.setCommentCreateTime(LocalDateTime.now());
        comment.setCommentLikesNum(0);
        Boolean commentFlag = commentMapper.insert(comment) == 1;
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getPostId, comment.getPostId());
        updateWrapper.setSql("post_comments_num = post_comments_num + 1");
        Boolean postFlag = postMapper.update(null, updateWrapper) == 1;
        if (commentFlag && postFlag) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean likeComment(Integer commentId) {
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getCommentId, commentId);
        updateWrapper.setSql("comment_likes_num = comment_likes_num + 1");
        return commentMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean cancelLikeComment(Integer commentId) {
        LambdaUpdateWrapper<Comment> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Comment::getCommentId, commentId);
        updateWrapper.setSql("comment_likes_num = comment_likes_num - 1");
        return commentMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean commentComment(Comment comment) {
        comment.setCommentCreateTime(LocalDateTime.now());
        comment.setCommentLikesNum(0);
        int insert = commentMapper.insert(comment);
        if (insert == 1) {
            LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Post::getPostId, comment.getPostId());
            updateWrapper.setSql("post_comments_num = post_comments_num + 1");
            return postMapper.update(null, updateWrapper) == 1;
        }
        return false;
    }

    @Override
    public Comment getCommentById(Integer commentId) {
        Comment comment = commentMapper.selectById(commentId);
        User user = userMapper.selectById(comment.getUserId());
        comment.setUserName(user.getUserName());
        comment.setUserImgUrl(user.getUserImgUrl());
        return comment;
    }

    @Override
    public List<Comment> getCommentsByPage(PostPage page) {
        IPage<Comment> iPage = new Page<>(page.getPageNum(), page.getCommentSum());
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getCommentRootParentId, page.getCommentId());
        commentMapper.selectPage(iPage, queryWrapper);
        List<Comment> comments = iPage.getRecords();
        for (Comment comment : comments) {
            User user = userMapper.selectById(comment.getUserId());
            comment.setUserName(user.getUserName());
            comment.setUserImgUrl(user.getUserImgUrl());
            if (comment.getCommentParentId() != null && !comment.getCommentParentId().equals(comment.getCommentRootParentId())) {
                User parentUser = userMapper.selectById(commentMapper.selectById(comment.getCommentParentId()).getUserId());
                comment.setCommentParentUserId(parentUser.getUserId());
                comment.setCommentParentName(parentUser.getUserName());
            }
        }
        return comments;
    }
}
