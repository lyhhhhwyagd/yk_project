package com.exam.util;

public class nullOrNot {
    public static void istrue(Boolean flag,String msg){
        if(flag){
         throw new ParamsException(msg);
        }
    }
}
