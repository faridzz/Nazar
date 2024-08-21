package org.example.nazar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.nazar.model.UserSecurityData;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private UserSecurityData userSecurityData;
    private JwtTokenDTO jwtToken;

    @Override
    public String toString() {
        return "SavedUserResponseDTO{" +
                "user name =" + userSecurityData.getUsername() +
                ", jwtToken=" + jwtToken.getToken() +
                '}';
    }
}
