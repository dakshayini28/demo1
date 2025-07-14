package com.example.demo1.service;

import com.example.demo1.dto.ConnectionDto;
import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.entity.UserEntity;
import com.example.demo1.repository.ConnectionRepo;
import com.example.demo1.repository.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.spec.ECField;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ConnectionService {
    @Autowired
    ConnectionRepo repo;
    @Autowired
    UserRepo repo1;
    public void add(ConnectionDto data, int user_id){
        Optional<UserEntity> o=repo1.findById(user_id);
        if(o.isEmpty())
            throw new RuntimeException("This user doesnt exist");
        UserEntity user=o.get();
        ConnectionEntity connection=new ConnectionEntity();
        connection.setName(data.getName());
        connection.setUsername(data.getUsername());
        connection.setUrl(data.getUrl());
        connection.setPassword(data.getPassword());
        connection.setUser(user);
        repo.save(connection);
    }
    public void delete(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repo1.findByUserName(username);
        Optional<ConnectionEntity> optionalEntity = repo.findById(id);
        if (optionalEntity.isPresent()) {
            ConnectionEntity entity = optionalEntity.get();
            if (entity.getUser().getUserId() != user.getUserId()) {
                throw new RuntimeException("You are not authorized to delete this connection.");
            }
            repo.delete(entity);
        } else {
            throw new RuntimeException("Connection not found with id: " + id);
        }
    }
    public List<ConnectionEntity> getAll(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repo1.findByUserName(username);
//        System.out.println("hey daksh"+repo.findByUser_UserId(user.getUserId()));
        return repo.findByUser_UserId(user.getUserId());

    }
    public void update(int id, ConnectionDto dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = repo1.findByUserName(username);

        Optional<ConnectionEntity> o = repo.findById(id);

        if (o.isPresent()) {
            ConnectionEntity c = o.get();

            if (c.getUser().getUserId() != user.getUserId()) {
                throw new RuntimeException("You are not authorized to update this connection.");
            }

            if (dto.getName() != null) c.setName(dto.getName());
            if (dto.getUrl() != null) c.setUrl(dto.getUrl());
            if (dto.getUsername() != null) c.setUsername(dto.getUsername());
            if (dto.getPassword() != null) c.setPassword(dto.getPassword());

            repo.save(c);
        } else {
            throw new RuntimeException("Connection not found with id: " + id);
        }
    }
    public List<ConnectionEntity> searchConnection(String s,int user_id ){
        return repo.findConnection(s,user_id);
    }
    public List<ConnectionEntity> recentConnections(int userId,int page,int offset) {
        Pageable lastModifiedAt = PageRequest.of(page, offset).withSort(Sort.by(Sort.Direction.DESC, "lastModified_at"));
        Page<ConnectionEntity> recentConnections = repo.findRecentConnections(userId, lastModifiedAt);
        return recentConnections.getContent();
    }

}
