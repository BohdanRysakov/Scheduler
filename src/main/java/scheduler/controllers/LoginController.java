package scheduler.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import scheduler.dao.UserDAO;
import scheduler.dao.TaskDAO;
import scheduler.models.Task;
import scheduler.models.User;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class LoginController {
    private final UserDAO userDAO;
    private final TaskDAO taskDAO;

    @Autowired
    public LoginController(UserDAO userDAO,TaskDAO taskDAO) {
        this.userDAO = userDAO;
        this.taskDAO = taskDAO;
    }
    @GetMapping()
    public String enter(@ModelAttribute("user") User user) {
        return "login";
    }
    @GetMapping("/login")
    public String enter(Model model, @ModelAttribute("user") User user){
        return "personalRoom";
    }

    @PostMapping("/login")
    public String enterInAccount(@ModelAttribute("user") User user,
                                 Model model, HttpSession session) {

        //Дастать из БД пользователя по Логину и сравнить пароли
        //Если Пароли совпадают то редирект на страничку пользователя
        //в модель добавить пользователя
        //Если не совпадают то вернуть форму с ошибкой
        if(user.getPassword().equals(userDAO.getUserByName(user.getName()).getPassword())){
            model.addAttribute("user",userDAO.getUserByName(user.getName()));
            model.addAttribute("tasks",taskDAO.showTasks(userDAO.getUserByName(user.getName()).getId()));
            session.setAttribute("user", userDAO.getUserByName(user.getName()));
            return "personalRoom";

        }
        System.out.println(userDAO.getUserByName(user.getName()).getName());
        model.addAttribute("user",user);
        return "redirect:";
    }
    @GetMapping("/registration")
    public String registrateAnAccount(@ModelAttribute("user") User user) {
        return "/registration";
    }
    @PostMapping("/create")
    public String create(@ModelAttribute("user") User user,
                         BindingResult bindingResult,RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors())
//            return "people/new";
        userDAO.save(user);
        redirectAttributes.addFlashAttribute(userDAO.getUserByName(user.getName()));
        return "redirect:/user/login";
    }
    @GetMapping("/edit/{name}")
    public String editAccount(Model model, @PathVariable("name") String name) {
        model.addAttribute("user", userDAO.getUserByName(name));
        return "/editUser";
    }
    @PatchMapping("/edit/{name}")
    public String update(@ModelAttribute("user")  User user, BindingResult bindingResult, Model model,
                         @PathVariable("name") String name, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors())
//            return "people/edit";

        userDAO.update(user,name);
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