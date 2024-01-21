package com.exam.service;

import com.exam.Entity.Paper;
import com.exam.util.ResponseVO;

public interface PaperService {

    ResponseVO addPaper(Paper paper);

    ResponseVO updatePaper(Paper paper);

    ResponseVO deletePaper(int paperId);

    ResponseVO getUserPapers(int userId);

    ResponseVO checkPaper(int paperId);

    ResponseVO reviewPaper(int paperId);
}
