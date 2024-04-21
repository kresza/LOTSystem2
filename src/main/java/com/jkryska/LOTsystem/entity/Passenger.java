package com.jkryska.LOTsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "passenger")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "cannot be empty")
    @Positive(message = "Only positive numbers are allowed!")
    @Digits(integer = 10, fraction = 0, message = "Only numbers with 0 fractional digits are allowed!")
    @Column(name = "flight_id", nullable = false) //mapowanie obiektowo relacyjne
    private Long flightID;

    @NotEmpty(message = "cannot be empty")
    @Pattern(regexp = "[a-zA-ZęĘóÓąĄśŚłŁżŻźŹćĆńŃ]+", message = "Only alphabetical letters are allowed!!")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "cannot be empty")
    @Pattern(regexp = "[a-zA-ZęĘóÓąĄśŚłŁżŻźŹćĆńŃ]+", message = "Only alphabetical letters are allowed!!")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "cannot be empty")
    @Column(name = "telephone", nullable = false)
    @Pattern(regexp="\\d{9}", message="Telephone must be 9 digits")
    private String telephone;

    public Passenger() {
    }

    public Passenger(Long id, Long flightID, String firstName, String lastName, String telephone) {
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

    public Long getFlightID() {
        return flightID;
    }

    public void setFlightID(Long flightID) {
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
