package com.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    @TableId
    private Integer postId;
    private Integer partitionId;
    private Integer userId;
    @TableField(exist = false)
    private String userName;
    private String postTitle;
    private String postArticle;
    private String postAbbreviation;
    private Integer postLikesNum;
    private Integer postLooksNum;
    private Integer postCollectionsNum;
    private Integer postCommentsNum;
    private LocalDateTime postCreateTime;
    private LocalDateTime postEditTime;
    private Integer postState;
}
