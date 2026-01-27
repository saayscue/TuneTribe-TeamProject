/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.assign.TuneTribe.mod;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author shauna
 */
@Service
public class ModService {

    @Autowired
    ModRepository repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<Mod> getMods() {
        List<Mod> allMods = repo.findAll();
        return allMods;
                }

    public Mod getMod(long id) {
        return repo.getReferenceById(id);
    }

    public void deleteMod(long id) {
        repo.deleteById(id);
    }



}
