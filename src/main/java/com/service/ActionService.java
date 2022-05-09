package com.service;

import com.pojo.Action;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.PostBrief;

import java.util.List;

public interface ActionService {

    /**
     * 给postBrief添加postLikeFlag值
     *
     * @param postBriefs 原postBrief
     * @param userId     用户id
     * @return 添加flag后的postBrief
     */
    List<PostBrief> setUserActionsByPost(List<PostBrief> postBriefs, Integer userId);

    /**
     * 添加Action
     *
     * @param action 添加用户的操作Action
     * @return 返回是否添加成功
     */
    Boolean addAction(Action action);

    /**
     * 删除用户操作
     *
     * @param action 需要删除的用户操作
     * @return 是否删除成功
     */
    Boolean delAction(Action action);

    /**
     * 获取操作信息
     *
     * @param postId 帖子id
     * @param userId 用户id
     * @return 返回用户对帖子的操作
     */
    List<Action> getActions(Integer postId, Integer userId);

    /**
     * 为commentBriefs添加是否已点赞信息
     *
     * @param commentBriefs 原commentBriefs
     * @param userId        已登录用户id
     * @return 返回set后的commentBriefs
     */
    List<CommentBrief> setLikeFlag(List<CommentBrief> commentBriefs, Integer userId);

}
