package com.mapper;

import com.pojo.wrapper.PostBrief;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PostBriefMapper {

    public List<PostBrief> selectPostBriefByPage(@Param("start") Integer start, @Param("size") Integer size);

    public List<PostBrief> selectPostBriefByPagePartition(@Param("start") Integer start, @Param("size") Integer size, @Param("partitionId") Integer partitionId);

    public List<PostBrief> selectPostBriefByPageUser(@Param("start") Integer start, @Param("size") Integer size, @Param("userId") Integer userId);

    public List<PostBrief> selectSearchPostBriefByPage(@Param("start") Integer start, @Param("size") Integer size, @Param("searchParam") String searchParam);

    public List<PostBrief> selectSearchPostBriefByPagePartition(@Param("start") Integer start, @Param("size") Integer size, @Param("partitionId") Integer partitionId, @Param("searchParam") String searchParam);

    public List<PostBrief> selectSearchPostBriefByPageUser(@Param("start") Integer start, @Param("size") Integer size, @Param("userId") Integer userId, @Param("searchParam") String searchParam);
}
