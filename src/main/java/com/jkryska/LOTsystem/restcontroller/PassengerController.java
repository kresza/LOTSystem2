package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api_passenger")
public class PassengerController {
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    FlightRepository flightRepository;

    @GetMapping("/all")
    List<Passenger> passengers(){
        return passengerRepository.findAll();
    }

    @Transactional
    @PostMapping("/create_passenger")
    Passenger createPassenger(@RequestBody Passenger passenger){
        flightRepository.decrementSeatsByFlightId(passenger.getFlightID()); // seats decremental
        return passengerRepository.save(passenger);
    }

    @Transactional
    @DeleteMapping("/delete_passenger/{id}")
    void deletePassenger(@PathVariable Long id) {
        Passenger passenger = null;
        for(var localpassenger : passengers()){
            if(Objects.equals(localpassenger.getId(), id)){ // find passenger by id
                passenger = localpassenger;
            }
        }
        assert passenger != null;
        flightRepository.incrementSeatsByFlightId(passenger.getFlightID());
        passengerRepository.deleteById(id);
    }

    @PutMapping("/update_passenger")
    Passenger updatePassenger(@RequestBody Passenger passenger){
        return passengerRepository.save(passenger);
    }

    @DeleteMapping("/delete_all_passengers")
    void deleteAllPassengers(){
        passengerRepository.deleteAll();
    }
}
