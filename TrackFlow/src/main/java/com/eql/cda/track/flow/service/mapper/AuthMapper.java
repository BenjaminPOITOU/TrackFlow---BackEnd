package com.eql.cda.track.flow.service.mapper;

import com.eql.cda.track.flow.dto.auth.RegisterDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianCreateDto;
import org.springframework.stereotype.Component;

/**
 * A mapper component responsible for converting DTOs within the authentication flow.
 * This class isolates the mapping logic from the service layer, allowing for flexible
 * conversion from a generic registration request to specific profile creation DTOs.
 */
@Component
public class AuthMapper {

    /**
     * Converts a generic RegisterDto into a specific MusicianCreateDto.
     * It extracts the fields relevant for creating a musician.
     *
     * @param registerDto The generic registration data received from the API.
     * @return A MusicianCreateDto populated with data from the RegisterDto.
     */
    public MusicianCreateDto toMusicianCreateDto(RegisterDto registerDto) {
        if (registerDto == null) {
            return null;
        }

        MusicianCreateDto createDto = new MusicianCreateDto();
        createDto.setLogin(registerDto.getLogin());
        createDto.setPassword(registerDto.getPassword());
        createDto.setFirstName(registerDto.getFirstName());
        createDto.setLastName(registerDto.getLastName());

        return createDto;
    }

    /*
     * In the future, you would add a new method here for another profile type:
     *
     * public ListenerCreateDto toListenerCreateDto(RegisterDto registerDto) {
     *     ListenerCreateDto dto = new ListenerCreateDto();
     *     dto.setLogin(registerDto.getLogin());
     *     dto.setPassword(registerDto.getPassword());
     *     // Listeners might not have first/last names, but maybe a nickname
     *     // dto.setNickname(registerDto.getFirstName());
     *     return dto;
     * }
     */
}