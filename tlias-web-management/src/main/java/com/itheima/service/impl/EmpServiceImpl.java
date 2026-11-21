package com.itheima.service.impl;

import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.*;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private EmpExprMapper empExprMapper;

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

    //默认情况下，只有出现RuntimeException(运行时异常)才会回滚事务。
    //所有的异常都回滚，需要来配置@Transactional注解当中的rollbackFor属性
    @Transactional(rollbackFor = {Exception.class})  //创建一个事务，当这个方法多次操作数据库时使用，避免异常导致的数据库信息不完善
    @Override
    public void save(Emp emp) {
        //1.补全基础属性
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());

        //2.保存基本信息
        empMapper.save(emp);

        //3.保存历史工作经历 上面方法中的@Options注解可以帮助我们获得插入数据的id（唯一标识）
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        // isEmpty 在列表为 null 或没有元素时返回 true；取反后，只有列表中有工作经历才进入。
        // 没有工作经历时跳过保存，避免批量插入 SQL 的 VALUES 后面没有数据。
        if(!CollectionUtils.isEmpty(exprList)){
            // 遍历工作经历列表，empExpr 表示每次遍历到的一条工作经历。
            // 将每条经历的 empId 设置为当前新增员工的 ID，使这些经历都关联到该员工。
            // 这里修改的是列表中对象的属性，还没有写入数据库。
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));
            // 调用 Mapper，将设置好员工 ID 的工作经历列表批量插入 emp_expr 表。
            empExprMapper.insertBatch(exprList);
        }
    }

    @Transactional(rollbackFor = {Exception.class})  //创建一个事务
    @Override
    public void deleteByIds(List<Integer> ids) {
        //1.删除员工基本信息
        empMapper.deleteByIds(ids);

        //2.删除员工历史工作经历
        empExprMapper.deleteByEmpIds(ids);
    }


    @Override
    public Emp getInfo(Integer id) {
        //1.查询回显(直接返回查询到的结果即可)
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = {Exception.class})  //创建一个事务
    @Override
    public void update(Emp emp) {
        //1. 根据ID更新员工基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        //2. 根据员工ID删除员工的工作经历信息 【删除老的】 变成列表！！！
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        //3. 新增员工的工作经历数据 【新增新的】
        Integer empId = emp.getId();
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            exprList.forEach(empExpr -> empExpr.setEmpId(empId));
            empExprMapper.insertBatch(exprList);
        }


    }

    @Override
    public LoginInfo login(Emp emp) {
        //根据用户名和密码查询用户信息
        Emp empLogin = empMapper.getUsernameAndPassword(emp);
        if(empLogin != null){

            //生成JWT令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id",empLogin.getId());
            claims.put("username",empLogin.getUsername());
            String jwt = JwtUtils.generateJwt(claims);

            return new LoginInfo(empLogin.getId(), empLogin.getUsername(), empLogin.getName(), jwt);
        }
        return null;
    }
}
