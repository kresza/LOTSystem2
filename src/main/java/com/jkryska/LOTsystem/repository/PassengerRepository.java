package com.jkryska.LOTsystem.repository;

import com.jkryska.LOTsystem.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    @Query("SELECT* FROM")
    void decrementSeats();
}
