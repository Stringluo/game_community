package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ActionCategory {
    @TableId
    private Integer actionCategoryId;
    private String actionCategoryName;
}
