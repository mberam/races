package com.casumo.races.service;

import com.casumo.races.dto.RacesUserDto;
import com.casumo.races.db.RacesUser;
import com.casumo.races.db.UserType;
import com.casumo.races.exception.RacesException;
import com.casumo.races.mapper.UserMapper;
import com.casumo.races.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersService implements UserDetailsService {
    private final UserRepository userRepository;

    public List<RacesUserDto> getAllCustomers() {
        return UserMapper.mapToRaceDtos(this.userRepository.getRacesUserByTypeEquals(UserType.CUSTOMER));
    }

    public RacesUserDto getCustomerById(long id) {
        var customer = this.userRepository.getRacesUserByTypeEqualsAndIdEquals(UserType.CUSTOMER, id);
        if (customer == null) {
            throw new RacesException("Customer not found !", "Customer not found !", HttpStatus.NOT_FOUND);

        }
        return UserMapper.mapToRaceDto(customer);
    }

    public RacesUserDto getUserByUsername(String username) {
        var user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new RacesException("Customer not found !", "Customer not found !", HttpStatus.NOT_FOUND);
        }
        return UserMapper.mapToRaceDto(user);
    }

    public List<RacesUserDto> getAllUsers() {
        return UserMapper.mapToRaceDtos(this.userRepository.findAll(PageRequest.of(0, 5)).getContent());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userFromDb = userRepository.findByUsername(username);
        if (userFromDb == null) {
            throw new UsernameNotFoundException(String.format("User not found : %s", username));
        }
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userFromDb.getType().name()));
        return new User(userFromDb.getUsername(), userFromDb.getPassword(), authorities);
    }

    public RacesUserDto createUser(RacesUser user) {
        var newUser = this.userRepository.save(user);
        return UserMapper.mapToRaceDto(newUser);
    }
}
