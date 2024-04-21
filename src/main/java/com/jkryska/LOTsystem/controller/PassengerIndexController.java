package com.jkryska.LOTsystem.controller;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        return "create_passenger";
    }
    @Transactional
    @PostMapping("/create_passenger")
    public String savePassenger(@ModelAttribute("passenger") @Valid Passenger passenger, BindingResult result, Model model){
        if(result.hasErrors()){
            List<Flight> flights = flightRepository.findAll();
            model.addAttribute("flights", flights);
            model.addAttribute("passenger", passenger);
            return "/create_passenger";
        }
        flightRepository.decrementSeatsByFlightId(passenger.getFlightID()); // seats decremental
        passengerRepository.save(passenger);
        return "redirect:/passengers";
    }

    @Transactional
    @PostMapping(value = "/passengers/{id}")
    String deletePassenger(@PathVariable Long id){
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isPresent()){
            Passenger localpassenger = passenger.get();
            System.out.println("Present");
            flightRepository.incrementSeatsByFlightId(localpassenger.getFlightID());
        }
        passengerRepository.deleteById(id);
        return "redirect:/passengers";
    }
    @GetMapping("/update_passenger")
    public String getUpdatePassenger(Model model) {
        List<Passenger> passengers = passengerRepository.findAll();
        model.addAttribute("passengers", passengers);
        model.addAttribute("passenger", new Passenger());
        return "update_passenger";
    }

    @PostMapping("/update_passenger")
    public String updatePassenger(@RequestParam("id") Long id,
                                  @RequestParam(value = "flightID", required = false) Long flightID,
                                  @RequestParam(value = "firstName", required = false) String firstName,
                                  @RequestParam(value = "lastName", required = false) String lastName,
                                  @RequestParam(value = "telephone", required = false) String telephone,
                                  @ModelAttribute("passenger") @Valid Passenger passenger,
                                  BindingResult result,
                                  Model model) {

        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger actualPassenger = optionalPassenger.get();

            // Przeprowadź walidację tylko dla pól, które nie są puste
            if (flightID != null) {
                // Walidacja pola flightID
                if (result.hasFieldErrors("flightID")) {
                    model.addAttribute("passengers", passengerRepository.findAll());
                    return "/update_passenger";
                }
                actualPassenger.setFlightID(flightID);
            }
            if (firstName != null && !firstName.isEmpty()) {
                // Walidacja pola firstName
                if (result.hasFieldErrors("firstName")) {
                    model.addAttribute("passengers", passengerRepository.findAll());
                    return "/update_passenger";
                }
                actualPassenger.setFirstName(firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                // Walidacja pola lastName
                if (result.hasFieldErrors("lastName")) {
                    model.addAttribute("passengers", passengerRepository.findAll());
                    return "/update_passenger";
                }
                actualPassenger.setLastName(lastName);
            }
            if (telephone != null && !telephone.isEmpty()) {
                // Walidacja pola telephone
                if (result.hasFieldErrors("telephone")) {
                    model.addAttribute("passengers", passengerRepository.findAll());
                    return "/update_passenger";
                }
                actualPassenger.setTelephone(telephone);
            }
            passengerRepository.save(actualPassenger);
        }
        return "redirect:/passengers";
    }




}
