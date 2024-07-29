package com.jkryska.LOTsystem.service;

import com.jkryska.LOTsystem.Exceptions.AppException;
import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private FlightRepository flightRepository;


    public List<Passenger> getAllPassengers() {
        return passengerRepository.findAll();
    }

    public Passenger createPassenger() {
        return new Passenger();
    }

    public Passenger createPassengerWithFlightDetails(Flight flight) {
        Passenger passenger = createPassenger(); // Assuming createPassenger initializes a new Passenger
        passenger.setFlightID(flight.getId());
        return passenger;
    }

    public Passenger getPassenger(Long id) {
        Optional<Passenger> passengerOpt = passengerRepository.findById(id);
        if (passengerOpt.isPresent()) {
            return passengerOpt.get();
        }
        throw new AppException("Passenger does not exist", HttpStatus.BAD_REQUEST);
    }

    @Transactional
    public String savePassenger(Passenger passenger, Model model) {
        if (passenger == null) {
            return "/create_passenger";
        }
        Optional<Flight> optionalFlight = flightRepository.findById(passenger.getFlightID());
        if (optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            if (flight.getSeats() <= 0) {
                model.addAttribute("flights", flightRepository.findAll());
                model.addAttribute("error", "Selected flight is full. Please choose another flight.");
                return "/create_passenger";
            }
        }
        flightRepository.decrementSeatsByFlightId(passenger.getFlightID());
        passengerRepository.save(passenger);
        return "redirect:/passengers";
    }

    @Transactional
    public void deletePassenger(Long id) {
        Optional<Passenger> passenger = passengerRepository.findById(id);
        if (passenger.isPresent()) {
            Passenger localpassenger = passenger.get();
            flightRepository.incrementSeatsByFlightId(localpassenger.getFlightID());
        }
        passengerRepository.deleteById(id);
    }


    @Transactional
    public String updatePassenger(Long id, Long flightID, String firstName, String lastName, String telephone, Passenger passenger, BindingResult result, Model model) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(id);
        if (optionalPassenger.isPresent()) {
            Passenger actualPassenger = optionalPassenger.get();

            if (flightID != null) {
                Optional<Flight> optionalFlight = flightRepository.findById(flightID);

                if (optionalFlight.isPresent()) {
                    Flight flight = optionalFlight.get();
                    if (flight.getSeats() <= 0) {
                        model.addAttribute("error", "Selected flight is full. Please choose another flight.");
                        return "edit_passenger";
                    }
                } else {
                    model.addAttribute("error", "Flight with ID " + flightID + " does not exist");
                    return "edit_passenger";
                }
                flightRepository.decrementSeatsByFlightId(flightID);
                flightRepository.incrementSeatsByFlightId(actualPassenger.getFlightID());
                actualPassenger.setFlightID(flightID);
            }
            if (firstName != null && !firstName.isEmpty()) {
                if (result.hasFieldErrors("firstName")) {
                    model.addAttribute("firstNameErrors", result);
                    return "edit_passenger";
                }
                actualPassenger.setFirstName(firstName);
            }
            if (lastName != null && !lastName.isEmpty()) {
                if (result.hasFieldErrors("lastName")) {
                    return "edit_passenger";
                }
                actualPassenger.setLastName(lastName);
            }
            if (telephone != null && !telephone.isEmpty()) {
                if (result.hasFieldErrors("telephone")) {
                    return "edit_passenger";
                }
                actualPassenger.setTelephone(telephone);
            }

            passengerRepository.save(actualPassenger);
        } else {
            model.addAttribute("error", "Passenger not found");
            return "edit_passenger";
        }
        return null;
    }
}

