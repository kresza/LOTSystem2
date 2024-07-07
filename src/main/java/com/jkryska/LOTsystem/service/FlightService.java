package com.jkryska.LOTsystem.service;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PassengerRepository passengerRepository;

    public List<Flight> getAllFligts(){
        return flightRepository.findAll();
    }
}
