package com.controller;

import com.pojo.Action;
import com.pojo.User;
import com.pojo.wrapper.RtnData;
import com.service.ActionService;
import com.service.CommentService;
import com.service.PostService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/actions")
public class ActionController {

    private final ActionService actionService;
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @Autowired
    public ActionController(ActionService actionService, PostService postService, UserService userService, CommentService commentService) {
        this.actionService = actionService;
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    /**
     * 点赞帖子
     *
     * @param action  用户操作信息
     * @param session 从session中获取已登录用户进行操作
     * @return 返回是否操作成功
     */
    @PostMapping("/likePost")
    public RtnData likePost(@RequestBody Action action, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            action.setUserId(loginUser.getUserId());
            if (actionService.addAction(action)) {
                if (postService.likePost(action.getPostId()) && userService.likeUserPost(action.getPostId())) {
                    rtnData.setFlag(true);
                    rtnData.setData(true);
                    return rtnData;
                }
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("点赞失败");
        return rtnData;
    }

    /**
     * 取消点赞帖子
     *
     * @param action  用户操作信息
     * @param session 从session中获取用户登录信息用来操作
     * @return 返回是否成功
     */
    @PostMapping("/cancelLikePost")
    public RtnData cancelLikePost(@RequestBody Action action, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            action.setUserId(loginUser.getUserId());
            if (actionService.delAction(action)) {
                if (postService.cancelLikePost(action.getPostId()) && userService.cancelLikeUserPost(action.getPostId())) {
                    rtnData.setFlag(true);
                    rtnData.setData(true);
                    return rtnData;
                }
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("取消点赞失败");
        return rtnData;
    }

    /**
     * 收藏帖子
     *
     * @param action  用户操作信息
     * @param session 从session中获取已登录用户id进行操作
     * @return 返回是否收藏成功
     */
    @PostMapping("/collectionPost")
    public RtnData collectionPost(@RequestBody Action action, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            action.setUserId(loginUser.getUserId());
            if (actionService.addAction(action)) {
                if (postService.collectionPost(action.getPostId())) {
                    rtnData.setFlag(true);
                    rtnData.setData(true);
                    return rtnData;
                }
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("收藏失败");
        return rtnData;
    }

    /**
     * 取消收藏帖子
     *
     * @param action  用户操作信息
     * @param session 从session中获取已登录用户id进行操作
     * @return 返回是否取消收藏成功
     */
    @PostMapping("/cancelCollectionPost")
    public RtnData cancelCollectionPost(@RequestBody Action action, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            action.setUserId(loginUser.getUserId());
            if (actionService.addAction(action)) {
                if (postService.cancelCollectionPost(action.getPostId())) {
                    rtnData.setFlag(true);
                    rtnData.setData(true);
                    return rtnData;
                }
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("收藏失败");
        return rtnData;
    }

    /**
     * 点赞评论
     *
     * @param action  用户操作信息
     * @param session 从session中获取已登录用户进行操作
     * @return 返回是否操作成功
     */
    @PostMapping("/likeComment")
    public RtnData likeComment(@RequestBody Action action, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            action.setUserId(loginUser.getUserId());
            if (actionService.addAction(action)) {
                if (commentService.likeComment(action.getCommentId())
                        && userService.likeUserComment(action.getCommentId())) {
                    rtnData.setFlag(true);
                    rtnData.setData(true);
                    return rtnData;
                }
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("点赞失败");
        return rtnData;
    }

    /**
     * 取消点赞评论
     *
     * @param action  用户操作信息
     * @param session 从session中获取用户登录信息用来操作
     * @return 返回是否成功
     */
    @PostMapping("/cancelLikeComment")
    public RtnData cancelLikeComment(@RequestBody Action action, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            action.setUserId(loginUser.getUserId());
            if (actionService.delAction(action)) {
                if (commentService.cancelLikeComment(action.getCommentId())
                        && userService.cancelLikeUserComment(action.getCommentId())) {
                    rtnData.setFlag(true);
                    rtnData.setData(true);
                    return rtnData;
                }
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("取消点赞失败");
        return rtnData;
    }

}
