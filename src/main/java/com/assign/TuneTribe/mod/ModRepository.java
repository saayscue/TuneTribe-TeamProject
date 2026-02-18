package com.assign.TuneTribe.mod;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModRepository extends JpaRepository<Mod, Long> {
    Optional<Mod> findByUserName(String username);

}
