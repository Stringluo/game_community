package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Role {
    @TableId
    private Integer roleId;
    private String roleName;
    private String roleDec;
}
