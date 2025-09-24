package com.ankit.Smart.Contact.Config;

import com.ankit.Smart.Contact.DAO.UserRepository;
import com.ankit.Smart.Contact.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsService_Implementation implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user= userRepository.getUserByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("Could not found USer !!");
        }
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
         return customUserDetails;
    }
}
