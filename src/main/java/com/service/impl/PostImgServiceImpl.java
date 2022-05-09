package com.service.impl;

import com.mapper.PostImgMapper;
import com.pojo.PostImg;
import com.service.PostImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostImgServiceImpl implements PostImgService {

    private final PostImgMapper postImgMapper;

    @Autowired
    public PostImgServiceImpl(PostImgMapper postImgMapper) {
        this.postImgMapper = postImgMapper;
    }

    @Override
    public Boolean insertPostImg(List<PostImg> postImgList) {
        Boolean flag = true;
        for (PostImg postImg : postImgList) {
            if (postImgMapper.insert(postImg) != 1) {
                flag = false;
            }
        }
        return flag;
    }
}
