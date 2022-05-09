package com;

import com.mapper.CommentBriefMapper;
import com.mapper.PostBriefMapper;
import com.pojo.Comment;
import com.pojo.wrapper.CommentBrief;
import com.pojo.wrapper.PostBrief;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private CommentBriefMapper commentBriefMapper;

    @Test
    void contextLoads() {
        List<CommentBrief> commentBriefs = commentBriefMapper.selectCommentBriefByPage(49,0,5);
        for (CommentBrief commentBrief: commentBriefs){
            System.out.println(commentBrief);
            for (CommentBrief commentBrief1: commentBrief.getCommentBriefs()){
                System.out.println(commentBrief1);
            }
        }
    }

}
