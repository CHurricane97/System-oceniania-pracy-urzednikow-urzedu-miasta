package com.example.town_application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "motion_type", schema = "public", catalog = "Town_Database")
public class MotionType {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "motion_type_id", nullable = false)
    private int motionTypeId;
    @Basic
    @Column(name = "type", nullable = false, length = 255, unique = true)
    private String type;


    public MotionType(String type) {
        this.type = type;
    }

    public MotionType() {

    }
}
