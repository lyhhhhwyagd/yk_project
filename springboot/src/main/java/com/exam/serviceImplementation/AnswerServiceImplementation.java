package com.exam.serviceImplementation;

import com.exam.Entity.Answer;
import com.exam.Entity.Paper;
import com.exam.util.PaperStatus;
import com.exam.mapper.AnswerMapper;
import com.exam.mapper.PaperMapper;
import com.exam.service.AnswerService;
import com.exam.util.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImplementation implements AnswerService {
    private final static String EARLY="问卷未开始发放";
    private final static String INVALIDATION="问卷已失效";
    @Autowired
    AnswerMapper answerMapper;
    @Autowired
    PaperMapper paperMapper;

    @Override

    public ResponseVO addAnswers(List<Answer> answerList) {
        try {
            int paperId= answerList.get(0).getPaperId();
            Paper paper =paperMapper.selectByPaperId(paperId);
            if(paper.getStartTime()!=null && paper.getEndTime()!=null){
                if(paper.getStatus()== PaperStatus.INIT)
                    return ResponseVO.buildFailure(EARLY);
                if(paper.getStatus()== PaperStatus.STOP)
                    return ResponseVO.buildFailure(INVALIDATION);
            }
            for(Answer answer : answerList)
                answerMapper.addAnswer(answer);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }


    /*
    public ResponseVO addAnswers(List<Answer> answerList) {
        try {
            int paperId=answerList.get(0).getPaperId();
            Paper paperVO=paperMapper.selectByPaperId(paperId);
            if(paperVO.getStartTime()!=null && paperVO.getEndTime()!=null){
                if(paperVO.getStatus()== PaperStatus.INIT)
                    return ResponseVO.buildFailure(EARLY);
                if(paperVO.getStatus()== PaperStatus.STOP)
                    return ResponseVO.buildFailure(INVALIDATION);
            }
            for(Answer answer:answerList)
                answerMapper.addAnswer(answer);
            return ResponseVO.buildSuccess();
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }
    *
     */
}
