package scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scheduler.addition.SortByDate;
import scheduler.addition.SortById;
import scheduler.addition.Status;
import scheduler.dao.TaskDAO;
import scheduler.exceptions.TaskException;
import scheduler.models.Task;
import scheduler.models.User;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;


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


    @GetMapping("/kostil")
    public String kostil(@ModelAttribute("task") Task task, @ModelAttribute("user") User user,
                         RedirectAttributes redirectAttributes, HttpSession session) {

        redirectAttributes.addFlashAttribute("tasks", taskDAO.showTasks(((User) session.getAttribute("user")).getId()));

        return "redirect:/user/login";


    }

    @GetMapping("/createtask")
    public String createTask(@ModelAttribute("task") Task task) {
        return "/newTask";


    }

    @PostMapping("/createtask")
    public String postTask(@ModelAttribute("task") @Valid Task task,BindingResult bindingResult , HttpSession session,
                           Model model, RedirectAttributes redirectAttributes) throws TaskException {

        if (bindingResult.hasErrors()) {
            return "newTask";
        }

        if (task.getPriority() != null && !statusList.contains(task.getPriority().name().toLowerCase())) {
            redirectAttributes.addFlashAttribute("errorPriority", "Select correct priority");
            return "redirect:/user/login/createtask";
        }
        if(task.getName()==null){
            redirectAttributes.addFlashAttribute("errorTaskName", "Name is empty!");
            return "redirect:/user/login/createtask";
        }
        if(task.getDescription()==null || task.getDescription().equals("") ||
                task.getDescription().replaceAll("\\s","").equals("")){
            redirectAttributes.addFlashAttribute("errorDescription", "Empty description");
            return "redirect:/user/login/createtask";
        }
        if(task.isWrongDate()){
            redirectAttributes.addFlashAttribute("errorDate", "Invalid date format");
            return "redirect:/user/login/createtask";
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
    public String getTask(Model model, @PathVariable("id") int id, HttpSession session,
                          RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!taskDAO.isTaskExist(id)){
            redirectAttributes.addFlashAttribute("error","No task found");
            return "redirect:/user/login";
        }
        Task task = taskDAO.getTaskById(id, user.getId());
        if(task==null){
            redirectAttributes.addFlashAttribute("errorName","Слушай сюда, сын портовой шлюхи, если такса не твоя - не трогай. " +
                    "еще раз увижу такую хуйню и я тебе кадык вырву");
            return "redirect:/user";
        }

        model.addAttribute("task", task);
        return "task";
    }

    @PatchMapping("/{id}")
    public String patchTask(@ModelAttribute("task") @Valid Task task,BindingResult bindingResult, @PathVariable("id") int id,
                            HttpSession session, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "editTask";
        }
        if (task.getPriority() != null && !statusList.contains(task.getPriority().name().toLowerCase())) {
            redirectAttributes.addFlashAttribute("errorPriority", "Select correct priority");
            return "redirect:/user/login/"+task.getId()+"/edit";
        }
        if(task.getName()==null){
            redirectAttributes.addFlashAttribute("errorTaskName", "Name is empty!");
            return "redirect:/user/login/"+task.getId()+"/edit";
        }
        if(task.getDescription()==null || task.getDescription().equals("") ||
                task.getDescription().replaceAll("\\s","").equals("")){
            redirectAttributes.addFlashAttribute("errorDescription", "Empty description");
            return "redirect:/user/login/"+task.getId()+"/edit";
        }
        if(task.isWrongDate()){
            redirectAttributes.addFlashAttribute("errorDate", "Invalid date format");
            return "redirect:/user/login/"+task.getId()+"/edit";
        }
        User user = (User) session.getAttribute("user");
        taskDAO.updateTask(id, task, user.getId());
        redirectAttributes.addFlashAttribute("user", user);
        redirectAttributes.addFlashAttribute("tasks", taskDAO.showTasks(user.getId()));
        String str = "redirect:/user/login/" + id;
        return str;


    }


    @GetMapping("/{id}/edit")
    public String updateTask(@PathVariable("id") int id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!taskDAO.isTaskExist(id)){
            redirectAttributes.addFlashAttribute("error","No task found");
            return "redirect:/user/login";
        }
        Task task = taskDAO.getTaskById(id, user.getId());
        if(task==null){
            redirectAttributes.addFlashAttribute("errorName","Слушай сюда, сын портовой шлюхи, если такса не твоя - не трогай. " +
                    "еще раз увижу такую хуйню и я тебе кадык вырву");
            return "redirect:/user";
        }
        model.addAttribute("task", task);
        return "editTask";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (!taskDAO.isTaskExist(id)){
            redirectAttributes.addFlashAttribute("error","No task found to delete");
            return "redirect:/user/login";
        }
        Task task = taskDAO.getTaskById(id, user.getId());
        if(task==null){
            redirectAttributes.addFlashAttribute("errorName","Слушай сюда, сын портовой шлюхи, если такса не твоя - не трогай. " +
                    "еще раз увижу такую хуйню и я тебе кадык вырву");
            return "redirect:/user";
        }
        taskDAO.deleteTask(id, user.getId());
        List<Task> tasks = taskDAO.showTasks(user.getId());
        redirectAttributes.addFlashAttribute(user);
        redirectAttributes.addFlashAttribute("tasks", tasks);


        return "redirect:/user/login";
    }

    @GetMapping("/sortByPriority")
    public String sortByPriority(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Task> sortedTasks = taskDAO.showTasks(((User) session.getAttribute("user")).getId());

        if (session.getAttribute("directionPriority") == "true") {
            session.setAttribute("directionPriority", "false");
            SortById.sortById(sortedTasks, true);
        } else {
            session.setAttribute("directionPriority", "true");
            SortById.sortById(sortedTasks, false);
        }


        redirectAttributes.addFlashAttribute("tasks", sortedTasks);
        return "redirect:/user/login";

    }

    @GetMapping("/sortByDate")
    public String sortByDate(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Task> sortedTasks = taskDAO.showTasks(((User) session.getAttribute("user")).getId());

        if (session.getAttribute("directionDate") == "true") {
            session.setAttribute("directionDate", "false");
            SortByDate.sortByDate(sortedTasks, true);
        } else {
            session.setAttribute("directionDate", "true");
            SortByDate.sortByDate(sortedTasks, false);
        }
        redirectAttributes.addFlashAttribute("tasks", sortedTasks);

        return "redirect:/user/login";

    }


}
