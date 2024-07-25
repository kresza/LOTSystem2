package com.jkryska.LOTsystem.controller;

import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.service.FlightService;
import com.jkryska.LOTsystem.service.PassengerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PassengerController {
    @Autowired
    PassengerService passengerService;

    @Autowired
    FlightService flightService;

//    show passengers
    @GetMapping("/passengers")
    public String allPassengers(Model model){
        model.addAttribute("passengers", passengerService.getAllPassengers());
        return "/passengers"; //tu zmienione zeby nie wchodzilo w petle, trzeba pozmieniac wsztstko
    }

//    show create passenger form
    @GetMapping("/create_passenger")
    public String createPassenger(Model model){
        model.addAttribute("flights", flightService.getAllFlights());
        model.addAttribute("passenger", passengerService.createPassenger());
        return "create_passenger";
    }
//    save passenger in database
    @PostMapping("/create_passenger")
    public String savePassenger(@ModelAttribute("passenger") @Valid Passenger passenger, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("flights", flightService.getAllFlights());
            return "/create_passenger";
        }
        return passengerService.savePassenger(passenger, model);
    }

//    delete passenger
    @PostMapping(value = "/passengers/{id}")
    String deletePassenger(@PathVariable Long id){
        passengerService.deletePassenger(id);
        return "redirect:/passengers";
    }

//    show update passenger form
    @GetMapping("/update_passenger")
    public String getUpdatePassenger(@ModelAttribute Passenger passenger, Model model) {
        model.addAttribute("passengers", passengerService.getAllPassengers());
        model.addAttribute("passenger", passenger);
        return "update_passenger";
    }

//    update passenger in database
    @PostMapping("/update_passenger")
    public String updatePassenger(@RequestParam("id") Long id,
                                  @RequestParam(value = "flightID", required = false)  Long flightID,
                                  @RequestParam(value = "firstName", required = false) String firstName,
                                  @RequestParam(value = "lastName", required = false) String lastName,
                                  @RequestParam(value = "telephone", required = false) String telephone,
                                  @ModelAttribute("passenger") @Valid Passenger passenger,
                                  BindingResult result,
                                  Model model) {

        String error = passengerService.updatePassenger(id,flightID,firstName,lastName,telephone,passenger,result,model);
        if(error != null){
            model.addAttribute("passengers", passengerService.getAllPassengers());
            return "/update_passenger";
        }
        return "redirect:/passengers";
    }

}
