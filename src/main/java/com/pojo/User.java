package com.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class User {
    @TableId
    private Integer userId;
    private String userName;
    private String userMail;
    private String userPhone;
    private String userPassword;
    private String userImgUrl;
    private String userSign;
    private Integer userSex;
    private Integer userFansNum;
    private Integer userFocusNum;
    private Integer userLikesNum;
    @TableField(exist = false)
    private Boolean likeFlag = false;
}
