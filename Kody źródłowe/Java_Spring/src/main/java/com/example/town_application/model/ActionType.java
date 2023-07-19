package com.example.town_application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "action_type", schema = "public", catalog = "Town_Database")
public class ActionType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "action_type_id", nullable = false)
    private int actionTypeId;
    @Basic
    @Column(name = "type", nullable = false, length = 255, unique = true)
    private String type;


}
