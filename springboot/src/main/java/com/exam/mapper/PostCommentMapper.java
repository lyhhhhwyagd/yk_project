package com.exam.mapper;

import com.exam.Entity.PostComment;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostCommentMapper {
    //update/insert/delete默认返回语句影响的行数的一个整数。
    @Select("select * from post_comment")
    List<PostComment> selectAll();

    @Select("select * from post_comment where commentID = #{commentID}")
    PostComment selectById(String postID);

    @Update("update post_comment set " +
            "content = #{postComment.content}, updateTime = #{postComment.updateTime}, " +
            "status = #{postComment.status} " +
            "where commentID = #{commentID}")
    int update(String commentID,PostComment postComment);

    @Options(useGeneratedKeys = true,keyProperty = "commentID")
    @Insert("insert into post_comment" +
            "(commentID, authorID, postID, postAuthorID, content, postTime, updateTime, status) " +
            "values(#{commentID}, #{authorID}, #{postID}, #{postAuthorID}, #{content}" +
            ", #{postTime}, #{updateTime}, #{status})")
    int insert(PostComment postComment);

    @Delete("delete from post_comment where commentID = #{commentID}")
    int deleteById(String commentID);
}
