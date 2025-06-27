package com.example.demo1.controller;

import com.example.demo1.entity.DbDetails;
import com.example.demo1.service.ConnectionService;
import com.example.demo1.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    MainService m;

    @Autowired
    ConnectionService connectionService;

    @GetMapping("/connect/test")
    public ResponseEntity<String> testConnection(@RequestParam int id) {
        try{
            List<String> al=connectionService.getDetails(id);
            DbDetails db = new DbDetails(al.get(0), al.get(1), al.get(2));
            if (m.isConnected(db)) {
                return ResponseEntity.ok("Connected successfully");
            } else {
                return ResponseEntity.status(500).body("Internal Server Error");
            }
        }catch(RuntimeException e){
            return ResponseEntity.ok("Id not present");
        }

    }

    @GetMapping("/catalogs")
    public List<String> getDatabases(@RequestParam int id) {
        try{
            List<String> al=connectionService.getDetails(id);
            DbDetails db = new DbDetails(al.get(0), al.get(1), al.get(2));
            return m.databases(db);
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("Connection ID not found")){
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    @GetMapping("/tables")
    public List<String> getTables(
                                  @RequestParam int id,
                                  @RequestParam String database) {
        try{
            List<String> al=connectionService.getDetails(id);
            DbDetails db = new DbDetails(al.get(0), al.get(1), al.get(2));
            return m.tables(database, db);
        }
        catch(RuntimeException e){
            if(e.getMessage().contains("Connection ID not found")){
                return new ArrayList<String>();
            }
        }
        return new ArrayList<String>();
    }

    @GetMapping("/columns")
    public HashMap<String, String> getColumns(
                                              @RequestParam int id,
                                              @RequestParam String database,
                                              @RequestParam String table) {
        try{
            List<String> al=connectionService.getDetails(id);
            DbDetails db = new DbDetails(al.get(0), al.get(1), al.get(2));
            return m.columns(database, table, db);
        }catch(RuntimeException e){
            if(e.getMessage().contains("ID")){
                return new HashMap<String,String>();
            }
        }
        return new HashMap<String,String>();
    }
}
