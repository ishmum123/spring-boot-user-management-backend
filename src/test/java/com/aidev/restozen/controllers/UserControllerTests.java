package com.aidev.restozen.controllers;

import com.aidev.restozen.database.repositories.CredentialRepository;
import com.aidev.restozen.helpers.components.UserCreationComponent;
import com.aidev.restozen.helpers.dtos.CredentialCreationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserCreationComponent userCreationComponent;

    @Autowired
    private CredentialRepository credentialRepository;

    private static String transformToJson(Object o) throws JsonProcessingException {
        return new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(o);
    }

    @Test
    public void listUsersWhenUnauthenticatedThenResponseIsUnauthorized() throws Exception {
        mvc
                .perform(get("/users"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(emptyOrNullString()));
    }

    @Test
    public void listUsersWhenAuthenticatedThenReturnsList() throws Exception {
        mvc
                .perform(addToken(get("/users"), "admin", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].type.name").value("ADMIN"));
    }

    @Test
    public void registerCustomerWhenInvalidDataThenReturnsError() throws Exception {
        String json = transformToJson(new CredentialCreationDTO("", "pass"));
        mvc
                .perform(post("/users/customers").content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username", hasItem("must not be empty")))
                .andExpect(jsonPath("$.errors.password[0]").value("length must be between 6 and 50"));
    }

    @Test
    @Transactional
    public void registerCustomerWhenValidDataThenReturnsNewCustomerToken() throws Exception {
        String json = transformToJson(new CredentialCreationDTO("customer", "secret"));
        mvc
                .perform(post("/users/customers").content(json).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(startsWith("eyJhbGciOiJSUzI1NiJ9" /* algorithm header = RS256 */)));
    }

    @Test
    public void createEmployeeWhenEmployeeRequestsThenResponseIsForbidden() throws Exception {
        userCreationComponent.createUser(new CredentialCreationDTO("employee", "secret"), "EMPLOYEE");
        long lastCredentialCount = credentialRepository.count();

        String json = transformToJson(new CredentialCreationDTO("employee2", "secret"));
        mvc
                .perform(
                        addToken(post("/users/employees"), "employee", "secret")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().string(emptyOrNullString()));

        long currentCredentialCount = credentialRepository.count();
        assertEquals(lastCredentialCount, currentCredentialCount, "Number of employees should remain same");
    }

    @Test
    @Transactional
    public void createEmployeeWhenAdminRequestsThenNewEmployeeIsReturned() throws Exception {
        long lastCredentialCount = credentialRepository.count();
        createEmployee("employee");
        long currentCredentialCount = credentialRepository.count();
        assertEquals(lastCredentialCount + 1, currentCredentialCount, "Number of employees should increment by 1");
    }

    @Test
    @Transactional
    public void createEmployeeWhenAdminRequestsMultipleEmployeeCreationThenSuccess() throws Exception {
        long lastCredentialCount = credentialRepository.count();
        createEmployee("employee1");
        createEmployee("employee2");
        createEmployee("employee3");
        long currentCredentialCount = credentialRepository.count();
        assertEquals(lastCredentialCount + 3, currentCredentialCount, "Number of employees should increment by 3");
    }

    private void createEmployee(String username) throws Exception {
        mvc
                .perform(
                        addToken(post("/users/employees"), "admin", "password")
                                .content(transformToJson(new CredentialCreationDTO(username, "secret")))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.type.name").value("EMPLOYEE"));
    }

    private MockHttpServletRequestBuilder addToken(MockHttpServletRequestBuilder builder, String username, String password) throws Exception {
        String token = mvc
                .perform(post("/token").with(httpBasic(username, password)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return builder.header("Authorization", "Bearer " + token);
    }

}
