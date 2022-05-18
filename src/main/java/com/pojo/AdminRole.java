package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AdminRole {
    @TableId
    private Integer adminRoleId;
    private Integer adminId;
    private Integer roleId;
}
