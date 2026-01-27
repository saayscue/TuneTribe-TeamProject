

package com.assign.TuneTribe.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author shauna
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String userFName;
    private String userLName;
    private String userEmail;
    private boolean banned = false;
    private String userPassword;
    private String role;


    public User(String username, String userEmail, boolean banned, String userPassword, String userFName, String userLName, String role) {
        this.userName = username;
        this.userEmail = userEmail;
        this.banned = banned;
        this.userPassword = userPassword;
        this.userFName = userFName;
        this.userLName = userLName;
        this.role = role;
    }
    

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
    public String getUserFName() {
        return userFName;
    }

    public void setUserFName(String userFName) {
        this.userFName = userFName;
    }

    public String getUserLName() {
        return userLName;
    }

    public void setUserLName(String userLName) {
        this.userLName = userLName;
    }

    
    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public boolean isBanned() {
        return banned;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    
    
    
}