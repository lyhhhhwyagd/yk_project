package com.exam.service;

import com.exam.Entity.Question;
import com.exam.util.ResponseVO;

public interface QuestionService {

    ResponseVO addQuestion(Integer paperId);

    //ResponseVO updateQuestion(QuestionVO questionVO);
    ResponseVO updateQuestion(Question question);


    ResponseVO deleteQuestion(Integer questionId);
}
