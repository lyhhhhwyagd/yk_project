package com.exam.util;

import com.exam.Entity.ApiResult;

public class ApiResultHandler {
    public static ApiResult success(Object object) {
        if(object != null) {
            ApiResult apiResult = new ApiResult();
            apiResult.setData(object);
            apiResult.setCode(200);
            apiResult.setMessage("请求成功");
            return apiResult;
        } else {
            ApiResult apiResult = new ApiResult();
            apiResult.setData(null);
            apiResult.setCode(404);
            apiResult.setMessage("返回对象为空");
            return apiResult;
        }
    }

    public static ApiResult success() {
        return success(null);
    }

    public static <T> ApiResult buildApiRes(Integer code, String message, T data) {
        ApiResult apiResult = new ApiResult();
        apiResult.setCode(code);
        apiResult.setMessage(message);
        apiResult.setData(data);
        return apiResult;
    }
}
