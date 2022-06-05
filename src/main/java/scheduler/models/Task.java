package scheduler.models;

import org.springframework.format.annotation.DateTimeFormat;
import scheduler.addition.Status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Task  {
    private int idUser;
    private String name;
    private String description;
    private Date date;
    private Status priority;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Status getPriority() {
        return priority;
    }


    public void setPriority(Status priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        if(date==null){
            return null;
        }
        return sd.format(date);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            this.date = sd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}
