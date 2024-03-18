package com.casumo.races.controller;

import com.casumo.races.dto.RacesUserDto;
import com.casumo.races.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
    UsersService usersService;

    @GetMapping("/customers/{id}")
    RacesUserDto getCustomerById(@PathVariable long id) {
        return this.usersService.getCustomerById(id);
    }

    @GetMapping("/customers")
    @PreAuthorize("hasAnyRole('ADMIN')")
    List<RacesUserDto> getCustomers() {
        return this.usersService.getAllCustomers();
    }

    @GetMapping
    List<RacesUserDto> getAllUsers() {
        return this.usersService.getAllUsers();
    }


}
