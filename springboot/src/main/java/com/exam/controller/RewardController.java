package com.exam.controller;


import com.exam.Entity.Reward;
import com.exam.Entity.ApiResult;
import com.exam.service.RewardService;
import com.exam.util.ApiResultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class RewardController {

    private final RewardService rewardService;
    @Autowired
    public RewardController(RewardService rewardService){
        this.rewardService = rewardService;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/reward")
    public ApiResult selectAll(){
        System.out.println("查询全部悬赏信息");
        return ApiResultHandler.success(rewardService.selectAll());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/reward/{RewardsId}")
    public ApiResult selectById(@PathVariable("RewardsId") String rewardsID){
        System.out.println("根据ID查询悬赏信息");
        return ApiResultHandler.success(rewardService.selectById(rewardsID));
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/reward/{RewardsId}")
    public ApiResult update(@PathVariable("RewardsId") String rewardsID, @RequestBody Reward reward){
        System.out.println("更新指定ID的悬赏信息");
        System.out.println(reward.getReceivedTime());
        return ApiResultHandler.success(rewardService.update(rewardsID,reward));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/reward")
    public ApiResult insert(@RequestBody Reward reward){
        System.out.println("添加悬赏");
        return ApiResultHandler.success(rewardService.insert(reward));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/reward/{RewardsId}")
    public ApiResult deleteById(@PathVariable("RewardsId") String rewardsID){
        System.out.println("删除悬赏");
        return ApiResultHandler.success(rewardService.deleteById(rewardsID));
    }
}
