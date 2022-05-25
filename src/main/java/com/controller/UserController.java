package com.controller;

import com.pojo.wrapper.UserCode;
import com.pojo.wrapper.RtnData;
import com.pojo.User;
import com.service.PostService;
import com.service.SendMailService;
import com.service.UserService;
import com.utils.AjaxUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final SendMailService sendMailService;

    @Autowired
    public UserController(UserService userService, PostService postService, SendMailService sendMailService) {
        this.userService = userService;
        this.sendMailService = sendMailService;
    }

    /**
     * 获取验证码
     *
     * @param mail:邮箱号
     * @return data:code:验证码
     */
    @GetMapping("/getCode/{mail}/{type}")
    public RtnData getCode(@PathVariable("mail") String mail, @PathVariable("type") String type) {
        RtnData rtnData = new RtnData();
        //1、判断邮箱是否已注册
        //1.1、校验邮箱格式
        if (!AjaxUtil.checkEmail(mail)) {
            //邮箱格式有误
            rtnData.setFlag(false);
            rtnData.setMsg("邮箱格式有误");
            return rtnData;
        }
        //1.2、查询数据库
        User user = new User();
        user.setUserMail(mail);
        if (!userService.isOneUserExist(user) && !"register".equals(type)) {
            //数据库不存在邮箱
            rtnData.setFlag(false);
            rtnData.setMsg("用户邮箱尚未注册");
            return rtnData;
        }
        //2、生成验证码
        String code = userService.getCode(mail, type);
        Boolean flag = sendMailService.sendMail(mail, code, type);
        rtnData.setFlag(flag);
        rtnData.setData(flag);
        return rtnData;
    }

    /**
     * 根据验证码登录
     * @param userCode mail:邮箱号,code:验证码
     * @param session 登录用户添加至session
     * @return 返回登录用户信息
     */
    @PostMapping("/doLoginByCode")
    public RtnData doLoginByVCode(@RequestBody UserCode userCode, HttpSession session) {
        RtnData rtnData = new RtnData();
        //根据邮箱和验证码登录
        User loginUser = userService.loginByCode(userCode);
        if (loginUser != null) {
            //登录成功，打包返回数据
            rtnData.setFlag(true);
            rtnData.setData(loginUser);
            //添加session
            session.setAttribute("loginUser", loginUser);
            return rtnData;
        }
        rtnData.setFlag(false);
        return rtnData;
    }

    /**
     * 验证验证码
     * @param userCode mail:邮箱号
     * @return 返回Boolean是否发送验证码成功
     */
    @PostMapping("/verifyCode")
    public RtnData verifyCode(@RequestBody UserCode userCode) {
        RtnData rtnData = new RtnData();
        if (userService.verifyCode(userCode)) {
            rtnData.setFlag(true);
            rtnData.setData(true);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("验证码错误");
        return rtnData;
    }

    /**
     * 注册
     * @param userCode mail:邮箱,code:验证码
     * @return boolean返回是否注册成功
     */
    @PostMapping("/register")
    public RtnData register(@RequestBody UserCode userCode) {
        RtnData rtnData = new RtnData();
        if (userService.register(userCode)) {
            rtnData.setFlag(true);
            rtnData.setData(true);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("注册失败");
        return rtnData;
    }

    /**
     * 根据密码登录
     * @param user userMail:邮箱号,userPassword:密码
     * @param request 从request中获取cookie并添加
     * @param response 向response中添加session
     * @return 返回登录用户信息
     */
    @PostMapping("/doLoginByPassword")
    public RtnData doLoginByPassword(@RequestBody UserCode user, HttpServletRequest request, HttpServletResponse response) {
        RtnData rtnData = new RtnData();
        // 根据密码登录
        User loginUser = userService.loginByPassword(user);
        if (loginUser != null) {
            //登录成功，打包返回数据
            rtnData.setFlag(true);
            rtnData.setData(loginUser);
            //添加session
            request.getSession().setAttribute("loginUser", loginUser);
            if (user.getRememberMe()) {
                //记住密码,创建Cookie
                Cookie userMailCookie = new Cookie("userMail", loginUser.getUserMail());
                Cookie userPasswordCookie = new Cookie("userPassword", loginUser.getUserPassword());
                userMailCookie.setMaxAge(60*24*30);
                userMailCookie.setPath("/");
                userPasswordCookie.setMaxAge(60*24*30);
                userPasswordCookie.setPath("/");
                response.addCookie(userMailCookie);
                response.addCookie(userPasswordCookie);
            }
            return rtnData;
        }
        rtnData.setFlag(false);
        return rtnData;
    }

    /**
     * 根据cookie自动登录
     * @param request 从request中获取用户密码以登录
     * @return 返回登录用户信息
     */
    @GetMapping("/doLoginByCookie")
    public RtnData doLoginByCookie(HttpServletRequest request){
        RtnData rtnData = new RtnData();
        int trueData = 0;
        UserCode user = new UserCode();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if("userMail".equals(cookie.getName())){
                user.setMail(cookie.getValue());
                trueData++;
            }
            if("userPassword".equals(cookie.getName())){
                user.setPassword(cookie.getValue());
                trueData++;
            }
        }
        if(trueData==2){
            // 根据密码登录
            User loginUser = userService.loginByPassword(user);
            if (loginUser != null) {
                //登录成功，打包返回数据
                rtnData.setFlag(true);
                rtnData.setData(loginUser);
                //添加session
                request.getSession().setAttribute("loginUser", loginUser);
                return rtnData;
            }
        }
        rtnData.setFlag(false);
        return rtnData;
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
}
