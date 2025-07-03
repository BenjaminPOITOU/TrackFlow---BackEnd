// Exemple de structure de classe de test
import com.eql.cda.track.flow.dto.securityDto.RegistrationDto;
import com.eql.cda.track.flow.dto.userDto.AccountType;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerViewDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.implementation.UserServiceImpl;
import com.eql.cda.track.flow.entity.*;
import com.eql.cda.track.flow.exception.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.time.Instant;

@ExtendWith(MockitoExtension.class) // Permet d'utiliser les annotations Mockito
class UserServiceImplTest {

    @Mock // Crée un mock pour UserRepository
    private UserRepository userRepository;

    @InjectMocks // Crée une instance de UserServiceImpl et injecte les mocks (userRepository) dedans
    private UserServiceImpl userService;

    // Des objets Musician et Listener de test pourraient être définis ici
    private Musician testMusician;
    private Listenner testListener;
    private RegistrationDto registrationDtoMusician;
    private RegistrationDto registrationDtoListener;
    private MusicianProfileUpdateDto musicianUpdateDto;
    private ListennerProfileUpdateDto listenerUpdateDto;

    @BeforeEach // Exécuté avant chaque test
    void setUp() {
        // --- Initialisation des DTOs ---

        // 1. RegistrationDto pour créer un Musicien (ARTIST)
        registrationDtoMusician = new RegistrationDto();
        registrationDtoMusician.setLogin("test.musician.register@music.com");
        registrationDtoMusician.setPassword("SecurePass123!"); // Doit faire >= 8 caractères
        registrationDtoMusician.setFirstName("Mia");
        registrationDtoMusician.setLastName("MusicianReg");
        registrationDtoMusician.setUserRole(ProfileType.ARTIST); // Rôle musicien
        registrationDtoMusician.setAccountType(AccountType.MUSICIAN); // Type de compte musicien

        // 2. RegistrationDto pour créer un Listener (MANAGER)
        registrationDtoListener = new RegistrationDto();
        registrationDtoListener.setLogin("test.listener.register@label.net");
        registrationDtoListener.setPassword("AnotherSecurePass456?"); // Doit faire >= 8 caractères
        registrationDtoListener.setFirstName("Leo");
        registrationDtoListener.setLastName("ListenerReg");
        registrationDtoListener.setUserRole(ProfileType.MANAGER); // Rôle listener
        registrationDtoListener.setAccountType(AccountType.LISTENER); // Type de compte listener

        // 3. MusicianProfileUpdateDto pour Mettre à Jour un Musicien (partiel)
        //    Seuls les champs qu'on veut tester dans la mise à jour sont définis.
        musicianUpdateDto = new MusicianProfileUpdateDto();
        musicianUpdateDto.setFirstName("Mia-Updated"); // Mise à jour du prénom
        musicianUpdateDto.setMobile("0611223344");   // Mise à jour du mobile
        musicianUpdateDto.setBiography("Updated Bio Text."); // Mise à jour de la bio
        // Les autres champs (lastName, address, picture, userRole) restent null dans le DTO

        // 4. ListenerProfileUpdateDto pour Mettre à Jour un Listener (partiel)
        listenerUpdateDto = new ListennerProfileUpdateDto();
        listenerUpdateDto.setLastName("ListenerReg-Updated"); // Mise à jour du nom de famille
        listenerUpdateDto.setUserRole(ProfileType.VISITOR);     // Mise à jour du rôle
        // Le champ firstName reste null dans le DTO


        // --- Initialisation optionnelle des Entités (pour les mocks de findById) ---
        // Utilisez des IDs qui correspondent à ceux que vous utiliserez dans les 'when' des tests

        // Musician de test (ID = 1L, PRODUCER)
        testMusician = new Musician();
        testMusician.setId(1L);
        testMusician.setLogin("producer.test@studio.org");
        testMusician.setPassword("hashedPasswordMusician"); // En pratique, ce serait un hash
        testMusician.setFirstName("Pete");
        testMusician.setLastName("Producer");
        testMusician.setUserRole(ProfileType.PRODUCER);
        testMusician.setAddress("1 Music Avenue");
        testMusician.setMobile("0101010101");
        testMusician.setBiography("Initial Producer Bio");
        testMusician.setCreationDate(Instant.now().minus(Duration.ofDays(5))); // CORRIGÉ
        testMusician.setUpdateDate(Instant.now().minus(Duration.ofDays(1))); // Date factice passée
        testMusician.setProjects(new ArrayList<>()); // Initialiser les collections pour éviter NPE
        testMusician.setAccesses(new HashSet<>());
        testMusician.setPlaylists(new ArrayList<>());

        // Listener de test (ID = 3L, VISITOR)
        testListener = new Listenner();
        testListener.setId(3L);
        testListener.setLogin("visitor.test@email.com");
        testListener.setPassword("hashedPasswordListener"); // Hash
        testListener.setFirstName("Vicky");
        testListener.setLastName("Visitor");
        testListener.setUserRole(ProfileType.VISITOR);
        testListener.setCreationDate(Instant.now().minus(Duration.ofDays(10))); // CORRIGÉ
        testListener.setUpdateDate(Instant.now().minus(Duration.ofDays(2)));
        testListener.setProjects(new ArrayList<>());
        testListener.setAccesses(new HashSet<>());
        // Listenner n'a pas de champs spécifiques à initialiser
    }

