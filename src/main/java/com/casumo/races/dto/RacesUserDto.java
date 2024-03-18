package com.casumo.races.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RacesUserDto {

    private long id;

    private String type;

    private String username;

    private String fullName;

    private String email;

}
