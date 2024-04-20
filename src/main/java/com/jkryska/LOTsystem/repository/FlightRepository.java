package com.jkryska.LOTsystem.repository;

import com.jkryska.LOTsystem.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {


    // decremental seats
    @Modifying
    @Query("UPDATE Flight f SET f.seats = f.seats - 1 WHERE f.id = :flightId")
    void decrementSeatsByFlightId(@Param("flightId") Long flightId);
    // incremental seats

    @Modifying
    @Query("UPDATE Flight f SET f.seats = f.seats + 1 WHERE f.id = :flightId")
    void incrementSeatsByFlightId(@Param("flightId") Long flightId);

    @Query("SELECT f FROM Flight f ORDER BY " +
            "CASE :param " +
            "WHEN 'flightNumber' THEN f.flightNumber " +
            "WHEN 'startingPlace' THEN f.startingPlace " +
            "WHEN 'destination' THEN f.destination " +
            "WHEN 'flightDate' THEN f.flightDate " +
            "WHEN 'seats' THEN f.seats " +
            "END ASC")
    List<Flight> sortFlightsByASC(@Param("param") String param);

    @Query("SELECT f FROM Flight f ORDER BY " +
            "CASE :param " +
            "WHEN 'flightNumber' THEN f.flightNumber " +
            "WHEN 'startingPlace' THEN f.startingPlace " +
            "WHEN 'destination' THEN f.destination " +
            "WHEN 'flightDate' THEN f.flightDate " +
            "WHEN 'seats' THEN f.seats " +
            "END DESC")
    List<Flight> sortFlightsByDESC(@Param("param") String param);
}
