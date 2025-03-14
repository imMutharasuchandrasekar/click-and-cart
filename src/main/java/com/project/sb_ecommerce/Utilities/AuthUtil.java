package com.project.sb_ecommerce.Utilities;

import com.project.sb_ecommerce.model.User;
import com.project.sb_ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil
{
    @Autowired
    UserRepository userRepository;

    public User loggedInUser()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername( authentication.getName() )
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));
        return user;
    }

    public String loggedInEmail()
    {
        User user = loggedInUser();
        return user.getEmail();
    }

    public Long loggedInUserId()
    {
        User user = loggedInUser();
        return user.getUserId();
    }
}
