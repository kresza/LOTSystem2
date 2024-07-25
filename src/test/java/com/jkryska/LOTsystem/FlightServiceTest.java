package com.jkryska.LOTsystem;

import com.jkryska.LOTsystem.Exceptions.AppException;
import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.repository.FlightRepository;
import com.jkryska.LOTsystem.repository.PassengerRepository;
import com.jkryska.LOTsystem.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FlightServiceTest {
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private Model model;

    @Mock
    private BindingResult result;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFlights() {
        List<Flight> flights = new ArrayList<>();
        flights.add(new Flight());

        when(flightRepository.findAll()).thenReturn(flights);

        List<Flight> result = flightService.getAllFlights();
        assertEquals(1, result.size());
        verify(flightRepository, times(1)).findAll();
    }

    @Test
    void testSaveFlightWithErrors() {
        when(result.hasErrors()).thenReturn(true);

        Flight flight = new Flight();
        String view = flightService.saveFlight(flight, model, result);

        assertEquals("/create_flight", view);
    }

    @Test
    void testSaveFlightWithExistingFlightNumber() {
        when(result.hasErrors()).thenReturn(false);
        Flight existingFlight = new Flight();
        existingFlight.setFlightNumber("FL123");

        Flight newFlight = new Flight();
        newFlight.setFlightNumber("FL123");

        when(flightRepository.findAll()).thenReturn(List.of(existingFlight));

        String view = flightService.saveFlight(newFlight, model, result);

        assertEquals("/create_flight", view);
        verify(model).addAttribute("error", "Flight Number already exist");
    }

    @Test
    void testSaveFlightSuccessfully() {
        when(result.hasErrors()).thenReturn(false);
        Flight flight = new Flight();
        flight.setFlightNumber("FL123");

        when(flightRepository.findAll()).thenReturn(new ArrayList<>());

        String view = flightService.saveFlight(flight, model, result);

        assertEquals(null, view);
        verify(flightRepository).save(flight);
    }

    @Test
    void testDeleteFlightWithErrors() {
        when(result.hasFieldErrors("id")).thenReturn(true);

        String view = flightService.deleteFlight(1L, result, model);

        assertEquals("/delete_flight", view);
    }

    @Test
    void testDeleteFlightNotFound() {
        when(result.hasFieldErrors("id")).thenReturn(false);
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        String view = flightService.deleteFlight(1L, result, model);

        assertEquals("/delete_flight", view);
        verify(model).addAttribute("error", "Flight with ID 1 does not exist");
    }

    @Test
    void testDeleteFlightSuccessfully() {
        Flight flight = new Flight();
        flight.setId(1L);

        Passenger passenger1 = new Passenger();
        passenger1.setId(1L);
        passenger1.setFlightID(1L);

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFlightID(1L);

        when(result.hasFieldErrors("id")).thenReturn(false);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerRepository.findAll()).thenReturn(List.of(passenger1, passenger2));

        String view = flightService.deleteFlight(1L, result, model);

        verify(passengerRepository).deleteById(1L);
        verify(passengerRepository).deleteById(2L);

        verify(flightRepository).deleteById(1L);

        verify(model).addAttribute("flights", flightRepository.findAll());

        assertEquals(null, view);
    }

    @Test
    void testUpdateFlightWithErrors() {
        // Ustawienie mocka, aby symulować błąd walidacji dla pola "flightNumber"
        when(result.hasFieldErrors("flightNumber")).thenReturn(true);

        // Tworzenie przykładowych danych
        Long id = 1L;
        String flightNumber = "FL123";
        String startingPlace = "Start";
        String destination = "Dest";
        String flightDate = "2024-01-01";
        Integer seats = 100;
        Flight flight = new Flight();

        // Symulowanie znalezienia lotu w repozytorium
        when(flightRepository.findById(id)).thenReturn(Optional.of(new Flight()));

        // Wywołanie metody serwisu
        try {
            flightService.updateFlight(id, flightNumber, startingPlace, destination, flightDate, seats, flight, result, model);
        } catch (AppException ex) {
            // Sprawdzenie, czy wyjątek został rzucony z odpowiednim komunikatem
            assertEquals("invalid Flight Number", ex.getMessage());
        }

        // Weryfikacja, czy metoda serwisu została wywołana
        verify(flightRepository).findById(id);
        verify(result).hasFieldErrors("flightNumber");
        verify(model).addAttribute(eq("flights"), anyList());
    }



    @Test
    void testSortFlightsByASC() {
        List<Flight> flights = List.of(new Flight(), new Flight());

        when(flightRepository.sortFlightsByASC("flightNumber")).thenReturn(flights);

        List<Flight> result = flightService.sortFlightsByASC("flightNumber");
        assertEquals(flights, result);
    }

    @Test
    void testSortFlightsByDESC() {
        List<Flight> flights = List.of(new Flight(), new Flight());

        when(flightRepository.sortFlightsByDESC("flightNumber")).thenReturn(flights);

        List<Flight> result = flightService.sortFlightsByDESC("flightNumber");
        assertEquals(flights, result);
    }
    @Test
    void testSearchFlightById() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<Flight> result = flightService.searchFlight(1L, null, null, null, null, null);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testSearchFlightByFlightNumber() {
        Flight flight = new Flight();
        flight.setFlightNumber("FL123");

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<Flight> result = flightService.searchFlight(null, "FL123", null, null, null, null);
        assertEquals(1, result.size());
        assertEquals("FL123", result.get(0).getFlightNumber());
    }



    @Test
    void testSaveFlightWithMultipleErrors() {
        when(result.hasErrors()).thenReturn(true);

        Flight flight = new Flight();
        String view = flightService.saveFlight(flight, model, result);

        assertEquals("/create_flight", view);
    }



    @Test
    void testDeleteFlightNoPassengers() {
        Flight flight = new Flight();
        flight.setId(1L);

        when(result.hasFieldErrors("id")).thenReturn(false);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(passengerRepository.findAll()).thenReturn(new ArrayList<>());

        String view = flightService.deleteFlight(1L, result, model);

        verify(passengerRepository, never()).deleteById(anyLong());
        verify(flightRepository).deleteById(1L);
        verify(model).addAttribute("flights", flightRepository.findAll());

        assertEquals(null, view);
    }

    @Test
    void testSearchFlightByMultipleCriteria() {
        Flight flight = new Flight();
        flight.setFlightNumber("FL123");
        flight.setStartingPlace("Start");

        when(flightRepository.findAll()).thenReturn(List.of(flight));

        List<Flight> result = flightService.searchFlight(null, "FL123", "Start", null, null, null);
        assertEquals(1, result.size());
        assertEquals("FL123", result.get(0).getFlightNumber());
        assertEquals("Start", result.get(0).getStartingPlace());
    }

    @Test
    void testSortFlightsByASCWithEmptyDatabase() {
        when(flightRepository.sortFlightsByASC("flightNumber")).thenReturn(new ArrayList<>());

        List<Flight> result = flightService.sortFlightsByASC("flightNumber");
        assertEquals(0, result.size());
    }

    @Test
    void testSortFlightsByDESCWithEmptyDatabase() {
        when(flightRepository.sortFlightsByDESC("flightNumber")).thenReturn(new ArrayList<>());

        List<Flight> result = flightService.sortFlightsByDESC("flightNumber");
        assertEquals(0, result.size());
    }









}


