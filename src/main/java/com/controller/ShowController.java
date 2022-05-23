package com.controller;

import com.pojo.*;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.PostBrief;
import com.pojo.wrapper.PostPage;
import com.pojo.wrapper.RtnData;
import com.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shows")
public class ShowController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final PostBriefService postBriefService;
    private final ActionService actionService;
    private final FocusService focusService;

    @Autowired
    public ShowController(PostService postService, UserService userService, CommentService commentService, PostBriefService postBriefService, ActionService actionService, FocusService focusService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.postBriefService = postBriefService;
        this.actionService = actionService;
        this.focusService = focusService;
    }

    /**
     * 获取首页5张轮播图信息
     *
     * @return 返回轮播图信息
     */
    @GetMapping("/getNewOfficial")
    public RtnData getNewOfficial() {
        RtnData rtnData = new RtnData();
        List<PostImg> officialPosts = postService.getOfficialPost();
        if (officialPosts.size() == 5) {
            rtnData.setFlag(true);
            rtnData.setData(officialPosts);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("获取轮播图数据错误");
        return rtnData;
    }

    /**
     * 获取帖子列表
     *
     * @param postPage pageNum:页码,postSum:每页信息条数,partitionId:分区ID
     * @return 返回帖子列表
     */
    @PostMapping("/getPostList")
    public RtnData getPostList(@RequestBody PostPage postPage, HttpServletRequest request) {
        RtnData rtnData = new RtnData();
        List<PostBrief> postBriefs = new ArrayList<>();
        if (postPage.getSearchParam() != null && !"".equals(postPage.getSearchParam())) {
            postBriefs = postBriefService.getSearchPostBriefByPage(postPage);
        } else {
            postBriefs = postBriefService.getPostBriefByPage(postPage);
        }
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null && postBriefs.size() > 0) {
            //用户已经登陆，获取用户对帖子的操作
            actionService.setUserActionsByPost(postBriefs, loginUser.getUserId());
        }
        rtnData.setFlag(true);
        rtnData.setData(postBriefs);
        return rtnData;
    }

    /**
     * 获取帖子数据
     *
     * @param postPage postId:帖子Id
     * @return 返回帖子数据
     */
    @PostMapping("/getPostData")
    public RtnData getPostData(@RequestBody PostPage postPage, HttpSession session) {
        RtnData rtnData = new RtnData();
        Post post = postService.getPostById(postPage.getPostId());
        User user = userService.getUserById(post.getUserId());
        List<CommentBrief> commentBriefs = commentService.getCommentBriefsByPage(postPage);
        Map<String, Object> map = new HashMap<>();
        map.put("post", post);
        map.put("user", user);
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            Map<String, Boolean> flagMap = new HashMap<>();
            flagMap.put("like", false);
            flagMap.put("collection", false);
            flagMap.put("likeUser", focusService.isFocusUser(loginUser.getUserId(), user.getUserId()));
            List<Action> actions = actionService.getActions(post.getPostId(), loginUser.getUserId());
            for (Action action : actions) {
                if (action.getActionCategoryId().equals(1)) {
                    flagMap.put("like", true);
                }
                if (action.getActionCategoryId().equals(2)) {
                    flagMap.put("collection", true);
                }
            }
            map.put("flag", flagMap);
            actionService.setLikeFlag(commentBriefs, loginUser.getUserId());
        }
        map.put("comments", commentBriefs);
        rtnData.setFlag(true);
        rtnData.setData(map);
        return rtnData;
    }

    /**
     * 获取帖子简要数据，编辑使用
     *
     * @param postId 帖子id
     * @return 返回帖子数据
     */
    @GetMapping("/getPost/{id}")
    public RtnData getPost(@PathVariable("id") Integer postId) {
        RtnData rtnData = new RtnData();
        Post post = postService.getPostById(postId);
        if (post != null && post.getUserId() != null) {
            //查询到帖子数据
            rtnData.setFlag(true);
            rtnData.setData(post);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("未查询到该帖子数据");
        return rtnData;
    }

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 返回用户信息
     */
    @GetMapping("/getUserById/{id}")
    public RtnData getUserById(@PathVariable("id") Integer id, HttpSession session) {
        RtnData rtnData = new RtnData();
        User user = userService.getUserById(id);
        if (user != null && user.getUserId() != null) {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser != null && loginUser.getUserId() != null) {
                user.setLikeFlag(focusService.isFocusUser(loginUser.getUserId(), user.getUserId()));
            }
            rtnData.setFlag(true);
            rtnData.setData(user);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("没有该用户信息");
        return rtnData;
    }

    /**
     * 获取帖子
     *
     * @param commentId 帖子id
     * @return 返回帖子
     */
    @GetMapping("/getCommentById/{id}")
    public RtnData getComment(@PathVariable("id") Integer commentId) {
        RtnData rtnData = new RtnData();
        Comment comment = commentService.getCommentById(commentId);
        if (comment != null && comment.getCommentId() != null) {
            rtnData.setFlag(true);
            rtnData.setData(comment);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("获取评论信息失败");
        return rtnData;
    }

    /**
     * 获取评论列表
     *
     * @param page 页面参数
     * @return 返回评论集合
     */
    @PostMapping("/getCommentsByPage")
    public RtnData getCommentsByPage(@RequestBody PostPage page) {
        RtnData rtnData = new RtnData();
        List<Comment> comments = commentService.getCommentsByPage(page);
        if (comments != null && comments.size() > 0) {
            rtnData.setFlag(true);
            rtnData.setData(comments);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("获取评论列表失败");
        return rtnData;
    }
}
