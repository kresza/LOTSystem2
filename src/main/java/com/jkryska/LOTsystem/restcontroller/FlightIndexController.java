package com.jkryska.LOTsystem.restcontroller;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class FlightIndexController {
    @Autowired
    private FlightRepository flightRepository;

//    show flights list
    @GetMapping("/flights")
    public String getFlights(Model model) {
        List<Flight> flights = flightRepository.findAll();
        model.addAttribute("flights", flights);
        return "flights";
    }
// show create flight page
    @GetMapping("/create_flight")
    public String createFlight(Model model){
        Flight flight = new Flight();
        model.addAttribute("flight", flight);
        return "create_flight";
    }
// save flight to database
    @PostMapping("/create_flight")
    public String saveFlight(@ModelAttribute("flight") Flight flight){
        flightRepository.save(flight);
        return "redirect:/flights";
    }
// show delete flight form
    @GetMapping("/delete_flight")
    public String getDeleteFlight(Model model){
        List<Flight> flights = flightRepository.findAll();
        model.addAttribute("flights", flights);
        return "/delete_flight";
    }
//    delete from database
    @PostMapping("/delete_flight")
    String deleteFlight(@RequestParam("id") Long id){
        flightRepository.deleteById(id);
        return "redirect:/flights";
    }
//   show update flight
@GetMapping("/update_flight")
public String getUpdateFlight(Model model){
    List<Flight> flights = flightRepository.findAll();
    model.addAttribute("flights", flights);
    return "update_flight";
}
// update flight in database
@PostMapping("/update_flight")
    public String updateFlight(@RequestParam("id") Long id,
                               @RequestParam(value = "flightNumber", required = false ) String flightNumber,
                               @RequestParam(value = "startingPlace", required = false) String startingPlace,
                               @RequestParam(value = "destination", required = false) String destination,
                               @RequestParam(value = "flightDate", required = false) String flightDate,
                               @RequestParam(value = "seats", required = false) Integer seats){
            Optional<Flight> flight = flightRepository.findById(id);
            if(flight.isPresent()){
                Flight actualFlight = flight.get();

                if(flightNumber != null && !flightNumber.isEmpty()) actualFlight.setFlightNumber(flightNumber);
                if(startingPlace != null && !startingPlace.isEmpty()) actualFlight.setStartingPlace(startingPlace);
                if(destination != null && !destination.isEmpty()) actualFlight.setDestination(destination);
                if(flightDate != null && !flightDate.isEmpty()) actualFlight.setFlightDate(flightDate);
                if(seats != null) {
                    actualFlight.setSeats(seats);
                }
                flightRepository.save(actualFlight);


            }
        return "redirect:/flights";
    }

    @GetMapping("/flights/ASC/{param}")
    public String sortingASC(@PathVariable String param, Model model){
        List<Flight> flights = flightRepository.sortFlightsByASC(param);
        model.addAttribute("flights", flights);
        return "flights";
    }

    @GetMapping("/flights/DESC/{param}")
    public String sortingDESC(@PathVariable String param, Model model){
        List<Flight> flights = flightRepository.sortFlightsByDESC(param);
        model.addAttribute("flights", flights);
        return "flights";

    }




}
