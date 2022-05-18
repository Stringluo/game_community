package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class RolePermission {
    @TableId
    private Integer rolePermissionId;
    private Integer roleId;
    private Integer permissionId;
}
