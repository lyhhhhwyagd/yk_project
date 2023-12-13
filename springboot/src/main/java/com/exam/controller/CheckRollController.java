package com.exam.controller;

import com.exam.Entity.CheckRoll;
import com.exam.service.CheckRollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/checkroll")
public class CheckRollController {
    @Autowired
    private CheckRollService checkRollService;
    @CrossOrigin(origins = "*")
    @GetMapping("/find_all")
    public List<CheckRoll> checkRoll() {
        return checkRollService.checkRollList();
    }
}
