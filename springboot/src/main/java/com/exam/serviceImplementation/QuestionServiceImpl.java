package com.exam.serviceImplementation;

import com.exam.mapper.OptionsMapper;
import com.exam.mapper.QuestionMapper;
import com.exam.Entity.Options;
import com.exam.Entity.Question;
import com.exam.service.QuestionService;
import com.exam.util.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class QuestionServiceImpl implements QuestionService {
    private final static String INVALIDATION="无效ID";
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    OptionsMapper optionsMapper;

    @Override
    public ResponseVO addQuestion(Integer paperId) {
        try {     //前端的逻辑是 添加问题时啥也没有是空的 只返回questionId 真正填写完了调updateQuestion
            Question question=new Question();
            question.setPaperId(paperId);
            questionMapper.addQuestion(question);
            return ResponseVO.buildSuccess(question.getId());
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO updateQuestion(Question question1) {
        try {
            if(questionMapper.selectByQuestionId(question1.getId())==null)
                return ResponseVO.buildFailure(INVALIDATION);
            Question question=new Question();
            BeanUtils.copyProperties(question1,question);
            questionMapper.updateQuestion(question);
            for(Options options:question1.getOptions()){
                Options Options=new Options();
                BeanUtils.copyProperties(options,Options);
                if(optionsMapper.findOption(Options)==null)
                    optionsMapper.addOption(Options);
                else
                    optionsMapper.updateOption(Options);
            }

            return ResponseVO.buildSuccess();
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO deleteQuestion(Integer questionId) {
        try {
            if(questionMapper.selectByQuestionId(questionId)==null)
                return ResponseVO.buildFailure(INVALIDATION);
            questionMapper.deleteQuestion(questionId);
            optionsMapper.deleteByQuestionId(questionId);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }
}
