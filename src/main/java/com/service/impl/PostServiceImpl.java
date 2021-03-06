package com.service.impl;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mapper.*;
import com.pojo.*;
import com.pojo.wrapper.ImgFlag;
import com.pojo.wrapper.PostPage;
import com.service.PostService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PostServiceImpl implements PostService {

    @Value("${static.base-dir}")
    private String STATIC_BASE_URL;
    private final PostMapper postMapper;
    private final PostImgMapper postImgMapper;
    private final CommentMapper commentMapper;
    private final ActionMapper actionMapper;
    private final ReportingMapper reportingMapper;

    @CreateCache(name = "officialPostCache_", expire = 1, timeUnit = TimeUnit.DAYS)
    private Cache<String, List<PostImg>> officialPostCache;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, PostImgMapper postImgMapper, CommentMapper commentMapper, ActionMapper actionMapper, ReportingMapper reportingMapper) {
        this.postMapper = postMapper;
        this.postImgMapper = postImgMapper;
        this.commentMapper = commentMapper;
        this.actionMapper = actionMapper;
        this.reportingMapper = reportingMapper;
    }

    @Override
    public Boolean releasePost(Post post) {
        post.setPostCollectionsNum(0);
        post.setPostCommentsNum(0);
        post.setPostLikesNum(0);
        post.setPostLooksNum(0);
        post.setPostCreateTime(LocalDateTime.now());
        post.setPostEditTime(post.getPostCreateTime());
        return postMapper.insert(post) == 1;
    }

    @Override
    public Boolean editPost(Post post) {
        post.setPostEditTime(LocalDateTime.now());
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getPostId, post.getPostId());
        updateWrapper.eq(Post::getUserId, post.getUserId());
        return postMapper.update(post, updateWrapper) == 1;
    }

    @Override
    public String uploadImg(MultipartFile file) throws Exception {
        // ??????????????????
        String path = STATIC_BASE_URL + "img/postImg";
        // ?????????????????????
        if (!new File(path).exists()) {
            new File(path).mkdirs();
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ".jpg";
        File tempFile = new File(path, fileName);
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        } else {
            uploadImg(file);
        }
        file.transferTo(tempFile);
        return fileName;
    }

    @Override
    public void deleteOtherImg(List<ImgFlag> imgFlags) {
        for (ImgFlag imgFlag : imgFlags) {
            if (!imgFlag.getFlag()) {
                File img = new File(STATIC_BASE_URL + imgFlag.getUrl());
                if (img.exists()) {
                    img.delete();
                }
            }
        }
    }

    @Override
    public List<Post> getPostPage(PostPage postPage) {
        IPage<Post> postIPage = new Page<Post>(postPage.getPageNum(), postPage.getPageNum());
        QueryWrapper<Post> postQueryWrapper = new QueryWrapper<>();
        postMapper.selectPage(postIPage, postQueryWrapper);
        return null;
    }

    @Override
    public List<PostImg> getOfficialPost() {
        if (officialPostCache.get("global") != null) {
            return officialPostCache.get("global");
        }
        setOfficialPostCache();
        return getOfficialPost();
    }

    @Override
    public Post getPostById(Integer postId) {
        Post post = postMapper.selectById(postId);
        if (post != null && post.getPostId() != null) {
            LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Post::getPostId, postId);
            updateWrapper.setSql("post_looks_num = post_looks_num + 1");
            postMapper.update(null, updateWrapper);
        }
        return postMapper.selectById(postId);
    }

    @Override
    public Boolean likePost(Integer postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getPostId, postId);
        updateWrapper.setSql("post_likes_num = post_likes_num + 1");
        return postMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean cancelLikePost(Integer postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getPostId, postId);
        updateWrapper.setSql("post_likes_num = post_likes_num - 1");
        return postMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean collectionPost(Integer postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getPostId, postId);
        updateWrapper.setSql("post_collections_num = post_collections_num + 1");
        return postMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean cancelCollectionPost(Integer postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Post::getPostId, postId);
        updateWrapper.setSql("post_collections_num = post_collections_num - 1");
        return postMapper.update(null, updateWrapper) == 1;
    }

    @Transactional
    @Override
    public Boolean deletePostById(Integer postId, Integer userId) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getPostId, postId);
        queryWrapper.eq(Post::getUserId, userId);
        if (postMapper.selectList(queryWrapper).size() == 1) {
            //??????????????????????????????????????????????????????
            //???????????????action???comment???comment?????????action???postImg???reporting
            //1.??????comment??????action
            //1.1??????????????????
            LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLambdaQueryWrapper.eq(Comment::getPostId, postId);
            List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
            if (comments != null && comments.size() > 0) {
                //1.2???????????????action???reporting
                for (Comment comment : comments) {
                    LambdaQueryWrapper<Action> actionLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    actionLambdaQueryWrapper.eq(Action::getCommentId, comment.getCommentId());
                    actionMapper.delete(actionLambdaQueryWrapper);
                    LambdaQueryWrapper<Reporting> reportingLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    reportingLambdaQueryWrapper.eq(Reporting::getCommentId, comment.getCommentId());
                    reportingMapper.delete(reportingLambdaQueryWrapper);
                }
                //1.3??????????????????????????????????????????comment
                for (Comment comment : comments) {
                    if (postId.equals(comment.getPostId())
                            && comment.getCommentParentId() != null
                            && !"".equals(comment.getCommentParentId())
                            && comment.getCommentRootParentId() != null
                            && !comment.getCommentParentId().equals(comment.getCommentRootParentId())) {
                        if (commentMapper.deleteById(comment) == 1) {
                            comments.remove(comment);
                        }
                    }
                }
                //1.4?????????????????????comment
                for (Comment comment : comments) {
                    if (postId.equals(comment.getPostId())
                            && comment.getCommentParentId() != null
                            && !"".equals(comment.getCommentParentId())) {
                        if (commentMapper.deleteById(comment) == 1) {
                            comments.remove(comment);
                        }
                    }
                }
                //1.5???????????????comment
                for (Comment comment : comments) {
                    if (commentMapper.deleteById(comment) == 1) {
                        comments.remove(comment);
                    }
                }
            }
            //1.2??????action
            LambdaQueryWrapper<Action> actionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            actionLambdaQueryWrapper.eq(Action::getPostId, postId);
            actionMapper.delete(actionLambdaQueryWrapper);
            //1.3??????postImg
            LambdaQueryWrapper<PostImg> postImgLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postImgLambdaQueryWrapper.eq(PostImg::getPostId, postId);
            postImgMapper.delete(postImgLambdaQueryWrapper);
            //1.4??????reporting
            LambdaQueryWrapper<Reporting> reportingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reportingLambdaQueryWrapper.eq(Reporting::getPostId, postId);
            reportingMapper.delete(reportingLambdaQueryWrapper);
        }
        return postMapper.delete(queryWrapper) == 1;
    }

    private void setOfficialPostCache() {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("post_id");
        queryWrapper.eq("partition_id", 3);
        queryWrapper.orderByDesc("post_create_time");
        IPage<Post> page = new Page<>(1, 5);
        postMapper.selectPage(page, queryWrapper);
        List<Post> posts = page.getRecords();
        List<PostImg> postImgList = new ArrayList<>();
        for (Post post : posts) {
            QueryWrapper<PostImg> postImgQueryWrapper = new QueryWrapper<>();
            postImgQueryWrapper.eq("post_id", post.getPostId());
            IPage<PostImg> postImgIPage = new Page<>(1, 1);
            postImgMapper.selectPage(postImgIPage, postImgQueryWrapper);
            PostImg postImg = postImgIPage.getRecords().get(0);
            postImgList.add(postImg);
        }
        officialPostCache.put("global", postImgList);
    }

}
