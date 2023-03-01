package com.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pojo.Admin;
import com.pojo.wrapper.RtnData;
import com.service.AdminService;
import com.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final PostService postService;
    private final AdminService adminService;

    @Autowired
    public AdminController(PostService postService, AdminService adminService) {
        this.postService = postService;
        this.adminService = adminService;
    }

    @PostMapping("/getReviewPost")
    public RtnData getReviewPost(@RequestBody Page page) {
        RtnData rtnData = new RtnData();
        rtnData.setFlag(true);
        rtnData.setData(postService.getReviewPostPage(page));
        return rtnData;
    }

    @GetMapping("/passPost/{postId}")
    public RtnData passPost(@PathVariable("postId") Integer postId) {
        RtnData rtnData = new RtnData();
        if (postService.passPost(postId)) {
            rtnData.setFlag(true);
            rtnData.setData(true);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("审核通过失败");
        return rtnData;
    }

    @GetMapping("/unPassPost/{postId}")
    public RtnData unPassPost(@PathVariable("postId") Integer postId) {
        RtnData rtnData = new RtnData();
        if (postService.unPassPost(postId)) {
            rtnData.setFlag(true);
            rtnData.setData(true);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("审核通过失败");
        return rtnData;
    }

    @PostMapping("/doLogin")
    public RtnData doLogin(@RequestBody Admin admin) {
        RtnData rtnData = new RtnData();
        Admin loginUser = adminService.doLogin(admin);
        if (loginUser != null && loginUser.getAdminId() != null) {
            rtnData.setFlag(true);
            rtnData.setData(loginUser);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("登录失败");
        return rtnData;
    }

}
