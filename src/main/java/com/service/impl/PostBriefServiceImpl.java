package com.service.impl;

import com.mapper.PostBriefMapper;
import com.pojo.wrapper.PostBrief;
import com.pojo.wrapper.PostPage;
import com.service.PostBriefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostBriefServiceImpl implements PostBriefService {

    private final PostBriefMapper postBriefMapper;

    @Autowired
    public PostBriefServiceImpl(PostBriefMapper postBriefMapper) {
        this.postBriefMapper = postBriefMapper;
    }

    @Override
    public List<PostBrief> getPostBriefByPage(PostPage postPage) {
        int size = postPage.getPostSum();
        int start = (postPage.getPageNum() - 1) * size;
        if (postPage.getPartitionId() != null) {
            return postBriefMapper.selectPostBriefByPagePartition(start, size, postPage.getPartitionId());
        }
        if (postPage.getUserId() != null) {
            return postBriefMapper.selectPostBriefByPageUser(start, size, postPage.getUserId());
        }
        if (postPage.getActionUserId() != null) {
            return postBriefMapper.selectPostBriefByPageConnectionUser(start, size, postPage.getActionUserId(), postPage.getActionCategoryId());
        }
        return postBriefMapper.selectPostBriefByPage(start, size);
    }

    @Override
    public List<PostBrief> getSearchPostBriefByPage(PostPage postPage) {
        int size = postPage.getPostSum();
        int start = (postPage.getPageNum() - 1) * size;
        if (postPage.getPartitionId() != null) {
            return postBriefMapper.selectSearchPostBriefByPagePartition(start, size, postPage.getPartitionId(), postPage.getSearchParam());
        }
        if (postPage.getUserId() != null) {
            return postBriefMapper.selectSearchPostBriefByPageUser(start, size, postPage.getUserId(), postPage.getSearchParam());
        }
        /*
        if (postPage.getConnectionUserId() != null) {
            return postBriefMapper.selectSearchPostBriefByPageConnectionUser(start, size, postPage.getUserId(), postPage.getSearchParam());
        }*/
        return postBriefMapper.selectSearchPostBriefByPage(start, size, postPage.getSearchParam());
    }
}
