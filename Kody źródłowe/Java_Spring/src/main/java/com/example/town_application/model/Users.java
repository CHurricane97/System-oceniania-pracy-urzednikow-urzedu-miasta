package com.example.town_application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Users {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id", nullable = false)
    private int userId;

    @Basic
    @Column(name = "login", nullable = false, length = 255, unique = true)
    private String login;

    @Basic
    @Column(name = "password", nullable = false, length = 511)
    private String password;

    @Basic
    @Column(name = "permission_level", nullable = false, length = 255)
    private String permissionLevel;

    @ManyToOne(targetEntity = PersonalData.class)
    @JoinColumn(name = "personal_data_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PersonalData personalDataForUsers;


    public Users(String login, String password, String permissionLevel, PersonalData pdata) {
        this.login = login;
        this.password = password;
        this.permissionLevel = permissionLevel;
        this.personalDataForUsers = pdata;
    }

    public Users() {

    }
}
