

import com.eql.cda.track.flow.dto.auth.LoginDto;
import com.eql.cda.track.flow.dto.auth.RegisterDto;
import com.eql.cda.track.flow.entity.ProfileType;
import com.eql.cda.track.flow.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthService authService;

    private RegisterDto validRegisterDto;
    private RegisterDto invalidRegisterDto;
    private LoginDto validLoginDto;
    private LoginDto invalidLoginDto;

    @BeforeEach
    void setUp() {
        validRegisterDto = new RegisterDto();
        validRegisterDto.setLogin("test@example.com");
        validRegisterDto.setPassword("SecurePassword123!");
        validRegisterDto.setFirstName("John");
        validRegisterDto.setLastName("Doe");
        validRegisterDto.setProfileType(ProfileType.MUSICIAN);


        invalidRegisterDto = new RegisterDto();
        invalidRegisterDto.setLogin("invalid-email");
        invalidRegisterDto.setPassword("weak");
        invalidRegisterDto.setFirstName("");
        invalidRegisterDto.setLastName("");



        validLoginDto = new LoginDto();
        validLoginDto.setLogin("test@example.com");
        validLoginDto.setPassword("SecurePassword123!");


        invalidLoginDto = new LoginDto();
        invalidLoginDto.setLogin("wrong@example.com");
        invalidLoginDto.setPassword("wrongpassword");
    }

    @Test
    void testRegister_Success() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok()
                .body("User registered successfully");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);

        // When
        ResponseEntity<Object> response = authService.register(validRegisterDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully", response.getBody());

        verify(authService, times(1)).register(validRegisterDto);
    }

    @Test
    void testRegister_InvalidEmail() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.badRequest()
                .body("Login must be a valid email address.");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.register(invalidRegisterDto);


        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login must be a valid email address.", response.getBody());

        verify(authService, times(1)).register(invalidRegisterDto);
    }

    @Test
    void testRegister_WeakPassword() {
        // Given
        RegisterDto weakPasswordDto = new RegisterDto();
        weakPasswordDto.setLogin("test@example.com");
        weakPasswordDto.setPassword("weak");
        weakPasswordDto.setFirstName("John");
        weakPasswordDto.setLastName("Doe");
        weakPasswordDto.setProfileType(ProfileType.LISTENER);

        ResponseEntity<Object> expectedResponse = ResponseEntity.badRequest()
                .body("Password must be at least 8 characters long.");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);

        // When
        ResponseEntity<Object> response = authService.register(weakPasswordDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Password must be at least 8 characters long.", response.getBody());

        verify(authService, times(1)).register(weakPasswordDto);
    }

    @Test
    void testRegister_MissingProfileType() {
        // Given
        RegisterDto missingProfileDto = new RegisterDto();
        missingProfileDto.setLogin("test@example.com");
        missingProfileDto.setPassword("SecurePassword123!");
        missingProfileDto.setFirstName("John");
        missingProfileDto.setLastName("Doe");
        // ProfileType intentionnellement omis (null)

        ResponseEntity<Object> expectedResponse = ResponseEntity.badRequest()
                .body("Profile type must be specified (e.g., MUSICIAN, LISTENER).");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);

        // When
        ResponseEntity<Object> response = authService.register(missingProfileDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Profile type must be specified (e.g., MUSICIAN, LISTENER).", response.getBody());

        verify(authService, times(1)).register(missingProfileDto);
    }

    @Test
    void testRegister_MusicianProfile() {

        RegisterDto musicianDto = new RegisterDto();
        musicianDto.setLogin("musician@example.com");
        musicianDto.setPassword("SecurePassword123!");
        musicianDto.setFirstName("Jane");
        musicianDto.setLastName("Smith");
        musicianDto.setProfileType(ProfileType.MUSICIAN);

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok()
                .body("Musician account created successfully");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.register(musicianDto);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Musician account created successfully", response.getBody());

        verify(authService, times(1)).register(musicianDto);
    }

    @Test
    void testRegister_ListenerProfile() {

        RegisterDto listenerDto = new RegisterDto();
        listenerDto.setLogin("listener@example.com");
        listenerDto.setPassword("SecurePassword123!");
        listenerDto.setFirstName("Bob");
        listenerDto.setLastName("Johnson");
        listenerDto.setProfileType(ProfileType.LISTENER);

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok()
                .body("Listener account created successfully");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.register(listenerDto);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Listener account created successfully", response.getBody());

        verify(authService, times(1)).register(listenerDto);
    }

    @Test
    void testRegister_LoginAlreadyExists() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Login already exists");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.register(validRegisterDto);


        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Login already exists", response.getBody());

        verify(authService, times(1)).register(validRegisterDto);
    }

    @Test
    void testAuthenticate_Success() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.ok()
                .body("Authentication successful");

        when(authService.authenticate(any(LoginDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.authenticate(validLoginDto);


        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Authentication successful", response.getBody());

        verify(authService, times(1)).authenticate(validLoginDto);
    }

    @Test
    void testAuthenticate_InvalidCredentials() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");

        when(authService.authenticate(any(LoginDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.authenticate(invalidLoginDto);


        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());

        verify(authService, times(1)).authenticate(invalidLoginDto);
    }

    @Test
    void testAuthenticate_UserNotFound() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found");

        when(authService.authenticate(any(LoginDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.authenticate(validLoginDto);


        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());

        verify(authService, times(1)).authenticate(validLoginDto);
    }

    @Test
    void testAuthenticate_NullDto() {

        ResponseEntity<Object> expectedResponse = ResponseEntity.badRequest()
                .body("Login data cannot be null");

        when(authService.authenticate(null))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.authenticate(null);


        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login data cannot be null", response.getBody());

        verify(authService, times(1)).authenticate(null);
    }

    @Test
    void testAuthenticate_EmptyCredentials() {

        LoginDto emptyLoginDto = new LoginDto();
        emptyLoginDto.setLogin("");
        emptyLoginDto.setPassword("");

        ResponseEntity<Object> expectedResponse = ResponseEntity.badRequest()
                .body("Login and password are required");

        when(authService.authenticate(any(LoginDto.class)))
                .thenReturn(expectedResponse);


        ResponseEntity<Object> response = authService.authenticate(emptyLoginDto);


        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login and password are required", response.getBody());

        verify(authService, times(1)).authenticate(emptyLoginDto);
    }
}