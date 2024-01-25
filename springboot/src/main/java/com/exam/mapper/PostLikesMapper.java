package com.exam.mapper;

import com.exam.Entity.PostLikes;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostLikesMapper {
    //update/insert/delete默认返回语句影响的行数的一个整数。
    @Select("select * from xm04_post_likes")
    List<PostLikes> selectAll();

    @Options(useGeneratedKeys = true,keyProperty = "commentID")
    @Insert("insert into xm04_post_likes" +
            "(id, authorID, postID, createTime) " +
            "values(#{ID}, #{authorID}, #{postID}, #{createTime})")
    int insert(PostLikes postLikes);

    @Delete("delete from xm04_post_likes where authorID = #{authorID} and postID= #{postID}")
    int deleteByAuthorIDAndPostID(String authorID,String postID);
}
