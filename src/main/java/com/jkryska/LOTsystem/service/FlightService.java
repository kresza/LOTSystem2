package com.jkryska.LOTsystem.service;

import com.jkryska.LOTsystem.Exceptions.AppException;
import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PassengerRepository passengerRepository;

    public List<Flight> getAllFlights(){
        return flightRepository.findAll();
    }

    public Flight getflight(Long id){
        Optional<Flight> flight = flightRepository.findById(id);
        if(flight.isPresent())
        {
            return flight.get();
        }
        else {
            throw new AppException("Flight with this ID does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public void saveFlight(Flight flight, Model model, BindingResult result){
        if(result.hasErrors()){
            throw new AppException("invalid data", HttpStatus.BAD_REQUEST);
        }
        for(var localFlight : flightRepository.findAll()){
            if(Objects.equals(localFlight.getFlightNumber(), flight.getFlightNumber())){
                model.addAttribute("error", "Flight Number already exist");
                throw new AppException("Flight Number already exist", HttpStatus.BAD_REQUEST);
            }
        }
        flightRepository.save(flight);
    }

    @Transactional
    public void deleteFlight(Long id, Model model){

        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if (optionalFlight.isEmpty()) {
            model.addAttribute("error", "Flight with ID " + id + " does not exist");
            model.addAttribute("flights", flightRepository.findAll());
            throw new AppException("Flight with this ID does not exist", HttpStatus.BAD_REQUEST);
        }

        for (var passenger : passengerRepository.findAll()){
            if(passenger.getFlightID().equals(id)) passengerRepository.deleteById(passenger.getId());
        }
        flightRepository.deleteById(id);
        model.addAttribute("flights", flightRepository.findAll());
    }

    @Transactional
    public void updateFlight(Long id, String flightNumber, String startingPlace, String destination, String flightDate, Integer seats, Flight flight, BindingResult result, Model model){
        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if (optionalFlight.isPresent()) {
            Flight actualFlight = optionalFlight.get();

            if (flightNumber != null && !flightNumber.isEmpty()) {
                if (result.hasFieldErrors("flightNumber")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    throw new AppException("invalid Flight Number", HttpStatus.BAD_REQUEST);
                }
                List<Flight> flights = flightRepository.findAll();
                for (var localFlight : flights) {
                    if (!(localFlight.getId() == id) && Objects.equals(localFlight.getFlightNumber(), flightNumber)) {
                        model.addAttribute("flights", flights);
                        model.addAttribute("error", "Flight Number already exists");
                        throw new AppException("Flight Number already exists", HttpStatus.BAD_REQUEST);
                    }
                }
                actualFlight.setFlightNumber(flightNumber);
            }
            if (startingPlace != null && !startingPlace.isEmpty()) {
                if (result.hasFieldErrors("startingPlace")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    throw new AppException("invalid starting place", HttpStatus.BAD_REQUEST);
                }
                actualFlight.setStartingPlace(startingPlace);
            }
            if (destination != null && !destination.isEmpty()) {
                if (result.hasFieldErrors("destination")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    throw new AppException("invalid destination", HttpStatus.BAD_REQUEST);
                }
                actualFlight.setDestination(destination);
            }
            if (flightDate != null && !flightDate.isEmpty()) {
                if (result.hasFieldErrors("flightDate")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    throw new AppException("invalid flight date", HttpStatus.BAD_REQUEST);
                }
                actualFlight.setFlightDate(flightDate);
            }
            if (seats != null) {
                if (result.hasFieldErrors("seats")) {
                    model.addAttribute("flights", flightRepository.findAll());
                    throw new AppException("invalid seats", HttpStatus.BAD_REQUEST);
                }
                actualFlight.setSeats(seats);
            }
            flightRepository.save(actualFlight);
        }
    }
    public List<Flight> sortFlightsByASC(String param){
        return flightRepository.sortFlightsByASC(param);
    }
    public List<Flight> sortFlightsByDESC(String param){
        return flightRepository.sortFlightsByDESC(param);
    }

public List<Flight> searchFlight(Long id, String flightNumber, String startingPlace, String destination, String flightDate, Integer seats) {
    return flightRepository.findAll().stream()
            .filter(flight -> id == null || Objects.equals(flight.getId(), id))
            .filter(flight -> flightNumber == null || flightNumber.isEmpty() || flight.getFlightNumber().equals(flightNumber))
            .filter(flight -> startingPlace == null || startingPlace.isEmpty() || flight.getStartingPlace().equals(startingPlace))
            .filter(flight -> destination == null || destination.isEmpty() || flight.getDestination().equals(destination))
            .filter(flight -> flightDate == null || flightDate.isEmpty() || flight.getFlightDate().substring(0, 10).equals(flightDate))
            .filter(flight -> seats == null || Objects.equals(flight.getSeats(), seats))
            .collect(Collectors.toList());
}

}
