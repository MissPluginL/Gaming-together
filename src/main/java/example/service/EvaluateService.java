package example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import example.dto.EvaluateCreateDTO;

public interface EvaluateService {
    void add(Long userId, EvaluateCreateDTO dto);
    IPage<?> myEvaluate(Long userId);
    IPage<?> playerEvaluate(Long userId);
    IPage<?> publicEvaluateList(Long playerId);
}