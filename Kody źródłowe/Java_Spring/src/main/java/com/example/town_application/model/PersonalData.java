package com.example.town_application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "personal_data", schema = "public", catalog = "Town_Database")
public class PersonalData {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "personal_data_id", nullable = false)
    private int personalDataId;

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 255)
    private String surname;

    @Basic
    @Column(name = "pesel", nullable = false, length = 11, unique = true)
    private String pesel;

    @Basic
    @Column(name = "date_of_birth", nullable = true)
    private Date dateOfBirth;

    @Basic
    @Column(name = "city", nullable = true, length = 255)
    private String city;

    @Basic
    @Column(name = "city_code", nullable = true, length = 255)
    private String cityCode;

    @Basic
    @Column(name = "street", nullable = true, length = 255)
    private String street;

    @Basic
    @Column(name = "house_number", nullable = true, length = 255)
    private String houseNumber;

    @Basic
    @Column(name = "flat_number", nullable = true, length = 255)
    private String flatNumber;

    @OneToMany(mappedBy = "personalDataForActionTakenInMotion", cascade = CascadeType.ALL)
    private Collection<ActionTakenInMotion> actionTakenInMotionsByPersonalDataId;

    @OneToMany(mappedBy = "personalDataForUsers", cascade = CascadeType.ALL)
    private Collection<Users> usersByPersonalDataId;

    public PersonalData(String name, String surname, String pesel, Date dateOfBirth, String city, String cityCode, String street, String houseNumber, String flatNumber) {
        this.name = name;
        this.surname = surname;
        this.pesel = pesel;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.cityCode = cityCode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.flatNumber = flatNumber;
    }

    public PersonalData() {

    }
}
