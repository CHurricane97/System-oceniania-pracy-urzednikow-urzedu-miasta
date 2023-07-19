package com.example.town_application.repository;

import com.example.town_application.model.LoginRegister;
import com.example.town_application.model.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LoginRegisterRepository extends JpaRepository<LoginRegister, Integer> {











}
