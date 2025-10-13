package com.ankit.Smart.Contact.Controller;

import com.ankit.Smart.Contact.DAO.ContactRepository;
import com.ankit.Smart.Contact.DAO.UserRepository;
import com.ankit.Smart.Contact.Entity.Contact;
import com.ankit.Smart.Contact.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search/{query}")
    public ResponseEntity<List<Contact>> search(@PathVariable("query") String query ,Principal principal){
        User user= this.userRepository.getUserByUsername(principal.getName());
        List<Contact> contacts= this.contactRepository.findByNameContainingAndUser(query,user);

        return ResponseEntity.ok(contacts);
    }
}
