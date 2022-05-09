package com.service;

public interface FocusService {

    /**
     * 是否是已关注用户
     * @param userId 用户id
     * @param focusUserId 关注用户id
     * @return 是否已关注
     */
    Boolean isFocusUser(Integer userId, Integer focusUserId);

    /**
     * 关注用户
     * @param userId 用户id
     * @param focusUserId 被关注用户id
     * @return 返回是否关注成功
     */
    Boolean focusUser(Integer userId, Integer focusUserId);

    /**
     * 取消关注用户
     * @param userId 用户id
     * @param focusUserId 关注用户id
     * @return 返回是否取消关注成功
     */
    Boolean cancelFocusUser(Integer userId, Integer focusUserId);

}
