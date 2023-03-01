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
    private final UserMapper userMapper;

    @CreateCache(name = "officialPostCache_", expire = 1, timeUnit = TimeUnit.DAYS)
    private Cache<String, List<PostImg>> officialPostCache;

    @Autowired
    public PostServiceImpl(PostMapper postMapper, PostImgMapper postImgMapper, CommentMapper commentMapper, ActionMapper actionMapper, ReportingMapper reportingMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.postImgMapper = postImgMapper;
        this.commentMapper = commentMapper;
        this.actionMapper = actionMapper;
        this.reportingMapper = reportingMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Boolean releasePost(Post post) {
        post.setPostCollectionsNum(0);
        post.setPostCommentsNum(0);
        post.setPostLikesNum(0);
        post.setPostLooksNum(0);
        post.setPostCreateTime(LocalDateTime.now());
        post.setPostEditTime(post.getPostCreateTime());
        post.setPostState(0);
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
        // 图片存储路径
        String path = STATIC_BASE_URL + "img/postImg";
        // 判断是否有路径
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
        postQueryWrapper.eq("post_state", 1);
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
        queryWrapper.eq(Post::getPostState, 1);
        if (postMapper.selectList(queryWrapper).size() == 1) {
            //查询到属于一登陆用户的帖子，可以删除
            //删除相关的action、comment、comment相关的action、postImg、reporting
            //1.删除comment及其action
            //1.1查询所有评论
            LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentLambdaQueryWrapper.eq(Comment::getPostId, postId);
            List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
            if (comments != null && comments.size() > 0) {
                //1.2删除其相关action和reporting
                for (Comment comment : comments) {
                    LambdaQueryWrapper<Action> actionLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    actionLambdaQueryWrapper.eq(Action::getCommentId, comment.getCommentId());
                    actionMapper.delete(actionLambdaQueryWrapper);
                    LambdaQueryWrapper<Reporting> reportingLambdaQueryWrapper = new LambdaQueryWrapper<>();
                    reportingLambdaQueryWrapper.eq(Reporting::getCommentId, comment.getCommentId());
                    reportingMapper.delete(reportingLambdaQueryWrapper);
                }
                //1.3删除具有父级且父级不为根级的comment
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
                //1.4删除具有父级的comment
                for (Comment comment : comments) {
                    if (postId.equals(comment.getPostId())
                            && comment.getCommentParentId() != null
                            && !"".equals(comment.getCommentParentId())) {
                        if (commentMapper.deleteById(comment) == 1) {
                            comments.remove(comment);
                        }
                    }
                }
                //1.5删除剩余的comment
                for (Comment comment : comments) {
                    if (commentMapper.deleteById(comment) == 1) {
                        comments.remove(comment);
                    }
                }
            }
            //1.2删除action
            LambdaQueryWrapper<Action> actionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            actionLambdaQueryWrapper.eq(Action::getPostId, postId);
            actionMapper.delete(actionLambdaQueryWrapper);
            //1.3删除postImg
            LambdaQueryWrapper<PostImg> postImgLambdaQueryWrapper = new LambdaQueryWrapper<>();
            postImgLambdaQueryWrapper.eq(PostImg::getPostId, postId);
            postImgMapper.delete(postImgLambdaQueryWrapper);
            //1.4删除reporting
            LambdaQueryWrapper<Reporting> reportingLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reportingLambdaQueryWrapper.eq(Reporting::getPostId, postId);
            reportingMapper.delete(reportingLambdaQueryWrapper);
        }
        return postMapper.delete(queryWrapper) == 1;
    }

    @Override
    public IPage<Post> getReviewPostPage(IPage<Post> page) {
        LambdaQueryWrapper<Post> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Post::getPostState, 0);
        postMapper.selectPage(page, queryWrapper);
        page.getRecords().stream().forEach((post) -> {
            post.setUserName(userMapper.selectById(post.getUserId()).getUserName());
        });
        return page;
    }

    @Override
    public Boolean passPost(Integer postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Post::getPostState, 1);
        updateWrapper.eq(Post::getPostId,postId);
        return postMapper.update(null, updateWrapper) == 1;
    }

    @Override
    public Boolean unPassPost(Integer postId) {
        LambdaUpdateWrapper<Post> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Post::getPostState, 2);
        updateWrapper.eq(Post::getPostId,postId);
        return postMapper.update(null, updateWrapper) == 1;
    }

    private void setOfficialPostCache() {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("post_id");
        queryWrapper.eq("partition_id", 3);
        queryWrapper.eq("post_state", 1);
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
