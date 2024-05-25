package zti.jira_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zti.jira_project.models.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> { }