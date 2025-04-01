package com.eql.cda.track.flow.service.implementation;

import com.eql.cda.track.flow.dto.securityDto.RegistrationDto;
import com.eql.cda.track.flow.dto.userDto.AccountType;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerViewDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.entity.Listenner;
import com.eql.cda.track.flow.entity.Musician;
import com.eql.cda.track.flow.entity.User;
import com.eql.cda.track.flow.exception.EmailAlreadyExistsException;
import com.eql.cda.track.flow.exception.MismatchedUserTypeException;
import com.eql.cda.track.flow.exception.ResourceNotFoundException;
import com.eql.cda.track.flow.repository.UserRepository;
import com.eql.cda.track.flow.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;


    @Override
    @Transactional
    public Object getUserViewById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id)); // Utilisez votre exception

        if (user instanceof Musician) {
            return mapToMusicianViewDto((Musician) user);
        } else if (user instanceof Listenner) {
            return mapToListenerViewDto((Listenner) user);
        } else {
            // Ce cas ne devrait pas arriver avec votre structure User->Musician/Listener
            // mais une sécurité est une bonne chose.
            throw new IllegalStateException("User found with ID " + id + " is neither a Musician nor a Listener.");
        }


    }

    @Override
    public MusicianViewDto updateMusicianProfile(Long id, MusicianProfileUpdateDto musicianProfileUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!(user instanceof Musician)) {
            // Si ce n'est pas un Musicien, on ne peut pas appliquer la mise à jour de profil musicien.
            throw new MismatchedUserTypeException(
                    "Cannot update profile: User with ID " + id + " is not a Musician (actual type: " + user.getClass().getSimpleName() + ").");
        }

        Musician musicianToUpdate = (Musician) user;

        if (musicianProfileUpdateDto.getFirstName() != null) {
            musicianToUpdate.setFirstName(musicianProfileUpdateDto.getFirstName());
        }
        if (musicianProfileUpdateDto.getLastName() != null) {
            musicianToUpdate.setLastName(musicianProfileUpdateDto.getLastName());
        }
        if (musicianProfileUpdateDto.getMobile() != null) {
            musicianToUpdate.setMobile(musicianProfileUpdateDto.getMobile());
        }
        if (musicianProfileUpdateDto.getAddress() != null) {
            musicianToUpdate.setAddress(musicianProfileUpdateDto.getAddress());
        }
        if (musicianProfileUpdateDto.getBiography() != null) {
            musicianToUpdate.setBiography(musicianProfileUpdateDto.getBiography());
        }
        if (musicianProfileUpdateDto.getPicture() != null) {
            // Attention: la gestion des images/fichiers peut nécessiter une logique plus complexe
            musicianToUpdate.setPicture(musicianProfileUpdateDto.getPicture());
        }

        Musician updatedMusician = userRepository.save(musicianToUpdate);



        return mapToMusicianViewDto(updatedMusician);
    }

    @Override
    public ListennerViewDto updateListenerProfile(Long id, ListennerProfileUpdateDto listenerProfileUpdateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (!(user instanceof Listenner)) {
            // Si ce n'est pas un Musicien, on ne peut pas appliquer la mise à jour de profil musicien.
            throw new MismatchedUserTypeException(
                    "Cannot update profile: User with ID " + id + " is not a Listener (actual type: " + user.getClass().getSimpleName() + ").");
        }

        Listenner listennerToUpdate = (Listenner) user;
        if (listenerProfileUpdateDto.getFirstName() != null) {
            listennerToUpdate.setFirstName(listenerProfileUpdateDto.getFirstName());
        }
        if (listenerProfileUpdateDto.getLastName() != null) {
            listennerToUpdate.setLastName(listenerProfileUpdateDto.getLastName());
        }
        if (listenerProfileUpdateDto.getUserRole() != null) {
            listennerToUpdate.setUserRole(listenerProfileUpdateDto.getUserRole());
        }

        Listenner updatedListenner = userRepository.save(listennerToUpdate);

        return mapToListenerViewDto(updatedListenner);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);

    }

    @Override
    public User registerNewUser(RegistrationDto registrationDto) {

        AccountType accountType = registrationDto.getAccountType();
        User newUser;


        if (userRepository.existsByLogin(registrationDto.getLogin())) {
            throw new EmailAlreadyExistsException("Login/Email already registered: " + registrationDto.getLogin());
        }
        if (accountType == null) {
            throw new IllegalArgumentException("Account type (MUSICIAN or LISTENER) is required for registration.");
        }
        if (registrationDto.getUserRole() == null) {
            throw new IllegalArgumentException("User role (ARTIST, PRODUCER, etc.) is required for registration.");
        }



        if (accountType == AccountType.MUSICIAN) {
            Musician newMusician = new Musician();
            newUser = newMusician;

        } else {
            Listenner newListener = new Listenner();
            newUser = newListener;
        }

        newUser.setLogin(registrationDto.getLogin());
        newUser.setFirstName(registrationDto.getFirstName());
        newUser.setLastName(registrationDto.getLastName());
        newUser.setUserRole(registrationDto.getUserRole());

        // TODO: !! SÉCURITÉ !! Remplacer immédiatement par l'encodage avec PasswordEncoder
        newUser.setPassword(registrationDto.getPassword());
        newUser.setCreationDate(LocalDateTime.now());
        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    private MusicianViewDto mapToMusicianViewDto(Musician musician) {
        return new MusicianViewDto(
                musician.getId(),
                musician.getLogin(),
                musician.getFirstName(),
                musician.getLastName(),
                musician.getMobile(),
                musician.getAddress(),
                musician.getBiography(),
                musician.getPicture(),
                musician.getUserRole(),
                musician.getCreationDate()
        );
    }

    private ListennerViewDto mapToListenerViewDto(Listenner listenner) {
        return new ListennerViewDto(
                listenner.getId(),
                listenner.getLogin(),
                listenner.getFirstName(),
                listenner.getLastName(),
                listenner.getUserRole());
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
