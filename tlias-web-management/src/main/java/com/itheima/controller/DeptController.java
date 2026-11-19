package com.itheima.controller;

import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j  //日志记录，可以把下面的sout删掉了
@RestController
public class DeptController {


    //调用 service 层的方法
    @Autowired
    private DeptService deptService;

    //查询
    //@RequestMapping(value = "/depts",method = RequestMethod.GET)  //method 指定请求方式
    @GetMapping("/depts")  //必须是 GET 请求
    public Result list(){
        //System.out.println("查询全部部门数据");
        log.info("查询全部部门数据");  //直接使用这个记录日志
        //调用 service 层的方法 ，需要返回值
        List<Dept> deptList = deptService.findAll();
        // 代码走到这里就代表一定请求成功了
        return Result.success(deptList);
    }

    //删除   当前端传递的变量和形参名一致时，可以省略@RequestParam
    @DeleteMapping("/depts")
    public Result delete(Integer id){
        //System.out.println("根据ID删除部门：" + id);
        log.info("根据ID删除部门：{}" ,id);
        //调用 service 层的方法 ，不需要返回值
        deptService.deleteById(id);
        return Result.success();
    }


    //新增
    // 如果想把json格式的数据存到对象里面添加@RequestBody注解，并且传入的参数，必须和对象的属性名一样
    @PostMapping("/depts")
    public Result save(@RequestBody Dept dept){
        //System.out.println("新增部门, dept=" + dept);
        log.info("新增部门, dept={}",dept);
        //调用 service 层的方法 ，不需要返回值
        deptService.save(dept);
        return Result.success();
    }


    //修改
    //1.查询回显
    @GetMapping("/depts/{id}")  // @PathVariable 获得路径参数。形参名和路径参数一样，可以省略@PathVariable("id")
    public Result getById(@PathVariable Integer id){
        //System.out.println("查询部门信息，id=" + id);
        log.info("查询部门信息，id={}",id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }
    //2.修改数据  接受的是修改后的name，我们放到 dept 对象中
    // 如果想把json格式的数据存到对象里面添加@RequestBody注解，并且传入的参数，必须和对象的属性名一样
    @PutMapping("/depts")
    public Result update(@RequestBody Dept dept){
        //System.out.println("修改部门信息，dept=" + dept);
        log.info("新增部门, dept={}",dept);
        deptService.update(dept);
        return Result.success();
    }



}
