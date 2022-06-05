package scheduler.dao;

import scheduler.models.Task;

import java.util.List;

public interface TaskServiceInterface {
    List<Task> showTasks(int userId);
    Task getTaskById(int id,int userId);
    void updateTask(int id,Task task,int userId);
    void createTask(Task task,int userId);
    void deleteTask(int id,int userId);
}
