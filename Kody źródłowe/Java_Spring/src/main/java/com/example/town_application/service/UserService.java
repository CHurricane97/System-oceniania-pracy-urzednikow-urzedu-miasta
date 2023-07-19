package com.example.town_application.service;

import com.example.town_application.utility.JwtUtils;
import com.example.town_application.utility.MessageResponse;
import com.example.town_application.model.Users;
import com.example.town_application.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PersonalDataRepository personalDataRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository userRepository;
    private final ModelMapper modelMapper;
    private final MotionRepository motionRepository;
    private final MotionStateRepository motionStateRepository;
    private final MotionTypeRepository motionTypeRepository;
    private final ActionTakenInMotionRepository actionTakenInMotionRepository;
    private final ActionTypeRepository actionTypeRepository;
    private final EvaluationRepository evaluationRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public ResponseEntity<?> changePasswordUser(String oldPass, String newPass, HttpServletRequest request) {
        Users user = userRepository.findByLogin(jwtUtils.getUserNameFromJwtToken(jwtUtils.parseJwt(request)))
                .orElseThrow(() -> new RuntimeException("Błąd: Zły użytkownik"));

        if (authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getLogin(), oldPass)).isAuthenticated()
        ) {
            user.setPassword(encoder.encode(newPass));
            userRepository.save(user);
        } else {
            throw new RuntimeException("Błąd: Złe hasło");
        }
        return ResponseEntity.ok(new MessageResponse("Hasło zmienione pomyślnie"));
    }

    public ResponseEntity<?> changePasswordAdmin(String login, String newPass, HttpServletRequest request) {
        Users user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Błąd: Zły użytkownik"));

            user.setPassword(encoder.encode(newPass));
            userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Hasło zmienione pomyślnie"));
    }




}
