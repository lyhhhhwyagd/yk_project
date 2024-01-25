package com.exam.service;
// HomeworkService.java
import com.baomidou.mybatisplus.extension.service.IService;
import com.exam.Entity.Homework;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface HomeworkService extends IService<Homework> {

    List<Homework> getAllHomeworks();

    Homework getHomeworkById(Long id);

    void addHomework(Homework homework);

    void updateHomework(Long id, Homework homework);

    void deleteHomework(Long id);

    void submitHomework(Long id);

    void cancelSubmitHomework(Long id);
   
    String uploadFile(Long id, MultipartFile file);
        
    byte[] downloadHomework();

  

}
