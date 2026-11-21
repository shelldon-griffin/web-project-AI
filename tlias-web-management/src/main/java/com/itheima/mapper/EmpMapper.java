package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 操作员工信息
 */
@Mapper
public interface EmpMapper {

    /**
     * 查询总记录数
     *
     */
    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id ")
    public Long count();

    /**
     * 查询所有的员工及其对应的部门名称，使用逻辑外键
     */
    //@Select("select e.*, d.name deptName from emp as e left join dept as d on e.dept_id = d.id limit #{start}, #{pageSize}")
    public List<Emp> list(Integer start, EmpQueryParam empQueryParam);

    /**
     * 新增员工数据
     * Options两个参数分别表示：
     * - useGeneratedKeys = true：插入后，获取数据库自动生成的主键，例如 MySQL 自增 ID。
     * - keyProperty = "id"：把获取到的主键写入传入对象的 id 属性，也就是 emp.id。
     * 这个注解负责获取并回填 ID；数据库的自增规则需要由表结构配置，例如 AUTO_INCREMENT。
     *
     * 在 MyBatis 中，Mapper 方法只有一个普通 Java 对象参数，而且没有用 @Param 给它命名时，就可以通过 #{属性名} 获取该对象的属性值。
     * 这个说的 @Param 的作用只是起个别名，起别名后使用 #{employee.username} 来获得
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp(username, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image},#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void save(Emp emp);


    void deleteByIds(List<Integer> ids);


    Emp getById(Integer id);

    void updateById(Emp emp);

    /**
     * 统计各个职位的员工人数
     */
    @MapKey("pos")
    List<Map<String, Object>> countEmpJobData();

    @MapKey("name")
    List<Map> countEmpGenderData();

    /**
     * 根据用户名和密码查询员工信息
     */
    @Select("select * from emp where username = #{username} and password = #{password}")
    Emp getUsernameAndPassword(Emp emp);
}
