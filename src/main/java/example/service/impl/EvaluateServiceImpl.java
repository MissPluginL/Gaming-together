package example.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import example.dto.EvaluateCreateDTO;
import example.entity.Evaluate;
import example.mapper.EvaluateMapper;
import example.service.EvaluateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Resource
    private EvaluateMapper evaluateMapper;

    @Override
    public void add(Long userId, EvaluateCreateDTO dto) {
        Evaluate e = new Evaluate();
        e.setUserId(userId);
        evaluateMapper.insert(e);
    }

    @Override
    public IPage<?> myEvaluate(Long userId) { return null; }

    @Override
    public IPage<?> playerEvaluate(Long userId) { return null; }

    @Override
    public IPage<?> publicEvaluateList(Long playerId) { return null; }
}