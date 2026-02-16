
package com.assign.TuneTribe.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

/**
 *
 * @author shauna
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserName(String username);

  List<User> findByUserNameNot(String username);

  List<User> findByRoleIgnoreCase(String role);

  List<User> findByUserNameNotAndBannedFalse(String username);

  List<User> findByRoleIgnoreCaseAndBannedFalse(String role);

}