    // --- Tests pour getUserViewById ---

    @Test
    void getUserViewById_MusicianFound_ShouldReturnMusicianViewDto() {
        // Given (Préconditions)
        Long musicianId = 1L;
        // Configuration du Mock : quand findById(1L) est appelé, retourne notre musicien de test
        when(userRepository.findById(musicianId)).thenReturn(Optional.of(testMusician));

        // When (Action)
        Object result = userService.getUserViewById(musicianId);

        // Then (Vérifications)
        assertNotNull(result); // Le résultat ne doit pas être null
        assertTrue(result instanceof MusicianViewDto); // Doit être un DTO de vue Musicien
        // Vérifier éventuellement quelques champs du DTO
        assertEquals(testMusician.getFirstName(), ((MusicianViewDto) result).getFirstName());
        verify(userRepository, times(1)).findById(musicianId); // Vérifie que findById a été appelé une fois
    }

    @Test
    void getUserViewById_ListenerFound_ShouldReturnListenerViewDto() {
        // Given
        Long listenerId = 3L;
        when(userRepository.findById(listenerId)).thenReturn(Optional.of(testListener));

        // When
        Object result = userService.getUserViewById(listenerId);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ListennerViewDto);
        assertEquals(testListener.getFirstName(), ((ListennerViewDto) result).getFirstName());
        verify(userRepository, times(1)).findById(listenerId);
    }

    @Test
    void getUserViewById_UserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        Long nonExistentId = 99L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty()); // Aucun utilisateur trouvé

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserViewById(nonExistentId); // L'appel doit lancer l'exception
        });
        verify(userRepository, times(1)).findById(nonExistentId);
    }

    // --- Tests pour updateMusicianProfile ---

    @Test
    void updateMusicianProfile_Success_ShouldReturnUpdatedMusicianViewDto() {
        // Given
        Long musicianId = 1L;
        // Simuler la récupération du musicien existant
        when(userRepository.findById(musicianId)).thenReturn(Optional.of(testMusician));
        // Simuler la sauvegarde (on retourne le même objet ou une copie modifiée)
        // any(Musician.class) car l'objet sera modifié DANS la méthode
        when(userRepository.save(any(Musician.class))).thenAnswer(invocation -> invocation.getArgument(0));
        musicianUpdateDto.setFirstName("UpdatedFirstName"); // Mettre à jour le prénom dans le DTO

        // When
        MusicianViewDto result = userService.updateMusicianProfile(musicianId, musicianUpdateDto);

        // Then
        assertNotNull(result);
        assertEquals("UpdatedFirstName", result.getFirstName()); // Vérifier que le champ a été mis à jour
        // Vérifier que les setters ont été appelés (plus avancé avec ArgumentCaptor ou vérification état)
        // Vérifier que l'updatedDate a été mis à jour (nécessite une horloge mockée ou comparaison relative si audit activé)
        verify(userRepository).findById(musicianId);
        verify(userRepository).save(any(Musician.class));
    }

    @Test
    void updateMusicianProfile_UserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        Long nonExistentId = 99L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateMusicianProfile(nonExistentId, musicianUpdateDto);
        });
        verify(userRepository, never()).save(any()); // save ne doit jamais être appelé
    }

    @Test
    void updateMusicianProfile_UserIsListener_ShouldThrowMismatchedUserTypeException() {
        // Given
        Long listenerId = 3L;
        when(userRepository.findById(listenerId)).thenReturn(Optional.of(testListener)); // Retourne un Listener

        // When & Then
        assertThrows(MismatchedUserTypeException.class, () -> {
            userService.updateMusicianProfile(listenerId, musicianUpdateDto);
        });
        verify(userRepository, never()).save(any());
    }

    // --- Tests pour updateListenerProfile (similaires à Musician) ---

    @Test
    void updateListenerProfile_Success_ShouldReturnUpdatedListenerViewDto() {
        // Given
        Long listenerId = 3L;
        when(userRepository.findById(listenerId)).thenReturn(Optional.of(testListener));
        when(userRepository.save(any(Listenner.class))).thenAnswer(invocation -> invocation.getArgument(0));
        listenerUpdateDto.setLastName("UpdatedLastName");

        // When
        ListennerViewDto result = userService.updateListenerProfile(listenerId, listenerUpdateDto);

        // Then
        assertNotNull(result);
        assertEquals("UpdatedLastName", result.getLastName());
        verify(userRepository).findById(listenerId);
        verify(userRepository).save(any(Listenner.class));
    }

    @Test
    void updateListenerProfile_UserNotFound_ShouldThrowResourceNotFoundException() {
        // Given
        Long nonExistentId = 99L;
        when(userRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.updateListenerProfile(nonExistentId, listenerUpdateDto);
        });
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateListenerProfile_UserIsMusician_ShouldThrowMismatchedUserTypeException() {
        // Given
        Long musicianId = 1L;
        when(userRepository.findById(musicianId)).thenReturn(Optional.of(testMusician)); // Retourne un Musician

        // When & Then
        assertThrows(MismatchedUserTypeException.class, () -> {
            userService.updateListenerProfile(musicianId, listenerUpdateDto);
        });
        verify(userRepository, never()).save(any());
    }


    // --- Tests pour deleteUser ---

    @Test
    void deleteUser_Success_ShouldCallDeleteById() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true); // Simuler que l'utilisateur existe
        doNothing().when(userRepository).deleteById(userId); // Ne rien faire quand deleteById est appelé

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId); // Vérifier que deleteById a été appelé
    }

    @Test
    void deleteUser_UserNotFound_ShouldThrowEntityNotFoundException() { // ou ResourceNotFoundException selon votre impl
        // Given
        Long nonExistentId = 99L;
        when(userRepository.existsById(nonExistentId)).thenReturn(false); // L'utilisateur n'existe pas

        // When & Then
        // Adapter l'exception attendue si nécessaire
        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(nonExistentId);
        });
        verify(userRepository, times(1)).existsById(nonExistentId);
        verify(userRepository, never()).deleteById(anyLong()); // deleteById ne doit pas être appelé
    }


    // --- Tests pour registerNewUser ---

    @Test
    void registerNewUser_Musician_Success_ShouldSaveMusician() {
        // Given
        registrationDtoMusician.setLogin("new.musician@test.com");
        registrationDtoMusician.setAccountType(AccountType.MUSICIAN);
        when(userRepository.existsByLogin(registrationDtoMusician.getLogin())).thenReturn(false);
        // Simuler la sauvegarde: retourne l'objet sauvegardé (on peut capturer l'argument pour vérifier)
        when(userRepository.save(any(Musician.class))).thenAnswer(invocation -> {
            Musician saved = invocation.getArgument(0);
            saved.setId(5L); // Simuler l'assignation d'un ID
            // Simuler l'audit:
            saved.setCreationDate(Instant.now());
            saved.setUpdateDate(Instant.now());
            return saved;
        });


        // When
        User result = userService.registerNewUser(registrationDtoMusician);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Musician);
        assertEquals(registrationDtoMusician.getLogin(), result.getLogin());
        assertEquals(registrationDtoMusician.getFirstName(), result.getFirstName());
        assertEquals(registrationDtoMusician.getUserRole(), result.getUserRole());
        assertNotNull(result.getCreationDate()); // Vérifier que la date est mise
        // !! Vérifier que le mot de passe est HACHÉ (quand ce sera implémenté) !!
        assertNotEquals(registrationDtoMusician.getPassword(), result.getPassword()); // Exemple si haché

        verify(userRepository).existsByLogin(registrationDtoMusician.getLogin());
        verify(userRepository).save(any(Musician.class));
    }

    @Test
    void registerNewUser_Listener_Success_ShouldSaveListener() {
        // Given
        registrationDtoListener.setLogin("new.listener@test.com");
        registrationDtoListener.setAccountType(AccountType.LISTENER);
        when(userRepository.existsByLogin(registrationDtoListener.getLogin())).thenReturn(false);
        when(userRepository.save(any(Listenner.class))).thenAnswer(invocation -> {
            Listenner saved = invocation.getArgument(0);
            saved.setId(6L);
            saved.setCreationDate(Instant.now());
            saved.setUpdateDate(Instant.now());
            return saved;
        });

        // When
        User result = userService.registerNewUser(registrationDtoListener);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Listenner);
        assertEquals(registrationDtoListener.getLogin(), result.getLogin());
        // ... autres assertions ...
        verify(userRepository).existsByLogin(registrationDtoListener.getLogin());
        verify(userRepository).save(any(Listenner.class));
    }

    @Test
    void registerNewUser_LoginExists_ShouldThrowEmailAlreadyExistsException() {
        // Given
        registrationDtoMusician.setLogin("existing.user@test.com");
        when(userRepository.existsByLogin(registrationDtoMusician.getLogin())).thenReturn(true); // Le login existe déjà

        // When & Then
        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerNewUser(registrationDtoMusician);
        });
        verify(userRepository, never()).save(any()); // Save ne doit pas être appelé
    }

    @Test
    void registerNewUser_NullAccountType_ShouldThrowIllegalArgumentException() {
        // Given
        registrationDtoMusician.setAccountType(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(registrationDtoMusician);
        });
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerNewUser_NullUserRole_ShouldThrowIllegalArgumentException() {
        // Given
        registrationDtoMusician.setUserRole(null);
        when(userRepository.existsByLogin(anyString())).thenReturn(false); // Pour passer la 1ere vérif

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerNewUser(registrationDtoMusician);
        });
        verify(userRepository, never()).save(any());
    }
}