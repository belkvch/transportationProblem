package com.example.transport.repository;

import com.example.transport.entity.*;
import org.springframework.data.jpa.repository.*;

public interface MatrixInfoRepo extends JpaRepository<MatrixInfo, Integer> {
    @Query(value = "select * from matrix_info where id = (select max(id) from matrix_info)",nativeQuery = true)
    MatrixInfo findByIdMax();
}
