/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assign.TuneTribe.artist;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "artist")
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   
    private Long artistId;
    private String userName;
    private String artistFName;
    private String artistLName;
    private String artistEmail;
    private String artistPassword;

    public Artist(String userName, String artistFName, String artistEmail, String artistPassword) {
        this.userName = userName;
        this.artistFName = artistFName;
        this.artistEmail = artistEmail;
        this.artistPassword = artistPassword;
    }
    

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArtistFName() {
        return artistFName;
    }

    public void setArtistFName(String artistFName) {
        this.artistFName = artistFName;
    }

    public String getArtistLName() {
        return artistLName;
    }

    public void setArtistLName(String artistLName) {
        this.artistLName = artistLName;
    }

    public String getArtistEmail() {
        return artistEmail;
    }

    public void setArtistEmail(String artistEmail) {
        this.artistEmail = artistEmail;
    }

    public String getArtistPassword() {
        return artistPassword;
    }

    public void setArtistPassword(String artistPassword) {
        this.artistPassword = artistPassword;
    }
    
    
    
    
}
