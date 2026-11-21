package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工管理控制层
 */
@Slf4j
@RequestMapping("/emps")
@RestController
public class EmpController {

    @Autowired
    private EmpService empService;

//    @GetMapping  // 后面的参数名称只要和路径的参数名称一样就可以正常接受。@RequestParam增加默认值
//    public Result count(@RequestParam(defaultValue = "1") Integer page,
//                        @RequestParam(defaultValue = "10") Integer pageSize,
//                        String name, Integer gender, //未写 @RequestParam 时，不会强制要求参数存在。
//                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
//                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
//        log.info("查询请求参数： {}, {}, {}, {}, {}, {}", page, pageSize, name, gender, begin, end);
//        PageResult<Emp> pageResult = empService.count(page, pageSize, name, gender, begin, end);
//        return Result.success(pageResult);
//
//    }

    @GetMapping  // 后面的参数名称只要和路径的参数名称一样就可以正常接受。@RequestParam增加默认值
    public Result count(EmpQueryParam empQueryParam){
        log.info("查询请求参数： {}", empQueryParam);
        PageResult<Emp> pageResult = empService.count(empQueryParam);
        return Result.success(pageResult);

    }

    @PostMapping
    public Result save(@RequestBody Emp emp){
        log.info("请求参数emp: {}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 批量删除员工
     * @param ids
     * @return
     */
    @DeleteMapping  // @RequestParam 不可以省略(接受多个参数，使用集合来接收)
    public Result delete(@RequestParam List<Integer> ids){
        log.info("批量删除部门: ids={} ", ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    /**
     * 修改员工
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("修改员工信息: id={} ", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    @PutMapping
    public Result update(@RequestBody Emp emp){
        log.info("修改员工信息, {}", emp);
        empService.update(emp);
        return Result.success();
    }




















}
