package com.example.demo1.repository;

import com.example.demo1.entity.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepo extends JpaRepository<ConnectionEntity,Integer> {
}
