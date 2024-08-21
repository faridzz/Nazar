package org.example.nazar.security;

import lombok.extern.slf4j.Slf4j;
import org.example.nazar.dto.JwtTokenDTO;
import org.example.nazar.dto.AuthenticationResponseDTO;
import org.example.nazar.dto.LoginDTO;
import org.example.nazar.exception.DuplicateUserException;
import org.example.nazar.exception.NotFoundException;
import org.example.nazar.model.UserData;
import org.example.nazar.model.UserSecurityData;
import org.example.nazar.repository.UserDataRepository;
import org.example.nazar.repository.UserSecurityDataRepository;
import org.example.nazar.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceDataBase {
    private final UserSecurityDataRepository userSecurityDataRepository;
    private final UserDataRepository userDataRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserServiceDataBase(
            UserSecurityDataRepository userSecurityDataRepository,
            UserDataRepository userDataRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userSecurityDataRepository = userSecurityDataRepository;
        this.userDataRepository = userDataRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponseDTO registering(UserSecurityData userSecurityData) {
        // Check if username already exists
        if (userSecurityDataRepository.existsByUsername(userSecurityData.getUsername())) {
            throw new DuplicateUserException(userSecurityData.getUsername());
        }

        // Hash the password before saving the user
        userSecurityData.setPassword(passwordEncoder.encode(userSecurityData.getPassword()));

        // Save the user data
        UserSecurityData savedUser = userSecurityDataRepository.save(userSecurityData);

        // Create a JWT Token
        JwtTokenDTO jwtToken = createJwtToken(savedUser);

        // Return a response DTO
        return new AuthenticationResponseDTO(savedUser, jwtToken);
    }

    public UserData saveUserData(UserData userData) {
        return userDataRepository.save(userData);
    }


    public AuthenticationResponseDTO login(LoginDTO loginData) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginData.getUsername(),
                            loginData.getPassword()
                    )
            );

            // Find the user by username
            Optional<UserSecurityData> userSecurityDataByUsername =
                    userSecurityDataRepository.findUserSecurityDataByUsername(loginData.getUsername());

            // Handle case where user is not found
            if (userSecurityDataByUsername.isEmpty()) {
                throw new NotFoundException("User not found : " + loginData.getUsername());
            }

            UserSecurityData foundUser = userSecurityDataByUsername.get();

            // Create JWT Token
            JwtTokenDTO jwtToken = createJwtToken(foundUser);

            // Return the response DTO
            return new AuthenticationResponseDTO(foundUser, jwtToken);
        } catch (Exception e) {
            throw new NotFoundException("username or password is uncorrected");
        }
    }

    // Method to create JWT Token
    private JwtTokenDTO createJwtToken(UserSecurityData userSecurityData) {
        return new JwtTokenDTO(jwtService.buildJwt(userSecurityData));
    }
}
