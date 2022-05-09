package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Reporting {
    @TableId
    private Integer reportingId;
    private Integer userId;
    private Integer reportingCategoryId;
    private Integer commentId;
    private Integer postId;

}
