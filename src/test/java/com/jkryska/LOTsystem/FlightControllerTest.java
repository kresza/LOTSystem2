package com.jkryska.LOTsystem;

import com.jkryska.LOTsystem.controller.FlightController;
import com.jkryska.LOTsystem.entity.Flight;
import com.jkryska.LOTsystem.service.FlightService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FlightController.class)
public class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightService flightService;

    @Test
    public void testGetFlights() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("1234");
        flight.setStartingPlace("CityA");
        flight.setDestination("CityB");
        flight.setFlightDate("2024-07-23");
        flight.setSeats(150);

        Mockito.when(flightService.getAllFlights()).thenReturn(List.of(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/flights"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("flights"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attribute("flights", List.of(flight)))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void testSaveFlight() throws Exception {
        Flight flight = new Flight();
        flight.setFlightNumber("1234");
        flight.setStartingPlace("CityA");
        flight.setDestination("CityB");
        flight.setFlightDate("2024-07-23");
        flight.setSeats(150);

        Mockito.when(flightService.saveFlight(
                Mockito.any(Flight.class),
                Mockito.any(),
                Mockito.any()
        )).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/create_flight")
                        .flashAttr("flight", flight))
                .andExpect(status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/flights"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void testUpdateFlight() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("1234");
        flight.setStartingPlace("CityA");
        flight.setDestination("CityB");
        flight.setFlightDate("2024-07-23");
        flight.setSeats(150);

        Mockito.when(flightService.updateFlight(
                Mockito.anyLong(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.anyInt(),
                Mockito.any(Flight.class),
                Mockito.any(BindingResult.class),
                Mockito.any(Model.class)
        )).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/update_flight")
                        .param("id", "1")
                        .param("flightNumber", "1234")
                        .param("startingPlace", "CityA")
                        .param("destination", "CityB")
                        .param("flightDate", "2024-07-23")
                        .param("seats", "150"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/flights"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCreateFlight() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/create_flight"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_flight"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testSaveFlightWithErrors() throws Exception {
        Flight flight = new Flight();
        Mockito.when(flightService.saveFlight(Mockito.any(Flight.class), Mockito.any(), Mockito.any()))
                .thenReturn("/create_flight");

        mockMvc.perform(MockMvcRequestBuilders.post("/create_flight")
                        .flashAttr("flight", flight))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_flight"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetDeleteFlight() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        Mockito.when(flightService.getAllFlights()).thenReturn(List.of(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/delete_flight"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/delete_flight"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void testGetUpdateFlight() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        Mockito.when(flightService.getAllFlights()).thenReturn(List.of(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/update_flight")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update_flight"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flight"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testSortingASC() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        Mockito.when(flightService.sortFlightsByASC(Mockito.anyString())).thenReturn(List.of(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/flights/ASC/flightNumber"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("flights"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testSortingDESC() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        Mockito.when(flightService.sortFlightsByDESC(Mockito.anyString())).thenReturn(List.of(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/flights/DESC/flightNumber"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("flights"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("flights"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testSearchFlight() throws Exception {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("1234");
        flight.setStartingPlace("CityA");
        flight.setDestination("CityB");
        flight.setFlightDate("2024-07-23");
        flight.setSeats(150);

        Mockito.when(flightService.searchFlight(
                Mockito.eq(1L),
                Mockito.eq("1234"),
                Mockito.eq("CityA"),
                Mockito.eq("CityB"),
                Mockito.eq("2024-07-23"),
                Mockito.eq(150)
        )).thenReturn(List.of(flight));

        mockMvc.perform(MockMvcRequestBuilders.get("/search_flight")
                        .param("id", "1")
                        .param("flightNumber", "1234")
                        .param("startingPlace", "CityA")
                        .param("destination", "CityB")
                        .param("flightDate", "2024-07-23")
                        .param("seats", "150"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/search_flight"))
                .andExpect(MockMvcResultMatchers.model().attribute("flights", List.of(flight)))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testDeleteFlight() throws Exception {
        Mockito.when(flightService.deleteFlight(
                Mockito.anyLong(),
                Mockito.any(BindingResult.class),
                Mockito.any(Model.class))
        ).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/delete_flight")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/delete_flight"))
                .andExpect(MockMvcResultMatchers.model().attribute("attributeName", Matchers.nullValue()))
                .andDo(MockMvcResultHandlers.print());
    }






}
