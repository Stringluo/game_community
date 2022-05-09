package com.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mapper.ActionMapper;
import com.pojo.Action;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.PostBrief;
import com.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActionServiceImpl implements ActionService {

    private final ActionMapper actionMapper;

    @Autowired
    public ActionServiceImpl(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }

    /**
     * 给postBrief添加postLikeFlag值
     *
     * @param postBriefs 原postBrief
     * @param userId     用户id
     * @return 添加flag后的postBrief
     */
    @Override
    public List<PostBrief> setUserActionsByPost(List<PostBrief> postBriefs, Integer userId) {
        List<Integer> postIds = new ArrayList<>();
        for (PostBrief postBrief : postBriefs) {
            postIds.add(postBrief.getPostId());
        }
        LambdaQueryWrapper<Action> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Action::getUserId, userId);
        queryWrapper.eq(Action::getActionCategoryId, 1);
        queryWrapper.in(Action::getPostId, postIds);
        List<Action> actions = actionMapper.selectList(queryWrapper);
        for (Action action : actions) {
            if (action.getActionCategoryId() != null && action.getActionCategoryId().equals(1)) {
                for (PostBrief postBrief : postBriefs) {
                    if (postBrief.getPostId().equals(action.getPostId())) {
                        postBrief.setPostLikeFlag(true);
                    }
                }
            }
        }
        return postBriefs;
    }

    /**
     * 添加Action
     *
     * @param action 添加用户的操作Action
     * @return 返回是否添加成功
     */
    @Override
    public Boolean addAction(Action action) {
        action.setActionTime(LocalDateTime.now());
        return actionMapper.insert(action) == 1;
    }

    /**
     * 删除用户操作Action
     *
     * @param action 需要删除的用户操作
     * @return 是否删除成功
     */
    @Override
    public Boolean delAction(Action action) {
        LambdaQueryWrapper<Action> queryWrapper = new LambdaQueryWrapper<>();
        if (action.getPostId() != null) {
            queryWrapper.eq(Action::getPostId, action.getPostId());
        }
        if (action.getCommentId() != null) {
            queryWrapper.eq(Action::getCommentId, action.getCommentId());
        }
        queryWrapper.eq(Action::getUserId, action.getUserId());
        queryWrapper.eq(Action::getActionCategoryId, action.getActionCategoryId());
        return actionMapper.delete(queryWrapper) == 1;
    }

    /**
     * 获取操作信息
     *
     * @param postId 帖子id
     * @param userId 用户id
     * @return 返回用户对帖子的操作
     */
    @Override
    public List<Action> getActions(Integer postId, Integer userId) {
        LambdaQueryWrapper<Action> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Action::getPostId, postId);
        queryWrapper.eq(Action::getUserId, userId);
        List<Action> actions = actionMapper.selectList(queryWrapper);
        return actions;
    }

    /**
     * 为commentBriefs添加是否已点赞信息
     *
     * @param commentBriefs 原commentBriefs
     * @param userId        已登录用户id
     * @return 返回set后的commentBriefs
     */
    @Override
    public List<CommentBrief> setLikeFlag(List<CommentBrief> commentBriefs, Integer userId) {
        for (CommentBrief commentBrief : commentBriefs) {
            LambdaQueryWrapper<Action> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Action::getUserId, userId);
            queryWrapper.eq(Action::getCommentId, commentBrief.getCommentId());
            queryWrapper.eq(Action::getActionCategoryId, 1);
            if (actionMapper.selectList(queryWrapper).size() == 1) {
                commentBrief.setCommentLikeFlag(true);
            }
            if (commentBrief.getCommentBriefs() != null && commentBrief.getCommentBriefs().size() > 0) {
                setLikeFlag(commentBrief.getCommentBriefs(), userId);
            }
        }
        return commentBriefs;
    }

}
