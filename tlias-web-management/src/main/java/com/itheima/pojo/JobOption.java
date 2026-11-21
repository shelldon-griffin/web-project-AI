package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOption {
    //这种 List 叫作原始类型（Raw Type），语法合法，但会失去编译时的元素类型检查。
    private List jobList;
    private List dataList;
}
