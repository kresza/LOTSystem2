package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api_flight")
public class FlightController {
    @Autowired
    FlightRepository flightRepository;

    @GetMapping("/all")
    List<Flight> flights(){
        return flightRepository.findAll();
    }

    @PostMapping("/create_flight")
    Flight createFlight(@RequestBody Flight flight){
        return flightRepository.save(flight);
    }

    @DeleteMapping("/delete_flight/{id}")
    void deleteFlight(@PathVariable Long id){
        flightRepository.deleteById(id);
    }

    @PutMapping("/update_flight")
    Flight updateFlight(@RequestBody Flight flight){
        return flightRepository.save(flight);
    }
}
