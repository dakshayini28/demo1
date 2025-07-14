package com.example.demo1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "connection_u",
uniqueConstraints = {
@UniqueConstraint(name = "connection_name", columnNames = {"user_id","name"})
    })
public class ConnectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column()
    private String name;
    @Column
    private String url;
    @Column
    private String username;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime created_at;

    @LastModifiedDate
    private LocalDateTime lastModified_at;

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getLastModified_at() {
        return lastModified_at;
    }

    public void setLastModified_at(LocalDateTime lastModified_at) {
        this.lastModified_at = lastModified_at;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Column
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "fk_user_connection"))
    @JsonIgnore
    private UserEntity user;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ConnectionEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", user=" + user +
                '}';
    }
}
