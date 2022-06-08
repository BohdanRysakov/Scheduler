package scheduler.models;

import scheduler.addition.Status;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Task  {
    private int idUser;
    @NotBlank(message = "Task's name can't be empty")
    @Size(min=1,max=10, message = "Task Name must be between 1 and 10 characters")
    private String name;
    @NotBlank(message = "Description's name can't be empty")
    @Size(min=1,max=255, message = "Description must be between 1 and 255 characters")
    private String description;
    private Date date;
    private Status priority;
    private int id;
    private boolean wrongDate;

    public boolean isWrongDate() {
        return wrongDate;
    }

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
        if(priority==null) {
            this.priority = Status.common;
        }
        else{
            this.priority = priority;
        }

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        if(date==null){
            return "";
        }
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        if(  date==null || date.equals("")){
            this.date=null;
        }
        else {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            try {
                this.date = sd.parse(date);
            } catch (ParseException e) {
               wrongDate=true;
            }
        }
    }
    public boolean dateIsEmpty(){
        if(getDate().equals("")){
            return true;
        }
        return false;
    }


}
