package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ReportingCategory {
    @TableId
    private Integer reportingId;
    private String reportingName;

}
