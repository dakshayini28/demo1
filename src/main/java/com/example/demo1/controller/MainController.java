package com.example.demo1.controller;

import com.example.demo1.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class MainController {
    @Autowired
    MainService m;

    @GetMapping("test")
    public ResponseEntity<String> testConnection(){
        if(m.isConnected()){
            return ResponseEntity.ok("Connected successfully");
        }else{
            return ResponseEntity.status(500).body("Internal Server error");
        }
    }
    @GetMapping("databases")
    public List<String> getDatabases(){
        return m.databases();
    }
    @GetMapping("tables/{database}")
    public List<String> getTables(@PathVariable String database){
        return m.tables(database);
    }
    @GetMapping("columns/{database}/{table}")
    public HashMap<String,String> getColumns(@PathVariable String database, @PathVariable String table){
        return m.columns(database,table);
    }
}
