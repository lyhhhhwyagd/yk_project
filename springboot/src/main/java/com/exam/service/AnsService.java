package com.exam.service;

import com.exam.Entity.Ans;
import com.exam.common.Result;
import com.exam.mapper.AnsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service

public class AnsService {
    @Resource
    private AnsMapper ansMapper;
    //total 总数 list 数据列表
    //pageNum 当前页码  pageSize 总数
    public PageInfo<Ans> selectPages(Integer pageNum, Integer pageSize, Ans ans)
    {
        PageHelper.startPage(pageNum,pageSize);
        List<Ans> ansList=ansMapper.selectAll(ans);
        return PageInfo.of(ansList);
    }

    public Integer add(Ans ans) {
        Ans ans1= ansMapper.selectByCondition(ans);
        Result res;
        if(ans1 ==null)
        {
            ansMapper.insert(ans);
            return  1;
        }
        else return 0;

    }
    public Ans query(Ans ans)
    {
        Ans ans1 =ansMapper.selectByCondition(ans);
        return ans1;
    }
    public   List<Ans> listAll()
    {
        List<Ans> ansList=ansMapper.listAll();
        return ansList;
    }


}
