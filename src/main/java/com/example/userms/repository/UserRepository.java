package com.example.userms.repository;

import com.example.userms.entity.Role;
import com.example.userms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByOtp(String otp);

    Optional<User> findByUuid(String uuid);


    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.email = :email")
    List<Role> findAllByEmail(String email);
}
