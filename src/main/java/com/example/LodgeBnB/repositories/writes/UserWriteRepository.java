package com.example.LodgeBnB.repositories.writes;

import com.example.LodgeBnB.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWriteRepository extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
}
