package com.exam.Entity;

import lombok.Data;

@Data
public class Exam {
    private String examCode;
    private String description;
    private String source;
    private String paperId;
    private String examDate;
    private String totalTime;
    private String grade;
    private String term;
    private String major;
    private String institute;
    private String totalScore;
    private String type;
    private String tips;

    public String getExamId() {
        return examCode;
    }
}
