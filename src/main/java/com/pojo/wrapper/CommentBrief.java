package com.pojo.wrapper;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class CommentBrief {
    private Integer commentId;
    private Integer userId;
    private String userImgUrl;
    private String userName;
    private String commentContent;
    private LocalDateTime commentCreateTime;
    private Integer commentLikesNum;
    private Integer commentChildrenNum;
    private Integer commentRootParentId;
    private Integer commentParentId;
    private String commentParentName;
    private Integer commentParentUserId;

    private List<CommentBrief> commentBriefs;
    private Integer childrenCommentCount;

    private Boolean commentLikeFlag = false;
}
