package com.pojo.wrapper;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class PostImg {
    private String postTitle;
    private String postArticle;
    private String postAbbreviation;
    private Integer partitionId;
    private List<ImgFlag> imgUrls;
}
