package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.JobOption;
import com.itheima.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;

    @Override
    public JobOption getEmpJobData() {
        //这里也可以直接封装成对象 即List<JobOption> list 然后修改xml文件中对应的类对象
        // 此时list存储的是[
        //  {"pos": "班主任", "total": 2},
        //  {"pos": "讲师", "total": 5}
        //]
        List<Map<String,Object>> list = empMapper.countEmpJobData();
        // list存储的是[
        //    {"pos": "班主任", "total": 2},
        //    {"pos": "讲师",   "total": 5},
        //    {"pos": "咨询师", "total": 8}
        //]
        //list.stream()	创建一条数据处理流，依次处理列表中的元素
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("total")).toList();
        return new JobOption(jobList, dataList);
    }

    @Override
    public List<Map> getEmpGenderData() {
        return empMapper.countEmpGenderData();
    }
}
