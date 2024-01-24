package com.exam.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.exam.Entity.Activity;
import com.exam.Entity.ActivityS;
import com.exam.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
@Service
public class ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    public List<Activity> activityList(){
        System.out.println("开始查询");
        return activityMapper.selectList();
    }

    public List<ActivityS> activityStatistic(){
        return activityMapper.statistic();
    }
    public void activityAdd(Activity activity){//添加
        activityMapper.insert(activity);
    }
    public void activityDelete(String id){//删除by ID
        activityMapper.deleteById(id);
    }

    public void updateById(Activity activity) {
        activityMapper.updateById(activity);
    }
}



