package com.exam.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.exam.common.Result;
import com.exam.service.AnsService;
import com.exam.Entity.Ans;
import com.exam.service.ProblemService;
import com.exam.Entity.Anse;
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
    private ProblemService problemService;

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
    public Result query( Ans ans)
    {
        return Result.success(ansService.query(ans));

    }


    @GetMapping("/export")
    public void exportData(HttpServletResponse response,Anse anse)throws IOException
    {
        ExcelWriter writer= ExcelUtil.getWriter(true);
        List<Anse> list=ansService.selects(anse);
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





}
