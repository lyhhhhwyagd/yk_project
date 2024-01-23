package com.exam.controller;

import com.exam.Entity.Activity;
import com.exam.Entity.ActivityS;
import com.exam.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @CrossOrigin(origins = "*")
    @GetMapping("/find_all")
    public List<Activity> checkRoll() {
        return activityService.activityList();
    }

    @GetMapping("/statistic")
    public List<ActivityS> statistic(){
        return activityService.activityStatistic();
    }

    @PostMapping("/add")
    public int add(@RequestBody Activity activity){
        activityService.activityAdd(activity);
        return 0;
    }

    @DeleteMapping("/delete/{ActivityID}")
    public int delete(@PathVariable("ActivityID") String activityID){
        activityService.activityDelete(activityID);
        return 0;
    }

    @PostMapping("/update")
    public int update(@RequestBody Activity activity){
        activityService.updateById(activity);
        return 0;
    }


}
