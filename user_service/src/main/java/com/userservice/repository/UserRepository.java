package com.userservice.repository;

import com.userservice.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("FROM User u LEFT JOIN FETCH u.roles WHERE u.email =:email")
    Optional<User> findByEmail(String email);

}
