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

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email is already taken");
        }
        validatePassword(user.getPassword());
        return userRepository.save(user);
    }

    public User login(User passedUser) {
        User user = userRepository.findByEmail(passedUser.getEmail());
        if (user != null && user.getPassword().equals(passedUser.getPassword())) {
            return user;
        }
        return null;
    }

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

    public Optional<UserDTO> getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPassword());
    }
}
