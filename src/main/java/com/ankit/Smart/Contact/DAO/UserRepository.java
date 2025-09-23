package com.ankit.Smart.Contact.DAO;

import com.ankit.Smart.Contact.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

}
