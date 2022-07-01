package com.example.transport.repository;

import com.example.transport.entity.*;
import org.springframework.data.jpa.repository.*;

public interface MatrixRepo extends JpaRepository<Matrix, Integer> {
    @Query(value = "select * from matrix order by id",nativeQuery = true)
    MatrixInfo findAllByOrderId();
}
