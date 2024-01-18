package com.exam.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.exam.Entity.Ans;
import com.exam.common.Result;
import com.exam.service.AnsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/ans")
public class AnsController {

    @Resource
    private AnsService ansService;

    @GetMapping("/selects")
    public Result selectPages(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              Ans ans)
    {
        PageInfo<Ans> pageInfo = ansService.selectPages(pageNum, pageSize,ans);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Ans ans)
    {
        if(ansService.add(ans)==1)
        {
            return Result.success(ans);
        }
        else
        {
            return Result.error("请勿重复作答");
        }
    }
    @GetMapping("/query")
    public Result query(Ans ans)
    {
        return Result.success(ansService.query(ans));

    }


    @GetMapping("/export")
    public void exportData(HttpServletResponse response)throws IOException
    {
        ExcelWriter writer= ExcelUtil.getWriter(true);
        List<Ans> list=ansService.listAll();
        // List<> list=new ArrayList<>(2);
        if(!list.isEmpty()) {
            writer.write(list, true);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("习题表", "UTF-8") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            writer.flush(outputStream, true);
            outputStream.flush();
            outputStream.close();
            writer.close();

        }
    }
    @GetMapping("/map")
    public Result get()
    {
        List<Ans>  list=ansService.listAll();
        int q1=0,q2=0,q3=0,q4=0;
        for( Ans it:list)
        {
            switch (it.getMyans())
            {
                case "计算机学院":q1+=1;break;
                case "软件学院":q2+=1;break;
                case "数学学院":q3+=1;break;
                case "文学与新闻学院":q4+=1;break;
            }

        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }




}
