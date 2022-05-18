package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LogInfo {
    @TableId
    private Integer logId;
    private Integer adminId;
    private Integer moduleId;
    private String operateLog;
    private LocalDateTime operateTime;

}
