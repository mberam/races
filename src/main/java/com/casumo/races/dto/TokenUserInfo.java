package com.casumo.races.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenUserInfo {

    private String username;

    private long id;

    private String fullName;

    private String email;

    private String type;
}
