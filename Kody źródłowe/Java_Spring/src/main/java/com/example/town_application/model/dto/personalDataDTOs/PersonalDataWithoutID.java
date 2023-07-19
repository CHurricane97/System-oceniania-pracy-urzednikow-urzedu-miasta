package com.example.town_application.model.dto.personalDataDTOs;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.sql.Date;
@Getter
@Setter
public class PersonalDataWithoutID {

    private String name;

    private String surname;

    private String pesel;

    private Date dateOfBirth;

    private String city;

    private String cityCode;

    private String street;

    private String houseNumber;

    private String flatNumber;


}
