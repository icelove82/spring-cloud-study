package com.ymh.ms.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query(value = "SELECT * FROM CUSTOMER WHERE FIRST_NAME = :firstName AND LAST_NAME = :lastName ORDER BY FIRST_NAME, LAST_NAME", nativeQuery = true)
    List<Customer> findByNativeQuery(@Param("firstName") String firstName, @Param("lastName") String lastName);
}
