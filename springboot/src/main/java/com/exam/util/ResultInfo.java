package com.exam.util;

import lombok.Data;

@Data
public class ResultInfo {
    private  Integer code=200;
    private  String msg="success";
    private  Object result;
}
