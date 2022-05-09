package com.pojo.wrapper;

import lombok.Data;

@Data
public class UserCode {
    private String mail;
    private String code;
    private String password;
    private Boolean rememberMe;
}
