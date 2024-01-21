package com.exam.controller;

import com.exam.Entity.Paper;
import com.exam.service.PaperService;
import com.exam.util.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paper")
public class PaperController {

    private final PaperService paperService;
    @Autowired
    public PaperController(PaperService paperService){
        this.paperService = paperService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addPaper")
    public ResponseVO addPaper(@RequestBody Paper paper){
        return paperService.addPaper(paper);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/updatePaper")
    public ResponseVO updatePaper(@RequestBody Paper paper){
        return paperService.updatePaper(paper);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{paperId}/deletePaper")
    public ResponseVO invalidatePaper(@PathVariable Integer paperId){
        return paperService.deletePaper(paperId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{userId}/getUserPapers")
    public ResponseVO getUserPapers(@PathVariable Integer userId){
        return paperService.getUserPapers(userId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{paperId}/checkPaper")
    public ResponseVO checkPaper(@PathVariable Integer paperId){
        return paperService.checkPaper(paperId);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{paperId}/reviewPaper")
    public ResponseVO reviewPaper(@PathVariable Integer paperId){
        return paperService.reviewPaper(paperId);
    }
}
