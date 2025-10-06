package com.ankit.Smart.Contact.Controller;

import com.ankit.Smart.Contact.DAO.ContactRepository;
import com.ankit.Smart.Contact.DAO.UserRepository;
import com.ankit.Smart.Contact.Entity.Contact;
import com.ankit.Smart.Contact.Entity.User;
import com.ankit.Smart.Contact.Helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;
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
    public String processContact(@ModelAttribute Contact contact , @RequestParam("profileImage")MultipartFile file, Principal principal, HttpSession session){
        try{
            String name=principal.getName();
            User user=this.userRepository.getUserByUsername(name);
            //Processing and Uploading file
            if (file.isEmpty()){
                // sent the message
                contact.setImageUrl("profile.png");
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
            //Success Message sent to the Client
            session.setAttribute("message", new Message("Contact addedd Successfully","success"));
        } catch (Exception e) {
            System.out.println("Error"+e.getMessage());
            session.setAttribute("message", new Message("Something went wrong, Try again !!","danger"));

        }
        return "normal/add_contact_form";
    }
    @GetMapping("/show-contacts/{page}")
    public String showContacts( @PathVariable("page") int page,Model m , Principal principal){
        String userName=principal.getName();
        User user =this.userRepository.getUserByUsername(userName);

       Pageable pageable= PageRequest.of(page,7);
        Page<Contact> contacts =this.contactRepository.findContactByUser(user.getId(),pageable);
        m.addAttribute("contacts",contacts);
        m.addAttribute("currentPage",page);
        m.addAttribute("totalPages",contacts.getTotalPages());

       return "normal/show_contacts";
    }

   @GetMapping("/{cId}/contact")
    public String showContactDetail(@PathVariable("cId") Integer cId ,Model model , Principal principal){
       Optional<Contact> contactOptional=this.contactRepository.findById(cId);
       Contact contact= contactOptional.get();
       String userName=principal.getName();
       User user=this.userRepository.getUserByUsername(userName);
       if(user.getId() == contact.getUser().getId())
           model.addAttribute("contact",contact);
        return "normal/contact_detail";
   }

   @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") Integer cId ,Principal principal, HttpSession session){
        Optional<Contact> contactOptional=this.contactRepository.findById(cId);
        Contact contact =contactOptional.get();
       String userName=principal.getName();
       User user=this.userRepository.getUserByUsername(userName);
       if(user.getId() == contact.getUser().getId()){
           contact.setUser(null);
           this.contactRepository.delete(contact);
           session.setAttribute("message",new Message("Contact Deleted Successfully","success"));

       }

        return "redirect:/user/show-contacts/0";
   }

   @PostMapping("/update/{cId}")
   public String upadateContact(@PathVariable("cId") Integer cId,Model model){
        Optional<Contact> contactOptional= this.contactRepository.findById(cId);
        Contact contact=contactOptional.get();
        model.addAttribute("contact",contact);
        return "normal/updateForm";
   }
   @PostMapping("/process-update")
    public  String updateProcess(@ModelAttribute Contact contact ,@RequestParam("profileImage") MultipartFile file ,Principal principal, HttpSession session){
        try{
           Contact oldContact= this.contactRepository.findById(contact.getcId()).get();
            if (!file.isEmpty()) {
                // delete old image if not default.png
                File imageFolder = new ClassPathResource("static/Image").getFile();
                if (oldContact.getImageUrl() != null && !oldContact.getImageUrl().equals("profile.png")) {
                    File oldFile = new File(imageFolder, oldContact.getImageUrl());
                    if (oldFile.exists())
                        oldFile.delete();
                }

                // save new image
                Path path = Paths.get(imageFolder.getAbsolutePath() + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                contact.setImageUrl(file.getOriginalFilename());
            }else
                contact.setImageUrl(oldContact.getImageUrl());
            User user=this.userRepository.getUserByUsername(principal.getName());
            contact.setUser(user);

            this.contactRepository.save(contact);
            session.setAttribute("message", new Message("Contact Updated Successfully", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("message", new Message("Update failed!", "danger"));
        }
        return "redirect:/user/"+contact.getcId()+"/contact";
   }

   @GetMapping("/profile")
   public String yourProfile(Model model){
        model.addAttribute("title","profile page");
        return "normal/profile";
   }
   @GetMapping("/setting")
    public String setting(){
        return "normal/setting";
   }
}
