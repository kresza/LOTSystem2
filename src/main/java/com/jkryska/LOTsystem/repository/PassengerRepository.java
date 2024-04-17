package com.jkryska.LOTsystem.repository;

import com.jkryska.LOTsystem.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
