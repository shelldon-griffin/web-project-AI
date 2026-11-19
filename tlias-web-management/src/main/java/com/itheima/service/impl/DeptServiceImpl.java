package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> findAll() {
        //需要返回值
        return deptMapper.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        //需要前端传过来的参数
        deptMapper.deleteById(id);
    }

    @Override
    public void save(Dept dept) {
        //补全数据表中的其他值
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        //调用mapper层，操作底层数据库
        deptMapper.save(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void update(Dept dept) {
        //修改数据表中的 更新时间
        dept.setUpdateTime(LocalDateTime.now());
        deptMapper.update(dept);
    }
}
