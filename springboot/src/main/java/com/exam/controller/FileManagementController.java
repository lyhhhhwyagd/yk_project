package com.exam.controller;
import com.exam.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/file")
public class FileManagementController {

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
}
