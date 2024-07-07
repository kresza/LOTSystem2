package com.jkryska.LOTsystem.service;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PassengerRepository passengerRepository;

    public List<Flight> getAllFlights(){
        return flightRepository.findAll();
    }

    @Transactional
    public String saveFlight(Flight flight, Model model, BindingResult result){
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
        return null;
    }

    @Transactional
    public String deleteFlight(Long id, BindingResult result, Model model){
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
        return null;
    }

    @Transactional
    public String updateFlight(Long id, String flightNumber, String startingPlace, String destination, String flightDate, Integer seats, Flight flight, BindingResult result, Model model){
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
        return null;
    }
    public List<Flight> sortFlightsByASC(String param){
        return flightRepository.sortFlightsByASC(param);
    }
    public List<Flight> sortFlightsByDESC(String param){
        return flightRepository.sortFlightsByDESC(param);
    }

    public List<Flight> searchFlight(Long id, String flightNumber, String startingPlace, String destination, String flightDate, Integer seats){


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
        return resultFlights;
    }

}
