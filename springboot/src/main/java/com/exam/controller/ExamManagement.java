package com.exam.controller;

import com.exam.Entity.Exam;
import com.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamManagement {
    @Autowired
    private ExamService examService;
    @CrossOrigin(origins = "*")
    @GetMapping("/find_all")
    public List<Exam> checkRoll() {
        return examService.examList();
    }
}
