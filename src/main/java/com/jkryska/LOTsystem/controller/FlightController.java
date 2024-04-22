package com.jkryska.LOTsystem.controller;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class FlightController {
    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private PassengerRepository passengerRepository;



//  show flights list
    @GetMapping("/flights")
    public String getFlights(Model model) {
        model.addAttribute("flights", flightRepository.findAll());
        return "flights";
    }
// show create flight page
    @GetMapping("/create_flight")
    public String createFlight(Model model){
        model.addAttribute("flight", new Flight());
        return "create_flight";
    }
// save flight to database
    @PostMapping("/create_flight")
    public String saveFlight(@ModelAttribute("flight") @Valid Flight flight, BindingResult result, Model model){
        if(result.hasErrors()){
            return "/create_flight";
        }
        for(var localFlight : flightRepository.findAll()){
            if(Objects.equals(localFlight.getFlightNumber(), flight.getFlightNumber())){
                model.addAttribute("error", "Flight Number already exist");
                return "/create_flight";
            }
        }
        flightRepository.save(flight);
        return "redirect:/flights";

    }
// show delete flight form
    @GetMapping("/delete_flight")
    public String getDeleteFlight(Model model){
        model.addAttribute("flights", flightRepository.findAll());
        return "/delete_flight";
    }
// delete from database
    @PostMapping("/delete_flight")
    String deleteFlight(@ModelAttribute("flight")  Flight flight,
                        BindingResult result,
                        @RequestParam("id") @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Id must be a number") Long id,
                        Model model){
        if (result.hasFieldErrors("id")){
            model.addAttribute("flights", flightRepository.findAll());
            return "/delete_flight";
        }

        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if (optionalFlight.isEmpty()) {
            model.addAttribute("error", "Flight with ID " + id + " does not exist");
            model.addAttribute("flights", flightRepository.findAll());
            return "/delete_flight";
        }


        for (var passenger : passengerRepository.findAll()){
            if(passenger.getFlightID().equals(id)) passengerRepository.deleteById(passenger.getId());
        }
        flightRepository.deleteById(id);
        model.addAttribute("flights", flightRepository.findAll());
        return "/delete_flight";
    }
//   show update flight
@GetMapping("/update_flight")
public String getUpdateFlight(@ModelAttribute Flight flight, Model model){
    model.addAttribute("flights", flightRepository.findAll());
    model.addAttribute("flight", flight);
    return "update_flight";
}
// update flight in database
@PostMapping("/update_flight")
    public String updateFlight(@RequestParam("id")  Long id,
                               @RequestParam(value = "flightNumber", required = false ) String flightNumber,
                               @RequestParam(value = "startingPlace", required = false) String startingPlace,
                               @RequestParam(value = "destination", required = false) String destination,
                               @RequestParam(value = "flightDate", required = false) String flightDate,
                               @RequestParam(value = "seats", required = false) Integer seats,
                               @ModelAttribute("flight") @Valid Flight flight,
                               BindingResult result,
                               Model model) {

    Optional<Flight> optionalFlight = flightRepository.findById(id);
    if (optionalFlight.isPresent()) {
        Flight actualFlight = optionalFlight.get();

        if (flightNumber != null && !flightNumber.isEmpty()) {
            if (result.hasFieldErrors("flightNumber")) {
                model.addAttribute("flights", flightRepository.findAll());
                return "/update_flight";
            }
            List<Flight> flights = flightRepository.findAll();
            for(var localFlight : flights){
                if(Objects.equals(localFlight.getFlightNumber(), flight.getFlightNumber())){
                    model.addAttribute("flights", flights);
                    model.addAttribute("error", "Flight Number already exist");
                    return "/update_flight";
                }
            }
            actualFlight.setFlightNumber(flightNumber);
        }
        if (startingPlace != null && !startingPlace.isEmpty()) {
            if (result.hasFieldErrors("startingPlace")) {
                model.addAttribute("flights", flightRepository.findAll());
                return "/update_flight";
            }
            actualFlight.setStartingPlace(startingPlace);
        }
        if (destination != null && !destination.isEmpty()) {
            if (result.hasFieldErrors("destination")) {
                model.addAttribute("flights", flightRepository.findAll());
                return "/update_flight";
            }
            actualFlight.setDestination(destination);
        }
            if (flightDate != null && !flightDate.isEmpty()) {
                if (result.hasFieldErrors("flightDate")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    return "/update_flight";
                }
                actualFlight.setFlightDate(flightDate);
            }
            if (seats != null) {
                if (result.hasFieldErrors("seats")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    return "/update_flight";
                }
                actualFlight.setSeats(seats);
            }

            flightRepository.save(actualFlight);
        }
        return "redirect:/flights";
    }

//    sort ASC flights
    @GetMapping("/flights/ASC/{param}")
    public String sortingASC(@PathVariable String param, Model model){
        model.addAttribute("flights", flightRepository.sortFlightsByASC(param));
        return "flights";
    }

//    sort DESC flights
    @GetMapping("/flights/DESC/{param}")
    public String sortingDESC(@PathVariable String param, Model model){
        model.addAttribute("flights", flightRepository.sortFlightsByDESC(param));
        return "flights";

    }
// show search flight
    @DateTimeFormat
    @GetMapping("/search_flight")
    public String searchFlight(@RequestParam(value = "id", required = false) Long id,
                               @RequestParam(value = "flightNumber", required = false ) String flightNumber,
                               @RequestParam(value = "startingPlace", required = false) String startingPlace,
                               @RequestParam(value = "destination", required = false) String destination,
                               @RequestParam(value = "flightDate", required = false) String flightDate,
                               @RequestParam(value = "seats", required = false) Integer seats,
                               Model model){

        List<Flight> resultFlights = new ArrayList<>() {};
        for(var flight : flightRepository.findAll()){
            if(id != null && flight.getId() == id) {
                resultFlights.add(flight);
            }
            else if(flightNumber != null && !flightNumber.isEmpty() && flight.getFlightNumber().equals(flightNumber)) resultFlights.add(flight);
            else if(startingPlace != null && !startingPlace.isEmpty() && flight.getStartingPlace().equals(startingPlace)) resultFlights.add(flight);
            else if(destination != null &&!destination.isEmpty() && flight.getDestination().equals(destination)) resultFlights.add(flight);
            else if(flightDate != null &&!flightDate.isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder(flight.getFlightDate());
                StringBuilder stringBuilder2 = new StringBuilder(flightDate);
                stringBuilder.delete(10, 19);
                if(stringBuilder.compareTo(stringBuilder2) == 0) resultFlights.add(flight);
            }
            else if(seats != null && Objects.equals(flight.getSeats(), seats)) resultFlights.add(flight);
        }
        if(resultFlights.isEmpty()){
            model.addAttribute("flights", flightRepository.findAll());
            return "/search_flight";
        }
        model.addAttribute("flights", resultFlights);
        return "/search_flight";

    }
}
