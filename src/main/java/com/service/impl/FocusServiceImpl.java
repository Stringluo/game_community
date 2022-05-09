package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mapper.FocusMapper;
import com.mapper.UserMapper;
import com.pojo.Focus;
import com.pojo.User;
import com.service.FocusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FocusServiceImpl implements FocusService {

    private final FocusMapper focusMapper;
    private final UserMapper userMapper;

    @Autowired
    public FocusServiceImpl(FocusMapper focusMapper, UserMapper userMapper) {
        this.focusMapper = focusMapper;
        this.userMapper = userMapper;
    }

    /**
     * 是否是已关注用户
     *
     * @param userId      用户id
     * @param focusUserId 关注用户id
     * @return 是否已关注
     */
    @Override
    public Boolean isFocusUser(Integer userId, Integer focusUserId) {
        LambdaQueryWrapper<Focus> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Focus::getUserId, userId);
        queryWrapper.eq(Focus::getFocusUserId, focusUserId);
        Focus focus = focusMapper.selectOne(queryWrapper);
        if (focus != null && focus.getFocusId() != null) {
            return true;
        }
        return false;
    }

    /**
     * 关注用户
     *
     * @param userId      用户id
     * @param focusUserId 被关注用户id
     * @return 返回是否关注成功
     */
    @Override
    public Boolean focusUser(Integer userId, Integer focusUserId) {
        Focus focus = new Focus();
        focus.setUserId(userId);
        focus.setFocusUserId(focusUserId);
        int insert = focusMapper.insert(focus);
        if (insert == 1) {
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getUserId, userId);
            updateWrapper.setSql("user_focus_num = user_focus_num + 1");
            userMapper.update(null, updateWrapper);
            LambdaUpdateWrapper<User> updateWrapper1 = new LambdaUpdateWrapper<>();
            updateWrapper1.eq(User::getUserId, focusUserId);
            updateWrapper1.setSql("user_fans_num = user_fans_num + 1");
            userMapper.update(null, updateWrapper1);
            return true;
        }
        return false;
    }

    /**
     * 取消关注用户
     *
     * @param userId      用户id
     * @param focusUserId 关注用户id
     * @return 返回是否取消关注成功
     */
    @Override
    public Boolean cancelFocusUser(Integer userId, Integer focusUserId) {
        LambdaUpdateWrapper<Focus> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Focus::getUserId, userId);
        updateWrapper.eq(Focus::getFocusUserId, focusUserId);
        int insert = focusMapper.delete(updateWrapper);
        if (insert == 1) {
            LambdaUpdateWrapper<User> updateWrapper1 = new LambdaUpdateWrapper<>();
            updateWrapper1.eq(User::getUserId, userId);
            updateWrapper1.setSql("user_focus_num = user_focus_num - 1");
            userMapper.update(null, updateWrapper1);
            LambdaUpdateWrapper<User> updateWrapper2 = new LambdaUpdateWrapper<>();
            updateWrapper2.eq(User::getUserId, focusUserId);
            updateWrapper2.setSql("user_fans_num = user_fans_num - 1");
            userMapper.update(null, updateWrapper2);
            return true;
        }
        return false;
    }

}
