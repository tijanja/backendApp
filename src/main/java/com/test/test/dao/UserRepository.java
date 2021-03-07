package com.test.test.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserDao, Long> {

    @Query(value="SELECT * FROM users u WHERE u.email= :email",nativeQuery=true)
    UserDao findUseByEmail(@Param("email") String email);
}
