package com.exam.service;
import com.exam.Entity.LoginIn;

import java.util.List;

public interface LoginInService {
    LoginIn select(LoginIn loginIn);
    LoginIn selectByID(String userID);
}
