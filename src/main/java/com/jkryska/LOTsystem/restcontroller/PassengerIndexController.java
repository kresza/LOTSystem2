package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
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
}
