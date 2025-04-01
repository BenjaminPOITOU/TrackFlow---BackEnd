package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.securityDto.AuthenticationDto;
import com.eql.cda.track.flow.dto.securityDto.RegistrationDto;
import com.eql.cda.track.flow.dto.userDto.AuthenticatedUserDto;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.exception.EmailAlreadyExistsException;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.SecurityService;
import com.eql.cda.track.flow.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class DummySecurityServiceImpl implements SecurityService {


    private static final Logger logger = LogManager.getLogger();

    private final UserRepository userRepository;
    private final UserService userService; // Pour déléguer la création de l'entité User

    // Injecte les dépendances via le constructeur
    public DummySecurityServiceImpl(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    @Transactional // Garde la transaction si la création utilisateur en a besoin
    public ResponseEntity<Object> register(RegistrationDto registrationDto) {
        logger.warn("Executing DUMMY registration. Password will be stored INSECURELY (plain text).");

        try {
           User newUser = userService.registerNewUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration : Ok");

        } catch (EmailAlreadyExistsException e) {
            // Gérer le cas où l'utilisateur existe déjà
            logger.warn("Registration failed: {}", e.getMessage());
            // Retourner un statut 409 Conflict
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            // Gérer les autres erreurs potentielles de création
            logger.error("Unexpected error during dummy registration: {}", e.getMessage(), e);
            // Retourner un statut 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registration failed due to an internal error.");
        }
    }

    @Override
    public ResponseEntity<Object> authenticate(AuthenticationDto authenticationDto) { // Renommé de LoginDto si besoin
        logger.warn("Executing DUMMY authentication. Password check is SKIPPED.");

        String identifier = authenticationDto.getLogin() != null ? authenticationDto.getLogin() : authenticationDto.getLogin(); // Gère email ou login

        // 1. Essayer de trouver l'utilisateur
        User user;
        try {
            user = userRepository.findByLogin(identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with identifier: " + identifier));
        } catch (EntityNotFoundException e) {
            logger.warn("Authentication failed: User not found ({})", identifier);
            // Retourner un statut 401 Unauthorized si l'utilisateur n'existe pas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
        }

        // 2. !! VÉRIFICATION DU MOT DE PASSE FACTICE / IGNORÉE !!
        //    NE PAS comparer authenticationDto.getPassword() avec user.getPassword() ici,
        //    car le mot de passe stocké est (pour l'instant) en clair et ce n'est pas la bonne pratique.
        //    La vraie implémentation utilisera AuthenticationManager qui gère la comparaison sécurisée.
        logger.warn("Password check skipped for user: {}", identifier);
        // **TODO (Sécurité):** Remplacer toute cette méthode par un appel à AuthenticationManager
        //                     et la génération d'un vrai token JWT.

        // 3. Générer un faux token
        String fakeToken = "dummy-token-for-" + user.getLogin();

        // 4. Créer le DTO de réponse
        AuthenticatedUserDto responseDto = new AuthenticatedUserDto(
                user.getId(),
                user.getLogin(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserRole(),
                fakeToken
        );

        // 5. Retourner une réponse succès (200 OK)
        return ResponseEntity.ok(responseDto);
    }
}
