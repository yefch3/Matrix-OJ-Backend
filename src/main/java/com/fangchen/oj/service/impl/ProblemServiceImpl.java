package com.fangchen.oj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fangchen.oj.model.entity.Problem;
import com.fangchen.oj.service.ProblemService;
import generator.mapper.ProblemMapper;
import org.springframework.stereotype.Service;

/**
* @author fangchen.ye
* @description 针对表【problem(题目表)】的数据库操作Service实现
* @createDate 2024-03-26 17:45:04
*/
@Service
public class ProblemServiceImpl extends ServiceImpl<ProblemMapper, Problem> implements ProblemService {

}




