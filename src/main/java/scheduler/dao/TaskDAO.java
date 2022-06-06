package scheduler.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import scheduler.addition.Status;
import scheduler.models.Task;
import scheduler.models.User;

import java.util.List;

@Component
public class TaskDAO implements TaskServiceInterface {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Task> showTasks(int userId) {
        return jdbcTemplate.query("Select * from tasks where idUser=?", new Object[]{userId}, new BeanPropertyRowMapper<>(Task.class));
    }

    @Override
    public Task getTaskById(int id, int userId) {
        return jdbcTemplate.query("Select * from tasks where id=? and iduser=?",
                        new Object[]{id,userId}, new BeanPropertyRowMapper<>(Task.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public void updateTask(int id, Task task, int userId) {
        jdbcTemplate.update("update tasks Set name=?,description=?,priority=?,date=? where id = ? and idUser=?",
                task.getName(), task.getDescription(), task.getPriority().name(),
                task.getDate(), id, userId);
    }

    @Override
    public void createTask(Task task, int userId) {
        Status priority = task.getPriority();
        String priorityString;
        if (priority != null) {
            priorityString = priority.name();
        } else {
            priorityString="";
        }

        jdbcTemplate.update("Insert into tasks(idUser,name,description,priority,date) values(?,?,?,?,?)",
                userId, task.getName(), task.getDescription(), priorityString,
                task.getDate());
    }

    @Override
    public void deleteTask(int id, int userId) {
        jdbcTemplate.update("DELETE from tasks where id=? and idUser=?", id, userId);
    }

    public boolean isTaskExist(int id){
        Task task = jdbcTemplate.query("Select * from tasks where id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Task.class))
                .stream().findAny().orElse(null);
        if(task==null){
            return false;
        }
        return true;
    }
}
