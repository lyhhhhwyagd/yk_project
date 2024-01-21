package com.exam.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.exam.Entity.Homework;
import com.exam.service.HomeworkService;

import java.io.File;
import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private HomeworkService homeworkService;

    @GetMapping("/all")

    public List<Homework> getAllHomeworks() {
        return homeworkService.list();
    }

    @GetMapping("/{id}")
    public Homework getHomeworkById(@PathVariable Long id) {
        return homeworkService.getById(id);
    }
    @GetMapping("/downloadHomework")
    public ResponseEntity<byte[]> downloadUserExcel() {
        byte[] excelBytes = homeworkService.downloadHomework();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "files.xlsx");  // Specify the filename

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
    @PostMapping("/add")
public ResponseEntity<String> addHomework(@RequestBody Homework homework) {
    // 保存作业信息
    homeworkService.save(homework);

    // 生成作业文件夹路径
    String folderPath = "../static/homework/" + homework.getTitle();

    // 创建作业文件夹
    File folder = new File(folderPath);
    if (!folder.exists()) {
        folder.mkdirs();
    }

    return ResponseEntity.ok("Homework added successfully.");
}

    @PostMapping("/upload/{id}")
public ResponseEntity<String> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
    try {
        homeworkService.uploadFile(id, file);
        return ResponseEntity.ok("File uploaded successfully.");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
    }
}

    @PutMapping("/{id}")
    public void updateHomework(@PathVariable Long id, @RequestBody Homework homework) {
        homework.setId(id);
        homeworkService.updateById(homework);
    }

    @DeleteMapping("/{id}")
    public void deleteHomework(@PathVariable Long id) {
        homeworkService.removeById(id);
    }
}