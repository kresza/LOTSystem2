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

import java.util.Optional;

@Controller
public class PassengerController {
    @Autowired
    FlightRepository flightRepository;

    @Autowired
    PassengerRepository passengerRepository;

//    show passengers
    @GetMapping("/passengers")
    public String allPassengers(Model model){
        model.addAttribute("passengers", passengerRepository.findAll());
        return "/passengers";
    }
//    show create passenger form
    @GetMapping("/create_passenger")
    public String createPassenger(Model model){
        model.addAttribute("flights", flightRepository.findAll());
        model.addAttribute("passenger", new Passenger());
        return "create_passenger";
    }
//    save passenger in database
    @Transactional
    @PostMapping("/create_passenger")
    public String savePassenger(@ModelAttribute("passenger") @Valid Passenger passenger, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("flights", flightRepository.findAll());
            return "/create_passenger";
        }
        Optional<Flight> optionalFlight = flightRepository.findById(passenger.getFlightID());
        if(optionalFlight.isPresent())
        {
            Flight flight = optionalFlight.get();
            if(flight.getSeats() <= 0){
                model.addAttribute("flights", flightRepository.findAll());
                model.addAttribute("error", "Selected flight is full. Please choose another flight.");
                return "/create_passenger";
            }
        }
        flightRepository.decrementSeatsByFlightId(passenger.getFlightID());
        passengerRepository.save(passenger);
        return "redirect:/passengers";
    }

//    delete passenger
    @Transactional
    @PostMapping(value = "/passengers/{id}")
    String deletePassenger(@PathVariable Long id){
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if(passenger.isPresent()){
            Passenger localpassenger = passenger.get();
            flightRepository.incrementSeatsByFlightId(localpassenger.getFlightID());
        }
        passengerRepository.deleteById(id);
        return "redirect:/passengers";
    }

//    show update passenger form
    @GetMapping("/update_passenger")
    public String getUpdatePassenger(@ModelAttribute Passenger passenger, Model model) {
        model.addAttribute("passengers", passengerRepository.findAll());
        model.addAttribute("passenger", passenger);
        return "update_passenger";
    }

//    update passenger in database
    @Transactional
    @PostMapping("/update_passenger")
    public String updatePassenger(@RequestParam("id") Long id,
                                  @RequestParam(value = "flightID", required = false)  Long flightID,
                                  @RequestParam(value = "firstName", required = false) String firstName,
                                  @RequestParam(value = "lastName", required = false) String lastName,
                                  @RequestParam(value = "telephone", required = false) String telephone,
                                  @ModelAttribute("passenger") @Valid Passenger passenger,
                                  BindingResult result,
                                  Model model) {

        model.addAttribute("passengers", passengerRepository.findAll());

        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger actualPassenger = optionalPassenger.get();

            if (flightID != null) {
                Optional<Flight> optionalFlight = flightRepository.findById(flightID);

                if(optionalFlight.isPresent())
                {
                    Flight flight = optionalFlight.get();
                    if(flight.getSeats() <= 0){
                        model.addAttribute("error", "Selected flight is full. Please choose another flight.");
                        return "/update_passenger";
                    }
                }

                if (optionalFlight.isEmpty()) {
                    model.addAttribute("error", "Flight with ID " + flightID + " does not exist");
                    return "/update_passenger";
                }
                flightRepository.decrementSeatsByFlightId(flightID);
                flightRepository.incrementSeatsByFlightId(actualPassenger.getFlightID());
                actualPassenger.setFlightID(flightID);
            }
            if (firstName != null && !firstName.isEmpty()) {
                if (result.hasFieldErrors("firstName")) {
                    model.addAttribute("firstNameErrors", result);
                    return "/update_passenger";
                }
                actualPassenger.setFirstName(firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                if (result.hasFieldErrors("lastName")) {
                    return "/update_passenger";
                }
                actualPassenger.setLastName(lastName);
            }
            if (telephone != null && !telephone.isEmpty()) {
                if (result.hasFieldErrors("telephone")) {
                    return "/update_passenger";
                }
                actualPassenger.setTelephone(telephone);
            }
            passengerRepository.save(actualPassenger);
        }
        return "redirect:/passengers";
    }




}
