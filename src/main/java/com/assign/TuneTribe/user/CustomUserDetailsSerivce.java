/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assign.TuneTribe.user;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author shauna
 */
@Service
public class CustomUserDetailsSerivce implements UserDetailsService {

    @Autowired
    private UserRepository repo;

   @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = repo.findByUserName(username).orElseThrow(() -> new UsernameNotFoundException(username + "not found"));

    if (user.isBanned()) {
        throw new DisabledException("User with username: " + username + " is banned.");
    }

    ArrayList<SimpleGrantedAuthority> authList = new ArrayList<>();
    authList.add(new SimpleGrantedAuthority(user.getRole()));

    return new org.springframework.security.core.userdetails.User(
            user.getUserName(), user.getUserPassword(), authList);
}
}