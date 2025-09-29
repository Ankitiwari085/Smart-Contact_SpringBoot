package com.ankit.Smart.Contact.Controller;

import com.ankit.Smart.Contact.DAO.UserRepository;
import com.ankit.Smart.Contact.Entity.Contact;
import com.ankit.Smart.Contact.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @ModelAttribute
    public void addCommanData(Model model,Principal principal){
        String userName=principal.getName();
        System.out.println("UserName(E-mail) : "+userName);
        User user =this.userRepository.getUserByUsername(userName);
        System.out.println(user);
        model.addAttribute("user",user);
    }
    @GetMapping("/user_dashboard")
    public  String dashboard(Model model, Principal principal){

        return "normal/user_dashboard";
    }
    @GetMapping("/add-contact")
    public String openAddContactform(Model model){
        model.addAttribute("title","Add Contact Form");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact , @RequestParam("profileImage")MultipartFile file, Principal principal){
        try{
            String name=principal.getName();
            User user=this.userRepository.getUserByUsername(name);
            //Processing and Uploading file
            if (file.isEmpty()){
                // sent the message
            }else {
                contact.setImageUrl(file.getOriginalFilename());
                File saveFile= new ClassPathResource("static/Image").getFile();
                Path path=Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image is Uploaded");
            }
            contact.setUser(user);
            user.getContactList().add(contact);
            this.userRepository.save(user);
            System.out.println("Added to Database. ");
        } catch (Exception e) {
            System.out.println("Error"+e.getMessage());
        }
        return "normal/add_contact_form";
    }
}
