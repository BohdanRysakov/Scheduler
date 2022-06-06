package scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scheduler.dao.UserDAO;
import scheduler.dao.TaskDAO;
import scheduler.exceptions.UserException;
import scheduler.models.User;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class LoginController {
    private final UserDAO userDAO;
    private final TaskDAO taskDAO;

    @Autowired
    public LoginController(UserDAO userDAO, TaskDAO taskDAO) {
        this.userDAO = userDAO;
        this.taskDAO = taskDAO;
    }

    @GetMapping("/login")
    public String enter(Model model, @ModelAttribute("user") User user, HttpSession session) throws UserException {


        if ((userDAO.getUserByName(user.getName()) == null) && session.getAttribute("user") == null) {
            return "redirect:/user";
            //throw new UserException("No user found with name: " + user.getName());
        }
        model.addAttribute("user", session.getAttribute("user"));

        return "personalRoom";
    }

    @GetMapping()
    public String enter(User user, Model model) {
        model.addAttribute(user);
        return "/login";
    }

    @PostMapping("/login")
    public String enterInAccount(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, Model model, HttpSession session) throws UserException {

        if (bindingResult.hasErrors()) {
            return "login";
        }
        if (userDAO.getUserByName(user.getName()) == null) {
            redirectAttributes.addFlashAttribute("errorName", "No user found with name: " + user.getName());
            return "redirect:/user";
            // throw new UserException("No user found with name: " + user.getName());

        }
        if (!user.getPassword().equals(userDAO.getUserByName(user.getName()).getPassword())) {
            redirectAttributes.addFlashAttribute("errorPassword", "Wrong password for " + user.getName());
            return "redirect:/user";
        }
        session.setAttribute("directionDate", "true");
        session.setAttribute("directionPriority", "true");


        model.addAttribute("user", userDAO.getUserByName(user.getName()));
        model.addAttribute("tasks", taskDAO.showTasks(userDAO.getUserByName(user.getName()).getId()));
        session.setAttribute("user", userDAO.getUserByName(user.getName()));
        return "personalRoom";


    }

    @GetMapping("/registration")
    public String registrateAnAccount(@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/create")
    public String create(@Valid User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) throws UserException {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (userDAO.getUserByName(user.getName()) != null) {
            if(user.getPassword().length()<6){
                redirectAttributes.addFlashAttribute("errorPassword", "Password must be longer than 6 characters");
            }
            redirectAttributes.addFlashAttribute("errorName", "Name " + user.getName() + " is taken");
            return "redirect:/user/registration";
            //throw new UserException("User with name: " + user.getName() + " already exist");
        }
        userDAO.save(user);
        redirectAttributes.addFlashAttribute(userDAO.getUserByName(user.getName()));
        return "redirect:/user/login";
    }

    @GetMapping("/edit/{name}")
    public String editAccount(Model model, @PathVariable("name") String name) {
        model.addAttribute("user", userDAO.getUserByName(name));
        return "editUser";
    }

    @PatchMapping("/edit/{name}")
    public String update(@Valid User user, BindingResult bindingResult,
                         @PathVariable("name") String name, RedirectAttributes redirectAttributes) throws UserException {
        if (bindingResult.hasErrors()) {
            return "editUser";
        }
        if (userDAO.getUserByName(user.getName()) != null) {
            if(user.getPassword().length()<6){
                redirectAttributes.addFlashAttribute("errorPassword", "Password must be longer than 6 characters");
            }
            redirectAttributes.addFlashAttribute("errorName", "Name " + user.getName() + " is taken");
            return "redirect:/user/edit/"+name;
        }

        userDAO.update(user, name);
        redirectAttributes.addFlashAttribute(userDAO.getUserByName(user.getName()));
        return "redirect:/user/login";
    }

    //todo как достать логин
    @GetMapping("/login/logout")
    public String logout() {
        return "redirect:/user";
    }

    @DeleteMapping("/delete/{name}")
    public String delete(@PathVariable("name") String name) {
        userDAO.delete(name);
        return "redirect:/user";
    }


}