package com.assign.TuneTribe.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author shauna
 */
@Service
public class UserService {

    @Autowired
    UserRepository repo;

    @Autowired
    PasswordEncoder passwordEncoder;
   
    
  public void registerUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        repo.save(user);
    }
  
   public void saveUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        repo.save(user);
    }
}
