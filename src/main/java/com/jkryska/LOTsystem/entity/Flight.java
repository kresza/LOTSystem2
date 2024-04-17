package com.jkryska.LOTsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_number") //mapowanie obiektowo relacyjne
    private String flightNumber;

    @Column(name = "starting_place")
    private String startingPlace;

    @Column(name = "destination")
    private String destination;

    @Column(name = "flight_date")
    private String flightDate;

    @Column(name = "seats")
    private int seats;

    public Flight() {
    }

    public Flight(long id, String flightNumber, String staringPlace, String destination, String flightDate, int seats) {
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

    public void setId(long id) {
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

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
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
