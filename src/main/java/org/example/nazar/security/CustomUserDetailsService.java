package org.example.nazar.security;

import org.example.nazar.model.UserSecurityData;
import org.example.nazar.repository.UserSecurityDataRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component

public class CustomUserDetailsService implements UserDetailsService {
    private final UserSecurityDataRepository userRepository;

    public CustomUserDetailsService(UserSecurityDataRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserSecurityData> user = userRepository.findUserSecurityDataByUsername(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }

        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUserData(user.get());

        return customUserDetails;
    }

}
