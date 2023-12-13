package com.exam.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.exam.common.Result;
import com.exam.Entity.Course;
import com.exam.service.CourseService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jobob
 * @since 2023-12-02
 */

@RestController
@RequestMapping("/course")
public class CourseController {


    @Resource
    private CourseService courseService;

    @GetMapping("/selectpage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "3") Integer pageSize,
                             Course course)
    {
        PageInfo<Course> pageInfo = courseService.selectPage(pageNum, pageSize,course);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Course course)
    {
        courseService.add(course);
        return Result.success(course);
    }
    @PutMapping("/update")
    public Result update(@RequestBody Course course)
    {
        courseService.updateById(course);
        return Result.success(course);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id)
    {
        courseService.deleteById(id);
        return Result.success();
    }
    @GetMapping("/export")
    public void exportData(HttpServletResponse response)throws IOException
    {
        ExcelWriter writer= ExcelUtil.getWriter(true);
       List<Course> list=courseService.listAll();
       // List<> list=new ArrayList<>(2);
        if(!list.isEmpty()) {
            writer.write(list, true);

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode("课程表", "UTF-8") + ".xlsx");
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
        List<Course>  list=courseService.listAll();
        int q1=0,q2=0,q3=0,q4=0;
       for( Course it:list)
        {
            switch (it.getDep())
            {
                case "计算机学院":q1+=1;break;
                case "软件学院":q2+=1;break;
                case "数学学院":q3+=1;break;
                case "文学与新闻学院":q4+=1;break;
            }

        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }




//    @Autowired
//    private CourseService1 courseService;
//    //显示
//    @GetMapping("/list")
//    public List<Course> List()
//    {
//        return courseService.list();
//    }
//    //修改
//    @PostMapping("/mod")
//    public  boolean mod(@RequestBody Course course)
//    {
//        return courseService.updateById(course);
//
//    }
//    //新增或修改
//    @PostMapping("/add")
//    public  boolean add(@RequestBody Course course)
//    {
//        return courseService.saveOrUpdate(course);
//
//    }
//    @GetMapping ("/delete")
//    public  boolean delete(Integer id)
//    {
//        return courseService.removeById(id);
//
//    }
//    @PostMapping("/search")
//    public  List search(@RequestBody Course course)
//    {
//        LambdaQueryWrapper<Course> lambdaQueryWrapper=new LambdaQueryWrapper();
//        lambdaQueryWrapper.eq(Course::getDep,course.getDep());
//        return  courseService.list(lambdaQueryWrapper);
//    }
//    @PostMapping("/page")
//    public List<Course> ListPage(@RequestBody queryPageParam query)
//    {
//       // System.out.println(Param)
//        HashMap param=query.getParam();
//        param.get("dep");
//        String dep=(String) param.get("dep");
//        Page<Course> page=new Page();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<Course> lambdaQueryWrapper=new LambdaQueryWrapper();
//        lambdaQueryWrapper.like(Course::getDep,dep);
//
//
//         IPage result=courseService.page(page,lambdaQueryWrapper);
//         System.out.println("total"+result.getTotal());
//
//
//
//        return  result.getRecords();
//
//    }
//    @PostMapping("/pageC")
//    public List<Course> ListPageC(@RequestBody queryPageParam query)
//    {
//        // System.out.println(Param)
//        HashMap param=query.getParam();
//        param.get("dep");
//        String dep=(String) param.get("dep");
//        Page<Course> page=new Page();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<Course> lambdaQueryWrapper=new LambdaQueryWrapper();
//        lambdaQueryWrapper.like(Course::getDep,dep);
//
//
//        //IPage result=courseService.pageC(page);
//        IPage result=courseService.pageCC(page,lambdaQueryWrapper);
//
//        System.out.println("total"+result.getTotal());
//
//
//
//        return  result.getRecords();
//
//    }

//    @PostMapping("/pageRes")
//    public Result ListPageRes(@RequestBody queryPageParam query)
//    {
//        // System.out.println(Param)
//        HashMap param=query.getParam();
//        param.get("dep");
//        String dep=(String) param.get("dep");
//        Page<Course> page=new Page();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<Course> lambdaQueryWrapper=new LambdaQueryWrapper();
//        lambdaQueryWrapper.like(Course::getDep,dep);
//
//
//        //IPage result=courseService.pageC(page);
//        IPage result=courseService.pageCC(page,lambdaQueryWrapper);
//
//        System.out.println("total"+result.getTotal());
//
//
//
//        return  Result.suc(result.getRecords(),result.getTotal());
//
//    }

}
