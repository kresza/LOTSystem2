package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class FlightIndexController {
    @Autowired
    private FlightRepository flightRepository;

    @GetMapping("/flights")
    public String getFlights(Model model) {
        List<Flight> flights = flightRepository.findAll();
        model.addAttribute("flights", flights);
        return "flights";
    }

    @GetMapping("/create_flight")
    public String createFlight(Model model){
        Flight flight = new Flight();
        model.addAttribute("flight", flight);
        return "create_flight"; // Zwrócenie nazwy widoku
    }

    @PostMapping("/create_flight")
    public String saveFlight(@ModelAttribute("flight") Flight flight){
        flightRepository.save(flight);
        return "redirect:/flights";
    }
}
