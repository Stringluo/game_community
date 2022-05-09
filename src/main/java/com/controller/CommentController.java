package com.controller;

import com.pojo.Comment;
import com.pojo.User;
import com.pojo.wrapper.RtnData;
import com.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 发布评论
     *
     * @param comment 评论信息
     * @param request 从request获取session并获取登录用户id以发布评论
     * @return 返回boolean:是否发布成功
     */
    @PostMapping("/commentPost")
    public RtnData commentPost(@RequestBody Comment comment, HttpServletRequest request) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            comment.setUserId(loginUser.getUserId());
            if (commentService.commentPost(comment)) {
                rtnData.setFlag(true);
                rtnData.setData(comment);
                return rtnData;
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("发表评论失败");
        return rtnData;
    }

    @PostMapping("/commentComment")
    public RtnData commentComment(@RequestBody Comment comment, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            comment.setUserId(loginUser.getUserId());
            rtnData.setFlag(true);
            rtnData.setData(commentService.commentComment(comment));
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("评论错误");
        return rtnData;
    }
}
