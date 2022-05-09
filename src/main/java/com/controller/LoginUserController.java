package com.controller;

import com.pojo.User;
import com.pojo.wrapper.ImgFlag;
import com.pojo.wrapper.RtnData;
import com.pojo.wrapper.UserCode;
import com.service.FocusService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/loginUsers")
public class LoginUserController {

    private final UserService userService;
    private final FocusService focusService;

    @Autowired
    public LoginUserController(UserService userService, FocusService focusService) {
        this.userService = userService;
        this.focusService = focusService;
    }

    /**
     * 修改用户密码
     *
     * @param userCode mail:邮箱,code:验证码
     * @return 返回是否修改成功
     */
    @PostMapping("/changePassword")
    public RtnData changePassword(@RequestBody UserCode userCode) {
        RtnData rtnData = new RtnData();
        if (userService.verifyCode(userCode)) {
            //验证成功，可以修改密码
            if (userService.changePassword(userCode)) {
                //修改成功
                rtnData.setFlag(true);
                rtnData.setData(true);
                return rtnData;
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("修改密码失败");
        return rtnData;
    }

    /**
     * 退出登录
     *
     * @param request  从request获取session并注销
     * @param response 向response添加空信息
     * @return 返回是否退出登录成功
     */
    @GetMapping("/logoutUser")
    public RtnData logoutUser(HttpServletRequest request, HttpServletResponse response) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            request.getSession().invalidate();
            Cookie[] cookies = request.getCookies();
            Cookie userMailCookie = new Cookie("userMail", null);
            Cookie userPasswordCookie = new Cookie("userPassword", null);
            userMailCookie.setMaxAge(0);
            userMailCookie.setPath("/");
            userPasswordCookie.setMaxAge(0);
            userPasswordCookie.setPath("/");
            response.addCookie(userMailCookie);
            response.addCookie(userPasswordCookie);
            rtnData.setFlag(true);
            rtnData.setData(true);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setData("您尚未登陆");
        return rtnData;
    }

    /**
     * 上传头像
     *
     * @param file 头像图片
     * @return 返回是否上传成功
     */
    @PostMapping("/uploadAvatar")
    public RtnData uploadAvatar(@RequestParam("file") MultipartFile file) {
        RtnData rtnData = new RtnData();
        try {
            String fileName = userService.uploadAvatar(file);
            rtnData.setFlag(true);
            rtnData.setData("img/avatar/" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            rtnData.setFlag(false);
            rtnData.setMsg("上传图片失败");
        }
        return rtnData;
    }

    /**
     * 清除残余头像图片
     *
     * @param imgFlag url:头像路径,flag:是否需要清除
     * @return 返回清除是否成功
     */
    @PostMapping("/clearAvatar")
    public RtnData clearAvatar(@RequestBody ImgFlag imgFlag) {
        RtnData rtnData = new RtnData();
        userService.clearAvatar(imgFlag.getUrl());
        rtnData.setFlag(true);
        rtnData.setData(true);
        return rtnData;
    }

    /**
     * 修改用户
     *
     * @param user 新的用户信息
     * @return 返回修改后的用户信息
     */
    @PostMapping("/editUser")
    public RtnData editUser(@RequestBody User user) {
        RtnData rtnData = new RtnData();
        rtnData.setFlag(true);
        rtnData.setData(userService.editUser(user));
        return rtnData;
    }

    /**
     * 注销session
     *
     * @param request 从request中取出session并注销
     * @return 返回是否注销成功
     */
    @GetMapping("/destroySession")
    public RtnData destroySession(HttpServletRequest request) {
        RtnData rtnData = new RtnData();
        request.getSession().invalidate();
        rtnData.setFlag(true);
        rtnData.setData(true);
        return rtnData;
    }

    /**
     * 关注用户
     * @param focusUserId 被关注用户id
     * @param session 从session中获取操作用户id
     * @return 返回关注是否成功
     */
    @GetMapping("/focusUser/{focusUserId}")
    public RtnData focusUser(@PathVariable("focusUserId") Integer focusUserId, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            if (focusService.focusUser(loginUser.getUserId(), focusUserId)) {
                rtnData.setFlag(true);
                rtnData.setData(true);
                return rtnData;
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("关注用户失败");
        return rtnData;
    }

    /**
     * 取消关注用户
     * @param focusUserId 被关注用户id
     * @param session 从session中获取操作用户id
     * @return 返回是否取消关注成功
     */
    @GetMapping("/cancelFocusUser/{focusUserId}")
    public RtnData cancelFocusUser(@PathVariable("focusUserId") Integer focusUserId, HttpSession session) {
        RtnData rtnData = new RtnData();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser != null && loginUser.getUserId() != null) {
            if (focusService.cancelFocusUser(loginUser.getUserId(), focusUserId)) {
                rtnData.setFlag(true);
                rtnData.setData(true);
                return rtnData;
            }
        }
        rtnData.setFlag(false);
        rtnData.setMsg("取消关注用户失败");
        return rtnData;
    }
}
