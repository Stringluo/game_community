package com.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class PostImg implements Serializable {
    @TableId
    private Integer postImgId;
    private String postImgUrl;
    private Integer postId;

}
