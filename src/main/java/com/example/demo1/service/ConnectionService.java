package com.example.demo1.service;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.repository.ConnectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConnectionService {
    @Autowired
    ConnectionRepo repo;
    public void add(ConnectionEntity data){
        repo.save(data);
    }
    public void delete(int id) {
        Optional<ConnectionEntity> optionalEntity = repo.findById(id);
        if (optionalEntity.isPresent()) {
            ConnectionEntity entity = optionalEntity.get();
            repo.delete(entity);
        } else {
            throw new RuntimeException("Connection not found with id: " + id);
        }
    }

    public List<ConnectionEntity> getAll(){
        return repo.findAll();
    }
    public void update(int id, String field,String newVal){
        Optional<ConnectionEntity> o=repo.findById(id);
        if(o.isPresent()){
            ConnectionEntity c=o.get();
            if(field.equals("name")){
                c.setName(newVal);
            }else if(field.equals("url")){
                c.setUrl(newVal);
            }else if(field.equals("password")){
                c.setPassword(newVal);
            }else if(field.equals("username")){
                c.setUsername(newVal);
            }
            repo.save(c);
        }else{
            throw new RuntimeException("Id may not be present");
        }


    }
}
