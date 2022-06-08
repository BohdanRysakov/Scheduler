package scheduler.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class frontController {
    @GetMapping()
    public String frontPage(){
        return "redirect:/user";
    }
}
