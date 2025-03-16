package com.xushu.exquicker.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.xushu.exquicker.pojo.Student;

import java.util.List;

@Repository
public interface StudentRepository extends  JpaRepository<Student,Integer> {

    List<Student> findAll(Sort sort);

    Page<Student> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT * FROM st_student limit :pageIndex,:pageSize")
    List<Student> findAllByColumn(int pageIndex,int pageSize);
}
