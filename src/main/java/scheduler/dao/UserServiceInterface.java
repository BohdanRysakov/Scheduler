package scheduler.dao;

import scheduler.models.User;

import java.util.List;

public interface UserServiceInterface {
    List<User> showUsers();
    void save(User user);
    User getUserById(int id);
    User getUserByName(String name);
    void update(User user,int id);
    void update(User user,String name);
    void delete(String name);


}
