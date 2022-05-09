package com.mapper;

import com.pojo.wrapper.CommentBrief;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentBriefMapper {

    List<CommentBrief> selectCommentBriefByPage(@Param("postId") Integer postId, @Param("start") Integer start, @Param("size") Integer size);

}
