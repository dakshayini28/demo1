package com.example.demo1.service;

import com.example.demo1.entity.DbDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;



@Service
public class ConnectionService {
    @Autowired
    MainService mainService;
    public HashMap<Integer,String> getConnections(){
        HashMap<Integer,String> h=new HashMap<>();
        String sql="select * from connectiondb.connections";
        DbDetails db=new DbDetails("jdbc:mysql://localhost:3306","root","dakshu");
//        System.out.println("new details "+db.getUrl()+" "+db.getUsername());
        if(mainService.isConnected(db)){
            try(  Connection con= DriverManager.getConnection(db.getUrl(), db.getUsername(),db.getPassword());
                   Statement stmt=con.createStatement();
                   ResultSet rs=stmt.executeQuery(sql)){
                while(rs.next()){
                    h.put(rs.getInt("id"),rs.getString("name"));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return h;
    }
    public void addConnection(String name, String url, String username, String password) {
        String sql = "insert into connectiondb.connections(name, url, username, password) values (?, ?, ?, ?)";
        DbDetails db = new DbDetails("jdbc:mysql://localhost:3306", "root", "dakshu");
        if (mainService.isConnected(db)) {
            try (
                    Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
                    PreparedStatement pstmt = con.prepareStatement(sql)
            ) {
                pstmt.setString(1, name);
                pstmt.setString(2, url);
                pstmt.setString(3, username);
                pstmt.setString(4, password);
                int rowsInserted = pstmt.executeUpdate();
            } catch (Exception e) {
//                e.printStackTrace();
                throw new RuntimeException("Connection with this name already exists.");
            }
        }
    }

    public void deleteConnection(int id) {
        String sql = "delete from connectiondb.connections where id=?";
        DbDetails db = new DbDetails("jdbc:mysql://localhost:3306", "root", "dakshu");
        if (mainService.isConnected(db)) {
            try (
                    Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
                    PreparedStatement pstmt = con.prepareStatement(sql)
            ) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected == 0) {
                    throw new RuntimeException("Connection with given ID does not exist.");
                }
            } catch (Exception e) {
//                e.printStackTrace();
                throw new RuntimeException("Failed to delete connection: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Database not connected.");
        }
    }

    public ArrayList<String> getDetails(int id) {
        ArrayList<String> al = new ArrayList<>();
        String sql = "SELECT url, username, password FROM connectiondb.connections WHERE id = ?";
        DbDetails db = new DbDetails("jdbc:mysql://localhost:3306", "root", "dakshu");
        if (mainService.isConnected(db)) {
            try (
                    Connection con = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
                    PreparedStatement pstmt = con.prepareStatement(sql)
            ) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        al.add(rs.getString("url"));
                        al.add(rs.getString("username"));
                        al.add(rs.getString("password"));
                    } else {
                        throw new RuntimeException("Connection ID not found");
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch connection details: " + e.getMessage());
            }
        }
        return al;
    }
    public void updateConnection(int id,String name,String url,String username,String password){
        String sql="update connectiondb.connections set name=?,url=?,username=?,password=? where id=?";
        DbDetails db = new DbDetails("jdbc:mysql://localhost:3306", "root", "dakshu");
        try(Connection con=DriverManager.getConnection(db.getUrl(),db.getUsername(),db.getPassword());
        PreparedStatement pstmt=con.prepareStatement(sql);){
            pstmt.setString(1,name);
            pstmt.setString(2,url);
            pstmt.setString(3,username);
            pstmt.setString(4,password);
            pstmt.setInt(5,id);
            int rowsaffected=pstmt.executeUpdate();
            if(rowsaffected==0){
                throw new RuntimeException("Connection Id not found");
            }
        }catch (Exception e){
            throw new RuntimeException("Eroor");
        }

    }

}
