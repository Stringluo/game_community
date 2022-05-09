package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Partition {
    @TableId
    private Integer partitionId;
    private String partitionName;
}
