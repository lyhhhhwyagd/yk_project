package com.exam.service;

import com.exam.Entity.Answer;
import com.exam.util.ResponseVO;

import java.util.List;

public interface AnswerService {
    ResponseVO addAnswers(List<Answer> answerList);
}
