package com.controller;

import com.pojo.Post;
import com.pojo.User;
import com.pojo.wrapper.ImgFlag;
import com.pojo.wrapper.PostImg;
import com.pojo.wrapper.RtnData;
import com.service.PostImgService;
import com.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostImgService postImgService;

    @Autowired
    public PostController(PostService postService, PostImgService postImgService) {
        this.postService = postService;
        this.postImgService = postImgService;
    }

    /**
     * 发布帖子
     *
     * @param imgPost 帖子以及帖子图片信息
     * @param request 从request中取出session的已登录用户信息,用来发布帖子
     * @return 返回帖子是否发布成功
     */
    @PostMapping("/releasePost")
    public RtnData releasePost(@RequestBody PostImg imgPost, HttpServletRequest request) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            Post post = new Post();
            post.setPartitionId(imgPost.getPartitionId());
            post.setPostArticle(imgPost.getPostArticle());
            post.setPostAbbreviation(imgPost.getPostAbbreviation());
            post.setPostTitle(imgPost.getPostTitle());
            post.setUserId(loginUser.getUserId());
            /*if (postService.releasePost(post) && imgPost.getImgUrls().size() > 0) {
                List<com.pojo.PostImg> postImgList = new ArrayList<>();
                for (ImgFlag imgFlag : imgPost.getImgUrls()) {
                    if (imgFlag.getFlag()) {
                        com.pojo.PostImg postImg = new com.pojo.PostImg();
                        postImg.setPostId(post.getPostId());
                        postImg.setPostImgUrl(imgFlag.getUrl());
                        postImgList.add(postImg);
                    }
                }
                rtnData.setFlag(postImgService.insertPostImg(postImgList));
                rtnData.setData(true);
                postService.deleteOtherImg(imgPost.getImgUrls());
                return rtnData;
            }*/
            if (postService.releasePost(post)) {
                if (imgPost.getImgUrls().size() > 0) {
                    List<com.pojo.PostImg> postImgList = new ArrayList<>();
                    for (ImgFlag imgFlag : imgPost.getImgUrls()) {
                        if (imgFlag.getFlag()) {
                            com.pojo.PostImg postImg = new com.pojo.PostImg();
                            postImg.setPostId(post.getPostId());
                            postImg.setPostImgUrl(imgFlag.getUrl());
                            postImgList.add(postImg);
                        }
                    }
                    if (postImgList.size() > 0) {
                        postImgService.insertPostImg(postImgList);
                    }
                }
                rtnData.setFlag(true);
                rtnData.setData(true);
                postService.deleteOtherImg(imgPost.getImgUrls());
                return rtnData;
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("发布失败!");
        return rtnData;
    }

    /**
     * 上传图片
     *
     * @param file 上传的图片
     * @return 按照wangEditor的上传图片返回需求返回数据
     */
    @PostMapping("/uploadImg")
    public Map uploadImg(@RequestParam("file") MultipartFile file) {
        Map<String, Object> rtnData = new HashMap<>();
        Map<String, String> data = new HashMap<>();
        try {
            String fileName = postService.uploadImg(file);
            data.put("url", "img/postImg/" + fileName);
            rtnData.put("errno", 0);
            rtnData.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            rtnData.put("errno", 1);
            rtnData.put("message", "上传图片失败");
        }
        return rtnData;
    }

    /**
     * 删除多余图片
     *
     * @param imgFlags url:图片路径,flag:是否删除图片
     * @return 图片删除是否成功
     */
    @PostMapping("/deleteOtherImg")
    public RtnData deleteOtherImg(@RequestBody List<ImgFlag> imgFlags) {
        RtnData rtnData = new RtnData();
        postService.deleteOtherImg(imgFlags);
        rtnData.setFlag(true);
        rtnData.setData(true);
        return rtnData;
    }

}
