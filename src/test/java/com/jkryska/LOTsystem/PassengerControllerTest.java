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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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


}
