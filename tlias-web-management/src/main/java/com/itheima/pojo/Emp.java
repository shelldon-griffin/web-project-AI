package com.itheima.pojo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Emp {
    private Integer id; //ID,主键
    private String username; //用户名
    private String password; //密码
    private String name; //姓名
    private Integer gender; //性别, 1:男, 2:女
    private String phone; //手机号
    private Integer job; //职位, 1:班主任,2:讲师,3:学工主管,4:教研主管,5:咨询师
    private Integer salary; //薪资
    private String image; //头像
    // 关于使用@DateTimeFormat(pattern = "yyyy-MM-dd") 来明确日期格式。当是GET请求时需要，POST请求不需要
    // 因为POST请求，使用的是Json格式的数据，Jackson 默认支持 LocalDate 的 ISO 日期格式
    private LocalDate entryDate; //入职日期
    private Integer deptId; //关联的部门ID
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //修改时间

    //封装部门名称（为了补充关联的部门ID的显示）
    private String deptName;

    //封装员工工作经历信息
    private List<EmpExpr> exprList;
}
