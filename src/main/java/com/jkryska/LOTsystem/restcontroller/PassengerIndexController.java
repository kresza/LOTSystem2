package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PassengerIndexController {
    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @GetMapping("/passengers")
    public String allPassengers(Model model){
        List<Passenger> passengers = passengerRepository.findAll();
            model.addAttribute("passengers", passengers);
        return "/passengers";
    }
    @GetMapping("/create_passenger")
    public String createPassenger(Model model){
        List<Flight> flights = flightRepository.findAll();
        model.addAttribute("flights", flights);
        model.addAttribute("passenger", new Passenger());
        return "/create_passenger";
    }
    @Transactional
    @PostMapping("/create_passenger")
    public String savePassenger(@ModelAttribute Passenger passenger){
        flightRepository.decrementSeatsByFlightId(passenger.getFlightID()); // seats decremental
        passengerRepository.save(passenger);
        return "redirect:/passengers";
    }
    @GetMapping("/delete_passenger")
    public String getDeletePassenger(Model model){
        List<Passenger> passengers = passengerRepository.findAll();
        model.addAttribute("passengers", passengers);
        return "/delete_passenger";
    }

}
