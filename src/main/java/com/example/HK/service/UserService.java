package com.example.HK.service;

import com.example.HK.controller.form.UserForm;
import com.example.HK.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    /*
     * ログインユーザ情報取得

    public UserForm loginUser(String account, String password) {

    }*/


}
