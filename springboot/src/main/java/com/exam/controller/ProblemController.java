package com.exam.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.exam.common.Result;
import com.exam.service.ProblemService;
import com.exam.Entity.Problem;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;


@RestController
@RequestMapping("/problem")
public class ProblemController {

    @Resource
    private ProblemService problemService;

    @GetMapping("/selects")
    public Result selectPages(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "3") Integer pageSize,
                              Problem problem)
    {
        PageInfo<Problem> pageInfo = problemService.selectPages(pageNum, pageSize,problem);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Problem problem)
    {
        problemService.add(problem);
        return Result.success(problem);
    }
    @PutMapping("/update")
    public Result update(@RequestBody Problem problem)
    {
        problemService.updateById(problem);
        return Result.success(problem);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id)
    {
        problemService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/export")
    public void exportData(HttpServletResponse response)throws IOException
    {
        ExcelWriter writer= ExcelUtil.getWriter(true);
        List<Problem> list=problemService.listAll();
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
        List<Problem>  list=problemService.listAll();
        int q1=0,q2=0,q3=0,q4=0;
        for( Problem it:list)
        {
            switch (it.getAns())
            {
                case "A":q1+=1;break;
                case "B":q2+=1;break;
                case "C":q3+=1;break;
                case "D":q4+=1;break;
            }

        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
    @GetMapping("/smap")
    public Result gets()
    {
        List<Problem>  list=problemService.listAll();

        return Result.success(list);
    }




}
