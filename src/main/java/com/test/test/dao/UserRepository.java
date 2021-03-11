package com.test.test.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserDao, Long> {

    @Query(value="SELECT * FROM users u WHERE u.email= :email",nativeQuery=true)
    UserDao findUseByEmail(@Param("email") String email);

    @Query(value="UPDATE users set status = 1 WHERE id= :id limit 1",nativeQuery=true)
    void deactivateUser(@Param("id") Long id);

    @Query(value="SELECT * FROM users b WHERE b.status!= 1",nativeQuery=true)
    Page<UserDao> getAllActiveUsers(Pageable pageable);
}
