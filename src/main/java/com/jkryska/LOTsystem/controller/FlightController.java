package com.jkryska.LOTsystem.controller;

import com.jkryska.LOTsystem.Exceptions.AppException;
import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.service.FlightService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Controller
public class FlightController {

    @Autowired
    private FlightService flightService;


//  show flights list
    @GetMapping("/flights")
    public String getFlights(Model model) {
        model.addAttribute("flights", flightService.getAllFlights());
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
        if(flightService.saveFlight(flight, model,result) == null){
            return "redirect:/flights";
        }
        return "create_flight";
    }

// show delete flight form
    @GetMapping("/delete_flight")
    public String getDeleteFlight(Model model){
        model.addAttribute("flights", flightService.getAllFlights());
        return "/delete_flight";
    }

// delete from database
    @PostMapping("/delete_flight")
    String deleteFlight(@ModelAttribute("flight")  Flight flight,
                        BindingResult result,
                        @RequestParam("id") @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Id must be a number") Long id,
                        Model model){

        flightService.deleteFlight(id,result,model);
        return "/delete_flight";
    }

//   show update flight
    @GetMapping("/update_flight")
    public String getUpdateFlight(@ModelAttribute Flight flight, Model model){
        model.addAttribute("flights", flightService.getAllFlights());
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
                               @Valid Flight flight,
                               BindingResult result,
                               Model model) {

        try{
            flightService.updateFlight(id, flightNumber,startingPlace,destination,flightDate,seats,flight,result,model);
        }
        catch (AppException ex){
            return "/update_flight";
        }
        return "redirect:/flights";

    }

//    sort ASC flights
    @GetMapping("/flights/ASC/{param}")
    public String sortingASC(@PathVariable String param, Model model){
        model.addAttribute("flights", flightService.sortFlightsByASC(param));
        return "flights";
    }

//    sort DESC flights
    @GetMapping("/flights/DESC/{param}")
    public String sortingDESC(@PathVariable String param, Model model){
        model.addAttribute("flights", flightService.sortFlightsByDESC(param));
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

        List<Flight> resultFlights = flightService.searchFlight(id,flightNumber,startingPlace,destination,flightDate,seats);
        if(resultFlights.isEmpty()){
            model.addAttribute("flights", flightService.getAllFlights());
            return "/search_flight";
        }
        model.addAttribute("flights", resultFlights);
        return "/search_flight";
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex, Model model) {
        model.addAttribute("error", "Invalid input: " + ex.getValue());
        model.addAttribute("flights", flightService.getAllFlights());
        return "/search_flight";
    }
}
