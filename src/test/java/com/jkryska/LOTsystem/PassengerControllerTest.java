package com.jkryska.LOTsystem;

import com.jkryska.LOTsystem.controller.PassengerController;
import com.jkryska.LOTsystem.entity.Passenger;
import com.jkryska.LOTsystem.service.FlightService;
import com.jkryska.LOTsystem.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PassengerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PassengerService passengerService;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private PassengerController passengerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(passengerController).build();
    }

    @Test
    public void testDeletePassenger() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/passengers/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/passengers"));
    }

    @Test
    public void testUpdatePassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFirstName("John");

        when(passengerService.updatePassenger(
                Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/update_passenger")
                        .param("id", "1")
                        .param("flightID", "2")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("telephone", "123456789")
                        .flashAttr("passenger", passenger))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/passengers"));
    }

    @Test
    public void testAllPassengers() throws Exception {
        Passenger passenger1 = new Passenger();
        passenger1.setId(1L);
        passenger1.setFirstName("John");
        passenger1.setLastName("Doe");

        Passenger passenger2 = new Passenger();
        passenger2.setId(2L);
        passenger2.setFirstName("Jane");
        passenger2.setLastName("Doe");

        List<Passenger> passengers = List.of(passenger1, passenger2);

        when(passengerService.getAllPassengers()).thenReturn(passengers);

        mockMvc.perform(MockMvcRequestBuilders.get("/passengers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/getpassengers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passengers"))
                .andExpect(MockMvcResultMatchers.model().attribute("passengers", passengers))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreatePassenger() throws Exception {
        when(flightService.getAllFlights()).thenReturn(Collections.emptyList()); // Można zastąpić testowymi danymi

        mockMvc.perform(MockMvcRequestBuilders.get("/create_passenger"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_passenger"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passenger"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testSavePassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setFirstName("John");
        passenger.setLastName("Doe");

        when(passengerService.savePassenger(
                Mockito.any(Passenger.class),
                Mockito.any()
        )).thenReturn("redirect:/passengers");

        mockMvc.perform(MockMvcRequestBuilders.post("/create_passenger")
                        .flashAttr("passenger", passenger))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/passengers"))
                .andDo(MockMvcResultHandlers.print());
    }



    @Test
    public void testCreatePassengerWithErrors() throws Exception {
        Passenger passenger = new Passenger();
        when(passengerService.savePassenger(Mockito.any(Passenger.class), Mockito.any()))
                .thenReturn("/create_passenger");

        mockMvc.perform(MockMvcRequestBuilders.post("/create_passenger")
                        .flashAttr("passenger", passenger))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_passenger"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void testGetUpdatePassenger() throws Exception {
        Passenger passenger = new Passenger();
        passenger.setId(1L);

        when(passengerService.getAllPassengers()).thenReturn(List.of(passenger));

        mockMvc.perform(MockMvcRequestBuilders.get("/update_passenger")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update_passenger"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passengers"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("passenger"))
                .andDo(MockMvcResultHandlers.print());
    }


}
