package zti.jira_project.models;

import jakarta.persistence.*;

@Entity(name="task")
@Table(name="Tasks", schema="public")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String status;
    private String description;
    private Long userId; // Assuming userId is used to reference the user who owns the task

    public Task() {}

    public Task(String title, String description, Long userId, String status) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
