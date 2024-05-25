package zti.jira_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zti.jira_project.DTO.UserDTO;
import zti.jira_project.controllers.UserController;
import zti.jira_project.models.User;
import zti.jira_project.services.UserService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void testRegisterUser() throws Exception {
        when(userService.register(any(User.class))).thenReturn(new User("test@example.com", "Password123!"));

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Registration successful"));
    }

    @Test
    void testLoginUser() throws Exception {
        User user = new User("test@example.com", "Password123!");
        when(userService.login(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@example.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.getUserById(anyInt())).thenReturn(Optional.of(new UserDTO(1, "test@example.com", "Password123!")));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(new UserDTO(1, "test@example.com", "Password123!")));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
