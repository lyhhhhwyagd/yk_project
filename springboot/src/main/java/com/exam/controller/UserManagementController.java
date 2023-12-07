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

import java.util.List;

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

