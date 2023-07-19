package com.example.town_application.model.dto.loginRegisterDTOs;

import com.example.town_application.model.Users;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class LoginReg {


    private int loginRegisterId;

    private Timestamp dateOfLogging;

    private String usersByUserIdLogin;

    private String usersByUserIdPermissionLevel;

    private String usersByUserIdPersonalDataName;

    private String usersByUserIdPersonalDataSurname;
}
