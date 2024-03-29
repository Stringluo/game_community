package com.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mapper.CommentMapper;
import com.mapper.FocusMapper;
import com.mapper.PostMapper;
import com.mapper.UserMapper;
import com.pojo.Comment;
import com.pojo.Focus;
import com.pojo.Post;
import com.pojo.wrapper.UserCode;
import com.pojo.User;
import com.service.UserService;
import com.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Value("${static.base-dir}")
    private String STATIC_BASE_URL;

    @CreateCache(name = "vCodeCache_", expire = 300)
    private Cache<String, String> vCodeCache;

    private final UserMapper userMapper;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final FocusMapper focusMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, PostMapper postMapper, CommentMapper commentMapper, FocusMapper focusMapper) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.focusMapper = focusMapper;
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
        //验证码正确，可以注册
        User user = new User();
        if (userCode.getCode().equals(vCodeCache.get(userCode.getMail() + "register"))) {
            initUser(user, userCode);
            user.setUserRole(1);//普通用户
            return userMapper.insert(user) == 1;
        }
        if (userCode.getCode().equals(vCodeCache.get("officeRegister" + userCode.getCode()))) {
            initUser(user, userCode);
            user.setUserRole(0);//官方用户
            return userMapper.insert(user) == 1;
        }
        if (userCode.getCode().equals(vCodeCache.get("universalRegister"))) {
            initUser(user, userCode);
            user.setUserRole(2);//测试用户
            return userMapper.insert(user) == 1;
        }
        return false;
    }

    private void initUser(User user, UserCode userCode) {
        user.setUserName("旅行者");
        user.setUserImgUrl("img/avatar/official/defaultUserAvatar.png");
        user.setUserMail(userCode.getMail());
        user.setUserPassword(userCode.getPassword());
        user.setUserFansNum(0);
        user.setUserFocusNum(0);
        user.setUserLikesNum(0);
        user.setUserSign("系统原装签名，送给每一位小可爱");
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
        String path = STATIC_BASE_URL + "img/avatar";
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
        return fileName;
    }

    @Override
    public void clearAvatar(String url) {
        if (!"".equals(url)) {
            File img = new File(STATIC_BASE_URL + url);
            if (img.exists()) {
                img.delete();
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

    @Override
    public List<User> getUserFans(Integer userId) {
        LambdaQueryWrapper<Focus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Focus::getFocusUserId, userId);
        queryWrapper.ne(Focus::getUserId, userId);
        List<Focus> focusList = focusMapper.selectList(queryWrapper);
        List<User> users = new ArrayList<>();
        for (Focus focus : focusList) {
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUserId, focus.getUserId());
            User user = userMapper.selectOne(queryWrapper1);
            user.setUserPassword(null);
            users.add(user);
        }
        return users;
    }

    @Override
    public List<User> getUserFocus(Integer userId) {
        LambdaQueryWrapper<Focus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Focus::getFocusUserId, userId);
        queryWrapper.eq(Focus::getUserId, userId);
        List<Focus> focusList = focusMapper.selectList(queryWrapper);
        List<User> users = new ArrayList<>();
        for (Focus focus : focusList) {
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(User::getUserId, focus.getFocusUserId());
            User user = userMapper.selectOne(queryWrapper1);
            user.setUserPassword(null);
            users.add(user);
        }
        return users;
    }

    @Override
    public void setUserLikeFlag(List<User> users, Integer userId) {
        if (users.size() > 0) {
            for (User user : users) {
                LambdaQueryWrapper<Focus> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Focus::getUserId, userId);
                queryWrapper.eq(Focus::getFocusUserId, user.getUserId());
                Focus focus = focusMapper.selectOne(queryWrapper);
                if (focus != null && focus.getFocusId() != null) {
                    user.setLikeFlag(true);
                }
            }
        }
    }

}
