package zti.jira_project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zti.jira_project.DTO.TaskDTO;
import zti.jira_project.models.User;
import zti.jira_project.services.UserService;
import zti.jira_project.DTO.UserDTO;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructs a UserController with the given UserService.
     *
     * @param userService the user service to be used by this controller
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user.
     *
     * @param userDTO the user data to register
     * @return a ResponseEntity with a success message and status CREATED, or an error message and status CONFLICT
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), userDTO.getPassword());
        try {
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Registration successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already taken");
        }
    }

    /**
     * Logs in a user.
     *
     * @param userDTO the user data to log in
     * @return a ResponseEntity with the logged-in user and status OK, or an error message and status UNAUTHORIZED
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDTO userDTO) {
        User user = new User(userDTO.getEmail(), userDTO.getPassword());
        User loggedInUser = userService.login(user);
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve
     * @return a ResponseEntity containing the UserDTO if found, or an HTTP status of NOT FOUND if not
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        Optional<UserDTO> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a list of all UserDTOs and an HTTP status of OK
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
