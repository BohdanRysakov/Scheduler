package scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scheduler.dao.TaskDAO;
import scheduler.dao.UserDAO;
import scheduler.models.Task;
import scheduler.models.User;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/user/login")
public class TaskController {

    private final TaskDAO taskDAO;


    @Autowired
    public TaskController(TaskDAO taskDAO) {

        this.taskDAO = taskDAO;
    }
    @GetMapping("/createtask")
    public String createTask(Model model, @ModelAttribute("task") Task task, @ModelAttribute("user") User user, HttpSession session) {
        return "newTask";


    }

    @PostMapping("/createtask")
    public String postTask(Model model, @ModelAttribute("task") Task task,  HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        taskDAO.createTask(task,(user.getId()));
        model.addAttribute("user",user);
        model.addAttribute("tasks",taskDAO.showTasks(user.getId()));
        List<Task> tasks = taskDAO.showTasks(user.getId());
        redirectAttributes.addFlashAttribute(user);
        redirectAttributes.addFlashAttribute("tasks",tasks);
        return "redirect:/user/login";

    }
    @GetMapping("/{id}")
    public String getTask(Model model,@PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Task task  = taskDAO.getTaskById(id,user.getId());
        model.addAttribute( "task",task);
        return "task";


    }
    @PatchMapping("/{id}")
    public String patchTask(Model model,@PathVariable("id") int id, @ModelAttribute("task") Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
        taskDAO.updateTask(id,task,user.getId());
        String str = "redirect:/user/login/"+id;
        return str;


    }


    @GetMapping("/{id}/edit")
    public String updateTask(@PathVariable("id") int id, Model model,@ModelAttribute("task") Task task,HttpSession session) {

        return "/editTask";
    }
    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id,HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        taskDAO.deleteTask(id,user.getId());
        List<Task> tasks = taskDAO.showTasks(user.getId());
        redirectAttributes.addFlashAttribute(user);
        redirectAttributes.addFlashAttribute("tasks",tasks);



        return "redirect:/user/login";
    }


}
