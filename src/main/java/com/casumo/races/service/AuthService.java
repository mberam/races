package com.casumo.races.service;

import com.casumo.races.dto.AuthResponse;
import com.casumo.races.dto.LoginDto;
import com.casumo.races.dto.RegisterDto;
import com.casumo.races.dto.TokenUserInfo;
import com.casumo.races.db.RacesUser;
import com.casumo.races.db.UserType;
import com.casumo.races.exception.RacesException;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Log4j2
public class AuthService {

    public AuthService(AuthenticationManager authenticationManager, UsersService usersService,
                       JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, PasswordEncoder passwordEncoder,
                       @Value("${races.security.token:60}") int tokenDuration) {

        this.authenticationManager = authenticationManager;
        this.usersService = usersService;
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
        this.tokenDuration = tokenDuration;
    }

    private final String USER_CLAIM_KEY = "user";
    private final AuthenticationManager authenticationManager;
    private final UsersService usersService;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final PasswordEncoder passwordEncoder;


    private final int tokenDuration;


    public AuthResponse login(LoginDto authentication) {
        try {
            var auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authentication.getUsername(),
                    authentication.getPassword()));

            var role = auth.getAuthorities().stream().findFirst()
                    .orElseThrow().getAuthority();

            var user = this.usersService.getUserByUsername(authentication.getUsername());
            JwtClaimsSet claims = generateClaims(user.getUsername(), user.getEmail(), user.getFullName(), role, user.getId());
            return new AuthResponse(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());

        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new RacesException("Authentication failed ", e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }

    public AuthResponse register(@Valid RegisterDto authentication) {

        try {
            var user = RacesUser.builder()
                    .username(authentication.getUsername())
                    .fullName(authentication.getFullName())
                    .password(passwordEncoder.encode(authentication.getPassword()))
                    .email(authentication.getEmail())
                    .type(UserType.CUSTOMER)
                    .bets(null)
                    .build();

            var createdUser = usersService.createUser(user);


            JwtClaimsSet claims = generateClaims(authentication.getUsername(), authentication.getEmail(),
                    authentication.getFullName(), UserType.CUSTOMER.name(), createdUser.getId());

            return new AuthResponse(this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue());


        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RacesException("Registration failed ", e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    private JwtClaimsSet generateClaims(String username, String email, String fullName, String role, long id) {
        var userInfo = TokenUserInfo.builder().username(username)
                .email(email)
                .id(id)
                .fullName(fullName)
                .type(role);

        Instant now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenDuration * 60L))
                .subject(username)
                .claim(USER_CLAIM_KEY, userInfo)
                .build();

    }

    public UsernamePasswordAuthenticationToken parseToken(String token) {
        var jwt = jwtDecoder.decode(token);
        var info = (com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap) jwt.getClaims().get(USER_CLAIM_KEY);
        var role = (String) info.get("type");
        var id = (long) info.get("id");
        var username = (String)info.get("username");
        var email = (String)info.get("email");
        var fullName =(String) info.get("fullName");

        var userInfo = TokenUserInfo.builder().username(username)
                .email(email)
                .id(id)
                .fullName(fullName)
                .type(role);

        return new UsernamePasswordAuthenticationToken(username,userInfo,
                List.of(new SimpleGrantedAuthority(role)));

    }


}



