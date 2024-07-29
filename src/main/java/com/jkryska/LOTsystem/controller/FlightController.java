package com.jkryska.LOTsystem.controller;

import com.jkryska.LOTsystem.Exceptions.AppException;
import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.service.FlightService;
import jakarta.validation.Valid;
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
        try{
            flightService.saveFlight(flight, model,result);
            return "redirect:/flights";
        }catch (AppException e){
            return "create_flight";
        }
    }

    @PostMapping("/flights/{id}")
    String deleteFlight(@PathVariable Long id,Model model) {
        try{
            flightService.deleteFlight(id, model);
            model.addAttribute("success", "Flight successful deleted");
        }catch (AppException e){
            model.addAttribute("error", "invalid id");
            return "/flights";
        }
        return "/flights";
    }
    @GetMapping("/flights/{id}")
    public String handleGetRequest(@PathVariable Long id, Model model) {
        model.addAttribute("error", "invalid id");
        model.addAttribute("flights", flightService.getAllFlights());
        return "/flights";
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
