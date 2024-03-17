package com.aidev.restozen.controllers;

import com.aidev.restozen.database.entities.User;
import com.aidev.restozen.helpers.dtos.CustomerCreationDTO;
import com.aidev.restozen.helpers.dtos.EmployeeCreationDTO;
import com.aidev.restozen.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> listUsers() {
        return service.listUsers();
    }

    @PostMapping("employees")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public User addEmployee(@Valid @RequestBody EmployeeCreationDTO dto) throws IOException {
        return service.addEmployee(dto);
    }

    @PostMapping("customers")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String registerCustomer(@Valid @RequestBody CustomerCreationDTO dto) {
        return service.registerCustomer(dto);
    }

}