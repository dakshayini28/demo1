package com.example.demo1.service;

import com.example.demo1.entity.ConnectionEntity;
import com.example.demo1.repository.ConnectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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
    public void update(int id, Map<String, String> newVal) {
        Optional<ConnectionEntity> o = repo.findById(id);

        if (o.isPresent()) {
            ConnectionEntity c = o.get();

            for (Map.Entry<String, String> entry : newVal.entrySet()) {
                String field = entry.getKey();
                String value = entry.getValue();

                switch (field) {
                    case "name":
                        c.setName(value);
                        break;
                    case "url":
                        c.setUrl(value);
                        break;
                    case "username":
                        c.setUsername(value);
                        break;
                    case "password":
                        c.setPassword(value);
                        break;
                    default:
                        throw new RuntimeException("Invalid field: " + field);
                }
            }

            repo.save(c);
        } else {
            throw new RuntimeException("Id may not be present");
        }
    }

}
