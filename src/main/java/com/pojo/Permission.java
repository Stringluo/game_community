package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Permission {
    @TableId
    private Integer permissionId;
    private String permissionName;
    private String permissionAbb;
    private Integer permissionModuleId;
    private String permissionDec;
    private Integer permissionOrder;
    private String permissionUrl;

}
