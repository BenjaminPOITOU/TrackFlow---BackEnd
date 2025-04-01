package com.eql.cda.track.flow.service;

import com.eql.cda.track.flow.dto.securityDto.RegistrationDto;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.listennerDto.ListennerViewDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianProfileUpdateDto;
import com.eql.cda.track.flow.dto.userDto.musicianDto.MusicianViewDto;
import com.eql.cda.track.flow.entity.User;

public interface UserService {

    Object getUserViewById(Long id);
    MusicianViewDto updateMusicianProfile(Long id, MusicianProfileUpdateDto musicianProfileUpdateDto);
    ListennerViewDto updateListenerProfile(Long id, ListennerProfileUpdateDto listenerProfileUpdateDto);
    User registerNewUser(RegistrationDto registrationDto);
    void deleteUser (Long id);
}
