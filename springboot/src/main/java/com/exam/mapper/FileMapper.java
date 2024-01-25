package com.exam.mapper;
import com.exam.Entity.File;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
@Repository
public interface FileMapper extends BaseMapper<File> {
    @Select("SELECT * FROM xm04_file")
    List<File> selectAllFiles();
}
