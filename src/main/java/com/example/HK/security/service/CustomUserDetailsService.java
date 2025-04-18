package com.example.HK.security.service;

import com.example.HK.repository.UserRepository;
import com.example.HK.security.details.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.HK.repository.entity.User;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = (User)userRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for username : " + account));
        return new LoginUserDetails(user);
    }
}
