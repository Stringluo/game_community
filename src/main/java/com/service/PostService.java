package com.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pojo.Post;
import com.pojo.PostImg;
import com.pojo.wrapper.ImgFlag;
import com.pojo.wrapper.PostPage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Boolean releasePost(Post post);

    Boolean editPost(Post post);

    String uploadImg(MultipartFile file) throws Exception;

    void deleteOtherImg(List<ImgFlag> imgFlags);

    List<Post> getPostPage(PostPage postPage);

    List<PostImg> getOfficialPost();

    Post getPostById(Integer postId);

    Boolean likePost(Integer postId);

    Boolean cancelLikePost(Integer postId);

    Boolean collectionPost(Integer postId);

    Boolean cancelCollectionPost(Integer postId);

    Boolean deletePostById(Integer postId, Integer userId);

    IPage<Post> getReviewPostPage(IPage<Post> page);

    Boolean passPost(Integer postId);

    Boolean unPassPost(Integer postId);
}
