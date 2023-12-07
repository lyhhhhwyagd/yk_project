package com.exam.Entity;  // 注意包名的命名规范

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileId;

    private String fileUrl;

    private int courseId;  // 假设 courseId 是一个整数

    // 必须要有一个无参构造函数
    public File() {
    }

    // 其他构造函数和 getter/setter 方法

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public File(String fileUrl, int courseId) {
        this.fileUrl = fileUrl;
        this.courseId = courseId;
    }

}
