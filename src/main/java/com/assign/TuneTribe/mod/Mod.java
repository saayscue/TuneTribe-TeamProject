
package com.assign.TuneTribe.mod;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "moderator")
@NoArgsConstructor
@AllArgsConstructor
public class Mod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
    private Long modId;
    private String userName;
    private String modFName;
    private String modLName;
    private String modEmail;
    private String modPassword;
   
    public Mod(String userName, String modFName, String modLName, String modEmail, String modPassword) {
        this.userName = userName;
        this.modFName = modFName;
        this.modLName = modLName;
        this.modEmail = modEmail;
      
    }

    public Long getId() {
        return modId;
    }

    public void setId(Long modId) {
        this.modId = modId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getModFName() {
        return modFName;
    }

    public void setModFName(String modFName) {
        this.modFName = modFName;
    }

    public String getModLName() {
        return modLName;
    }

    public void setModLName(String modLName) {
        this.modLName = modLName;
    }

    public String getModEmail() {
        return modEmail;
    }

    public void setModEmail(String modEmail) {
        this.modEmail = modEmail;
    }

    public String getModPassword() {
        return modPassword;
    }

    public void setModPassword(String modPassword) {
        this.modPassword = modPassword;
    }
    
    
}