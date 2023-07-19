package com.example.town_application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "motion_state", schema = "public", catalog = "Town_Database")
public class MotionState {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "motion_state_id", nullable = false)
    private int motionStateId;
    @Basic
    @Column(name = "state", nullable = false, length = 255, unique = true)
    private String state;



}
