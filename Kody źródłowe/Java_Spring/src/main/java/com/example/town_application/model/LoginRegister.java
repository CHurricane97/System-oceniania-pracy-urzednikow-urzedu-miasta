package com.example.town_application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Optional;

@Getter
@Setter
@Entity
@Table(name = "login_register", schema = "public", catalog = "Town_Database")
public class LoginRegister {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "login_register_id", nullable = false)
    private int loginRegisterId;
    @Basic
    @Column(name = "date_of_logging", nullable = false)
    private Timestamp dateOfLogging;
    @ManyToOne(targetEntity = Users.class)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Users usersByUserId;

public LoginRegister(Timestamp dateOfLogging, Users users){
    this.dateOfLogging=dateOfLogging;
    this.usersByUserId=users;
}

    public LoginRegister(){
    }

}
