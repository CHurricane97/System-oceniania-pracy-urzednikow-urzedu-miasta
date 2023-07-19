package com.example.town_application.controller;

import com.example.town_application.utility.JwtResponse;
import com.example.town_application.utility.JwtUtils;
import com.example.town_application.utility.MessageResponse;
import com.example.town_application.utility.requests.account.ChangePasswordAdminRequest;
import com.example.town_application.utility.requests.account.ChangePasswordUserReqest;
import com.example.town_application.utility.requests.account.LoginRequest;
import com.example.town_application.utility.requests.account.SignupRequest;
import com.example.town_application.model.LoginRegister;
import com.example.town_application.model.PersonalData;
import com.example.town_application.model.Users;
import com.example.town_application.repository.LoginRegisterRepository;
import com.example.town_application.repository.PersonalDataRepository;
import com.example.town_application.repository.UsersRepository;
import com.example.town_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Timestamp;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    UsersRepository userRepository;
    PersonalDataRepository personalDataRepository;
    PasswordEncoder encoder;
    LoginRegisterRepository loginRegisterRepository;
    JwtUtils jwtUtils;
    UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLoginRegisterRepository(LoginRegisterRepository loginRegisterRepository) {
        this.loginRegisterRepository = loginRegisterRepository;
    }

    @Autowired
    public void setPersonalDataRepository(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setUsersRepository(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/loginUser")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        if (!userRepository.existsByLoginAndPermissionLevel(loginRequest.getLogin(), "ROLE_USER")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: nie znaleziono konta użytkownika"));
        }

        String jwt = jwtUtils.generateJwtToken(authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getLogin(),
                                loginRequest.getPassword())));


            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Users users = userRepository.findByLogin(loginRequest.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("Bład: Nie można znaleźć użytkownika"));
            LoginRegister loginRegister = new LoginRegister(timestamp, users);
            loginRegisterRepository.save(loginRegister);


        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    @PostMapping("/loginAdmin")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        if (!userRepository.existsByLoginAndPermissionLevel(loginRequest.getLogin(), "ROLE_ADMIN")) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: nie znaleziono konta administratora"));
        }

        String jwt = jwtUtils.generateJwtToken(authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getLogin(),
                                loginRequest.getPassword())));


            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Users users = userRepository.findByLogin(loginRequest.getLogin())
                    .orElseThrow(() -> new UsernameNotFoundException("Bład: Nie można znaleźć użytkownika"));
            LoginRegister loginRegister = new LoginRegister(timestamp, users);
            loginRegisterRepository.save(loginRegister);


        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer"));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByLogin(signUpRequest.getLogin())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Użytkownik już isnieje"));
        }

        PersonalData personalData = personalDataRepository.findByPesel(signUpRequest.getPesel())
                .orElseThrow(() -> new UsernameNotFoundException("Błąd nie znaleziono osoby z peselem: " + signUpRequest.getPesel()));

        if(!(signUpRequest.getRole().equals("ROLE_ADMIN")||signUpRequest.getRole().equals("ROLE_USER"))){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Zła rola"));
        }

        if (userRepository.existsByPermissionLevelAndPersonalDataForUsers(signUpRequest.getRole(), personalData)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Błąd: Konto dla tej osoby z tymi uprawnieniami już istnieje"));
        }



        Users user = new Users(
                signUpRequest.getLogin(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getRole(),
                personalData);

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Użytkownik zarejestrowany"));
    }

    @PutMapping("/changePasswordUser")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> changePasswordUser(@Valid @RequestBody ChangePasswordUserReqest changePasswordUserReqest, HttpServletRequest request) {
        return userService.changePasswordUser(changePasswordUserReqest.getPassword(), changePasswordUserReqest.getNewpassword(), request);
    }

    @PutMapping("/changePasswordAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> changePasswordAdmin(@Valid @RequestBody ChangePasswordAdminRequest changePasswordAdminRequest, HttpServletRequest request) {

        return userService.changePasswordAdmin(changePasswordAdminRequest.getLogin(), changePasswordAdminRequest.getNewpassword(), request);
    }


}
