package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Focus {
    @TableId
    private Integer focusId;
    private Integer userId;
    private Integer focusUserId;

}
