package com.exam.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Options {    //这里叫options是因为option是mysql保留关键字，没办法，很烦，看着很难受
    private Integer questionId;
    private Integer sequence;
    private String content;
    private Integer selectedNum;     //多少人此问题选了这个选项

}

