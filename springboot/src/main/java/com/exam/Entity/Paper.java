package com.exam.Entity;

import com.exam.util.PaperStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Paper {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String startTime;
    private String endTime;
    private PaperStatus status;
    //private List<QuestionStatistic> questionStatistics;
    private List<Question> questionStatistics;
    //private List<QuestionVO> questionList;    //带option的
    private List<Question> question1List;

}