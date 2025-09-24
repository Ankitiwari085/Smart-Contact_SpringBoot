package com.ankit.Smart.Contact.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping("/user_dashboard")
    public  String dashboard(){
        return "user_dashboard";
    }

}
