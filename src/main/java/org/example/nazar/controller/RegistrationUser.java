package org.example.nazar.controller;

import jakarta.validation.Valid;
import org.example.nazar.dto.AuthenticationResponseDTO;
import org.example.nazar.dto.LoginDTO;
import org.example.nazar.model.UserSecurityData;
import org.example.nazar.security.UserServiceDataBase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/")
public class RegistrationUser {
    private final UserServiceDataBase userServiceDataBase;


    public RegistrationUser(UserServiceDataBase userServiceDataBase) {
        this.userServiceDataBase = userServiceDataBase;
    }


    @PostMapping("registration")
    public ResponseEntity<String> addUser(@RequestBody @Valid UserSecurityData userSecurityData) {
        AuthenticationResponseDTO userData = userServiceDataBase.registering(userSecurityData);
        return ResponseEntity.ok(userData.toString());
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        String userData = userServiceDataBase.login(loginDTO).toString();
        return ResponseEntity.ok(userData.isEmpty() ? "username or password is wrong" : userData);

    }

    @GetMapping("check")
    public ResponseEntity<String> checkUserData(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.notFound().build();
        String username = userDetails.getUsername();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        return ResponseEntity.ok("userName : " + (username.isEmpty() ? "null" : username)
                + " role : " + (role.isEmpty() ? "null" : role));

    }
}
