package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 操作部门信息
 */
@Mapper
public interface DeptMapper {
    /**
     * 查询所有的部门数据
     * @return
     */
    @Select("select id, name, create_time, update_time from dept order by update_time desc")
    List<Dept> findAll();

    /**
     * 根据id删除部门
     */
    @Delete("delete from dept where id = #{id}")  //预编译的SQL
    void deleteById(Integer id);

    /**
     * 新增部门
     */
    @Insert("insert into dept(name,create_time,update_time) values (#{name},#{createTime},#{updateTime})")
    void save(Dept dept);  //id自增，不用我们添加。并且上面直接写属性名，即可以获得dept里面对应属性的值

    /**
     * 根据id查询部门
     */
    @Select("select id, name, create_time, update_time from dept where id = #{id}")
    Dept getById(Integer id);

    /**
     * 修改部门（根据 ID 更新）
     */
    @Update("update dept set name = #{name},update_time = #{updateTime} where id = #{id}")
    void update(Dept dept);
}
