package scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scheduler.addition.SortByDate;
import scheduler.addition.SortById;
import scheduler.addition.Status;
import scheduler.dao.TaskDAO;
import scheduler.models.Task;
import scheduler.models.User;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;


@Controller
@RequestMapping("/user/login")
public class TaskController {
    private final List<String> statusList;
    private final TaskDAO taskDAO;


    @Autowired
    public TaskController(TaskDAO taskDAO) {
        this.statusList = Status.getStatuses();
        this.taskDAO = taskDAO;
    }

    @GetMapping("/createtask")
    public String createTask(Model model, @ModelAttribute("task") Task task, @ModelAttribute("user") User user, HttpSession session) {
        return "newTask";


    }
    @GetMapping("/kostil")
    public String kostil(Model model, @ModelAttribute("task") Task task, @ModelAttribute("user") User user,
                         RedirectAttributes redirectAttributes, HttpSession session) {

        redirectAttributes.addFlashAttribute("tasks",taskDAO.showTasks(((User) session.getAttribute("user")).getId()));

        return "redirect:/user/login";


    }

    @PostMapping("/createtask")
    public String postTask(Model model, @ModelAttribute("task") Task task, HttpSession session, RedirectAttributes redirectAttributes) {

        if (!statusList.contains(task.getPriority().name().toLowerCase())) {
            throw new IllegalArgumentException("illegal status");
        }

        User user = (User) session.getAttribute("user");
        taskDAO.createTask(task, (user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("tasks", taskDAO.showTasks(user.getId()));
        List<Task> tasks = taskDAO.showTasks(user.getId());
        redirectAttributes.addFlashAttribute(user);
        redirectAttributes.addFlashAttribute("tasks", tasks);
        return "redirect:/user/login";

    }

    @GetMapping("/{id}")
    public String getTask(Model model, @PathVariable("id") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Task task = taskDAO.getTaskById(id, user.getId());
        model.addAttribute("task", task);
        return "task";


    }

    @PatchMapping("/{id}")
    public String patchTask(Model model, @PathVariable("id") int id, @ModelAttribute("task") Task task,
                            HttpSession session,RedirectAttributes redirectAttributes) {
        if (!statusList.contains(task.getPriority().name().toLowerCase())) {
            throw new IllegalArgumentException("illegal status");
        }
        User user = (User) session.getAttribute("user");
        taskDAO.updateTask(id, task, user.getId());
        redirectAttributes.addFlashAttribute("user",user);
        redirectAttributes.addFlashAttribute("tasks",taskDAO.showTasks(user.getId()));
        String str = "redirect:/user/login/" + id;
        return str;


    }


    @GetMapping("/{id}/edit")
    public String updateTask(@PathVariable("id") int id, Model model, @ModelAttribute("task") Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
        task=taskDAO.getTaskById(id,user.getId());
        model.addAttribute("task",task);
        return "/editTask";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        taskDAO.deleteTask(id, user.getId());
        List<Task> tasks = taskDAO.showTasks(user.getId());
        redirectAttributes.addFlashAttribute(user);
        redirectAttributes.addFlashAttribute("tasks", tasks);


        return "redirect:/user/login";
    }

    @GetMapping("/sortByPriority")
    public String sortByPriority(Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        List<Task> sortedTasks = taskDAO.showTasks(((User) session.getAttribute("user")).getId());

        if(session.getAttribute("directionPriority")=="true"){
            session.setAttribute("directionPriority","false");
            SortById.sortById(sortedTasks,true);
        }
        else{
            session.setAttribute("directionPriority","true");
            SortById.sortById(sortedTasks,false);
        }


        redirectAttributes.addFlashAttribute("tasks",sortedTasks);
        return "redirect:/user/login";

    }
    @GetMapping("/sortByDate")
    public String sortByDate(Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        List<Task> sortedTasks = taskDAO.showTasks(((User) session.getAttribute("user")).getId());

        if(session.getAttribute("directionDate")=="true"){
            session.setAttribute("directionDate","false");
            SortByDate.sortByDate(sortedTasks,true);
        }
        else{
            session.setAttribute("directionDate","true");
            SortByDate.sortByDate(sortedTasks,false);
        }


        redirectAttributes.addFlashAttribute("tasks",sortedTasks);

        return "redirect:/user/login";

    }


}
