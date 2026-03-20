package example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dto.PostPublishDTO;
import example.entity.Post;
import example.mapper.PostMapper;
import example.service.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Override
    public IPage<?> getPostList(Integer page, Integer size, String game) {
        return postMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public Object getDetail(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public void publish(Long userId, PostPublishDTO dto) {
        Post post = new Post();
        post.setUserId(userId);
        postMapper.insert(post);
    }

    @Override
    public IPage<?> myPost(Long userId) {
        return null;
    }

    @Override
    public void delete(Long userId, Long id) {}
}