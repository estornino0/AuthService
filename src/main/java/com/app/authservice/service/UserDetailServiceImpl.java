package com.app.authservice.service;

import com.app.authservice.repository.UserRepository;
import com.app.authservice.entity.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public void registerNewUser(String email, String password) {
        if(emailExists(email)){
            throw new IllegalStateException("Email already used");
        }
        String encodedPassword = passwordEncoder.encode(password);
        UserDetails userDetails = new UserDetails(email, encodedPassword);
        userRepository.save(userDetails);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(()->
                        new UsernameNotFoundException("UserDetails not found with email: " + email));
    }
}
