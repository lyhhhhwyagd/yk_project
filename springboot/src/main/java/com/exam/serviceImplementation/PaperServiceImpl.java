package com.exam.serviceImplementation;

import com.exam.Entity.Answer;
import com.exam.Entity.Options;
import com.exam.Entity.Paper;
import com.exam.Entity.Question;
import com.exam.util.PaperStatus;
import com.exam.mapper.AnswerMapper;
import com.exam.mapper.OptionsMapper;
import com.exam.mapper.PaperMapper;
import com.exam.mapper.QuestionMapper;
import com.exam.service.PaperService;
import com.exam.util.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {
    private final static String EMPTY="无效ID";
    private final static String USER_EMPTY="用户未创建任何问卷";
    @Autowired
    PaperMapper paperMapper;
    @Autowired    //注意每个都得Autowired 不能只写一个
    QuestionMapper questionMapper;
    @Autowired
    OptionsMapper optionsMapper;
    @Autowired
    AnswerMapper answerMapper;

    @Override
    public ResponseVO addPaper(Paper paper) {
        try {
            paperMapper.addPaper(paper);
            return ResponseVO.buildSuccess(paper);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO updatePaper(Paper paperVO) {
        try {
            Paper paper=paperMapper.selectByPaperId(paperVO.getId());
            if(paper==null)
                return ResponseVO.buildFailure(EMPTY);
            else{
                paperMapper.updatePaper(paperVO);
                return ResponseVO.buildSuccess();
            }
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }
    @Scheduled(cron = "0 0/1 * * * ?")    //每分钟检查一次
    void checkPaperStatus(){
        List<Paper> paperList =paperMapper.getTimePapers();
        for(Paper paper : paperList){
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = sdf.format(date);
            if(paper.getStatus()==PaperStatus.INIT){    //没开放的开放它
                if(currentDate.compareTo(paper.getStartTime())>=0)
                    paperMapper.changeStatus(PaperStatus.START, paper.getId());
            }
            else if(paper.getStatus()==PaperStatus.START){   //没结束的结束它
                if(currentDate.compareTo(paper.getEndTime())>=0)
                    paperMapper.changeStatus(PaperStatus.STOP, paper.getId());
            }
        }
    }

    @Override
    public ResponseVO deletePaper(int paperId) {
        try {
            Paper paper=paperMapper.selectByPaperId(paperId);
            if(paper==null)
                return ResponseVO.buildFailure(EMPTY);
            else{
                paperMapper.deletePaper(paperId);
                List<Question> questionList=questionMapper.selectByPaperId(paperId);
                for(Question question:questionList)
                    optionsMapper.deleteByQuestionId(question.getId());
                questionMapper.deleteByPaperId(paperId);
                return ResponseVO.buildSuccess();
            }
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO getUserPapers(int userId) {
        try {
            List<Paper> paperList =paperMapper.getUserPapers(userId);
            if(paperList ==null)
                return ResponseVO.buildFailure(USER_EMPTY);
            else
                return ResponseVO.buildSuccess(paperList);
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO checkPaper(int paperId) {
        try {
            Paper paper =paperMapper.selectByPaperId(paperId);
            if(paper ==null)
                return ResponseVO.buildFailure(EMPTY);
            //PaperDetail paperDetail=new PaperDetail();
            Paper paperDetail=new Paper();
            BeanUtils.copyProperties(paper,paperDetail);

            //List<QuestionVO> questionVOList=new ArrayList<>();
            List<Question> question1List=new ArrayList<>();
            List<Question> questionList=questionMapper.selectByPaperId(paperId);
            for(Question question:questionList){
                int questionId=question.getId();
                List<Options> optionsList=optionsMapper.selectByQuestionId(questionId);

                Question question1=new Question();
                BeanUtils.copyProperties(question,question1);
                question1.setOptions(optionsList);
                question1List.add(question1);
            }
            paperDetail.setQuestion1List(question1List);
            return ResponseVO.buildSuccess(paperDetail);

        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

    @Override
    public ResponseVO reviewPaper(int paperId) {
        try {
            Paper paper =paperMapper.selectByPaperId(paperId);
            if(paper ==null)
                return ResponseVO.buildFailure(EMPTY);
            else{
                //PaperStatistic paperStatistic=new PaperStatistic();
                Paper paperStatistic=new Paper();
                BeanUtils.copyProperties(paper,paperStatistic);

                //List<QuestionStatistic> questionStatisticList=new ArrayList<>();
                List<Question> questionStatisticList=new ArrayList<>();
                List<Question> questionList=questionMapper.selectByPaperId(paperId);

                for(Question question:questionList){

                    //QuestionStatistic questionStatistic=new QuestionStatistic();
                    Question questionStatistic=new Question();
                    BeanUtils.copyProperties(question,questionStatistic);

                    int questionId=question.getId();

                    if(question.getType()!=3){    //单选题和多选题
                        List<Options> optionsList=optionsMapper.selectByQuestionId(questionId);
                        //List<OptionStatistic> optionStatisticList =new ArrayList<>();
                        List<Options> optionStatisticList =new ArrayList<>();

                        for(Options options:optionsList){   //先都转成另一个VO
                            //OptionStatistic optionStatistic =new OptionStatistic();
                            Options optionStatistic =new Options();
                            BeanUtils.copyProperties(options, optionStatistic);
                            optionStatistic.setSelectedNum(0);   //后面用于+1
                            optionStatisticList.add(optionStatistic);
                        }

                        List<Answer> answerList =answerMapper.selectByQuestionId(questionId);

                        for(Answer answer : answerList){
                            String answerContent= answer.getAnswerContent();
                            String[] optionSequenceList=answerContent.split(",");
                            for(String sequenceStr:optionSequenceList){
                                int sequence= Integer.valueOf(sequenceStr);
                                for(Options optionStatistic : optionStatisticList){//for(OptionStatistic optionStatistic : optionStatisticList){
                                    if(optionStatistic.getSequence()==sequence){
                                        optionStatistic.setSelectedNum(optionStatistic.getSelectedNum()+1);
                                        break;
                                    }
                                }
                            }
                        }

                        /*
                        for(Answer answer:answerList){
                            String answerContent=answer.getAnswerContent();
                            String[] optionSequenceList=answerContent.split(",");
                            for(String sequenceStr:optionSequenceList){
                                int sequence=Integer.valueOf(sequenceStr);
                                for(OptionStatistic optionStatistic : optionStatisticList){
                                    if(optionStatistic.getSequence()==sequence){
                                        optionStatistic.setSelectedNum(optionStatistic.getSelectedNum()+1);
                                        break;
                                    }
                                }
                            }
                        }

                         */
                        questionStatistic.setOptionStatistics(optionStatisticList);
                    }
                    else{    //简答题
                        questionStatistic.setAnswerList(answerMapper.selectByQuestionId(questionId));
                        System.out.println("questionStatistic.setAnswerList"+questionStatistic.getAnswerList());
                    }
                    questionStatistic.setFilledInNum(answerMapper.selectByQuestionId(questionId).size());   //此题填写人数
                    questionStatisticList.add(questionStatistic);
                }
                paperStatistic.setQuestionStatistics(questionStatisticList);
                return ResponseVO.buildSuccess(paperStatistic);
            }
        }catch (Exception e){
            System.out.println(e);
            return ResponseVO.buildFailure(e.getMessage());
        }
    }

}
