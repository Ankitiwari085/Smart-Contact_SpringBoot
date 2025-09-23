package com.ankit.Smart.Contact.Controller;


import com.ankit.Smart.Contact.DAO.UserRepository;
import com.ankit.Smart.Contact.Entity.User;
import com.ankit.Smart.Contact.Helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("title","THis is a Home Page For Smart Contact");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","THis is a About Page For Smart Contact");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model){
//        model.addAttribute("title","Register  For Smart Contact");
        model.addAttribute("user",new User());
        return "signup";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title","Login in Smart Contact");
        return "login";
    }

    @PostMapping("/do_register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult validation, @RequestParam(value="agreement" ,defaultValue = "false")boolean agreement, Model model , HttpSession session){
       try{
           if(!agreement){
               System.out.println("Must acept Terms and Conditions");
               throw new RuntimeException("Must acept Terms and Conditions");
           }
           if (validation.hasErrors()){
               model.addAttribute("user",user);
               return "signup";
           }
           user.setEnabled(true);
           user.setRole("ROLE_USER");

           User result=this.userRepository.save(user);
           System.out.println(result);
           model.addAttribute("user",new User());
           session.setAttribute("message",new Message("Successfully Register","alert-success"));
           return "signup";
       } catch (Exception e) {
           e.printStackTrace();
           model.addAttribute("user",user);
           session.setAttribute("message",new Message("Something went wrong","alert-danger"));
           return "signup";
       }

    }

}
