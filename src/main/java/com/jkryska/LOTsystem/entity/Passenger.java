package com.jkryska.LOTsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "passenger")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "flight_id") //mapowanie obiektowo relacyjne
    private String flightID;

    @Column(name = "first_name") //mapowanie obiektowo relacyjne
    private String firstName;

    @Column(name = "last_name") //mapowanie obiektowo relacyjne
    private String lastName;

    @Column(name = "telephone") //mapowanie obiektowo relacyjne
    private String telephone;

    public Passenger() {
    }

    public Passenger(Long id, String flightID, String firstName, String lastName, String telephone) {
        this.id = id;
        this.flightID = flightID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightID() {
        return flightID;
    }

    public void setFlightID(String flightID) {
        this.flightID = flightID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", flightID='" + flightID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
