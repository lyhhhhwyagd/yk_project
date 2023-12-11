package com.exam.serviceImplementation;

import com.exam.Entity.Reward;
import com.exam.mapper.RewardMapper;
import com.exam.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardServiceImplementation implements RewardService{

    private final RewardMapper rewardMapper;
    @Autowired
    public RewardServiceImplementation(RewardMapper rewardMapper){
        this.rewardMapper = rewardMapper;
    }

    public List<Reward> selectAll(){
        System.out.println("正在执行查询");
        return rewardMapper.selectAll();
    }

    public Reward selectById(String rewardsID){
        System.out.println("正在执行查询"+rewardsID);
        return rewardMapper.selectById(rewardsID);
    }

    public int update(String rewardsID,Reward reward){
        System.out.println("正在执行修改");
        return rewardMapper.update(rewardsID,reward);
    }

    public int insert(Reward reward){
        return rewardMapper.insert(reward);
    }

    public int deleteById(String rewardsID){
        return rewardMapper.deleteById(rewardsID);
    }

}
