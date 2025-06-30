package com.example.demo1.service;

import com.example.demo1.entity.DbDetails;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainService {
    private Connection con;
    public boolean isConnected(DbDetails body) {
        try {
            con = DriverManager.getConnection(
                    body.getUrl(),
                    body.getUsername(), body.getPassword());
            if (con != null) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException("Check credentials");
        }
        return false;
    }
    public List<String> databases(DbDetails body){
        String sql="show databases";
        List<String> databaseList =new ArrayList<>();
        if(isConnected(body)){
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String dbName = rs.getString(1);
                    databaseList.add(dbName);
                }
                con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Check credentials");
            }
        }
        return databaseList;
    }

    public List<String> tables(String db,DbDetails body){
        String sql = "show tables in " +"`"+ db+"`";
        System.out.println("database selected: "+db);
        List<String> al=new ArrayList<>();
        if(isConnected(body)){
            try(Statement stmt= con.createStatement();
            ResultSet rs= stmt.executeQuery(sql)){
                while(rs.next()){
                    al.add(rs.getString(1));
                }
                con.close();
            }
            catch(SQLException e){
//                e.printStackTrace();
                throw new RuntimeException("Check if database exists");

            }
        }
        return al;
    }
    public HashMap<String,String> columns(String database, String table,DbDetails body){
        String sql="show columns in `"+database+"`."+"`"+table+"`";
        HashMap<String,String> h=new HashMap<>();
        if(isConnected(body)){
            try(Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(sql)){
                while(rs.next()){
                    h.put(rs.getString("Field"),rs.getString("Type"));
//                    System.out.println(rs.getString("Field"));
                }
            }catch (SQLException e){
//                e.printStackTrace();
                throw new RuntimeException("Check if database and table exists");
            }
        }
        return h;
    }

}
