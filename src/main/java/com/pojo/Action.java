package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Action {
    @TableId
    private Integer actionId;
    private Integer userId;
    private Integer postId;
    private Integer commentId;
    private Integer actionCategoryId;
    private LocalDateTime actionTime;


}
