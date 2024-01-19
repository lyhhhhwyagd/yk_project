package com.exam.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.exam.Entity.User;
import com.exam.mapper.FileMapper;
import com.exam.util.ExportExcelUtils;
import com.mysql.jdbc.Statement;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileMapper fileMapper;
    public List<com.exam.Entity.File> getAllFiles(){//数据库查询
        return fileMapper.selectList(new QueryWrapper<>());
    }
    public String uploadFile(Integer courseId, MultipartFile file) {
        try {
            // 指定保存文件的目录
            String uploadDir = "../static/files";
            String originalFileName = file.getOriginalFilename();
            long fileSize = file.getSize();
            java.sql.Timestamp uploadTime = new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
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

            // 在实际应用中，你可能需要将文件路径或相关信息保存到数据库
            saveFileToDatabase(filePath, originalFileName, fileSize, uploadTime, courseId);

            return "文件上传成功：" + filePath.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败：" + e.getMessage();
        }
    }
    public List<String> getFileList() {//文件夹查询
        String uploadDir = "../static/files";
        File uploadFolder = new File(uploadDir);

        if (uploadFolder.exists()) {
            return Arrays.stream(uploadFolder.listFiles())
                    .map(File::getName)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();}
        }
        
    private void saveFileToDatabase(Path fileUrl, String fileName, long fileSize, java.sql.Timestamp uploadTime, Integer courseId) {
        // JDBC连接配置
        String jdbcUrl = "jdbc:mysql://43.139.101.228:3306/projects_yjykfsj2023";
        String dbUsername = "public";
        String dbPassword = "12345678";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            // 插入文件信息到数据库的 'file' 表
            String sql = "INSERT INTO file (file_name, file_size, upload_time, course_id) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // 设置预处理语句的参数
                preparedStatement.setString(1, fileName); // 设置文件名
                preparedStatement.setLong(2, fileSize); // 设置文件大小
                preparedStatement.setTimestamp(3, uploadTime); // 设置上传时间
                preparedStatement.setInt(4, courseId); // 设置课程ID

                // 执行插入语句
                preparedStatement.executeUpdate();

                // 获取自动生成的主键（文件ID）
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int fileId = generatedKeys.getInt(1);
                        System.out.println("文件ID：" + fileId);
                    } else {
                        throw new SQLException("获取文件ID失败，没有生成的主键！");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 处理数据库操作异常
        }
    }
    

    public String fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        String downloadFilePath = "../static/files";
        File file = new File(downloadFilePath +'/'+ fileName);
        if(!file.exists()){
            return "下载文件不存在";
        }
    
        // 打印文件的相对路径
        System.out.println("文件下载路径: " + file.getPath());
    
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );
    
        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return "下载失败";
        }
    
        return "下载成功";
    }
    public byte[] downloadFile() {
        List<com.exam.Entity.File> files = getAllFiles();
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("id", "文件ID");
        fieldMap.put("fileName", "文件名");
        fieldMap.put("fileSize","文件大小");
        fieldMap.put("uploadTime","上传时间");
        fieldMap.put("courseId","课序号");
        
        return ExportExcelUtils.export("文件列表", files, fieldMap); // 在出现异常时返回一个空数组
    }
}
