package com.casumo.races.mapper;

import com.casumo.races.dto.RacesUserDto;
import com.casumo.races.db.RacesUser;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static RacesUserDto mapToRaceDto(RacesUser user) {
        return RacesUserDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .id(user.getId())
                .build();
    }

    public static List<RacesUserDto> mapToRaceDtos(List<RacesUser> users) {
        return users.stream().map(UserMapper::mapToRaceDto).collect(Collectors.toList());
    }


}
