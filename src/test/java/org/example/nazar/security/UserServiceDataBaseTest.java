package org.example.nazar.security;

import org.example.nazar.dto.AuthenticationResponseDTO;
import org.example.nazar.dto.LoginDTO;
import org.example.nazar.exception.NotFoundException;
import org.example.nazar.model.UserData;
import org.example.nazar.model.UserSecurityData;
import org.example.nazar.repository.UserDataRepository;
import org.example.nazar.repository.UserSecurityDataRepository;
import org.example.nazar.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceDataBaseTest {
    @Mock
    private UserDataRepository userDataRepository;
    @Mock
    private UserSecurityDataRepository userSecurityDataRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserServiceDataBase userServiceDataBase;

    @Test
    void registering() {

    }

    @Test
    void TestSaveUserData_Return_UserData_When_Registered() {
        //Assign
        UserData userData = UserData.builder().email("farid.d@gmail.com")
                .name("farid")
                .family("z")
                .build();
        when(userDataRepository.save(userData))
                .thenReturn(userData);
        //Act
        UserData userDataSaved = userServiceDataBase.saveUserData(userData);
        //Assert
        assertThat(userDataSaved).isEqualTo(userData);
        verify(userDataRepository, times(1)).save(any(UserData.class));
    }

    @Test
    void testLoginSuccessful() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("testUser", "password123");
        UserSecurityData userSecurityData = new UserSecurityData();
        userSecurityData.setUsername("testUser");
        userSecurityData.setPassword("encodedPassword");

        when(userSecurityDataRepository.findUserSecurityDataByUsername("testUser"))
                .thenReturn(Optional.of(userSecurityData));
        when(jwtService.buildJwt(any(UserSecurityData.class))).thenReturn("fake-jwt-token");

        // Act
        AuthenticationResponseDTO response = userServiceDataBase.login(loginDTO);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getJwtToken().getToken()).isEqualTo("fake-jwt-token");
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testUser", "password123")
        );
    }

    @Test
    void testLoginUserNotFound() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("testUser", "password123");

        when(userSecurityDataRepository.findUserSecurityDataByUsername("testUser"))
                .thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userServiceDataBase.login(loginDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("User not found : testUser");
        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("testUser", "password123")
        );
    }

    @Test
    void testLoginIncorrectPassword() {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("testUser", "wrongPassword");

        doThrow(new RuntimeException("Bad credentials")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userServiceDataBase.login(loginDTO);
        });

        assertThat(exception.getMessage()).isEqualTo("username or password is uncorrected");
    }
}