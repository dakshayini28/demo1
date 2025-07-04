package com.example.demo1.repository;

import com.example.demo1.entity.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepo extends JpaRepository<ConnectionEntity,Integer> {
    List<ConnectionEntity> findByUser_UserId(int id);
    @Query("SELECT s.id, s.name FROM ConnectionEntity s WHERE s.user.userId = :userId")
    List<Object[]> findIdAndNameByUsername(@Param("userId") int userId);

    @Query("select c from ConnectionEntity c where c.name like %:name% and user.userId=:userId")
    List<ConnectionEntity> findConnection(@Param("name") String name,@Param("userId") int userId);
}
