package com.jkryska.LOTsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @Positive(message = "Only positive numbers are allowed!")
    @Digits(integer = 10, fraction = 0, message = "Only numbers with 0 fractional digits are allowed!")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "cannot be empty")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Only alphabetical letters and numbers are allowed!")
    @Column(name = "flight_number") //mapowanie obiektowo relacyjne
    private String flightNumber;

    @NotEmpty(message = "cannot be empty")
    @Pattern(regexp = "[a-zA-ZęĘóÓąĄśŚłŁżŻźŹćĆńŃ ]+", message = "Only alphabetical letters are allowed!!")
    @Column(name = "starting_place")
    private String startingPlace;

    @NotEmpty(message = "cannot be empty")
    @Pattern(regexp = "[a-zA-ZęĘóÓąĄśŚłŁżŻźŹćĆńŃ ]+", message = "Only alphabetical letters are allowed!!")
    @Column(name = "destination")
    private String destination;

    @NotEmpty(message = "cannot be empty")
    @Column(name = "flight_date")
    private String flightDate;


    @NotNull(message = "cannot be empty")
    @Positive(message = "Only positive numbers are allowed!")
    @Digits(integer = 10, fraction = 0, message = "Only numbers with 0 fractional digits are allowed!")
    @Column(name = "seats")
    private Integer seats;

    public Flight() {
    }

    public Flight(Long id, String flightNumber, String staringPlace, String destination, String flightDate, Integer seats) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.startingPlace = staringPlace;
        this.destination = destination;
        this.flightDate = flightDate;
        this.seats = seats;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getStartingPlace() {
        return startingPlace;
    }

    public void setStartingPlace(String startingPlace) {
        this.startingPlace = startingPlace;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(String flightDate) {
        this.flightDate = flightDate;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "id=" + id +
                ", flightNumber='" + flightNumber + '\'' +
                ", staringPlace='" + startingPlace + '\'' +
                ", destination='" + destination + '\'' +
                ", flightDate='" + flightDate + '\'' +
                ", seats=" + seats +
                '}';
    }
}
