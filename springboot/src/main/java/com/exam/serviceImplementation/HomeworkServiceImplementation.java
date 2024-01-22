package com.exam.serviceImplementation;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exam.Entity.Homework;
import com.exam.mapper.HomeworkMapper;
import com.exam.service.HomeworkService;
import com.exam.util.ExportExcelUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class HomeworkServiceImplementation extends ServiceImpl<HomeworkMapper, Homework> implements HomeworkService {
    
    @Override
    public byte[] downloadHomework() {
        List<Homework> homeworkList = getAllHomeworks();
        
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("id", "作业ID");
        fieldMap.put("title", "作业标题");
        fieldMap.put("description", "作业描述");
        fieldMap.put("deadline", "截止时间");
        fieldMap.put("teacher", "授课教师");
        fieldMap.put("ifSubmit", "是否提交");
        // 其他属性...

        return ExportExcelUtils.export("homework", homeworkList, fieldMap);
    }
    @Override
    public List<Homework> getAllHomeworks() {
        return list();
    }

    @Override
    public Homework getHomeworkById(Long id) {
        return getById(id);
    }

    @Override
    @Transactional
    public void addHomework(Homework homework) {
        save(homework);
    }

    @Override
    @Transactional
    public void updateHomework(Long id, Homework homework) {
        homework.setId(id);
        updateById(homework);
    }

    @Override
    @Transactional
    public void deleteHomework(Long id) {
        removeById(id);
    }

    @Override
    @Transactional
    public void submitHomework(Long id) {
        Homework homework = getById(id);
        if (homework != null && !homework.getIfSubmit()) {
            homework.setIfSubmit(true);
            updateById(homework);
        }
    }

    @Override
    @Transactional
    public void cancelSubmitHomework(Long id) {
        Homework homework = getById(id);
        if (homework != null && homework.getIfSubmit()) {
            homework.setIfSubmit(false);
            updateById(homework);
        }
    }
    @Override
    public String uploadFile(Long id, MultipartFile file) {
        try {
            // 获取作业信息
            Homework homework = getById(id);

            // 检查作业信息是否存在
            if (homework == null) {
                throw new RuntimeException("Homework not found.");
            }

            // 指定保存文件的目录
            String uploadDir = "../static/homework/" + homework.getTitle();
            String originalFileName = file.getOriginalFilename();
            
            

            // 确保目录存在，如果不存在则创建
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 构建保存文件的路径
            assert originalFileName != null;
            Path filePath = uploadPath.resolve(originalFileName);

            // 将文件保存到指定路径
            Files.write(filePath, file.getBytes());

            

            // 更新作业信息
            homework.setIfSubmit(true);
            updateById(homework);

            return "文件上传成功：" + filePath.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败：" + e.getMessage();
        }
    }
}
