package com.example.LodgeBnB.repositories.writes;

import com.example.LodgeBnB.models.Airbnb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirbnbWriteRepository extends JpaRepository<Airbnb , Long> {
}
