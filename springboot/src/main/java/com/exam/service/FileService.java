package com.exam.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Service
public class FileService {

    public String uploadFile(Integer courseId, MultipartFile file) {
        try {
            // 指定保存文件的目录
            String uploadDir = "C:\\Users\\19463\\Desktop\\files";

            // 确保目录存在，如果不存在则创建
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 获取原始文件名
            String originalFileName = file.getOriginalFilename();

            // 构建保存文件的路径
            assert originalFileName != null;
            Path filePath = uploadPath.resolve(originalFileName);

            // 将文件保存到指定路径
            Files.write(filePath, file.getBytes());

            // 在实际应用中，你可能需要将文件路径或相关信息保存到数据库
            saveFileToDatabase(filePath, courseId);

            return "文件上传成功：" + filePath.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "文件上传失败：" + e.getMessage();
        }
    }

    private void saveFileToDatabase(Path file_url, Integer course_id) {
        // JDBC连接配置
        String jdbcUrl = "jdbc:mysql://localhost:3306/exam";
        String dbUsername = "root";
        String dbPassword = "123";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword)) {
            // 插入文件信息到数据库的 'file' 表
            String sql = "INSERT INTO file (file_url, course_id) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // 设置预处理语句的参数
                preparedStatement.setString(1, file_url.toString()); // 设置文件路径
                preparedStatement.setInt(2, course_id); // 设置课程ID

                // 执行插入语句
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 处理数据库操作异常
        }
    }


    public String fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        String downloadFilePath= "C:\\Users\\19463\\Desktop\\files";
        File file = new File(downloadFilePath +'/'+ fileName);
        if(!file.exists()){
            return "下载文件不存在";
        }
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
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
}
