package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Override  //还有一个 PageHelper 分页插件来简化分页展示的操作代码（减少分页这个步骤）
    public PageResult<Emp> count(EmpQueryParam empQueryParam) {
        //1. 获取总记录数
        Long total = empMapper.count();

        //2. 获取结果列表
        Integer start = (empQueryParam.getPage() - 1) * empQueryParam.getPageSize();
        List<Emp> empList = empMapper.list(start, empQueryParam);

        //3.拼接成一个新的对象
        return new PageResult<Emp>(total,empList);
    }
}
