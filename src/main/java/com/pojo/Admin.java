package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Admin {
    @TableId
    private Integer adminId;
    private String adminCode;
    private String adminName;
    private String adminPassword;
    private Integer adminState;
}
