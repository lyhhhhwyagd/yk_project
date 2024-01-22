package com.exam.controller;

import com.exam.Entity.Answer;
import com.exam.service.AnswerService;
import com.exam.util.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;
    @Autowired
    public AnswerController(AnswerService answerService){
        this.answerService = answerService;
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/addAnswers")
    public ResponseVO addAnswers(@RequestBody List<Answer> answerList){
        return answerService.addAnswers(answerList);
    }
    //public ResponseVO addAnswers(@RequestBody List<Answer> answerList){
     //   return answerService.addAnswers(answerList);
    //}
}
