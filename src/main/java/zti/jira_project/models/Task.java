package zti.jira_project.models;

import jakarta.persistence.*;

@Entity(name="task")
@Table(name="tasks", schema="public")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String status;
    private String priority;
    private String description;
    private Long userId;

    public Task() {}

    public Task(String title, String description, Long userId, String status, String priority) {
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.status = status;
        this.priority = priority;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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
