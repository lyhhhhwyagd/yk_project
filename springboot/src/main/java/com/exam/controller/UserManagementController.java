package com.exam.controller;

import com.exam.Entity.User;
import com.exam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
 @CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserManagementController {
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public List<User> userManagement(){return userService.userList();}
    @PostMapping("/add")
    public int add(@RequestBody User user){
        userService.userAdd(user);
        return 0;
    }
    @DeleteMapping("/delete")
    public int delete(@RequestBody User user){
        userService.userDelete(user.getUserId());
        return 0;
    }
    @PostMapping("/update")
    public int update(@RequestBody User user){
        userService.userUpdate(user);
        return 0;
    }
    @GetMapping("/downloadUser")
    public ResponseEntity<byte[]> downloadUserExcel() {
        byte[] excelBytes = userService.downloadUser();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "users.xlsx");  // Specify the filename

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }
    @GetMapping("/userType/{id}")
    public ResponseEntity<String> getUserTypeById(@PathVariable Integer id) {
        String userType = userService.findUserTypeById(id);
        if (userType != null) {
            return new ResponseEntity<>(userType, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/countUserType")
    public Map<String, Object> countUserType() {
        // 查询user_zpq表中的user_type字段
        List<User> userList = userService.userList();

        // 统计不同权限的人数
        Map<String, Integer> userTypeCount = new HashMap<>();
        for (User userZpq : userList) {
            String userType = userZpq.getUserType();
            userTypeCount.put(userType, userTypeCount.getOrDefault(userType, 0) + 1);
        }

        // 将统计结果转换为JSON格式并返回
        Map<String, Object> result = new HashMap<>();
        result.put("userTypeCount", userTypeCount);
        return result;
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

