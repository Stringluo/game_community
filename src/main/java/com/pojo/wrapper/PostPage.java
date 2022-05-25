package com.pojo.wrapper;

import lombok.Data;

@Data
public class PostPage {
    private Integer partitionId;
    private Integer pageNum;
    private Integer postSum;
    private Integer userId;
    private Integer actionUserId;
    private Integer actionCategoryId;
    private Integer postId;
    private Integer commentSum;
    private Integer commentId;
    private String searchParam;
}
