package com.exam.service;
import com.exam.Entity.Reward;

import java.util.List;

public interface RewardService {

    List<Reward> selectAll();

    Reward selectById(String rewardsID);

    int update(String rewardsID,Reward reward);

    int insert(Reward reward);

    int deleteById(String rewardsID);
}
