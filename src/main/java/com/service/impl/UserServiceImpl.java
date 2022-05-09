package com.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mapper.CommentMapper;
import com.mapper.PostMapper;
import com.mapper.UserMapper;
import com.pojo.Comment;
import com.pojo.Post;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.UserCode;
import com.pojo.User;
import com.service.UserService;
import com.utils.RandomUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final String STATIC_BASE_URL = "E:\\02-javaStu\\Game_Community\\game_community_java\\target\\classes\\static\\";
    private final String ROOT_BASE_URL = "E:\\02-javaStu\\Game_Community\\game_community_java\\src\\main\\resources\\static\\";

    @CreateCache(name = "vCodeCache_", expire = 300)
    private Cache<String, String> vCodeCache;

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PostMapper postMapper, CommentMapper commentMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
    }

    @Override
    public User loginByPassword(UserCode user) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_mail", user.getMail());
        map.put("user_password", user.getPassword());
        List<User> users = userMapper.selectByMap(map);
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public Boolean isOneUserExist(User user) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_mail", user.getUserMail());
        return userMapper.selectCount(queryWrapper) == 1;
    }

    @Override
    public String getCode(String mail, String type) {
        String vCode = RandomUtil.getVCode(6);
        vCodeCache.put(mail + type, vCode);
        return vCode;
    }

    @Override
    public User loginByCode(UserCode userCode) {
        // 从redis缓存中取出code判断是否相同
        if (userCode.getCode().equals(vCodeCache.get(userCode.getMail() + "login"))) {
            // code相同，可以登录
            // 查询数据库是否具有该用户
            Map<String, Object> map = new HashMap();
            map.put("user_mail", userCode.getMail());
            List<User> users = userMapper.selectByMap(map);
            if (users.size() == 1) {
                //有且仅有一个用户数据，获取该用户数据
                User loginUser = users.get(0);
                // 密码置空
                loginUser.setUserPassword("");
                return loginUser;
            }
        }
        return null;
    }

    @Override
    public Boolean verifyCode(UserCode userCode) {
        return userCode.getCode().equals(vCodeCache.get(userCode.getMail() + "findPassword"));
    }

    @Override
    public Boolean changePassword(UserCode userCode) {
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("user_mail", userCode.getMail());
        userUpdateWrapper.set("user_password", userCode.getPassword());
        return userMapper.update(null, userUpdateWrapper) == 1;
    }

    @Override
    public Boolean register(UserCode userCode) {
        if (userCode.getCode().equals(vCodeCache.get(userCode.getMail() + "register"))) {
            //验证码正确，可以注册
            User user = new User();
            user.setUserName("旅行者");
            user.setUserImgUrl("img/avatar/official/defaultUserAvatar.png");
            user.setUserMail(userCode.getMail());
            user.setUserPassword(userCode.getPassword());
            user.setUserFansNum(0);
            user.setUserFocusNum(0);
            user.setUserLikesNum(0);
            user.setUserSign("系统原装签名，送给每一位小可爱");
            return userMapper.insert(user) == 1;
        }
        return false;
    }

    @Override
    public User getUserById(Integer userId) {
        User user = userMapper.selectById(userId);
        user.setUserPassword("");
        return user;
    }

    @Override
    public String uploadAvatar(MultipartFile file) throws Exception {
        // 图片存储路径
        String path = ROOT_BASE_URL + "img\\avatar";
        // 判断是否有路径
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
        File tempFile = new File(path, fileName);
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        } else {
            uploadAvatar(file);
        }
        file.transferTo(tempFile);
        //手动编译
        String targetPath = STATIC_BASE_URL + "img\\avatar";
        File newFile = new File(targetPath, fileName);
        FileUtils.copyFile(tempFile, newFile);
        return fileName;
    }

    @Override
    public void clearAvatar(String url) {
        if (!"".equals(url)) {
            File img = new File(STATIC_BASE_URL + url);
            if (img.exists()) {
                img.delete();
            }
            File imRoot = new File(ROOT_BASE_URL + url);
            if (imRoot.exists()) {
                imRoot.delete();
            }
        }
    }

    @Override
    public User editUser(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getUserId, user.getUserId());
        updateWrapper.set(User::getUserName, user.getUserName());
        updateWrapper.set(User::getUserImgUrl, user.getUserImgUrl());
        updateWrapper.set(User::getUserSign, user.getUserSign());
        if (user.getUserSex() != null) {
            updateWrapper.set(User::getUserSex, user.getUserSex());
        }
        int update = userMapper.update(null, updateWrapper);
        if (update == 1) {
            user = userMapper.selectById(user.getUserId());
            user.setUserPassword(null);
        }
        return user;
    }

    @Override
    public Boolean likeUserPost(Integer postId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        Post post = postMapper.selectById(postId);
        updateWrapper.eq(User::getUserId, post.getUserId());
        updateWrapper.setSql("user_likes_num = user_likes_num + 1");
        return userMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean cancelLikeUserPost(Integer postId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        Post post = postMapper.selectById(postId);
        updateWrapper.eq(User::getUserId, post.getUserId());
        updateWrapper.setSql("user_likes_num = user_likes_num - 1");
        return userMapper.update(null, updateWrapper) == 1;
    }
    @Override
    public Boolean likeUserComment(Integer commentId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        Comment comment = commentMapper.selectById(commentId);
        updateWrapper.eq(User::getUserId, comment.getUserId());
        updateWrapper.setSql("user_likes_num = user_likes_num + 1");
        return userMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean cancelLikeUserComment(Integer commentId) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        Comment comment = commentMapper.selectById(commentId);
        updateWrapper.eq(User::getUserId, comment.getUserId());
        updateWrapper.setSql("user_likes_num = user_likes_num - 1");
        return userMapper.update(null, updateWrapper) == 1;
    }

}
