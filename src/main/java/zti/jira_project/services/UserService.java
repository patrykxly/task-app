package zti.jira_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zti.jira_project.DTO.TaskDTO;
import zti.jira_project.DTO.UserDTO;
import zti.jira_project.models.Task;
import zti.jira_project.models.User;
import zti.jira_project.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Registers a new user.
     *
     * @param user the user to register
     * @return the registered user
     * @throws IllegalArgumentException if the email is already taken or the password is invalid
     */
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already taken");
        }
        validatePassword(user.getPassword());
        return userRepository.save(user);
    }

    /**
     * Logs in a user.
     *
     * @param passedUser the user to log in
     * @return the logged-in user, or null if the credentials are invalid
     */
    public User login(User passedUser) {
        User user = userRepository.findByEmail(passedUser.getEmail());
        if (user != null && user.getPassword().equals(passedUser.getPassword())) {
            return user;
        }
        return null;
    }

    /**
     * Validates the password for complexity.
     *
     * @param password the password to validate
     * @throws IllegalArgumentException if the password is invalid
     */
    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }
        if (!password.matches(".*[!@#$%^&+=].*")) {
            throw new IllegalArgumentException("Password must contain at least one special character (@#$%^&+=)");
        }
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id the ID of the user to retrieve
     * @return an Optional containing the user DTO if found, otherwise empty
     */
    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Retrieves all users.
     *
     * @return a list of all user DTOs
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user the User entity to convert
     * @return the corresponding UserDTO
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPassword());
    }
}
