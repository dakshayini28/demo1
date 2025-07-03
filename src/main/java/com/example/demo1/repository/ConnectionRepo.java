package com.example.demo1.repository;

import com.example.demo1.entity.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepo extends JpaRepository<ConnectionEntity,Integer> {
    List<ConnectionEntity> findByUser_UserId(int id);

}
