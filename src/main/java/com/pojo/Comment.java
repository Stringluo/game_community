package com.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    @TableId
    private Integer commentId;
    private String commentContent;
    private Integer commentRootParentId;
    private Integer commentParentId;
    private Integer postId;
    private Integer userId;
    private Integer commentLikesNum;
    private LocalDateTime commentCreateTime;

    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userImgUrl;
    @TableField(exist = false)
    private Integer commentParentUserId;
    @TableField(exist = false)
    private String commentParentName;

}
