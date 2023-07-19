package com.example.town_application.utility;

import net.bytebuddy.utility.RandomString;

import java.io.Serializable;

public class AuthToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String authToken;
    private final String email;
    private final Integer permissionLevel;
    private static final RandomString rString = new RandomString(20);


    public AuthToken(String authToken, String email, Integer permissionLevel) { // TODO: delete this constructor, made for testing
        this.authToken = authToken;
        this.email = email;
        this.permissionLevel = permissionLevel;
    }

    public AuthToken(String email, Integer permissionLevel) {
        this.authToken = rString.nextString();
        this.email = email;
        this.permissionLevel = permissionLevel;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getEmail() {
        return email;
    }

    public Integer getPermissionLevel() {
        return permissionLevel;
    }
}
