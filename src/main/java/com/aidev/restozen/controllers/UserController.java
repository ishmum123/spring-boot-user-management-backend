package com.aidev.restozen.controllers;

import com.aidev.restozen.database.entities.Credential;
import com.aidev.restozen.helpers.dtos.CredentialCreationDTO;
import com.aidev.restozen.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<Credential> listUsers() {
        return service.listUsers();
    }

    @PostMapping("employees")
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Credential createEmployee(@Valid @RequestBody CredentialCreationDTO dto) {
        return service.createEmployee(dto);
    }

    @PostMapping("customers")
    @ResponseStatus(code = HttpStatus.CREATED)
    public String registerCustomer(@Valid @RequestBody CredentialCreationDTO dto) {
        return service.registerCustomer(dto);
    }

}