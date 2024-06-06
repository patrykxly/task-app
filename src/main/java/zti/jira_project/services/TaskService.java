package zti.jira_project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zti.jira_project.DTO.TaskDTO;
import zti.jira_project.models.Task;
import zti.jira_project.repositories.TaskRepository;
import zti.jira_project.utils.Consts;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing tasks.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructs a TaskService with the given TaskRepository.
     *
     * @param taskRepository the task repository to be used by this service
     */
    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all tasks.
     *
     * @return a list of all tasks converted to DTOs
     */
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve
     * @return an Optional containing the task DTO if found, otherwise empty
     */
    public Optional<TaskDTO> getTaskById(Integer id) {
        return taskRepository.findById(id)
                .map(this::convertToDTO);
    }

    /**
     * Creates a new task.
     *
     * @param taskDTO the task data to create
     * @return the created task DTO
     */
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        task.setStatus(Consts.STATUS.TO_DO);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    /**
     * Updates an existing task.
     *
     * @param id      the ID of the task to update
     * @param taskDTO the updated task data
     * @return the updated task DTO
     * @throws IllegalArgumentException if no task is found with the given ID
     */
    public TaskDTO updateTask(Integer id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        Task updatedTask = updateTaskFromDTO(existingTask, taskDTO);
        Task savedTask = taskRepository.save(updatedTask);
        return convertToDTO(savedTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id the ID of the task to delete
     */
    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getUserId(), task.getStatus(), task.getPriority());
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        return new Task(taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.getUserId(), taskDTO.getStatus(), taskDTO.getPriority());
    }

    private Task updateTaskFromDTO(Task task, TaskDTO taskDTO) {
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUserId(taskDTO.getUserId());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        return task;
    }
}
