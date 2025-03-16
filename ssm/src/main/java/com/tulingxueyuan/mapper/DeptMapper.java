package com.tulingxueyuan.mapper;

import com.tulingxueyuan.pojo.Dept;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Dept record);

    Dept selectByPrimaryKey(Integer id);

    List<Dept> selectAll();

    int updateByPrimaryKey(Dept record);
}