package zti.jira_project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zti.jira_project.controllers.TaskController;
import zti.jira_project.DTO.TaskDTO;
import zti.jira_project.services.TaskService;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    public void testGetAllTasks() throws Exception {
        TaskDTO task = new TaskDTO(1, "Task 1", "Description", 1L, "To Do");
        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"));
    }

    @Test
    public void testGetTaskById() throws Exception {
        TaskDTO task = new TaskDTO(1, "Task 1", "Description", 1L, "To Do");
        when(taskService.getTaskById(anyInt())).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void testGetTaskByIdNotFound() throws Exception {
        when(taskService.getTaskById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTask() throws Exception {
        TaskDTO task = new TaskDTO(1, "Task 1", "Description", 1L, "To Do");
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(task);

        String requestBody = "{\"title\":\"Task 1\",\"description\":\"Description\",\"userId\":1,\"status\":\"To Do\"}";

        mockMvc.perform(post("/tasks")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        TaskDTO task = new TaskDTO(1, "Updated Task", "Updated Description", 1L, "In Progress");
        when(taskService.updateTask(anyInt(), any(TaskDTO.class))).thenReturn(task);

        String requestBody = "{\"title\":\"Updated Task\",\"description\":\"Updated Description\",\"userId\":1,\"status\":\"In Progress\"}";

        mockMvc.perform(put("/tasks/1")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isNoContent());
    }
}
