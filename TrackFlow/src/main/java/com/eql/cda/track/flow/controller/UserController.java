package com.eql.cda.track.flow.controller;


import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerViewDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.exception.MismatchedUserTypeException;
import com.eql.cda.track.flow.exception.ResourceNotFoundException;
import com.eql.cda.track.flow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;


    /**
     * Récupère les informations de vue d'un utilisateur par son ID.
     * Retourne soit un MusicianViewDto soit un ListenerViewDto.
     *
     * TODO (Sécurité): Définir qui peut voir quel profil (public, utilisateurs connectés, etc.)?
     *               Utiliser Spring Security (ex: @PreAuthorize).
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        try {
            Object userViewDto = userService.getUserViewById(id);
            return ResponseEntity.ok(userViewDto);
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
            } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
              }
        // Un @ControllerAdvice est recommandé pour une gestion centralisée des exceptions.
    }

    /**
     * Met à jour partiellement le profil d'un Musicien.
     * Attend un MusicianProfileUpdateDto en body.
     * Seuls les champs présents et non-null dans le DTO seront mis à jour.
     *
     * TODO (Sécurité): Protéger cet endpoint. Idéalement, un utilisateur ne met à jour que son
     *               propre profil. Utiliser '/api/profile/musician' sans ID et récupérer
     *               l'ID depuis l'authentification (AuthenticationPrincipal).
     *               Ajouter @PreAuthorize("isAuthenticated() and #id == principal.id") ou similaire.
     */
    @PatchMapping("/musician/{id}") // PATCH pour mise à jour partielle, chemin spécifique Musician
    public ResponseEntity<MusicianViewDto> updateMusicianProfile(
            @PathVariable long id,
            @RequestBody MusicianProfileUpdateDto musicianProfileUpdateDto) { // DTO spécifique
        try {
            MusicianViewDto updatedMusician = userService.updateMusicianProfile(id, musicianProfileUpdateDto);
            return ResponseEntity.ok(updatedMusician); // Retourne le profil mis à jour
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (MismatchedUserTypeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        // Gérer aussi les exceptions de validation du DTO (@Valid + BindingResult ou ControllerAdvice)
    }

    /**
     * Met à jour partiellement le profil d'un Auditeur (Listener).
     * Attend un ListenerProfileUpdateDto en body.
     * Seuls les champs présents et non-null dans le DTO seront mis à jour.
     *
     * TODO (Sécurité): Mêmes considérations que pour updateMusicianProfile.
     *               Utiliser '/api/profile/listener' pour l'utilisateur connecté.
     *               Ajouter @PreAuthorize(...)
     */
    @PatchMapping("/listener/{id}") // PATCH pour mise à jour partielle, chemin spécifique Listener
    public ResponseEntity<ListennerViewDto> updateListenerProfile(
            @PathVariable long id,
            @RequestBody ListennerProfileUpdateDto listenerProfileUpdateDto) { // DTO spécifique
        try {
            ListennerViewDto updatedListener = userService.updateListenerProfile(id, listenerProfileUpdateDto);
            return ResponseEntity.ok(updatedListener); // Retourne le profil mis à jour
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (MismatchedUserTypeException e) {
            // Si l'ID correspond à un Musician
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
        // Gérer aussi les exceptions de validation
    }


    /**
     * Supprime un utilisateur par son ID.
     *
     * TODO (Sécurité): Strictement nécessaire ! Qui peut supprimer qui ?
     *               @PreAuthorize("hasRole('ADMIN') or (#id == principal.id)")
     *               Attention aux implications (suppression en cascade, données orphelines).
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build(); // Standard pour DELETE réussi
        } catch (ResourceNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
        // Gérer les exceptions de contrainte d'intégrité (par ex. avec ControllerAdvice)
        // si la suppression échoue car l'utilisateur est référencé ailleurs.
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
