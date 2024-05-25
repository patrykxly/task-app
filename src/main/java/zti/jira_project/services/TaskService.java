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

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TaskDTO> getTaskById(Integer id) {
        return taskRepository.findById(id)
                .map(this::convertToDTO);
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToEntity(taskDTO);
        task.setStatus(Consts.STATUS.TO_DO);
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    public TaskDTO updateTask(Integer id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));
        Task updatedTask = updateTaskFromDTO(existingTask, taskDTO);
        Task savedTask = taskRepository.save(updatedTask);
        return convertToDTO(savedTask);
    }

    public void deleteTask(Integer id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO convertToDTO(Task task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getUserId(), task.getStatus());
    }

    private Task convertToEntity(TaskDTO taskDTO) {
        return new Task(taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.getUserId(), taskDTO.getStatus());
    }

    private Task updateTaskFromDTO(Task task, TaskDTO taskDTO) {
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setUserId(taskDTO.getUserId());
        task.setStatus(taskDTO.getStatus());
        return task;
    }
}
