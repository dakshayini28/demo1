package com.example.demo1.repository;

import com.example.demo1.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserEntity,Integer> {
    List<UserEntity> findByEmail(String s);
    List<UserEntity> findByUserNameLike(String s);
    UserEntity findByUserName(String s);
}
