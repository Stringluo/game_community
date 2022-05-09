package com.service;

import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.UserCode;
import com.pojo.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    User loginByPassword(UserCode user);

    Boolean isOneUserExist(User user);

    String getCode(String mail,String type);

    User loginByCode(UserCode userCode);

    Boolean verifyCode(UserCode userCode);

    Boolean changePassword(UserCode userCode);

    Boolean register(UserCode userCode);

    User getUserById(Integer userId);

    String uploadAvatar(MultipartFile file) throws Exception;

    void clearAvatar(String url);

    User editUser(User user);

    Boolean likeUserPost(Integer postId);

    Boolean cancelLikeUserPost(Integer postId);

    Boolean likeUserComment(Integer commentId);

    Boolean cancelLikeUserComment(Integer commentId);

}
