package com.jkryska.LOTsystem;

import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import com.jkryska.LOTsystem.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PassengerServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @InjectMocks
    private PassengerService passengerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testSavePassengerWithNullPassenger() {
        String view = passengerService.savePassenger(null, model);

        assertEquals("/create_passenger", view);
    }

    @Test
    void testSavePassengerWithFullFlight() {
        Passenger passenger = new Passenger();
        passenger.setFlightID(1L);

        Flight flight = new Flight();
        flight.setSeats(0);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        String view = passengerService.savePassenger(passenger, model);

        assertEquals("/create_passenger", view);
        verify(model).addAttribute("error", "Selected flight is full. Please choose another flight.");
    }

    @Test
    void testDeletePassengerSuccessfully() {
        Passenger passenger = new Passenger();
        passenger.setFlightID(1L);
        passenger.setId(1L);

        Flight flight = new Flight();
        flight.setSeats(50);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        passengerService.deletePassenger(1L);

        verify(flightRepository).incrementSeatsByFlightId(1L);
        verify(passengerRepository).deleteById(1L);
    }

    @Test
    void testDeletePassengerNotFound() {
        when(passengerRepository.findById(1L)).thenReturn(Optional.empty());

        passengerService.deletePassenger(1L);

        verify(flightRepository, never()).incrementSeatsByFlightId(anyLong());
        verify(passengerRepository).deleteById(1L);
    }
    @Test
    void testUpdatePassengerSuccessfully() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);
        existingPassenger.setFlightID(1L);
        existingPassenger.setFirstName("OldName");
        existingPassenger.setLastName("OldLastName");
        existingPassenger.setTelephone("OldTelephone");

        Passenger updatedPassenger = new Passenger();
        updatedPassenger.setFlightID(2L);
        updatedPassenger.setFirstName("John");
        updatedPassenger.setLastName("Doe");
        updatedPassenger.setTelephone("1234567890");

        // Obiekty lotów
        Flight flight1 = new Flight();
        flight1.setSeats(10);
        Flight flight2 = new Flight();
        flight2.setSeats(10);

        // Mockowanie metod repozytoriów
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight1));
        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight2));
        when(result.hasFieldErrors("firstName")).thenReturn(false);
        when(result.hasFieldErrors("lastName")).thenReturn(false);
        when(result.hasFieldErrors("telephone")).thenReturn(false);

        String view = passengerService.updatePassenger(1L, 2L, "John", "Doe", "1234567890", updatedPassenger, result, model);

        ArgumentCaptor<Passenger> passengerCaptor = ArgumentCaptor.forClass(Passenger.class);
        verify(passengerRepository).save(passengerCaptor.capture());

        Passenger capturedPassenger = passengerCaptor.getValue();

        assertEquals(1L, capturedPassenger.getId());
        assertEquals(2L, capturedPassenger.getFlightID());
        assertEquals("John", capturedPassenger.getFirstName());
        assertEquals("Doe", capturedPassenger.getLastName());
        assertEquals("1234567890", capturedPassenger.getTelephone());

        verify(flightRepository).decrementSeatsByFlightId(2L);
        verify(flightRepository).incrementSeatsByFlightId(1L);
    }

    @Test
    void testUpdatePassengerFlightNotExist() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);
        existingPassenger.setFlightID(1L);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(flightRepository.findById(2L)).thenReturn(Optional.empty());
        when(result.hasFieldErrors("flightID")).thenReturn(false);

        String view = passengerService.updatePassenger(1L, 2L, null, null, null, existingPassenger, result, model);

        assertEquals("update_passenger", view);
        verify(model).addAttribute("error", "Flight with ID 2 does not exist");
    }

    @Test
    void testGetAllPassengersWhenEmpty() {
        when(passengerRepository.findAll()).thenReturn(new ArrayList<>());

        List<Passenger> result = passengerService.getAllPassengers();

        assertEquals(0, result.size());
    }

    @Test
    void testSavePassengerWithFullFlightButSeatsAvailableInOtherFlights() {
        Passenger passenger = new Passenger();
        passenger.setFlightID(1L);

        Flight fullFlight = new Flight();
        fullFlight.setSeats(0);
        Flight otherFlight = new Flight();
        otherFlight.setSeats(10);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(fullFlight));
        when(flightRepository.findAll()).thenReturn(List.of(fullFlight, otherFlight));

        String view = passengerService.savePassenger(passenger, model);

        assertEquals("/create_passenger", view);
        verify(model).addAttribute("error", "Selected flight is full. Please choose another flight.");
    }
    @Test
    void testUpdatePassengerSameFlightID() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);
        existingPassenger.setFlightID(1L);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(result.hasFieldErrors("flightID")).thenReturn(false);

        String view = passengerService.updatePassenger(1L, 1L, "John", "Doe", "1234567890", existingPassenger, result, model);

        assertEquals("update_passenger", view);
        verify(flightRepository, never()).decrementSeatsByFlightId(anyLong());
        verify(flightRepository, never()).incrementSeatsByFlightId(anyLong());
    }
    @Test
    void testUpdatePassengerFlightCapacity() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);
        existingPassenger.setFlightID(1L);

        Flight oldFlight = new Flight();
        oldFlight.setSeats(5);
        Flight newFlight = new Flight();
        newFlight.setSeats(10);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(flightRepository.findById(1L)).thenReturn(Optional.of(oldFlight));
        when(flightRepository.findById(2L)).thenReturn(Optional.of(newFlight));
        when(result.hasFieldErrors("flightID")).thenReturn(false);

        String view = passengerService.updatePassenger(1L, 2L, "John", "Doe", "1234567890", existingPassenger, result, model);

        assertEquals(null, view);
        verify(flightRepository).decrementSeatsByFlightId(2L);
        verify(flightRepository).incrementSeatsByFlightId(1L);
    }
    @Test
    void testUpdatePassengerWithManyValidationErrors() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(result.hasFieldErrors("firstName")).thenReturn(true);
        when(result.hasFieldErrors("lastName")).thenReturn(false);
        when(result.hasFieldErrors("telephone")).thenReturn(false);

        String view = passengerService.updatePassenger(1L, 2L, "", "Doe", "1234567890", existingPassenger, result, model);

        assertEquals("update_passenger", view);
        verify(model).addAttribute("error","Flight with ID 2 does not exist");
    }
    @Test
    void testGetAllPassengersWhenListIsEmpty() {
        when(passengerRepository.findAll()).thenReturn(new ArrayList<>());

        List<Passenger> result = passengerService.getAllPassengers();

        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveMultiplePassengersForSameFlight() {
        Passenger passenger1 = new Passenger();
        passenger1.setFlightID(1L);
        Passenger passenger2 = new Passenger();
        passenger2.setFlightID(1L);

        Flight flight = new Flight();
        flight.setSeats(2);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));

        String view1 = passengerService.savePassenger(passenger1, model);
        String view2 = passengerService.savePassenger(passenger2, model);

        assertEquals("redirect:/passengers", view1);
        assertEquals("redirect:/passengers", view2);
        verify(flightRepository, times(2)).decrementSeatsByFlightId(1L);
    }


    @Test
    void testUpdatePassengerWithNullValues() {
        Passenger existingPassenger = new Passenger();
        existingPassenger.setId(1L);
        existingPassenger.setFirstName("John");
        existingPassenger.setLastName("Doe");
        existingPassenger.setTelephone("1234567890");

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(result.hasFieldErrors("firstName")).thenReturn(false);
        when(result.hasFieldErrors("lastName")).thenReturn(false);
        when(result.hasFieldErrors("telephone")).thenReturn(false);

        String view = passengerService.updatePassenger(1L, null, null, null, null, existingPassenger, result, model);

        assertEquals(null, view);
        assertEquals("John", existingPassenger.getFirstName());
        assertEquals("Doe", existingPassenger.getLastName());
        assertEquals("1234567890", existingPassenger.getTelephone());
    }

}
