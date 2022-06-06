package scheduler.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {
    private int id;
    @NotBlank(message = "Name can't be empty")
    @Size(min = 3, max = 30, message = "Name should be between 3 and 30 characters")
    private String name;
    @NotEmpty(message = "Password can't be empty")
    @Size(min = 6, max = 30, message = "Name should be greater than 6 characters ")
    private String password;



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setPassword(String password) {
        this.password = password;
    }
}
