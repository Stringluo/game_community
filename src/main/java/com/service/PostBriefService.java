package com.service;

import com.pojo.wrapper.PostBrief;
import com.pojo.wrapper.PostPage;

import java.util.List;

public interface PostBriefService {

    List<PostBrief> getPostBriefByPage(PostPage postPage);

    List<PostBrief> getSearchPostBriefByPage(PostPage postPage);
}
