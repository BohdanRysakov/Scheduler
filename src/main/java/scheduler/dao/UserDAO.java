package scheduler.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import scheduler.models.User;

import java.util.List;


@Component
public class UserDAO implements UserServiceInterface {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate){

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserById(int id) {
        return jdbcTemplate.query("Select * from users where id=?", new Object[]{id},new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    @Override
    public User getUserByName(String name) {
        return jdbcTemplate.query("Select * from users where name=?", new Object[]{name},new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }
    @Override
    public void update(User user,int id) {
        jdbcTemplate.update("update users Set name=?,password=? where id = ?",
                user.getName(),user.getPassword(),id);
    }

    @Override
    public void update(User user, String name) {
        jdbcTemplate.update("update users Set name=?,password=? where name = ?",
                user.getName(),user.getPassword(),name);
    }

    @Override
    public void delete(String name) {
        jdbcTemplate.update("DELETE from users where name=?",name);
    }
    @Override
    public List<User> showUsers() {
        return jdbcTemplate.query("Select * from users",new BeanPropertyRowMapper<>(User.class));
    }


    public void save(User user) {
        jdbcTemplate.update("Insert into users(name,password) values(?,?)",
                user.getName(),user.getPassword());
    }



}
