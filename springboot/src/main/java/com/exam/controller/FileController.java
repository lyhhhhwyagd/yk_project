package com.exam.controller;
import com.exam.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.util.List;
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
