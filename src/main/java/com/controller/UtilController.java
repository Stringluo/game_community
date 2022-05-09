package com.controller;

import com.pojo.User;
import com.pojo.wrapper.RtnData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/utils")
public class UtilController {

    /**
     * 刷新session寿命
     * @return 返回是否刷新成功
     */
    @GetMapping("/refreshSession")
    public RtnData refreshSession(){
        RtnData rtnData = new RtnData();
        System.out.println("请求访问，激活session");
        rtnData.setFlag(true);
        rtnData.setData(true);
        return rtnData;
    }

    /**
     * 判断session中是否具有已登陆用户信息
     * @param request 从request中取出session并判断是否具有loginUser
     * @return 返回是否具有已登录用户信息
     */
    @GetMapping("/hasLoginUser")
    public RtnData hasLoginUser(HttpServletRequest request){
        RtnData rtnData = new RtnData();
        User loginUser = (User) request.getSession().getAttribute("loginUser");
        if(loginUser != null && loginUser.getUserId() != null){
            rtnData.setFlag(true);
            rtnData.setData(true);
            return rtnData;
        }
        rtnData.setFlag(false);
        rtnData.setMsg("未登录");
        return rtnData;
    }

}
