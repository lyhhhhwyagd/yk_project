package com.exam.mapper;

import com.exam.Entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    //update/insert/delete默认返回语句影响的行数的一个整数。
    @Select("select * from xm04_post")
    List<Post> selectAll();

    @Select("select * from xm04_post where postID = #{postID}")
    Post selectById(String postID);

    @Update("update xm04_post set " +
            "title = #{post.title}, content = #{post.content}, updateTime = #{post.updateTime}, " +
            "status = #{post.status} " +
            "where postID = #{postID}")
    int update(String postID,Post post);

    @Options(useGeneratedKeys = true,keyProperty = "PostsID")
    @Insert("insert into xm04_post" +
            "(postID, title, content, postTime, updateTime, commentsCount, authorID, authorName, status) " +
            "values(#{postID}, #{title}, #{content}, #{postTime}, #{updateTime}" +
            ", #{commentsCount}, #{authorID}, #{authorName}, #{status})")
    int insert(Post post);

    @Delete("delete from xm04_post where postID = #{postID}")
    int deleteById(String postID);
}
