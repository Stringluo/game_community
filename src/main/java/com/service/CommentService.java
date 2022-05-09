package com.service;

import com.pojo.Comment;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.PostPage;

import java.util.List;

public interface CommentService {

    List<CommentBrief> getCommentBriefsByPage(PostPage postPage);

    Boolean commentPost(Comment comment);

    Boolean likeComment(Integer commentId);

    Boolean cancelLikeComment(Integer commentId);

    Boolean commentComment(Comment comment);

    Comment getCommentById(Integer commentId);

    List<Comment> getCommentsByPage(PostPage page);
}
