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
    public String createTask(@ModelAttribute("task") Task task,HttpSession session,Model model) {
        User user = (User) session.getAttribute("user");
        model.addAttribute(user);
        return "/newTask";


    }

    @PostMapping("/createtask")
    public String postTask(@ModelAttribute("task") @Valid Task task,BindingResult bindingResult , HttpSession session,
                           Model model, RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()) {
            User user = (User) session.getAttribute("user");
            model.addAttribute(user);
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
        session.setAttribute("tasks",taskDAO.showTasks(user.getId()));
        return "redirect:/user/login";

    }

    @GetMapping("/{id}")
    public String getTask(Model model, @PathVariable("id") int id, HttpSession session,
                          RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:";
        }

        if (!taskDAO.isTaskExist(id)){
            redirectAttributes.addFlashAttribute("error","No task found");

            return "redirect:/user/login";
        }
        Task task = taskDAO.getTaskById(id, user.getId());
        if(task==null){
            redirectAttributes.addFlashAttribute("error","Nice try");
            taskDAO.createTask(punishment(user.getId()), user.getId());
            return "redirect:/user";
        }
        model.addAttribute("user",user);
        model.addAttribute("task", task);
        return "task";
    }

    @PatchMapping("/{id}")
    public String patchTask(@ModelAttribute("task") @Valid Task task,BindingResult bindingResult, @PathVariable("id") int id,
                            HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:";
        }
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
        taskDAO.updateTask(id, task, user.getId());
        String str = "redirect:/user/login/" + id;
        return str;


    }


    @GetMapping("/{id}/edit")
    public String updateTask(@PathVariable("id") int id, Model model, HttpSession session,RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:";
        }
        if (!taskDAO.isTaskExist(id)){
            redirectAttributes.addFlashAttribute("error","No task found");
            return "redirect:/user/login";
        }
        Task task = taskDAO.getTaskById(id, user.getId());
        if(task==null){
            redirectAttributes.addFlashAttribute("error","Nice try");

                taskDAO.createTask(punishment(user.getId()), user.getId());

            return "redirect:/user";
        }
        model.addAttribute("task", task);
        model.addAttribute("user", user);
        return "editTask";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:";
        }
        if (!taskDAO.isTaskExist(id)){
            redirectAttributes.addFlashAttribute("error","No task found to delete");

            return "redirect:/user/login";
        }
        Task task = taskDAO.getTaskById(id, user.getId());
        if(task==null){
            taskDAO.createTask(punishment(user.getId()), user.getId());
            redirectAttributes.addFlashAttribute("error","Nice try");
            return "redirect:/user";
        }
        taskDAO.deleteTask(id, user.getId());
        session.setAttribute("tasks",taskDAO.showTasks(user.getId()));
        return "redirect:/user/login";
    }

    @GetMapping("/sortByPriority")
    public String sortByPriority(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Task> sortedTasks = (List<Task>) session.getAttribute("tasks");
        if(sortedTasks==null){
            return null;
        }
        if (session.getAttribute("directionPriority") == "true") {
            session.setAttribute("directionPriority", "false");
            SortById.sortById(sortedTasks, true);
        } else {
            session.setAttribute("directionPriority", "true");
            SortById.sortById(sortedTasks, false);
        }


        session.setAttribute("tasks",sortedTasks);
        return "redirect:/user/login";

    }

    @GetMapping("/sortByDate")
    public String sortByDate(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Task> sortedTasks = (List<Task>) session.getAttribute("tasks");
        if(sortedTasks==null){
            return null;
        }
        if (session.getAttribute("directionDate") == "true") {
            session.setAttribute("directionDate", "false");
            SortByDate.sortByDate(sortedTasks, true);
        } else {
            session.setAttribute("directionDate", "true");
            SortByDate.sortByDate(sortedTasks, false);
        }
      session.setAttribute("tasks",sortedTasks);

        return "redirect:/user/login";

    }
    private Task punishment(int id){
        Task task = new Task();
        task.setPriority(Status.critical);
        task.setDescription("I was trying to poke and pry and i am really sorry");
        task.setName("Never do this again");
        task.setIdUser(id);
        return task;
    }


}
