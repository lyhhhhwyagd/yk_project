package com.exam.mapper;

import com.exam.Entity.Reward;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RewardMapper {
    //update/insert/delete默认返回语句影响的行数的一个整数。
    @Select("select * from xm04_rewards")
    List<Reward> selectAll();

    @Select("select * from xm04_rewards where RewardsID = #{rewardsID}")
    Reward selectById(String rewardsID);

    @Update("update xm04_rewards set " +
            "Title = #{reward.title}, Description = #{reward.description}, RewardAmount = #{reward.rewardAmount}, ReceivedTime = #{reward.receivedTime}, " +
            "Status = #{reward.status}, DeadLine = #{reward.deadLine}, ReceiverID = #{reward.receiverID}, ReceiverName = #{reward.receiverName} " +
            "where RewardsID = #{rewardsID}")
    int update(@Param("rewardsID")String rewardsID,@Param("reward")Reward reward);

    @Options(useGeneratedKeys = true,keyProperty = "RewardsID")
    @Insert("insert into xm04_rewards" +
            "(RewardsID, Title, Description, PostedByUserID, PostedByUserName, RewardAmount, Status, PostedTime, DeadLine, ReceiverID, ReceiverName) " +
            "values(#{rewardsID}, #{title}, #{description}, #{postedByUserID}, #{PostedByUserName}" +
            ", #{rewardAmount}, #{status}, #{postedTime}, #{deadLine}, #{receiverID}, #{receiverName})")
    int insert(Reward reward);

    @Delete("delete from xm04_rewards where RewardsID = #{rewardsID}")
    int deleteById(String RewardsID);
}
