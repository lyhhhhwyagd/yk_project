package com.exam.controller;
import com.exam.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.exam.Entity.File;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 @CrossOrigin(origins = "*")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam Integer courseId, @RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(courseId, file);
    }
    @GetMapping("/download")
    public String downLoad(HttpServletResponse response, @RequestParam("fileName") String fileName) {
        return fileService.fileDownLoad(response, fileName);
    }
    @GetMapping("/all")
    public List<com.exam.Entity.File> getAllFiles() {
        return fileService.getAllFiles();
    }
    @GetMapping("/countByCourseId")
public Map<String, Object> countFilesByCourseId() {
    // 查询xm04_file表中的course_id字段
    List<com.exam.Entity.File> fileList = fileService.getAllFiles();

    // 统计不同course_id的文件数量
    Map<Integer, Integer> courseFileCount = new HashMap<>();
    for (com.exam.Entity.File file : fileList) {
        Integer courseId = file.getCourseId();
        courseFileCount.put(courseId, courseFileCount.getOrDefault(courseId, 0) + 1);
    }

    // 将统计结果转换为JSON格式并返回
    Map<String, Object> result = new HashMap<>();
    result.put("courseFileCount", courseFileCount);
    return result;
}

     @GetMapping("/list")
    public ResponseEntity<List<String>> getFileList() {
        List<String> fileList = fileService.getFileList();
        return new ResponseEntity<>(fileList, HttpStatus.OK);
    }
    @GetMapping("/downloadFiles")
    public ResponseEntity<byte[]> downloadUserExcel() {
        byte[] excelBytes = fileService.downloadFile();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "files.xlsx");  // Specify the filename

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
}
