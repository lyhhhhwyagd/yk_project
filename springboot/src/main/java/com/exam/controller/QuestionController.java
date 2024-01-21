package com.exam.controller;

import com.exam.Entity.Question;
import com.exam.service.QuestionService;
import com.exam.util.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/question")
public class QuestionController {

    public final QuestionService questionService;
    @Autowired
    public QuestionController(QuestionService questionService){
        this.questionService = questionService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{paperId}/addQuestion")
    public ResponseVO addQuestion(@PathVariable Integer paperId){
        return questionService.addQuestion(paperId);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updateQuestion")
    //public ResponseVO updateQuestion(@RequestBody QuestionVO questionVO){
     //   return questionService.updateQuestion(questionVO);
    //}
    public ResponseVO updateQuestion(@RequestBody Question question){
        return questionService.updateQuestion(question);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{questionId}/deleteQuestion")
    public ResponseVO deleteQuestion(@PathVariable Integer questionId){
        return questionService.deleteQuestion(questionId);
    }
}
