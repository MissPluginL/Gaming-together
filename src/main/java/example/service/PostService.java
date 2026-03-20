package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import example.dto.PostPublishDTO;

public interface PostService {
    IPage<?> getPostList(Integer page, Integer size, String game);
    Object getDetail(Long id);
    void publish(Long userId, PostPublishDTO dto);
    IPage<?> myPost(Long userId);
    void delete(Long userId, Long id);
}