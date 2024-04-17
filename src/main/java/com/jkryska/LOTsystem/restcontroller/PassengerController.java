package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api_passenger")
public class PassengerController {
    @Autowired
    PassengerRepository passengerRepository;

    @GetMapping("/all")
    List<Passenger> passengers(){
        return passengerRepository.findAll();
    }

    @PostMapping("/create_passenger")
    Passenger createPassenger(@RequestBody Passenger passenger){
        //wywolanie metody z repository
        return passengerRepository.save(passenger);
    }

    @DeleteMapping("/delete_passenger")
    void deletePassenger(@RequestBody Passenger passenger){
        passengerRepository.delete(passenger);
    }

    @PutMapping("/update_passenger")
    Passenger updatePassenger(@RequestBody Passenger passenger){
        return passengerRepository.save(passenger);
    }
}
