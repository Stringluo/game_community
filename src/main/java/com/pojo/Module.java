package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Module {
    @TableId
    private Integer moduleId;
    private String moduleName;
    private String moduleUrl;
    private Integer moduleParentId;
    private String moduleDec;
    private Integer moduleOrder;
}
