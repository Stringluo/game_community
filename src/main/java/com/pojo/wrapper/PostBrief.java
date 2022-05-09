package com.pojo.wrapper;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class PostBrief {
    private Integer userId;
    private String userImgUrl;
    private String userName;

    private Integer postId;
    private String postTitle;
    private String postAbbreviation;
    private Integer postLikesNum;
    private Boolean postLikeFlag = false;
    private Integer postLooksNum;
    private Integer postCommentsNum;
    private LocalDateTime postCreateTime;
    private Integer partitionId;

    private List<String> postImgUrls;

}
