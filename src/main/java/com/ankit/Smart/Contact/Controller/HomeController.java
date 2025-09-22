package com.ankit.Smart.Contact.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("title","THis is a Home Page For Smart Contact");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","THis is a About Page For Smart Contact");
        return "about";
    }

}
